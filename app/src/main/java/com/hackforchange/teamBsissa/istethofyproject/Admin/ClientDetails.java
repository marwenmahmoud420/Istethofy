package com.hackforchange.teamBsissa.istethofyproject.Admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackforchange.teamBsissa.istethofyproject.GlobalUrl;
import com.hackforchange.teamBsissa.istethofyproject.MySegleton;
import com.hackforchange.teamBsissa.istethofyproject.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.koushikdutta.ion.Ion;

public class ClientDetails extends AppCompatActivity {
    TextView nom, prenom, region, adresse, email, tel, a, b;
    SimpleDraweeView imageuser;
    Button affecter;
    LinearLayout password;
    AlertDialog.Builder builder1;
    private ProgressDialog pDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_client_details);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        tel = findViewById(R.id.tel);
        region = findViewById(R.id.region);
        adresse = findViewById(R.id.adresse);
        email = findViewById(R.id.mail);
        imageuser = (SimpleDraweeView) findViewById(R.id.imageuser);
        affecter = findViewById(R.id.choose);
        password = findViewById(R.id.password);
        password.setVisibility(View.INVISIBLE);

        nom.setText(getIntent().getStringExtra("nompatient"));
        prenom.setText(getIntent().getStringExtra("prenompatient"));
        tel.setText(getIntent().getStringExtra("telpatient"));
        region.setText(getIntent().getStringExtra("regionpatient"));
        adresse.setText(getIntent().getStringExtra("adressepatient"));
        email.setText(getIntent().getStringExtra("emailpatient"));
        email.setText(getIntent().getStringExtra("emailpatient"));
        a.setText(getIntent().getStringExtra("nompatient") + " " + getIntent().getStringExtra("prenompatient"));
        b.setText(getIntent().getStringExtra("regionpatient"));
        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);

        final Uri uri = Uri.parse(getIntent().getStringExtra("imagepatient"));
        imageuser.setImageURI(uri);

        builder1 = new AlertDialog.Builder(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        affecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                String server_url = GlobalUrl.url + "/bloquerclient/" + getIntent().getStringExtra("idpatient") + "/0";
                pDialog.setMessage("Logging in ...");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                builder1.setTitle("Server Response");
                                builder1.setMessage("Response : " + response);
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(ClientDetails.this, VerifDoc.class);
                                        startActivity(intent);

                                    }

                                });
                                AlertDialog alertDialog = builder1.create();
                                alertDialog.show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
                );
                MySegleton.getmInstance(ClientDetails.this).addTorequestque(stringRequest);

            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_tip:
                        Intent intent = new Intent(ClientDetails.this, AddComm.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);

                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(ClientDetails.this, VerifDoc.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_home:
                        Intent intent2 = new Intent(ClientDetails.this, AddComm.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;


                }


                return false;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    imageuser.setImageURI(data.getData());
                }
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
}
