package com.example.trw.maginder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.trw.maginder.R;
import com.example.trw.maginder.StaticStringHelper;
import com.example.trw.maginder.callback.FragmentCallback;
import com.example.trw.maginder.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity implements FragmentCallback {

    private static final String TAG = "MainActivity";
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
        onCheckLoginStatus();
    }

    private void onCheckLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        loginStatus = sharedPreferences.getBoolean(StaticStringHelper.STATUS, false);
        employeeType = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_TYPE, null);

        if (loginStatus) {
            verifyEmployeeType();
        }
    }

    private void verifyEmployeeType() {
        if (employeeType.equals(StaticStringHelper.EMPLOYEE_TYPE)) {
            onStartActivityHelper(this, ManageTableActivity.class);
        }
    }

    private void onStartActivityHelper(Context context, Class<? extends Activity> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
        finish();
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
