package com.hackforchange.teamBsissa.istethofyproject.Client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import java.util.List;

public class ListDoctors extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static Context mContext;
    ArrayList<Docteur> arrayListtt = new ArrayList<>();
    private SimpleStringRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_doctors);

        mContext = ListDoctors.this;

        arrayListtt = getList();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerviewproductssimilaire);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        adapter = new SimpleStringRecyclerViewAdapter(recyclerView, arrayListtt);
        recyclerView.setAdapter(adapter);

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
                        Intent intent = new Intent(ListDoctors.this, HomeClient.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(ListDoctors.this, ChatClient.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_tip:
                        Intent intent2 = new Intent(ListDoctors.this, HistoriqueClient.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_hospital:
                        Intent intent3 = new Intent(ListDoctors.this, Client_localisation.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_profile:
                        Intent intent4 = new Intent(ListDoctors.this, ProfileClient.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;
                }


                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_search, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<Docteur> newList = new ArrayList<>();

        for (Docteur doc : arrayListtt) {
            if (doc.getNom().toLowerCase().contains(userInput) || doc.getRegion().contains(userInput)){
                newList.add(doc);
            }
        }

        adapter.updateList(newList);
        return true;
    }
    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<ListDoctors.SimpleStringRecyclerViewAdapter.ViewHolder> {

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
        public ListDoctors.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ListDoctors.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ListDoctors.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {


            holder.region.setText(mWishlistImageUri.get(position).getRegion());
            holder.nom.setText(mWishlistImageUri.get(position).getNom());

            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, DoctorDetails.class);
                    i.putExtra("idDoc", mWishlistImageUri.get(position).getId());
                    i.putExtra("nomDoc", mWishlistImageUri.get(position).getNom());
                    i.putExtra("adresseDoc", mWishlistImageUri.get(position).getAdresse());
                    i.putExtra("telDoc", mWishlistImageUri.get(position).getTel());
                    i.putExtra("regionDoc", mWishlistImageUri.get(position).getRegion());
                    i.putExtra("emailDoc", mWishlistImageUri.get(position).getEmail());
                    i.putExtra("prenomDoc", mWishlistImageUri.get(position).getPrenom());
                    i.putExtra("imageDoc", mWishlistImageUri.get(position).getImage_prof());
                    mContext.startActivity(i);

                }
            });

        }

        @Override
        public int getItemCount() {
            return mWishlistImageUri.size();
        }
        public void updateList(List<Docteur> newList) {
            mWishlistImageUri = new ArrayList<>();
            mWishlistImageUri.addAll(newList);
            notifyDataSetChanged();
        }

    }


    public ArrayList<Docteur> getList() {
        final int[] size = {0};
        final ArrayList<Docteur> arrayList = new ArrayList<>();
        String server_url = GlobalUrl.url + "/getDoctors";
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
                                Docteur userComm = new Docteur(jsonObject.getString("id"), jsonObject.getString("nom"), jsonObject.getString("prenom"), jsonObject.getString("email"), jsonObject.getString("tel"), jsonObject.getString("region"), jsonObject.getString("image_prof"), jsonObject.getString("adresse"));
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
        MySegleton.getmInstance(ListDoctors.this).addTorequestque(jsonArrayRequest);
        return arrayList;

    }


    public void getList(String a) {
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
        MySegleton.getmInstance(ListDoctors.this).addTorequestque(jsonArrayRequest);

    }


}
