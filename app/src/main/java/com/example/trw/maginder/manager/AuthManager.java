package com.example.trw.maginder.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.trw.maginder.model.Contextor;
import com.example.trw.maginder.model.StaticStringHelper;
import com.example.trw.maginder.service.dao.LoginItemDao;
import com.example.trw.maginder.service.http_manger.HttpManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthManager {

    private static AuthManager manager;

    public static AuthManager getInstance() {
        if (manager == null) {
            manager = new AuthManager();
        }
        return manager;
    }

    private AuthManager() {

    }

    public void onLogIn(String username, String password, final AuthManagerCallback callback) {
        Call<LoginItemDao> call = HttpManager.getInstance().getService().repos(username, password);
        call.enqueue(new Callback<LoginItemDao>() {
            @Override
            public void onResponse(Call<LoginItemDao> call, Response<LoginItemDao> response) {
                if (response.isSuccessful()) {
                    LoginItemDao dao = response.body();
                    callback.onLogInSuccess(dao);
                } else {
                    callback.onLogInFailure(String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<LoginItemDao> call, Throwable t) {
                callback.onLogInFailure(t.toString());
            }
        });
    }

    public void setCurrentUser(LoginItemDao dao) {
        SharedPreferences sharedPreferences = Contextor.getInstance().getContext()
                .getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(StaticStringHelper.STATUS, dao.isStatus());
        editor.putString(StaticStringHelper.EMPLOYEE_NAME, dao.getName());
        editor.putString(StaticStringHelper.EMPLOYEE_TYPE, dao.getType());
        editor.putString(StaticStringHelper.RESTAURANT_ID, dao.getIdRestaurant());
        editor.putString(StaticStringHelper.RESTAURANT_NAME, dao.getRestaurantName());
        editor.commit();
    }

    public void clearCurrentUser() {
        SharedPreferences sharedPreferences = Contextor.getInstance().getContext()
                .getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getCurrentUserStatus();
    }

    public boolean getCurrentUserStatus() {
        SharedPreferences sharedPreferences = Contextor.getInstance().getContext()
                .getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        boolean logInState = sharedPreferences.getBoolean(StaticStringHelper.STATUS, false);

        return logInState;
    }

    public String getCurrentUserType() {
        SharedPreferences sharedPreferences = Contextor.getInstance().getContext()
                .getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_TYPE, null);

        if (userType != null) {
            return userType;
        } else {
            return null;
        }
    }

    public String getCurrentUserName() {
        SharedPreferences sharedPreferences = Contextor.getInstance().getContext()
                .getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_NAME, null);

        if (userName != null) {
            return userName;
        } else {
            return null;
        }
    }

    public String getCurrentRestaurantId() {
        SharedPreferences sharedPreferences = Contextor.getInstance().getContext()
                .getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        String restaurantId = sharedPreferences.getString(StaticStringHelper.RESTAURANT_ID, null);

        if (restaurantId != null) {
            return restaurantId;
        } else {
            return null;
        }
    }

    public String getCurrentRestaurantName() {
        SharedPreferences sharedPreferences = Contextor.getInstance().getContext()
                .getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        String restaurantName = sharedPreferences.getString(StaticStringHelper.RESTAURANT_NAME, null);

        if (restaurantName != null) {
            return restaurantName;
        } else {
            return null;
        }
    }

    public interface AuthManagerCallback {
        void onLogInSuccess(LoginItemDao dao);

        void onLogInFailure(String exception);
    }

}
