package com.hackforchange.teamBsissa.istethofyproject.Admin;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class VerifDetailsDoc extends AppCompatActivity {

    TextView nom, prenom, region, adresse, email, tel;
    SimpleDraweeView imageuser,cin;
    Button affecter;
    AlertDialog.Builder builder1;
    private ProgressDialog pDialog;
    LinearLayout password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_verif_details_doc);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        tel = findViewById(R.id.tel);
        region = findViewById(R.id.region);
        adresse = findViewById(R.id.adresse);
        email = findViewById(R.id.mail);
        imageuser = (SimpleDraweeView) findViewById(R.id.imageuser);
        cin = (SimpleDraweeView) findViewById(R.id.cin);
        affecter = findViewById(R.id.accepter);
        password = findViewById(R.id.password);
        password.setVisibility(View.INVISIBLE);
        nom.setText(getIntent().getStringExtra("nomDoc"));
        prenom.setText(getIntent().getStringExtra("prenomDoc"));
        tel.setText(getIntent().getStringExtra("telDoc"));
        region.setText(getIntent().getStringExtra("regionDoc"));
        adresse.setText(getIntent().getStringExtra("adresseDoc"));
        email.setText(getIntent().getStringExtra("emailDoc"));


        builder1 = new AlertDialog.Builder(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);

        final Uri uri = Uri.parse(getIntent().getStringExtra("imageDoc"));
        imageuser.setImageURI(uri);
        final Uri urii = Uri.parse(getIntent().getStringExtra("cinDoc"));
        cin.setImageURI(urii);


        affecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                String server_url = GlobalUrl.url + "/accepter/" + getIntent().getStringExtra("idDoc") + "/1";
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

                                        Intent intent = new Intent(VerifDetailsDoc.this, VerifDoc.class);
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
                MySegleton.getmInstance(VerifDetailsDoc.this).addTorequestque(stringRequest);

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
