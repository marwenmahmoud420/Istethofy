package com.hackforchange.teamBsissa.istethofyproject.Doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackforchange.teamBsissa.istethofyproject.Client.CustomListViewMessages;
import com.hackforchange.teamBsissa.istethofyproject.Client.Model;
import com.hackforchange.teamBsissa.istethofyproject.GlobalUrl;
import com.hackforchange.teamBsissa.istethofyproject.MySegleton;
import com.hackforchange.teamBsissa.istethofyproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class patientChat extends AppCompatActivity {
    String urladdress = GlobalUrl.urlmsg;
    EditText message;
    Button send;
    String messages;
    AlertDialog.Builder builder;
    private ProgressDialog pDialog;
    CustomListViewMessages adapter;
    CustomListViewMessages customListView;
    String[] name = {};
    String[] salut;
    String[] email = {}, number = {};
    String[] imagepath = {};
    ListView listView;
    BufferedInputStream is, is2, is3;
    String line = null;
    String result = null;
    String result2 = null;
    String result3 = null;
    String[] test, test2, test3;
    String getId;


    String[] title;
    String[] description;
    int[] icon;
    ArrayList<Model> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_client);




        String imageClient = getIntent().getStringExtra("imagepatient");
        String nomClient = getIntent().getStringExtra("nompatient");
        String prenomClient = getIntent().getStringExtra("prenompatient");

        SharedPreferences sharedPreferencess = getSharedPreferences("User", Context.MODE_PRIVATE);
        String ImageDocteur = sharedPreferencess.getString("image_prof","");
        String nomDocteur = sharedPreferencess.getString("nom","");
        String prenomDocteur = sharedPreferencess.getString("prenom","");




        listView = findViewById(R.id.lview);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        String idpatient = getIntent().getStringExtra("idpatient");

        collectData(sharedPreferencess.getString("id", ""),idpatient
        );
        CustomListViewMessages customListView = new CustomListViewMessages(this, name, email, imagepath, number);
        listView.setAdapter(customListView);


        message = (EditText) findViewById(R.id.messagetext);

        send = (Button) findViewById(R.id.btnsend);


        builder = new AlertDialog.Builder(this);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages = message.getText().toString();

                //  if (!name.isEmpty() || !lastName.isEmpty() || !mail.isEmpty() || !pass.isEmpty() || pass2.isEmpty())
                String email_receiver, idd;
                email_receiver = getIntent().getStringExtra("idpatient");
                idd = sharedPreferences.getString("id", "");


                if (!messages.isEmpty())

                {
                    String server_url = GlobalUrl.url + "/sendmessage/" + email_receiver + "/" + idd + "/" + messages + "";
                    String str = server_url.replaceAll(" ", "%20");

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, str,

                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    hideDialog();
                                    builder.setTitle("Sending message");
                                    builder.setMessage("" + response);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(patientChat.this, ChatDoc.class);
                                            startActivity(i);
                                        }

                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                    );
                    MySegleton.getmInstance(patientChat.this).addTorequestque(stringRequest);
                } else {
                    builder.setTitle("Error");
                    builder.setMessage("Response :Message alert");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i = new Intent(patientChat.this, ChatDoc.class);
                            startActivity(i);
                        }

                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }


        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent = new Intent(patientChat.this, HomeDoctor.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(patientChat.this, ChatDoc.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_tip:
                        Intent intent2 = new Intent(patientChat.this, HistoriqueDoc.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_hospital:
                        Intent intent3 = new Intent(patientChat.this, Patients.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_profile:
                        Intent intent4 = new Intent(patientChat.this, ProfilDoc.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;
                }


                return false;
            }
        });


    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    message.setText(result.get(0));
                }
                break;
        }
    }


    private void collectData(String a, String b) {
//Connection
        try {

            URL url = new URL(urladdress + a + "&&id_user_sending=" + b);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //content
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            result = sb.toString();

        } catch (Exception ex) {
            ex.printStackTrace();

        }


        //ok


//JSON
        try {
            JSONArray ja = new JSONArray(result);

            JSONObject jo;
            name = new String[ja.length()];
            email = new String[ja.length()];
            number = new String[ja.length()];
            test = new String[ja.length()];
            imagepath = new String[ja.length()];



            String imageClient = getIntent().getStringExtra("imagepatient");
            String nomClient = getIntent().getStringExtra("nompatient");
            String prenomClient = getIntent().getStringExtra("prenompatient");

            SharedPreferences sharedPreferencess = getSharedPreferences("User", Context.MODE_PRIVATE);
            String ImageDocteur = sharedPreferencess.getString("image_prof","");
            String nomDocteur = sharedPreferencess.getString("nom","");
            String prenomDocteur = sharedPreferencess.getString("prenom","");
            String nametest="ok";
            String imagetest="";
            String idd = sharedPreferencess.getString("id", "");




            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                test[i] = jo.getString("email_receiver");


                if (test[i].equals(idd)){
                    nametest =nomClient+" "+prenomClient;
                    imagetest=   imageClient;


                }
                else
                {

                    nametest=nomDocteur+prenomDocteur;
                    imagetest=ImageDocteur;
                }








                name[i] = nametest;

                imagepath[i] = imagetest;
                email[i] = jo.getString("message");
                number[i] = jo.getString("date");


            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }


}