package com.guy.backgroundgps;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MyClockTickerV4.initHelper();
    }
}
