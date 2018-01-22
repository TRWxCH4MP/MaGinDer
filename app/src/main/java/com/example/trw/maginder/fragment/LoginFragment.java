package com.example.trw.maginder.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trw.maginder.R;
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

    private static final String TYPE_ADMIN = "admin";
    private static final String TYPE_CASHIER = "cashier";
    private static final String TYPE_WAITRESS = "waitress";
    private static final String TYPE_CHEF = "chef";
    private static final String TAG = "LoginFragment";
    private static final String PREF_NAME = "PREF_NAME";
    private static final String STATUS = "status";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String ID_RESTAURANT = "id_restaurant";

    private OnFragmentCallback mCallback;
    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;
    private ProgressBar pgLogging;

    private List<EmployeeEntity> listEmployee;
    private List<MenuEntity> listMenuType;
    int status = 1;
    Intent intent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnFragmentCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentCallback ");
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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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
        pgLogging = view.findViewById(R.id.progress_bar_logging);

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
        pgLogging.setVisibility(View.VISIBLE);
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
                pgLogging.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void verifyLogin(LoginItemDao dao) {
        if (dao.isStatus()) {
            Toast.makeText(getContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
            verifyStatus(dao);
            pgLogging.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(getContext(), R.string.login_failure, Toast.LENGTH_SHORT).show();
            pgLogging.setVisibility(View.INVISIBLE);
        }
    }

    private void verifyStatus(LoginItemDao dao) {
        if (dao.getType().toLowerCase().equals(TYPE_ADMIN)) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(STATUS, dao.isStatus());
            editor.putString(NAME, dao.getName());
            editor.putString(TYPE, dao.getType());
            editor.putString(ID_RESTAURANT, dao.getIdRestaurant());
            editor.commit();

            intent = new Intent(getContext(), ManageTableActivity.class);
            startActivity(intent);
        } else if (dao.getType().toLowerCase().equals(TYPE_CASHIER)) {
            // do something
        } else if (dao.getType().toLowerCase().equals(TYPE_WAITRESS)) {
            // do something
        } else if (dao.getType().toLowerCase().equals(TYPE_CHEF)) {
            // do something
        }
    }

}
