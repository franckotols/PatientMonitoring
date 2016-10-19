package com.francesco.patientmonitoring;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


public class Settings extends PreferenceFragmentCompat {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey) {
        setPreferencesFromResource(R.xml.settings_menu, rootkey);

    }


}


