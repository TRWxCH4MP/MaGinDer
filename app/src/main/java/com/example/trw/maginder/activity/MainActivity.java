package com.example.trw.maginder.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.trw.maginder.R;
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
    }
}
