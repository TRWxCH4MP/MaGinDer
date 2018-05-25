package com.example.trw.maginder.model;

import android.app.Application;

public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
