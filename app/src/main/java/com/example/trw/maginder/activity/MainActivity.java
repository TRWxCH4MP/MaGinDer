package com.example.trw.maginder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.trw.maginder.R;
import com.example.trw.maginder.callback.OnFragmentCallback;
import com.example.trw.maginder.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity implements OnFragmentCallback {

    private static final String PREF_NAME = "PREF_NAME";
    public static final String STATUS = "status";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String ID_RESTAURANT = "id_restaurant";
    private Intent intent;
    private Bundle bundle;
    private String name;
    private String type;
    private String idRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new LoginFragment())
                    .commit();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean loginStatus = sharedPreferences.getBoolean(STATUS, false);
        type = sharedPreferences.getString(TYPE, null);

        if (loginStatus) {
            if (type.equals(TYPE)) {
                intent = new Intent(this, ManageTableActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onFragmentCallback(Fragment fragment) {
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment == null) {
            Intent intent = new Intent(this, ManageTableActivity.class);
            startActivity(intent);
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contentContainer, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
