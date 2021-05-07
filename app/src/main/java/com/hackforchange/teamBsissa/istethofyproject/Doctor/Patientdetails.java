package com.hackforchange.teamBsissa.istethofyproject.Doctor;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hackforchange.teamBsissa.istethofyproject.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.koushikdutta.ion.Ion;

public class Patientdetails extends AppCompatActivity {

    TextView nom, prenom, region, adresse, email, tel, regionn, nometprenom;
    SimpleDraweeView imageuser;
    Button affecter;
    LinearLayout pass;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_patientdetails);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        tel = findViewById(R.id.tel);
        region = findViewById(R.id.region);
        adresse = findViewById(R.id.adresse);
        email = findViewById(R.id.mail);
        imageuser = (SimpleDraweeView) findViewById(R.id.imageuser);
        affecter = findViewById(R.id.choose);
        pass = findViewById(R.id.password);

        pass.setVisibility(View.INVISIBLE);

        regionn = findViewById(R.id.regionn);
        regionn.setText(getIntent().getStringExtra("regionpatient"));

        nometprenom = findViewById(R.id.nometprenom);
        nometprenom.setText(getIntent().getStringExtra("nompatient") + " " + getIntent().getStringExtra("prenompatient"));

        nom.setText(getIntent().getStringExtra("nompatient"));
        prenom.setText(getIntent().getStringExtra("prenompatient"));
        tel.setText(getIntent().getStringExtra("telpatient"));
        region.setText(getIntent().getStringExtra("regionpatient"));
        adresse.setText(getIntent().getStringExtra("adressepatient"));
        email.setText(getIntent().getStringExtra("emailpatient"));

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);

        final Uri uri = Uri.parse(getIntent().getStringExtra("imagepatient"));
        imageuser.setImageURI(uri);

        affecter.setVisibility(View.GONE);
        affecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(Patientdetails.this);
                View view1 = getLayoutInflater().inflate(R.layout.pop_up_contact, null);
                ImageView contacttel = (ImageView) view1.findViewById(R.id.contacttel);
                ImageView contactemail = (ImageView) view1.findViewById(R.id.contactemail);
                ImageView contactsms = (ImageView) view1.findViewById(R.id.contactsms);

                contacttel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + getIntent().getStringExtra("telpatient")));
                        startActivity(callIntent);

                    }
                });
                contactsms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse("smsto:" + getIntent().getStringExtra("telpatient")));
                        startActivity(intent1);
                    }
                });
                contactemail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent emailIntent = new Intent(
                                android.content.Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent
                                .putExtra(
                                        android.content.Intent.EXTRA_EMAIL,
                                        new String[]{getIntent().getStringExtra("emailpatient")});
                        emailIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT,
                                "Hello There");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                "Add Message here");

                        emailIntent.setType("message/rfc822");

                        try {
                            startActivity(Intent.createChooser(emailIntent,
                                    "Send email using..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(Patientdetails.this,
                                    "No email clients installed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                mbuilder.setView(view1);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent = new Intent(Patientdetails.this, HomeDoctor.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(Patientdetails.this, ChatDoc.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_tip:
                        Intent intent2 = new Intent(Patientdetails.this, HistoriqueDoc.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_hospital:
                        Intent intent3 = new Intent(Patientdetails.this, Patients.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_profile:
                        Intent intent4 = new Intent(Patientdetails.this, ProfilDoc.class);
                        startActivity(intent4);
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
}
