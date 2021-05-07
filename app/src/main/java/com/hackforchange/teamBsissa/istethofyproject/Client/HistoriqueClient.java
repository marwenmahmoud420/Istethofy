package com.hackforchange.teamBsissa.istethofyproject.Client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.hackforchange.teamBsissa.istethofyproject.R;


public class HistoriqueClient extends AppCompatActivity {
    private ViewPager pager;
    private PagerSlidingTabStrip tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_client);


        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new HistoriqueClient.MyAdapter(getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent = new Intent(HistoriqueClient.this, HomeClient.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(HistoriqueClient.this, ChatClient.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_tip:
                        Intent intent2 = new Intent(HistoriqueClient.this, HistoriqueClient.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_hospital:
                        Intent intent3 = new Intent(HistoriqueClient.this, Client_localisation.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_profile:
                        Intent intent4 = new Intent(HistoriqueClient.this, ProfileClient.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;
                }


                return false;
            }
        });


    }


    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = {getString(R.string.tab_title_record1),
                getString(R.string.tab_title_record2)};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return LungsResultFragment.newInstance(position);
                }
                case 1: {
                    return HeartResultFragment.newInstance(position);
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

}
