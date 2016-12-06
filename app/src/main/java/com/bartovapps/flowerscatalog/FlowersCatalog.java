package com.bartovapps.flowerscatalog;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Created by BartovMoti on 02/14/15.
 */
public class FlowersCatalog extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseCrashReporting.enable(this);
        Parse.initialize(this, "TLyh0vqyEJOm38kprhztD6zp5kC9B0rpfbEdpEIh", "e50nuiU3BHYr4MAJaFFzfuHvbq8a3BkeCLu6khUQ");
    }
}
