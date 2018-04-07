package com.example.trw.maginder.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.trw.maginder.R;
import com.example.trw.maginder.StaticStringHelper;
import com.example.trw.maginder.callback.OnFragmentCallback;
import com.example.trw.maginder.db.QueryData;
import com.example.trw.maginder.db.callback.SendListDataCallback;
import com.example.trw.maginder.db.entity.MenuEntity;
import com.example.trw.maginder.fragment.LoginFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnFragmentCallback {

    private static final String TAG = "MainActivity";
    private Intent intent;
    private String employeeType;
    private boolean loginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new LoginFragment())
                    .commit();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        loginStatus = sharedPreferences.getBoolean(StaticStringHelper.STATUS, false);
        employeeType = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_TYPE, null);

        if (loginStatus) {
            if (employeeType.equals(StaticStringHelper.EMPLOYEE_TYPE)) {
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
