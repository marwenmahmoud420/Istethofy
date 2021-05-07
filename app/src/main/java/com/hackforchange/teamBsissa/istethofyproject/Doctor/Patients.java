package com.hackforchange.teamBsissa.istethofyproject.Doctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hackforchange.teamBsissa.istethofyproject.GlobalUrl;
import com.hackforchange.teamBsissa.istethofyproject.Model.Client;
import com.hackforchange.teamBsissa.istethofyproject.MySegleton;
import com.hackforchange.teamBsissa.istethofyproject.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Patients extends AppCompatActivity {

    private static Context mContext;
    ArrayList<Client> arrayListtt = new ArrayList<>();
    AlertDialog.Builder builder;

    SimpleStringRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        mContext = Patients.this;

        arrayListtt = getList();
        builder = new AlertDialog.Builder(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerviewproductssimilaire);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        adapter = new SimpleStringRecyclerViewAdapter(recyclerView, arrayListtt);
        recyclerView.setAdapter(adapter);


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
                        Intent intent = new Intent(Patients.this, HomeDoctor.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(Patients.this, ChatDoc.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_tip:
                        Intent intent2 = new Intent(Patients.this, HistoriqueDoc.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_hospital:
                        Intent intent3 = new Intent(Patients.this, Patients.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_profile:
                        Intent intent4 = new Intent(Patients.this, ProfilDoc.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;
                }


                return false;
            }
        });

    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<Patients.SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Client> mWishlistImageUri;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView region, nom;
            public final LinearLayout layout_item;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                region = (TextView) itemView.findViewById(R.id.region);
                nom = (TextView) itemView.findViewById(R.id.nom);
                layout_item = (LinearLayout) itemView.findViewById(R.id.layout_item);

            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<Client> wishlistImageUri) {
            mWishlistImageUri = wishlistImageUri;
            mRecyclerView = recyclerView;
        }

        @Override
        public Patients.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new Patients.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final Patients.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {


            holder.region.setText(mWishlistImageUri.get(position).getRegion());
            holder.nom.setText(mWishlistImageUri.get(position).getNom());

            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, Patientdetails.class);
                    i.putExtra("idpatient", mWishlistImageUri.get(position).getId());
                    i.putExtra("nompatient", mWishlistImageUri.get(position).getNom());
                    i.putExtra("telpatient", mWishlistImageUri.get(position).getTel());
                    i.putExtra("regionpatient", mWishlistImageUri.get(position).getRegion());
                    i.putExtra("emailpatient", mWishlistImageUri.get(position).getEmail());
                    i.putExtra("prenompatient", mWishlistImageUri.get(position).getPrenom());
                    i.putExtra("imagepatient", mWishlistImageUri.get(position).getImage_prof());
                    mContext.startActivity(i);

                }
            });
            holder.layout_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder mbuilder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view1 = inflater.inflate(R.layout.pop_up_contact, null);
                    ImageView contacttel = (ImageView) view1.findViewById(R.id.contacttel);
                    ImageView contactemail = (ImageView) view1.findViewById(R.id.contactemail);
                    ImageView contactsms = (ImageView) view1.findViewById(R.id.contactsms);

                    contacttel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + mWishlistImageUri.get(position).getTel()));
                            mContext.startActivity(callIntent);

                        }
                    });
                    contactsms.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent1 = new Intent(Intent.ACTION_VIEW);
                            intent1.setData(Uri.parse("smsto:" + mWishlistImageUri.get(position).getTel()));
                            mContext.startActivity(intent1);
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
                                            new String[]{mWishlistImageUri.get(position).getEmail()});
                            emailIntent.putExtra(
                                    android.content.Intent.EXTRA_SUBJECT,
                                    "Hello There");
                            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                                    "Add Message here");

                            emailIntent.setType("message/rfc822");

                            try {
                                mContext.startActivity(Intent.createChooser(emailIntent,
                                        "Send email using..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(mContext,
                                        "No email clients installed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    mbuilder.setView(view1);
                    AlertDialog dialog = mbuilder.create();
                    dialog.show();
                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            return mWishlistImageUri.size();
        }

    }


    public ArrayList<Client> getList() {
        final int[] size = {0};
        final ArrayList<Client> arrayList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String server_url = GlobalUrl.url + "/getclients/" + sharedPreferences.getString("id", "");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                server_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int count = 0;
                        while (count < response.length()) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                Client userComm = new Client(jsonObject.getString("id"), jsonObject.getString("nom"), jsonObject.getString("prenom"), jsonObject.getString("email"), jsonObject.getString("tel"), jsonObject.getString("region"), jsonObject.getString("image_prof"));
                                arrayList.add(userComm);
                                count++;
                                size[0]++;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter.notifyDataSetChanged();

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR...", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        MySegleton.getmInstance(Patients.this).addTorequestque(jsonArrayRequest);
        return arrayList;

    }

}
