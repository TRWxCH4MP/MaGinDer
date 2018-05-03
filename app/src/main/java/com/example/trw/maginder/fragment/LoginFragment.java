package com.example.trw.maginder.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.service.http_manger.HttpManager;
import com.example.trw.maginder.service.dao.LoginItemDao;
import com.example.trw.maginder.utility.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";

    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;
    private UserModel userModel;

    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        getCurrentUser();
        initializeUI(view);
        setupView();

        return view;
    }

    private void initializeUI(View view) {
        btnLogin = view.findViewById(R.id.btn_login);
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);

    }

    private void setupView() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.login_is_process));
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(this);
    }

    private void getCurrentUser() {
        userModel = new UserModel();
        userModel.getCurrentUser();

        if (userModel.getCurrentUserStatus()) {
            onStartActivityHelper(getContext(), userModel.verifyUserType(userModel.getCurrentUserType()));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            userLogin(username, password);
        }
    }

    private void userLogin(String username, String password) {
        if (userModel.loginInvalid(username, password)) {
            displayLoading();
            onLogin(username, password);
        } else {
            showToast(getString(R.string.error_form_invalid));
        }
    }

    private void onLogin(String username, String password) {
        Call<LoginItemDao> call = HttpManager.getInstance().getService().repos(username, password);
        call.enqueue(new Callback<LoginItemDao>() {
            @Override
            public void onResponse(Call<LoginItemDao> call, Response<LoginItemDao> response) {
                if (response.isSuccessful()) {
                    disableLoading();
                    LoginItemDao dao = response.body();
                    userModel.setCurrentUser(dao);
                    verifyLogin(dao.isStatus(), dao.getType());
                } else {
                    showToast(response.errorBody().toString());
                    disableLoading();
                }
            }

            @Override
            public void onFailure(Call<LoginItemDao> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                disableLoading();
            }
        });
    }

    private void verifyLogin(boolean status, String userType) {
        if (status) {
            showToast(getString(R.string.login_success));
            onStartActivityHelper(getContext(), userModel.verifyUserType(userType));
        } else {
            showToast(getString(R.string.login_failure));
        }
    }

    private void displayLoading() {
        progressDialog.show();
    }

    private void disableLoading() {
        progressDialog.dismiss();
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void onStartActivityHelper(Context context, Class<? extends Activity> activity) {
        if (activity != null) {
            Intent intent = new Intent(context, activity);
            startActivity(intent);
        }
    }

}
