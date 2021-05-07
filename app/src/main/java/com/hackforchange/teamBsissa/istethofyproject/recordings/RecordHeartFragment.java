package com.hackforchange.teamBsissa.istethofyproject.recordings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.acrcloud.rec.sdk.ACRCloudClient;
import com.acrcloud.rec.sdk.ACRCloudConfig;
import com.acrcloud.rec.sdk.IACRCloudListener;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackforchange.teamBsissa.istethofyproject.GlobalUrl;
import com.hackforchange.teamBsissa.istethofyproject.MySegleton;
import com.hackforchange.teamBsissa.istethofyproject.R;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RecordHeartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordHeartFragment extends Fragment implements IACRCloudListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION = "position";
    private static final String LOG_TAG = RecordHeartFragment.class.getSimpleName();

    private int position;

    //Recording controls
    private FloatingActionButton mRecordButton = null;
    private Button mPauseButton = null;

    private TextView mRecordingPrompt, mVolume, mResult;
    private int mRecordPromptCount = 0;

    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;

    private Chronometer mChronometer = null;
    long timeWhenPaused = 0; //stores time when user clicks pause button


    private ACRCloudClient mClient;
    private ACRCloudConfig mConfig;
    private boolean mProcessing = false;
    private boolean initState = false;
    private long startTime = 0;
    private long stopTime = 0;
    Intent intent ;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Record_Fragment.
     */
    public static RecordHeartFragment newInstance(int position) {
        RecordHeartFragment f = new RecordHeartFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }

    public RecordHeartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);


        this.mConfig = new ACRCloudConfig();
        this.mConfig.acrcloudListener = this;

        // If you implement IACRCloudResultWithAudioListener and override "onResult(ACRCloudResult result)", you can get the Audio data.
        //this.mConfig.acrcloudResultWithAudioListener = this;

        this.mConfig.context = getContext();
        this.mConfig.host = "identify-eu-west-1.acrcloud.com";
        this.mConfig.accessKey = "bbab8335ac7cf142daa36d3da21347ea";
        this.mConfig.accessSecret = "IAXwRn1kADKRxfQZA7I2lruWFsdI6xkbEWmWBhKa";
        this.mConfig.protocol = ACRCloudConfig.ACRCloudNetworkProtocol.PROTOCOL_HTTP; // PROTOCOL_HTTPS
        this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE;
        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_LOCAL;
        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_BOTH;

        this.mClient = new ACRCloudClient();
        // If reqMode is REC_MODE_LOCAL or REC_MODE_BOTH,
        // the function initWithConfig is used to load offline db, and it may cost long time.
        this.initState = this.mClient.initWithConfig(this.mConfig);
        if (this.initState) {
            this.mClient.startPreRecord(3000); //start prerecord, you can call "this.mClient.stopPreRecord()" to stop prerecord.
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View recordView = inflater.inflate(R.layout.fragment_record, container, false);
        mVolume = (TextView) recordView.findViewById(R.id.volume);
        mResult = (TextView) recordView.findViewById(R.id.result);
        mChronometer = (Chronometer) recordView.findViewById(R.id.chronometer);
        //update recording prompt text
        mRecordingPrompt = (TextView) recordView.findViewById(R.id.recording_status_text);

        mRecordButton = (FloatingActionButton) recordView.findViewById(R.id.btnRecord);
        mRecordButton.setColorNormal(getResources().getColor(R.color.colorPrimary));
        mRecordButton.setColorPressed(getResources().getColor(R.color.colorPrimary));
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            }
        });

        mPauseButton = (Button) recordView.findViewById(R.id.btnPause);
        mPauseButton.setVisibility(View.GONE); //hide pause button before recording starts
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPauseRecord(mPauseRecording);
                mPauseRecording = !mPauseRecording;
            }
        });

        return recordView;
    }

    // Recording Start/Stop
    private void onRecord(boolean start) {

        intent = new Intent(getActivity(), RecordingHeartService.class);

        if (start) {
            beg();
            // start recording
            mRecordButton.setImageResource(R.drawable.ic_media_stop);
            //mPauseButton.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), R.string.toast_recording_start, Toast.LENGTH_SHORT).show();
            File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorderHeart");
            if (!folder.exists()) {
                //folder /SoundRecorder doesn't exist, create the folder
                folder.mkdir();
            }

            //start Chronometer
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    if (mRecordPromptCount == 0) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
                    } else if (mRecordPromptCount == 1) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + "..");
                    } else if (mRecordPromptCount == 2) {
                        mRecordingPrompt.setText(getString(R.string.record_in_progress) + "...");
                        mRecordPromptCount = -1;
                    }

                    mRecordPromptCount++;
                }
            });

            //start RecordingService
            getActivity().startService(intent);
            //keep screen on while recording
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            mRecordingPrompt.setText(getString(R.string.record_in_progress) + ".");
            mRecordPromptCount++;

        } else {
            //stop recording
            stop();
            mRecordButton.setImageResource(R.drawable.ic_mic_white_36dp);
            //mPauseButton.setVisibility(View.GONE);
            mChronometer.stop();
            mChronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenPaused = 0;
            mRecordingPrompt.setText(getString(R.string.record_prompt));

            getActivity().stopService(intent);
            //allow the screen to turn off again once recording is finished
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
           // addresult(mResult.getText().toString());
        }
    }

    private void onPauseRecord(boolean pause) {

        if (pause || mProcessing && this.mClient != null) {
            //pause recording
            this.mClient.stopRecordToRecognize();
            mPauseButton.setCompoundDrawablesWithIntrinsicBounds
                    (R.drawable.ic_media_play, 0, 0, 0);
            mRecordingPrompt.setText((String) getString(R.string.resume_recording_button).toUpperCase());
            timeWhenPaused = mChronometer.getBase() - SystemClock.elapsedRealtime();
            mChronometer.stop();
        } else {
            //resume recording
            mPauseButton.setCompoundDrawablesWithIntrinsicBounds
                    (R.drawable.ic_media_pause, 0, 0, 0);
            mRecordingPrompt.setText((String) getString(R.string.pause_recording_button).toUpperCase());
            mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
            mChronometer.start();
        }
        mProcessing = false;

        stopTime = System.currentTimeMillis();
    }


    public void beg() {
        if (!this.initState) {
            Toast.makeText(getContext(), "init error", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mProcessing) {
            mProcessing = true;
            mVolume.setText("");
            mResult.setText("");
            if (this.mClient == null || !this.mClient.startRecognize()) {
                mProcessing = false;
                mResult.setText("start error!");
            }
            startTime = System.currentTimeMillis();
        }
    }

    protected void stop() {
        if (mProcessing && this.mClient != null) {
            this.mClient.stopRecordToRecognize();
        }
        mProcessing = false;

        stopTime = System.currentTimeMillis();
    }

    protected void cancel() {
        if (mProcessing && this.mClient != null) {
            mProcessing = false;
            this.mClient.cancel();
            mResult.setText("");
        }
    }


    // Old api
    @Override
    public void onResult(String result) {
        if (this.mClient != null) {
            this.mClient.cancel();
            mProcessing = false;
        }

        String tres = "\n";

        try {
            JSONObject j = new JSONObject(result);
            JSONObject j1 = j.getJSONObject("status");
            int j2 = j1.getInt("code");
            if (j2 == 0) {
                JSONObject metadata = j.getJSONObject("metadata");
                //
                if (metadata.has("humming")) {
                    JSONArray hummings = metadata.getJSONArray("humming");
                    for (int i = 0; i < hummings.length(); i++) {
                        JSONObject tt = (JSONObject) hummings.get(i);
                        String title = tt.getString("title");
                        JSONArray artistt = tt.getJSONArray("artists");
                        JSONObject art = (JSONObject) artistt.get(0);
                        String artist = art.getString("name");
                        tres = tres + (i + 1) + ".  " + title + "\n";
                    }
                }
                if (metadata.has("music")) {
                    JSONArray musics = metadata.getJSONArray("music");
                    for (int i = 0; i < musics.length(); i++) {
                        JSONObject tt = (JSONObject) musics.get(i);
                        String title = tt.getString("title");
                        JSONArray artistt = tt.getJSONArray("artists");
                        JSONObject art = (JSONObject) artistt.get(0);
                        String artist = art.getString("name");
                        tres = tres + (i + 1) + ".  Title: " + title + "    Artist: " + artist + "\n";
                    }
                }
                if (metadata.has("streams")) {
                    JSONArray musics = metadata.getJSONArray("streams");
                    for (int i = 0; i < musics.length(); i++) {
                        JSONObject tt = (JSONObject) musics.get(i);
                        String title = tt.getString("title");
                        String channelId = tt.getString("channel_id");
                        tres = tres + (i + 1) + ".  Title: " + title + "    Channel Id: " + channelId + "\n";
                    }
                }
                if (metadata.has("custom_files")) {
                    JSONArray musics = metadata.getJSONArray("custom_files");
                    for (int i = 0; i < musics.length(); i++) {
                        JSONObject tt = (JSONObject) musics.get(i);
                        String title = tt.getString("title");
                        tres = title + "\n";
                    }
                }
                tres = tres;
            } else {
                tres = "pas de resultat :(";
            }
        } catch (JSONException e) {
            tres = result;
            e.printStackTrace();
        }
        addresult(tres);
        mResult.setText(tres);
        mRecordButton.setImageResource(R.drawable.ic_mic_white_36dp);
        //mPauseButton.setVisibility(View.GONE);
        mChronometer.stop();
        mChronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenPaused = 0;
        mRecordingPrompt.setText(getString(R.string.record_prompt));
        intent = new Intent(getActivity(), RecordingService.class);
        getActivity().stopService(intent);
        //allow the screen to turn off again once recording is finished
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onVolumeChanged(double volume) {
        long time = (System.currentTimeMillis() - startTime) / 1000;
        mVolume.setText("Volume" + volume + "\n\nRecord Time: " + time + " s");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity", "release");
        if (this.mClient != null) {
            this.mClient.release();
            this.initState = false;
            this.mClient = null;
        }
    }

    public void addresult(String b) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("role", "").equals("1")) {
            String server_url = GlobalUrl.url + "/addheartresult/" + 1 + "/" + sharedPreferences.getString("id", "") + "/" + b + "";
            String str = server_url.replaceAll(" ", "%20");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, str,

                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
            );
            MySegleton.getmInstance(getContext()).addTorequestque(stringRequest);
        }
        if (sharedPreferences.getString("role", "").equals("0")) {
            String server_url = GlobalUrl.url + "/addheartresult/" + 0 + "/" + sharedPreferences.getString("id", "") + "/" + b + "";
            String str = server_url.replaceAll(" ", "%20");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, str,

                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
            );
            MySegleton.getmInstance(getContext()).addTorequestque(stringRequest);
        }


    }

}