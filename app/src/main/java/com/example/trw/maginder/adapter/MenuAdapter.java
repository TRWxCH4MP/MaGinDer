package com.example.trw.maginder.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by _TRW on 17/12/2560.
 */

public class MenuAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private static final int PAGE_COUNT = 3;
    private Context mContext;

    public MenuAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
       return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
