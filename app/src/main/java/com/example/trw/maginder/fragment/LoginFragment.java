package com.example.trw.maginder.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.StaticStringHelper;
import com.example.trw.maginder.activity.ManageTableActivity;
import com.example.trw.maginder.callback.OnFragmentCallback;
import com.example.trw.maginder.db.entity.EmployeeEntity;
import com.example.trw.maginder.db.entity.MenuEntity;
import com.example.trw.maginder.service.http_manger.HttpManager;
import com.example.trw.maginder.service.dao.LoginItemDao;

import java.util.List;

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

    private ProgressDialog progressDialog;
    private Intent intent;

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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        boolean loginStatus = sharedPreferences.getBoolean("status", false);

        if (loginStatus) {
            intent = new Intent(getContext(), ManageTableActivity.class);
            startActivity(intent);
        }

        initializeUI(view);

        return view;
    }

    private void initializeUI(View view) {
        btnLogin = view.findViewById(R.id.btn_login);
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("กำลังทำการล็อคอิน");
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(this);
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
        if (loginInvalid(username, password)) {
            login(username, password);
        } else {
            Toast.makeText(getContext(), R.string.error_form_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean loginInvalid(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    private void login(String username, String password) {
        progressDialog.show();
        Call<LoginItemDao> call = HttpManager.getInstance().getService().repos(username, password);
        call.enqueue(new Callback<LoginItemDao>() {
            @Override
            public void onResponse(Call<LoginItemDao> call, Response<LoginItemDao> response) {
                if (response.isSuccessful()) {
                    LoginItemDao dao = response.body();
                    verifyLogin(dao);
                } else {
                    Toast.makeText(getContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginItemDao> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void verifyLogin(LoginItemDao dao) {
        if (dao.isStatus()) {
            Toast.makeText(getContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
            verifyStatus(dao);
            progressDialog.dismiss();
        } else {
            Toast.makeText(getContext(), R.string.login_failure, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void verifyStatus(LoginItemDao dao) {
        if (dao.getType().toLowerCase().equals(StaticStringHelper.TYPE_ADMIN)) {

        } else if (dao.getType().toLowerCase().equals(StaticStringHelper.TYPE_CASHIER)) {
            // do something
        } else if (dao.getType().toLowerCase().equals(StaticStringHelper.TYPE_WAITRESS)) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(StaticStringHelper.STATUS, dao.isStatus());
            editor.putString(StaticStringHelper.EMPLOYEE_NAME, dao.getName());
            editor.putString(StaticStringHelper.EMPLOYEE_TYPE, dao.getType());
            editor.putString(StaticStringHelper.RESTAURANT_ID, dao.getIdRestaurant());
            editor.putString(StaticStringHelper.RESTAURANT_NAME, dao.getRestaurantName());
            editor.commit();

            intent = new Intent(getContext(), ManageTableActivity.class);
            startActivity(intent);
        } else if (dao.getType().toLowerCase().equals(StaticStringHelper.TYPE_CHEF)) {
            // do something
        }
    }

}
