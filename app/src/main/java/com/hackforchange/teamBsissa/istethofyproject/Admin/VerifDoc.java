package com.hackforchange.teamBsissa.istethofyproject.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hackforchange.teamBsissa.istethofyproject.GlobalUrl;
import com.hackforchange.teamBsissa.istethofyproject.Model.Docteur;
import com.hackforchange.teamBsissa.istethofyproject.MySegleton;
import com.hackforchange.teamBsissa.istethofyproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VerifDoc extends AppCompatActivity {

    private static Context mContext;
    ArrayList<Docteur> arrayListtt = new ArrayList<>();
    AlertDialog.Builder builder;

    SimpleStringRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif_doc);


        mContext = VerifDoc.this;

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
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_message:

                        break;

                    case R.id.nav_tip:
                        Intent intent1 = new Intent(VerifDoc.this, HistoriqueAdmin.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_home:
                        Intent intent2 = new Intent(VerifDoc.this, AddComm.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;


                }


                return false;
            }
        });


    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<VerifDoc.SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Docteur> mWishlistImageUri;
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

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<Docteur> wishlistImageUri) {
            mWishlistImageUri = wishlistImageUri;
            mRecyclerView = recyclerView;
        }

        @Override
        public VerifDoc.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new VerifDoc.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final VerifDoc.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {


            holder.region.setText(mWishlistImageUri.get(position).getRegion());
            holder.nom.setText(mWishlistImageUri.get(position).getNom());
            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, VerifDetailsDoc.class);
                    i.putExtra("idDoc", mWishlistImageUri.get(position).getId());
                    i.putExtra("nomDoc", mWishlistImageUri.get(position).getNom());
                    i.putExtra("adresseDoc", mWishlistImageUri.get(position).getAdresse());
                    i.putExtra("telDoc", mWishlistImageUri.get(position).getTel());
                    i.putExtra("regionDoc", mWishlistImageUri.get(position).getRegion());
                    i.putExtra("emailDoc", mWishlistImageUri.get(position).getEmail());
                    i.putExtra("prenomDoc", mWishlistImageUri.get(position).getPrenom());
                    i.putExtra("imageDoc", mWishlistImageUri.get(position).getImage_prof());
                    i.putExtra("cinDoc", mWishlistImageUri.get(position).getCin());
                    mContext.startActivity(i);

                }
            });

        }

        @Override
        public int getItemCount() {
            return mWishlistImageUri.size();
        }

    }


    public ArrayList<Docteur> getList() {
        final int[] size = {0};
        final ArrayList<Docteur> arrayList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String server_url = GlobalUrl.url + "/verifdocs";
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
                                Docteur userComm = new Docteur(jsonObject.getString("id"), jsonObject.getString("nom"), jsonObject.getString("prenom"), jsonObject.getString("email"), jsonObject.getString("tel"), jsonObject.getString("region"), jsonObject.getString("image_prof"), jsonObject.getString("adresse"), jsonObject.getString("image_cin"));
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
        MySegleton.getmInstance(VerifDoc.this).addTorequestque(jsonArrayRequest);
        return arrayList;

    }
}
