package com.hackforchange.teamBsissa.istethofyproject.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.hackforchange.teamBsissa.istethofyproject.Model.Client;
import com.hackforchange.teamBsissa.istethofyproject.MySegleton;
import com.hackforchange.teamBsissa.istethofyproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link clientlist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class clientlist extends Fragment {
    private static final String ARG_POSITION = "position";
    private int position;
    private static HistoriqueAdmin mActivity;

    ArrayList<Client> arrayListtt = new ArrayList<>();

    SimpleStringRecyclerViewAdapter adapter;

    public clientlist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment commerciallist.
     */
    public static clientlist newInstance(int position) {
        clientlist f = new clientlist();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        arrayListtt = getList();
        mActivity = (HistoriqueAdmin) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lungs_result, container, false);

        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewproductssimilaire);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        //newest to oldest order (database stores from oldest to newest)
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new SimpleStringRecyclerViewAdapter(mRecyclerView, arrayListtt);
        mRecyclerView.setAdapter(adapter);

        return v;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<clientlist.SimpleStringRecyclerViewAdapter.ViewHolder> {

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
        public clientlist.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new clientlist.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final clientlist.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {


            holder.region.setText(mWishlistImageUri.get(position).getNom());
            if (mWishlistImageUri.get(position).getEtat().equals("1")) {
                holder.nom.setText("active");
            } else holder.nom.setText("bloquer");

            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mActivity, ClientDetails.class);
                    i.putExtra("idpatient", mWishlistImageUri.get(position).getId());
                    i.putExtra("nompatient", mWishlistImageUri.get(position).getNom());
                    i.putExtra("telpatient", mWishlistImageUri.get(position).getTel());
                    i.putExtra("regionpatient", mWishlistImageUri.get(position).getRegion());
                    i.putExtra("emailpatient", mWishlistImageUri.get(position).getEmail());
                    i.putExtra("prenompatient", mWishlistImageUri.get(position).getPrenom());
                    i.putExtra("imagepatient", mWishlistImageUri.get(position).getImage_prof());
                    i.putExtra("etatpatient", mWishlistImageUri.get(position).getEtat());
                    mActivity.startActivity(i);
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
        String server_url = GlobalUrl.url + "/getAllClient";
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
                                Client userComm = new Client(jsonObject.getString("id"), jsonObject.getString("nom"), jsonObject.getString("prenom"), jsonObject.getString("email"), jsonObject.getString("tel"), jsonObject.getString("region"), jsonObject.getString("image_prof"), jsonObject.getString("etat"));
                                //  result2 userComm = new result2();
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
                Toast.makeText(getContext(), "ERROR...", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        MySegleton.getmInstance(getContext()).addTorequestque(jsonArrayRequest);
        return arrayList;
    }
}
