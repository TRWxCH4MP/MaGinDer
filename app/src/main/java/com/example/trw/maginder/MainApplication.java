package com.example.trw.maginder;

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
