package com.hackforchange.teamBsissa.istethofyproject.Client;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hackforchange.teamBsissa.istethofyproject.R;

public class HomeClient extends AppCompatActivity {

    Button doctorList;
   // ImageView Lungs,Heart;
    FloatingActionButton Lungs,Heart;
    private AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_client);
        doctorList = findViewById(R.id.doctorList);
        anim = (AnimationDrawable) doctorList.getBackground();
        anim.setEnterFadeDuration(2300);
        anim.setExitFadeDuration(2300);


        Lungs = findViewById(R.id.Lungs);
        Heart = findViewById(R.id.Heart);

        doctorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeClient.this, ListDoctors.class);
                startActivity(i);
            }
        });


        Lungs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animfab(Lungs);
                Intent i = new Intent(HomeClient.this, reording.class);
                startActivity(i);
            }
        });

        Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animfab(Heart);
                Intent i = new Intent(HomeClient.this, reordingHeart.class);
                startActivity(i);
            }
        });

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
                        Intent intent = new Intent(HomeClient.this, HomeClient.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(HomeClient.this, ChatClient.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_tip:
                        Intent intent2 = new Intent(HomeClient.this, HistoriqueClient.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_hospital:
                        Intent intent3 = new Intent(HomeClient.this, Client_localisation.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_profile:
                        Intent intent4 = new Intent(HomeClient.this, ProfileClient.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;
                }


                return false;
            }
        });

    }
    private void animfab(final ImageView img){
        img.animate().scaleX(1f).scaleY(1f).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                img.animate().scaleX(1f).scaleY(1f);
            }
        });
    }
    protected void onResume(){
        super.onResume();
        if(anim!=null && !anim.isRunning()){
            anim.start();
        }
    }

    protected void onPause(){
        super.onPause();
        if(anim!=null && anim.isRunning()){
            anim.stop();
        }
    }



    @Override
    public void onBackPressed() {

        //  super.onBackPressed();

    }


}
