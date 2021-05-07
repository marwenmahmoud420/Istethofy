package com.hackforchange.teamBsissa.istethofyproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hackforchange.teamBsissa.istethofyproject.Admin.AddComm;
import com.hackforchange.teamBsissa.istethofyproject.Client.HomeClient;
import com.hackforchange.teamBsissa.istethofyproject.Commercial.AddDoc;
import com.hackforchange.teamBsissa.istethofyproject.Doctor.HomeDoctor;
import com.hackforchange.teamBsissa.istethofyproject.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button btnup, btnlogin;
    EditText email, password;
    String mail, pass;
    AlertDialog.Builder builder;
    private ProgressDialog pDialog;
    private DocteurSessionManager docteurSessionManager;
    private ClientSessionManager clientSessionManager;
    private CommercialSessionManager commercialSessionManager;
    private AdminSessionManager adminSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnup = (Button) findViewById(R.id.btnup);
        btnlogin = (Button) findViewById(R.id.btnlogin);

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        builder = new AlertDialog.Builder(this);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        docteurSessionManager = new DocteurSessionManager(getApplicationContext());
        clientSessionManager = new ClientSessionManager(getApplicationContext());
        commercialSessionManager = new CommercialSessionManager(getApplicationContext());
        adminSessionManager = new AdminSessionManager(getApplicationContext());


        if (adminSessionManager.isLoggedIn()) {
            //   User is already logged in.Take him to main activity
            Intent intent = new Intent(LoginActivity.this, AddComm.class);
            startActivity(intent);
            finish();
        }

        if (clientSessionManager.isLoggedIn()) {
            //   User is already logged in.Take him to main activity
            Intent intent = new Intent(LoginActivity.this, HomeClient.class);
            startActivity(intent);
            finish();
        }

        if (commercialSessionManager.isLoggedIn()) {
            //   User is already logged in.Take him to main activity
            Intent intent = new Intent(LoginActivity.this, AddDoc.class);
            startActivity(intent);
            finish();
        }

        if (docteurSessionManager.isLoggedIn()) {
            //   User is already logged in.Take him to main activity
            Intent intent = new Intent(LoginActivity.this, HomeDoctor.class);
            startActivity(intent);
            finish();
        }


        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = email.getText().toString();
                pass = password.getText().toString();
                showDialog();

                String server_url = GlobalUrl.url + "/login/" + mail + "/" + pass + "";
                pDialog.setMessage("Logging in ...");

                if (mail.equals("admin") && pass.equals("admin")) {
                    hideDialog();
                    adminSessionManager.setLogin(true);
                    Intent i = new Intent(LoginActivity.this, AddComm.class);
                    startActivity(i);
                } else if (!mail.isEmpty() && !pass.isEmpty()) {

                    JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST, server_url, null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(final JSONArray response) {
                                    hideDialog();
                                    builder.setTitle("Authentification");
                                    try {
                                        builder.setMessage("" + response.getJSONObject(1).getString("response"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            String a = "Doctor connected !!!!";
                                            String b = "Client connected !!!!";
                                            String c = "Commercial connected !!!!";
                                            try {
                                                JSONObject jsonObject = response.getJSONObject(1);
                                                JSONObject user = response.getJSONObject(0);
                                                if (jsonObject.getString("response").contains(b)) {
                                                    hideDialog();
                                                    clientSessionManager.setLogin(true);
                                                    editor.putString("id", user.getString("id"));
                                                    editor.putString("id_doc", user.getString("id_doc"));
                                                    editor.putString("nom", user.getString("nom"));
                                                    editor.putString("prenom", user.getString("prenom"));
                                                    editor.putString("email", user.getString("email"));
                                                    editor.putString("tel", user.getString("tel"));
                                                    editor.putString("region", user.getString("region"));
                                                    editor.putString("age", user.getString("age"));
                                                    editor.putString("image_prof", user.getString("image_prof"));
                                                    editor.putString("password", user.getString("password"));
                                                    editor.putString("role","1");
                                                    editor.apply();
                                                    Log.e("idddddd",sharedPreferences.getString("id",""));
                                                    Log.e("photo",sharedPreferences.getString("image_prof",""));
                                                    Intent i = new Intent(LoginActivity.this, HomeClient.class);
                                                    startActivity(i);
                                                } else if (jsonObject.getString("response").contains(a)) {
                                                    hideDialog();
                                                    docteurSessionManager.setLogin(true);
                                                    editor.putString("id", user.getString("id"));
                                                    editor.putString("id_com", user.getString("id_com"));
                                                    editor.putString("nom", user.getString("nom"));
                                                    editor.putString("prenom", user.getString("prenom"));
                                                    editor.putString("email", user.getString("email"));
                                                    editor.putString("tel", user.getString("tel"));
                                                    editor.putString("region", user.getString("region"));
                                                    editor.putString("password", user.getString("password"));
                                                    editor.putString("image_cin", user.getString("image_cin"));                                                    editor.apply();
                                                    editor.putString("image_prof", user.getString("image_prof"));                                                    editor.apply();
                                                    editor.putString("adresse", user.getString("adresse"));
                                                    editor.putString("role","0");
                                                    editor.apply();
                                                    Intent i = new Intent(LoginActivity.this, HomeDoctor.class);
                                                    startActivity(i);
                                                } else if (jsonObject.getString("response").contains(c)) {
                                                    hideDialog();
                                                    commercialSessionManager.setLogin(true);
                                                    editor.putString("emailU", mail);
                                                    editor.putString("id", user.getString("id"));
                                                    editor.putString("idCom", user.getString("id"));
                                                    editor.apply();
                                                    Intent i = new Intent(LoginActivity.this, AddDoc.class);
                                                    startActivity(i);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }

                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
                            Toast.makeText(getApplicationContext(),
                                    error.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                    );

                    MySegleton.getmInstance(LoginActivity.this).addTorequestque(stringRequest);


                } else {
                    hideDialog();
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
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





    @Override
    public void onBackPressed() {

        //  super.onBackPressed();

    }

}
