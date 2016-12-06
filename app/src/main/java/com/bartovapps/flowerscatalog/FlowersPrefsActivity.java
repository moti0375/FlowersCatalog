package com.bartovapps.flowerscatalog;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by BartovMoti on 01/20/15.
 */
public class FlowersPrefsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new FlowersPreferences()).commit();
    }
}
