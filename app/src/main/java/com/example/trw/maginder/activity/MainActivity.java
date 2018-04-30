package com.example.trw.maginder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.trw.maginder.R;
import com.example.trw.maginder.StaticStringHelper;
import com.example.trw.maginder.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

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
        boolean loginStatus = sharedPreferences.getBoolean(StaticStringHelper.STATUS, false);
        String employeeType = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_TYPE, null);

        if (loginStatus) {
            userIsSignedIn(employeeType);
        }
    }

    private boolean isEmployeeTypeInvalid(String employeeType) {
        return employeeType != null;
    }

    private void userIsSignedIn(String employeeType) {
        if (isEmployeeTypeInvalid(employeeType)) {
            verifyEmployeeType(employeeType);
        }
    }

    private void verifyEmployeeType(String employeeType) {
        if (employeeType.equals(StaticStringHelper.EMPLOYEE_TYPE)) {
            onStartActivityHelper(this, ManageTableActivity.class);
        }
    }

    private void onStartActivityHelper(Context context, Class<? extends Activity> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
        finish();
    }
}
