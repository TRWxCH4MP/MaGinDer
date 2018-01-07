package com.example.trw.maginder.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.activity.ManageTableActivity;
import com.example.trw.maginder.callback.FragmentCallback;
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

    public static final String TYPE_ADMIN = "Admin";
    public static final String TYPE_CASHIER = "Cashier";
    public static final String TYPE_WAITRESS = "Waitress";
    public static final String TYPE_CHEF = "Chef";
    public static final String TAG = "LoginFragment";

    private FragmentCallback mCallback;
    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;

    private List<EmployeeEntity> listEmployee;
    private List<MenuEntity> listMenuType;
    int status = 1;

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

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        btnLogin = view.findViewById(R.id.btn_login);
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin.setOnClickListener(this);

        return view;
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
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyLogin(LoginItemDao dao) {
        if (dao.isStatus()) {
            Toast.makeText(getContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
            verifyStatus(dao);
        } else {
            Toast.makeText(getContext(), R.string.login_failure, Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyStatus(LoginItemDao dao) {
        Intent intent;
        if (dao.getType().equals(TYPE_ADMIN)) {
            intent = new Intent(getContext(), ManageTableActivity.class);
            intent.putExtra("name", dao.getName());
            intent.putExtra("type", dao.getType());
            startActivity(intent);
        } else if (dao.getType().equals(TYPE_CASHIER)) {
            // do something
        } else if (dao.getType().equals(TYPE_WAITRESS)) {
            // do something
        } else if (dao.getType().equals(TYPE_CHEF)) {
            // do something
        }
    }

}
