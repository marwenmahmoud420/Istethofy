package com.hackforchange.teamBsissa.istethofyproject.Client;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.hackforchange.teamBsissa.istethofyproject.Model.result;
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
 * Use the {@link HeartResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeartResultFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private int position;
    ArrayList<result> arrayListtt = new ArrayList<>();
    private SimpleStringRecyclerViewAdapter adapter;

    public HeartResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HeartResultFragment.
     */
    public static HeartResultFragment newInstance(int position) {
        HeartResultFragment f = new HeartResultFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lungs_result, container, false);

        arrayListtt = getList();
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerviewproductssimilaire);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        adapter = new SimpleStringRecyclerViewAdapter(recyclerView, arrayListtt);
        recyclerView.setAdapter(adapter);

        return v;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<HeartResultFragment.SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<result> mWishlistImageUri;
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

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<result> wishlistImageUri) {
            mWishlistImageUri = wishlistImageUri;
            mRecyclerView = recyclerView;
        }

        @Override
        public HeartResultFragment.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_record_item, parent, false);
            return new HeartResultFragment.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final HeartResultFragment.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {


            holder.nom.setText(mWishlistImageUri.get(position).getResultat());

            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return mWishlistImageUri.size();
        }

    }

    public ArrayList<result> getList() {
        final int[] size = {0};
        final ArrayList<result> arrayList = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("role", "").equals("1")) {
            String server_url = GlobalUrl.url + "/getresultHeartclient/" + sharedPreferences.getString("id", "");
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
                                    result userComm = new result(jsonObject.getString("id"), jsonObject.getString("id_client"), jsonObject.getString("id_doc"), jsonObject.getString("save_heart"));
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
        if (sharedPreferences.getString("role", "").equals("0")) {
            String server_url = GlobalUrl.url + "/getresultHeartdocteur/" + sharedPreferences.getString("id", "");
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
                                    result userComm = new result(jsonObject.getString("id"), jsonObject.getString("id_client"), jsonObject.getString("id_doc"), jsonObject.getString("save_heart"));
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
        return arrayList;
    }

}
