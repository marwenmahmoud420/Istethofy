package com.hackforchange.teamBsissa.istethofyproject.Client;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.astuetz.PagerSlidingTabStrip;
import com.hackforchange.teamBsissa.istethofyproject.R;

import com.hackforchange.teamBsissa.istethofyproject.recordings.FileViewerHeartFragment;
import com.hackforchange.teamBsissa.istethofyproject.recordings.RecordHeartFragment;

public class reordingHeart extends AppCompatActivity {
    private ViewPager pager;
    private PagerSlidingTabStrip tabs;
    private reordingHeart reordingHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reordingheart);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(reordingHeart.this);
        View mView = getLayoutInflater().inflate(R.layout.diag_app_updates, null);
        CheckBox mCheckBox = mView.findViewById(R.id.checkBox);
        mBuilder.setTitle("Welcome To Istethofy Smart Place");
        mBuilder.setMessage("1- Remove your Clothers. \n2- Choose a Quiet PLace . \n3- Place the Stethoscope on your lungs :) . \n4-Start recording :).");
        mBuilder.setView(mView);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    storeDialogStatus(true);
                } else {
                    storeDialogStatus(false);
                }
            }
        });

        if (getDialogStatus()) {
            mDialog.hide();
        } else {
            mDialog.show();
        }

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);


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
                        Intent intent = new Intent(reordingHeart.this, HomeClient.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(reordingHeart.this, ChatClient.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;


                    case R.id.nav_tip:
                        Intent intent2 = new Intent(reordingHeart.this, HistoriqueClient.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_hospital:
                        Intent intent3 = new Intent(reordingHeart.this, Client_localisation.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_profile:
                        Intent intent4 = new Intent(reordingHeart.this, ProfileClient.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;
                }


                return false;
            }
        });


    }


    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = {getString(R.string.tab_title_record),
                getString(R.string.tab_title_saved_recordings)};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return RecordHeartFragment.newInstance(position);
                }
                case 1: {
                    return FileViewerHeartFragment.newInstance(position);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private void storeDialogStatus(boolean isChecked) {
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus() {
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        return mSharedPreferences.getBoolean("item", false);

    }
}
