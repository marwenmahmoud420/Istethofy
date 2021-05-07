package com.hackforchange.teamBsissa.istethofyproject.Client;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.hackforchange.teamBsissa.istethofyproject.GlobalUrl;
import com.hackforchange.teamBsissa.istethofyproject.Model.Docteur;
import com.hackforchange.teamBsissa.istethofyproject.MySegleton;
import com.hackforchange.teamBsissa.istethofyproject.R;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DoctorDetails extends AppCompatActivity {

    TextView nom, prenom, region, adresse, email, tel, regionn, nometprenom;
    SimpleDraweeView imageuser;
    Button affecter;
    AlertDialog.Builder builder1;
    private ProgressDialog pDialog;
    LinearLayout password;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_doctor_details);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        tel = findViewById(R.id.tel);
        region = findViewById(R.id.region);
        adresse = findViewById(R.id.adresse);
        email = findViewById(R.id.mail);
        imageuser = (SimpleDraweeView) findViewById(R.id.imageuser);
        affecter = findViewById(R.id.choose);
        password = findViewById(R.id.password);
        password.setVisibility(View.INVISIBLE);

        regionn = findViewById(R.id.regionn);
        regionn.setText(getIntent().getStringExtra("regionDoc"));

        nometprenom = findViewById(R.id.nometprenom);
        nometprenom.setText(getIntent().getStringExtra("nomDoc") + " " + getIntent().getStringExtra("prenomDoc"));


        nom.setText(getIntent().getStringExtra("nomDoc"));
        prenom.setText(getIntent().getStringExtra("prenomDoc"));
        tel.setText(getIntent().getStringExtra("telDoc"));
        region.setText(getIntent().getStringExtra("adresseDoc"));
        adresse.setText(getIntent().getStringExtra("adresseDoc"));
        email.setText(getIntent().getStringExtra("emailDoc"));

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        if (getIntent().getStringExtra("idDoc").equals(sharedPreferences.getString("id_doc", ""))) {
            affecter.setVisibility(View.GONE);
        }
        builder1 = new AlertDialog.Builder(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);

        final Uri uri = Uri.parse(getIntent().getStringExtra("imageDoc"));
        imageuser.setImageURI(uri);


        affecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);

                String server_url = GlobalUrl.url + "/affecterdocteur/" + sharedPreferences.getString("id", "") + "/" + getIntent().getStringExtra("idDoc") + "";
                pDialog.setMessage("Logging in ...");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("id_doc", getIntent().getStringExtra("idDoc"));
                                editor.apply();
                                builder1.setTitle("Server Response");
                                builder1.setMessage("Response : " + response);
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences doc = getSharedPreferences("DocUser", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor e = doc.edit();
                                        e.putString("idd", getIntent().getStringExtra("idDoc"));
                                        e.putString("nomd", getIntent().getStringExtra("nomDoc"));
                                        e.putString("prenomd", getIntent().getStringExtra("prenomDoc"));
                                        e.putString("imaged", getIntent().getStringExtra("imageDoc"));
                                        e.putString("regiond", getIntent().getStringExtra("regionDoc"));
                                        e.putString("adressed", getIntent().getStringExtra("adresseDoc"));
                                        e.putString("teld", getIntent().getStringExtra("telDoc"));
                                        e.apply();
                                        Intent intent = new Intent(DoctorDetails.this, HomeClient.class);
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
                MySegleton.getmInstance(DoctorDetails.this).addTorequestque(stringRequest);

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
                        Intent intent = new Intent(DoctorDetails.this, HomeClient.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(DoctorDetails.this, ChatClient.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_tip:
                        Intent intent2 = new Intent(DoctorDetails.this, HistoriqueClient.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_hospital:
                        Intent intent3 = new Intent(DoctorDetails.this, Client_localisation.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_profile:
                        Intent intent4 = new Intent(DoctorDetails.this, ProfileClient.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;
                }


                return false;
            }
        });


    }


    public Docteur getList(String a) {
        final Docteur[] docteur = {new Docteur()};
        String server_url = GlobalUrl.url + "/getDoctor/" + a;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                server_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            Log.e("nameeee", jsonObject.getString("nom"));
                            SharedPreferences sharedPreferences = getSharedPreferences("docteurDetails", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nomD", jsonObject.getString("nom"));
                            editor.apply();
                            docteur[0] = new Docteur(jsonObject.getString("id"), jsonObject.getString("nom"), jsonObject.getString("prenom"), jsonObject.getString("email"), jsonObject.getString("tel"), jsonObject.getString("region"), jsonObject.getString("image_prof"), jsonObject.getString("adresse"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR...", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        MySegleton.getmInstance(DoctorDetails.this).addTorequestque(jsonArrayRequest);
        return docteur[0];

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

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
