package com.hackforchange.teamBsissa.istethofyproject.Admin;

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

public class HistoriqueAdmin extends AppCompatActivity {
    private ViewPager pager;
    private PagerSlidingTabStrip tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique_admin);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new HistoriqueAdmin.MyAdapter(getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        BottomNavigationView bottomNavigationView =  findViewById(R.id.bottomNavView_Bar);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_tip:
                        Intent intent = new Intent(HistoriqueAdmin.this, AddComm.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_message:
                        Intent intent1 = new Intent(HistoriqueAdmin.this, VerifDoc.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_home:
                        Intent intent2 = new Intent(HistoriqueAdmin.this, AddComm.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;


                }


                return false;
            }
        });


    }

    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = {getString(R.string.tab_title_record11),
                getString(R.string.tab_title_record12),getString(R.string.tab_title_record13),};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return commerciallist.newInstance(position);
                }
                case 1: {
                    return doctorlist.newInstance(position);
                }
                case 2: {
                    return clientlist.newInstance(position);
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
