package com.hackforchange.teamBsissa.istethofyproject.Commercial;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackforchange.teamBsissa.istethofyproject.CommercialSessionManager;
import com.hackforchange.teamBsissa.istethofyproject.GlobalUrl;
import com.hackforchange.teamBsissa.istethofyproject.LoginActivity;
import com.hackforchange.teamBsissa.istethofyproject.MySegleton;
import com.hackforchange.teamBsissa.istethofyproject.R;


public class AddDoc extends AppCompatActivity {

    EditText nom, prenom, email, tel, password, password2, age, adresse;
    Button Register, commerciallogout, doc;
    Spinner region;
    private AnimationDrawable anim;

    String name, lastName, telifoun, regionn, mail, pass, pass2, agge, adres;
    AlertDialog.Builder builder;
    private ProgressDialog pDialog;
    private CommercialSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doc);
        doc = findViewById(R.id.doc);
        anim = (AnimationDrawable) doc.getBackground();
        anim.setEnterFadeDuration(2300);
        anim.setExitFadeDuration(2300);


        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        email = (EditText) findViewById(R.id.mail);
        tel = (EditText) findViewById(R.id.tel);
        password = (EditText) findViewById(R.id.password);
        adresse = (EditText) findViewById(R.id.adresse);
        commerciallogout = (Button) findViewById(R.id.commerciallogout);


        region = (Spinner) findViewById(R.id.region);

        Register = (Button) findViewById(R.id.btnRegister);

        session = new CommercialSessionManager(getApplicationContext());

        final SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        builder = new AlertDialog.Builder(this);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nom.getText().toString();
                lastName = prenom.getText().toString();
                regionn = region.getSelectedItem().toString();
                mail = email.getText().toString();
                pass = password.getText().toString();
                telifoun = tel.getText().toString();
                adres = adresse.getText().toString();

                if (!name.isEmpty() && !lastName.isEmpty() && !mail.isEmpty() && !pass.isEmpty() && !telifoun.isEmpty() && !adres.isEmpty())

                {

                    pDialog.setMessage("Registration ...");
                    showDialog();
                    SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);

                    String server_url = GlobalUrl.url + "/adddocom/" + name + "/" + lastName + "/" + telifoun + "/" + mail + "/" + regionn + "/" + pass + "/" + adres + "/" + sharedPreferences.getString("idCom", "") + "";
                    String str = server_url.replaceAll(" ", "%20");


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, str,

                            new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    hideDialog();
                                    builder.setTitle("saving");
                                    builder.setMessage("" + response);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(AddDoc.this, AddDoc.class);
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
                    MySegleton.getmInstance(AddDoc.this).addTorequestque(stringRequest);
                } else {
                    builder.setTitle("Error");
                    builder.setMessage("Response :client alert");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(AddDoc.this, AddDoc.class);
                            startActivity(i);

                        }

                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }


        });


        commerciallogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);
                editor.clear();
                editor.apply();
                editor.commit();
                Intent i = new Intent(AddDoc.this, LoginActivity.class);
                startActivity(i);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent = new Intent(AddDoc.this, AddDoc.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(AddDoc.this, AllDoc.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;


                }


                return false;
            }
        });



    }
    protected void onResume(){
        super.onResume();
        if(anim!=null && !anim.isRunning()){
            anim.start();
        }
    }

    protected void onPause(){
        super.onPause();
        if(anim!=null && anim.isRunning()){
            anim.stop();
        }

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