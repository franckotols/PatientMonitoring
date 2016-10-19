package com.francesco.patientmonitoring;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Vector;

public class ParametriActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametri);




        List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(this, ParametriValoriMedi.class.getName()));
        fragments.add(Fragment.instantiate(this, ParametriValoriPuntuali.class.getName()));
        fragments.add(Fragment.instantiate(this, ParametriGrafici.class.getName()));

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),fragments);
        final ViewPager pager = (ViewPager)findViewById(R.id.viewPager_parametri);
        pager.setAdapter(adapter);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener(){

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft){
                pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft){

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft){

            }

        };

        actionBar.addTab(actionBar.newTab().setText("Valori Medi").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Valori Puntuali").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Grafici").setTabListener(tabListener));

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position){
                actionBar.setSelectedNavigationItem(position);
            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(ParametriActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
