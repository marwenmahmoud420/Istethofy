package com.hackforchange.teamBsissa.istethofyproject.Client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackforchange.teamBsissa.istethofyproject.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaiso on 13-02-2018.
 */



public class CustomListViewMessages extends ArrayAdapter<String>  {

    Context mContext;
    LayoutInflater inflater;
    List<Model> modellist;
    ArrayList<Model> arrayList;
    private String[] profilename;
    private String[] email;
    private String[] number;
    private String[] imagepath;
    private Activity context;
    Bitmap bitmap;
    private ImageView mHeartRed;
    private ImageView mHeartWhite;
    String getId;
    String pos;

    BufferedInputStream is;
    String line = null;
    String result = null;
    String[] testpost;
    String ok ;





    public CustomListViewMessages(Activity context, final String[] profilename, String[] email, String[] imagepath, String[] number ) {
        super(context, R.layout.layoutmessage, profilename);this.context = context;
        this.profilename = profilename;
        this.email = email;
        this.number = number;
        this.imagepath = imagepath;









    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override

    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        View r = convertView;
        ViewHolder viewHolder;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.layoutmessage, null, true);
            viewHolder = new ViewHolder(r);


            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();

        }

        viewHolder.tvw1.setText(profilename[position]);
        viewHolder.tvw2.setText(email[position]);
        viewHolder.tvw3.setText(number[position]);


        new GetImageFromURL(viewHolder.ivw).execute(imagepath[position]);
        final ViewHolder finalViewHolder = viewHolder;





        return r;
    }

    class ViewHolder {

        TextView tvw1;
        TextView tvw2;
        TextView tvw3;
        ImageView ivw;
        ImageView ivw2;


        ViewHolder(View v) {
            tvw1 =  v.findViewById(R.id.tvprofilename);
            tvw2 =  v.findViewById(R.id.tvemail);
            tvw3 =  v.findViewById(R.id.number);
            ivw = v.findViewById(R.id.imageView);
            tvw2.setMovementMethod(new ScrollingMovementMethod());
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {

        ImageView imgView;

        public GetImageFromURL(ImageView imgv) {
            this.imgView = imgv;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            bitmap = null;

            try {

                InputStream ist = new URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(ist);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            super.onPostExecute(bitmap);
            imgView.setImageBitmap(bitmap);
        }
    }



















}
