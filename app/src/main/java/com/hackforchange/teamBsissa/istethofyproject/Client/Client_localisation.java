package com.hackforchange.teamBsissa.istethofyproject.Client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hackforchange.teamBsissa.istethofyproject.R;



public class Client_localisation extends AppCompatActivity {

    private View layoutEncontrarPers;
    private ImageView imgMisFotos, imgAnim, imgAnim2;
    private Handler handlerAnimationCIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_localisation);


        this.handlerAnimationCIMG = new Handler();
        layoutEncontrarPers = findViewById(R.id.layoutPers);
        imgMisFotos = findViewById(R.id.imgMiFoto);
        imgMisFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Client_localisation.this, MapClient.class);
                startActivity(i);

            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final Uri uri = Uri.parse(sharedPreferences.getString("image_prof", ""));
        Glide.with(getBaseContext()).load(uri).apply(new RequestOptions().circleCrop()).into(imgMisFotos);
        this.imgAnim = findViewById(R.id.imganim);
        this.imgAnim2 = findViewById(R.id.imganim2);
        this.runnableAnim.run();
        this.layoutEncontrarPers.setVisibility(View.VISIBLE);


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
                        Intent intent = new Intent(Client_localisation.this, HomeClient.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(Client_localisation.this, ChatClient.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_tip:
                        Intent intent2 = new Intent(Client_localisation.this, HistoriqueClient.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_hospital:
                        Intent intent3 = new Intent(Client_localisation.this, Client_localisation.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_profile:
                        Intent intent4 = new Intent(Client_localisation.this, ProfileClient.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;
                }


                return false;
            }
        });

    }

    private Runnable runnableAnim = new Runnable() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            imgAnim.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                @Override
                public void run() {
                    imgAnim.setScaleX(1f);
                    imgAnim.setScaleY(1f);
                    imgAnim.setAlpha(1f);
                }
            });
            imgAnim2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                @Override
                public void run() {
                    imgAnim2.setScaleX(1f);
                    imgAnim2.setScaleY(1f);
                    imgAnim2.setAlpha(1f);
                }
            });
            handlerAnimationCIMG.postDelayed(runnableAnim, 1500);
        }
    };
}



