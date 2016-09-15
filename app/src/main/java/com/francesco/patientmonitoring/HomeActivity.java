package com.francesco.patientmonitoring;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class HomeActivity extends TabActivity {

        TabHost TabHostWindow;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);


            //Assign id to Tabhost.
            TabHostWindow = (TabHost)findViewById(android.R.id.tabhost);

            //Creating tab menu.
            TabHost.TabSpec TabMenu1 = TabHostWindow.newTabSpec("First tab");
            TabHost.TabSpec TabMenu2 = TabHostWindow.newTabSpec("Second Tab");
            TabHost.TabSpec TabMenu3 = TabHostWindow.newTabSpec("Third Tab");

            //Setting up tab 1 name.
            TabMenu1.setIndicator(getResources().getString(R.string.title_first_tab));
            //Set tab 1 activity to tab 1 menu.
            TabMenu1.setContent(new Intent(this,Notifiche.class));

            //Setting up tab 2 name.
            TabMenu2.setIndicator(getResources().getString(R.string.title_second_tab));
            //Set tab 3 activity to tab 1 menu.
            TabMenu2.setContent(new Intent(this,RicercaPaziente.class));

            //Setting up tab 2 name.
            TabMenu3.setIndicator(getResources().getString(R.string.title_third_tab));
            //Set tab 3 activity to tab 3 menu.
            TabMenu3.setContent(new Intent(this,Altro.class));

            //Adding tab1, tab2, tab3 to tabhost view.

            TabHostWindow.addTab(TabMenu1);
            TabHostWindow.addTab(TabMenu2);
            TabHostWindow.addTab(TabMenu3);

        }
    }
