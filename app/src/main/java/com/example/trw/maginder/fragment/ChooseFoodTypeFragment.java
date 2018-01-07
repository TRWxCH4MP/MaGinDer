package com.example.trw.maginder.fragment;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.trw.maginder.R;
import com.example.trw.maginder.callback.FragmentCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseFoodTypeFragment extends Fragment implements View.OnClickListener {

    private FragmentCallback mCallback;
    private ImageView mTypeFood;
    private ImageView mTypeDessert;
    private ImageView mTypeBeverage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (FragmentCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentCallback ");
        }
    }

    public ChooseFoodTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_food_type, container, false);

        initializeUI(view);
        handleOnclick();

        return view;
    }

    private void handleOnclick() {
        mTypeFood.setOnClickListener(this);
        mTypeDessert.setOnClickListener(this);
        mTypeBeverage.setOnClickListener(this);
    }

    private void initializeUI(View view) {
        mTypeFood = view.findViewById(R.id.imgv_type_food);
        mTypeDessert = view.findViewById(R.id.imgv_type_dessert);
        mTypeBeverage = view.findViewById(R.id.imgv_type_beverage);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = new AllMenuFragment();
        switch (view.getId()) {
            case R.id.imgv_type_food:
                mCallback.handleEvent(fragment);
                break;
            case R.id.imgv_type_dessert:
                mCallback.handleEvent(fragment);
                break;
            case R.id.imgv_type_beverage:
                mCallback.handleEvent(fragment);
                break;
        }
    }
}
