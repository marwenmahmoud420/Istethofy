package com.hackforchange.teamBsissa.istethofyproject;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackforchange.teamBsissa.istethofyproject.R;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Future;

public class RegisterActivity extends AppCompatActivity {

    EditText nom, prenom, email, tel, password, password2, age, adresse;
    Button Register, Login;
    Spinner region, role;
    LinearLayout doctors;
    ImageView cindoctor;
    String name, lastName, telifoun, regionn, mail, pass, pass2, agge, adres;
    AlertDialog.Builder builder;
    private ProgressDialog pDialog;


    String path;
    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        email = (EditText) findViewById(R.id.mail);
        tel = (EditText) findViewById(R.id.tel);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        age = (EditText) findViewById(R.id.age);
        adresse = (EditText) findViewById(R.id.adresse);


        region = (Spinner) findViewById(R.id.region);
        role = (Spinner) findViewById(R.id.role);

        Register = (Button) findViewById(R.id.btnRegister);
        Login = (Button) findViewById(R.id.btnlogin);

        doctors = (LinearLayout) findViewById(R.id.doctors);

        cindoctor = (ImageView) findViewById(R.id.cindoctor);

        builder = new AlertDialog.Builder(this);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (role.getSelectedItem().toString().equals("Docteur")) {
                    age.setVisibility(View.INVISIBLE);
                    doctors.setVisibility(View.VISIBLE);
                } else if (role.getSelectedItem().toString().equals("Client")) {
                    age.setVisibility(View.VISIBLE);
                    doctors.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


        cindoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fintent = new Intent(Intent.ACTION_GET_CONTENT);
                fintent.setType("image/jpeg");
                try {
                    startActivityForResult(fintent, 100);
                } catch (ActivityNotFoundException e) {

                }
            }
        });

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nom.getText().toString();
                lastName = prenom.getText().toString();
                regionn = region.getSelectedItem().toString();
                mail = email.getText().toString();
                pass = password.getText().toString();
                pass2 = password2.getText().toString();
                telifoun = tel.getText().toString();
                adres = adresse.getText().toString();
                agge = age.getText().toString();
                if (role.getSelectedItem().equals("Docteur")) {
                    if (!name.isEmpty() && !lastName.isEmpty() && !mail.isEmpty() && !pass.isEmpty() && !pass2.isEmpty() && a == 1 && !adres.isEmpty())

                    {
                        File f = new File(path);
                        pDialog.setMessage("Registration ...");
                        showDialog();
                        String server_url = GlobalUrl.url + "/addDoctor/" + name + "/" + lastName + "/" + telifoun + "/" + mail + "/" + regionn + "/" + pass + "/" + adres + "";
                        String str = server_url.replaceAll(" ", "%20");
                        Future uploading = Ion.with(RegisterActivity.this)
                                .load(str)
                                .setMultipartFile("image", f)
                                .asString()
                                .withResponse()
                                .setCallback(new FutureCallback<Response<String>>() {
                                    @Override
                                    public void onCompleted(Exception e, Response<String> result) {
                                        try {
                                            hideDialog();
                                            JSONObject jobj = new JSONObject(result.getResult());
                                            Toast.makeText(getApplicationContext(), jobj.getString("response"), Toast.LENGTH_SHORT).show();
                                            String a = "added";
                                            if (jobj.getString("response").contains(a)) {
                                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(i);
                                            }
                                        } catch (JSONException e1) {
                                            hideDialog();
                                            e1.printStackTrace();
                                        }

                                    }
                                });

                    } else {
                        builder.setTitle("Error");
                        builder.setMessage("Response :doctor alert");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }

                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                } else {

                    if (!name.isEmpty() && !lastName.isEmpty() && !mail.isEmpty() && !pass.isEmpty() && !pass2.isEmpty() && !agge.isEmpty())

                    {
                        String server_url = GlobalUrl.url + "/addclient/" + name + "/" + lastName + "/" + telifoun + "/" + mail + "/" + agge + "/" + regionn + "/" + pass + "";
                        String str = server_url.replaceAll(" ", "%20");

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, str,

                                new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response) {

                                        hideDialog();
                                        builder.setTitle("Enregistrement");
                                        builder.setMessage("" + response);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String a = "added";
                                                if (response.contains(a)) {
                                                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(i);
                                                }
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
                        MySegleton.getmInstance(RegisterActivity.this).addTorequestque(stringRequest);
                    } else {
                        builder.setTitle("Error");
                        builder.setMessage("Response :client alert");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }

                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                }

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


    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    path = decodeFile(getPathFromURI(data.getData()));
                    cindoctor.setImageURI(data.getData());
                    a = 1;

                }
        }
    }

    private String decodeFile(String path) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, 20, 20, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= 800 && unscaledBitmap.getHeight() <= 800)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 20, 20, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/myTmpDir");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }


}
