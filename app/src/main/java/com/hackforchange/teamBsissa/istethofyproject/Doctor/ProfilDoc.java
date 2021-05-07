package com.hackforchange.teamBsissa.istethofyproject.Doctor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackforchange.teamBsissa.istethofyproject.DocteurSessionManager;
import com.hackforchange.teamBsissa.istethofyproject.GlobalUrl;
import com.hackforchange.teamBsissa.istethofyproject.LoginActivity;
import com.hackforchange.teamBsissa.istethofyproject.MySegleton;
import com.hackforchange.teamBsissa.istethofyproject.R;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.Future;

public class ProfilDoc extends AppCompatActivity {

    LinearLayout firstname, name, phone, email, password;
    TextView nom, prenom, tel, mail, motdepasse, region, regionn, nometprenom;
    AlertDialog.Builder builder1;
    SimpleDraweeView imageuser;
    private ProgressDialog pDialog;
    String path;
    Button Logout, valider, reclamation;
    private DocteurSessionManager session;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profil_doc);


        Logout = (Button) findViewById(R.id.btnLogout);
        valider = (Button) findViewById(R.id.valider);
        reclamation = (Button) findViewById(R.id.reclamation);

        imageuser = (SimpleDraweeView) findViewById(R.id.imageuser);

        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
        builder1 = new AlertDialog.Builder(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new DocteurSessionManager(getApplicationContext());

        final SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final Uri uri = Uri.parse(sharedPreferences.getString("image_prof", ""));
        Log.e("photo", sharedPreferences.getString("image_prof", ""));
        imageuser.setImageURI(uri);

        valider.setVisibility(View.INVISIBLE);
        imageuser.setOnClickListener(new View.OnClickListener() {
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

        region = findViewById(R.id.region);
        region.setText(sharedPreferences.getString("adresseDoc", ""));

        regionn = findViewById(R.id.regionn);
        regionn.setText(sharedPreferences.getString("region", ""));

        nometprenom = findViewById(R.id.nometprenom);
        nometprenom.setText(sharedPreferences.getString("nom", "") + " " + sharedPreferences.getString("prenom", ""));

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = new File(path);
                Log.e("ffffffd5al", path);
                pDialog.setMessage("Logging in ...");
                showDialog();
                Future uploading = Ion.with(ProfilDoc.this)
                        .load(GlobalUrl.url + "/docteurprofileimage/'" + sharedPreferences.getString("id", "") + "'")
                        .setMultipartFile("image", f)
                        .asString()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<String>>() {
                            @Override
                            public void onCompleted(Exception e, com.koushikdutta.ion.Response<String> result) {
                                try {
                                    path = path.replace("/storage/emulated/0/DCIM/Facebook", GlobalUrl.url + "");
                                    Log.e("ffffff2222", path);
                                    editor.putString("image_prof", path);
                                    editor.apply();
                                    Intent intent = new Intent(ProfilDoc.this, ProfilDoc.class);
                                    ProfilDoc.this.startActivity(intent);

                                    hideDialog();
                                    JSONObject jobj = new JSONObject(result.getResult());
                                    Toast.makeText(getApplicationContext(), jobj.getString("response"), Toast.LENGTH_SHORT).show();

                                } catch (JSONException e1) {
                                    hideDialog();
                                    e1.printStackTrace();
                                }

                            }
                        });
            }
        });

        firstname = findViewById(R.id.firstname);
        nom = findViewById(R.id.nom);
        nom.setText(sharedPreferences.getString("nom", ""));
        firstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(ProfilDoc.this);
                View view1 = getLayoutInflater().inflate(R.layout.pop_up_update_profile, null);
                final EditText mvaleur = (EditText) view1.findViewById(R.id.input);
                Button menchere = (Button) view1.findViewById(R.id.validate);

                menchere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mvaleur.getText().toString().isEmpty()) {

                            String id = sharedPreferences.getString("id", "");
                            String server_url = GlobalUrl.url + "/updatenomdocteur/" + id + "/" + mvaleur.getText().toString() + "";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,

                                    new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("nom", mvaleur.getText().toString());
                                            editor.apply();
                                            builder1.setTitle("Server Response");
                                            builder1.setMessage("Response : " + response);
                                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    //  PhoneNumber.setText(mvaleur.getText().toString());
                                                    Intent intent = new Intent(ProfilDoc.this, ProfilDoc.class);
                                                    startActivity(intent);

                                                }

                                            });
                                            AlertDialog alertDialog = builder1.create();
                                            alertDialog.show();

                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),
                                            error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                            );
                            MySegleton.getmInstance(ProfilDoc.this).addTorequestque(stringRequest);


                        } else {
                            Toast.makeText(ProfilDoc.this, "nn 3abi se3a bb yehdik", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                mbuilder.setView(view1);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
        });


        name = findViewById(R.id.name);
        prenom = findViewById(R.id.prenom);
        prenom.setText(sharedPreferences.getString("prenom", ""));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(ProfilDoc.this);
                View view1 = getLayoutInflater().inflate(R.layout.pop_up_update_profile, null);
                final EditText mvaleur = (EditText) view1.findViewById(R.id.input);
                Button menchere = (Button) view1.findViewById(R.id.validate);

                menchere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mvaleur.getText().toString().isEmpty()) {

                            String id = sharedPreferences.getString("id", "");
                            String server_url = GlobalUrl.url + "/updateprenomdocteur/" + id + "/" + mvaleur.getText().toString() + "";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,

                                    new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("prenom", mvaleur.getText().toString());
                                            editor.apply();
                                            builder1.setTitle("Server Response");
                                            builder1.setMessage("Response : " + response);
                                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    //  PhoneNumber.setText(mvaleur.getText().toString());
                                                    Intent intent = new Intent(ProfilDoc.this, ProfilDoc.class);
                                                    startActivity(intent);

                                                }

                                            });
                                            AlertDialog alertDialog = builder1.create();
                                            alertDialog.show();

                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),
                                            error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                            );
                            MySegleton.getmInstance(ProfilDoc.this).addTorequestque(stringRequest);


                        } else {
                            Toast.makeText(ProfilDoc.this, "nn 3abi se3a bb yehdik", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                mbuilder.setView(view1);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
        });


        phone = findViewById(R.id.phone);
        tel = findViewById(R.id.tel);
        tel.setText(sharedPreferences.getString("tel", ""));
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(ProfilDoc.this);
                View view1 = getLayoutInflater().inflate(R.layout.pop_up_update_profile, null);
                final EditText mvaleur = (EditText) view1.findViewById(R.id.input);
                Button menchere = (Button) view1.findViewById(R.id.validate);

                menchere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mvaleur.getText().toString().isEmpty()) {

                            String id = sharedPreferences.getString("id", "");
                            String server_url = GlobalUrl.url + "/updateteldocteur/" + id + "/" + mvaleur.getText().toString() + "";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,

                                    new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("tel", mvaleur.getText().toString());
                                            editor.apply();
                                            builder1.setTitle("Server Response");
                                            builder1.setMessage("Response : " + response);
                                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    //  PhoneNumber.setText(mvaleur.getText().toString());
                                                    Intent intent = new Intent(ProfilDoc.this, ProfilDoc.class);
                                                    startActivity(intent);

                                                }

                                            });
                                            AlertDialog alertDialog = builder1.create();
                                            alertDialog.show();

                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),
                                            error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                            );
                            MySegleton.getmInstance(ProfilDoc.this).addTorequestque(stringRequest);


                        } else {
                            Toast.makeText(ProfilDoc.this, "nn 3abi se3a bb yehdik", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                mbuilder.setView(view1);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
        });


        email = findViewById(R.id.email);
        mail = findViewById(R.id.mail);
        mail.setText(sharedPreferences.getString("email", ""));
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(ProfilDoc.this);
                View view1 = getLayoutInflater().inflate(R.layout.pop_up_update_profile, null);
                final EditText mvaleur = (EditText) view1.findViewById(R.id.input);
                Button menchere = (Button) view1.findViewById(R.id.validate);

                menchere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mvaleur.getText().toString().isEmpty()) {

                            String id = sharedPreferences.getString("id", "");
                            String server_url = GlobalUrl.url + "/updateemaildocteur/" + id + "/" + mvaleur.getText().toString() + "";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,

                                    new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("email", mvaleur.getText().toString());
                                            editor.apply();
                                            builder1.setTitle("Server Response");
                                            builder1.setMessage("Response : " + response);
                                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    //  PhoneNumber.setText(mvaleur.getText().toString());
                                                    Intent intent = new Intent(ProfilDoc.this, ProfilDoc.class);
                                                    startActivity(intent);

                                                }

                                            });
                                            AlertDialog alertDialog = builder1.create();
                                            alertDialog.show();

                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),
                                            error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                            );
                            MySegleton.getmInstance(ProfilDoc.this).addTorequestque(stringRequest);


                        } else {
                            Toast.makeText(ProfilDoc.this, "nn 3abi se3a bb yehdik", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                mbuilder.setView(view1);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
        });


        password = findViewById(R.id.password);
        motdepasse = findViewById(R.id.motdepasse);
        motdepasse.setText(sharedPreferences.getString("password", ""));
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(ProfilDoc.this);
                View view1 = getLayoutInflater().inflate(R.layout.pop_up_update_profile, null);
                final EditText mvaleur = (EditText) view1.findViewById(R.id.input);
                Button menchere = (Button) view1.findViewById(R.id.validate);

                menchere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mvaleur.getText().toString().isEmpty()) {

                            String id = sharedPreferences.getString("id", "");
                            String server_url = GlobalUrl.url + "/updatepassworddocteur/" + id + "/" + mvaleur.getText().toString() + "";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,

                                    new com.android.volley.Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("password", mvaleur.getText().toString());
                                            editor.apply();
                                            builder1.setTitle("Server Response");
                                            builder1.setMessage("Response : " + response);
                                            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    //  PhoneNumber.setText(mvaleur.getText().toString());
                                                    Intent intent = new Intent(ProfilDoc.this, ProfilDoc.class);
                                                    startActivity(intent);

                                                }

                                            });
                                            AlertDialog alertDialog = builder1.create();
                                            alertDialog.show();

                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(),
                                            error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                            );
                            MySegleton.getmInstance(ProfilDoc.this).addTorequestque(stringRequest);


                        } else {
                            Toast.makeText(ProfilDoc.this, "nn 3abi se3a bb yehdik", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                mbuilder.setView(view1);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
        });

        reclamation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(
                        android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent
                        .putExtra(
                                android.content.Intent.EXTRA_EMAIL,
                                new String[]{"istethofy@gmail.com"});
                emailIntent.putExtra(
                        android.content.Intent.EXTRA_SUBJECT,
                        "Reclamation");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        "Add Message here");

                emailIntent.setType("message/rfc822");

                try {
                    startActivity(Intent.createChooser(emailIntent,
                            "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ProfilDoc.this,
                            "No email clients installed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);
                editor.clear();
                editor.apply();
                editor.commit();
                Intent i = new Intent(ProfilDoc.this, LoginActivity.class);
                startActivity(i);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent = new Intent(ProfilDoc.this, HomeDoctor.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(ProfilDoc.this, ChatDoc.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_tip:
                        Intent intent2 = new Intent(ProfilDoc.this, HistoriqueDoc.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_hospital:
                        Intent intent3 = new Intent(ProfilDoc.this, Patients.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_profile:
                        Intent intent4 = new Intent(ProfilDoc.this, ProfilDoc.class);
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
                    path = getPathFromURI(data.getData());
                    imageuser.setImageURI(data.getData());
                    valider.setVisibility(View.VISIBLE);


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
