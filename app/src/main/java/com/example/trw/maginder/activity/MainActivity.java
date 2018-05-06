package com.example.trw.maginder.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.manager.AuthManager;
import com.example.trw.maginder.service.dao.LoginItemDao;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        setupView();
        checkAuthState();
    }

    private void initializeUI() {
        btnLogin = findViewById(R.id.btn_login);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
    }

    private void setupView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_is_process));
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(this);
    }

    private void checkAuthState() {
        if (AuthManager.getInstance().isLoggedIn()) {
            showLogInSuccess();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            logIn(username, password);
        }
    }

    private void logIn(String username, String password) {
        showLoading();
        if (logInInvalid(username, password)) {
            AuthManager.getInstance().onLogIn(username, password, new AuthManager.AuthManagerCallback() {
                @Override
                public void onLogInSuccess(LoginItemDao dao) {
                    AuthManager.getInstance().setCurrentUser(dao);
                    verifyLogIn(dao.isStatus());
                    hideLoading();
                }

                @Override
                public void onLogInFailure(String exception) {
                    showException(exception);
                    hideLoading();
                }
            });
        } else {
            hideLoading();
            showLogInInvalid();
        }
    }

    public boolean logInInvalid(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    private void verifyLogIn(boolean status) {
        if (status) {
            showLogInSuccess();
        } else {
            showLogInFailure();
        }
    }

    private void showLoading() {
        progressDialog.show();
    }

    private void hideLoading() {
        progressDialog.dismiss();
    }

    private void showException(String exception) {
        showToast(exception);
    }

    private void showLogInInvalid() {
        showToast(getString(R.string.error_form_invalid));
    }

    private void showLogInFailure() {
        showToast(getString(R.string.login_failure));
    }

    private void showLogInSuccess() {
        showToast(getString(R.string.login_success));
        goToManageTableActivity();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void goToManageTableActivity() {
        Intent intent = new Intent(MainActivity.this, ManageTableActivity.class);
        startActivity(intent);
        finish();
    }
}
