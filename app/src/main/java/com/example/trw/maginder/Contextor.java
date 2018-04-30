package com.example.trw.maginder;

import android.content.Context;

public class Contextor {

    private static Contextor INSTANCE;

    public static Contextor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Contextor();
        }
        return INSTANCE;
    }

    private Context mContext;

    private Contextor() {}

    public void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

}
