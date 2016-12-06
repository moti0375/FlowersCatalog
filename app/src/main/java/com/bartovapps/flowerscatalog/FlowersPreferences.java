package com.bartovapps.flowerscatalog;


import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by BartovMoti on 01/20/15.
 */
public class FlowersPreferences extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
