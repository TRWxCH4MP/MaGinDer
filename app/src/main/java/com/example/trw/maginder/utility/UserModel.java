package com.example.trw.maginder.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.trw.maginder.activity.ManageTableActivity;
import com.example.trw.maginder.model.Contextor;
import com.example.trw.maginder.model.StaticStringHelper;
import com.example.trw.maginder.service.dao.LoginItemDao;

public class UserModel {

    private boolean loginStatus;
    private String userName;
    private String userType;
    private String restaurantId;
    private String restaurantName;

    private static UserModel instance;

    public static UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }

    public Class<? extends Activity> verifyUserType(String userType) {
        if (userType.toLowerCase().equals(StaticStringHelper.TYPE_ADMIN)) {
            // do something
        } else if (userType.toLowerCase().equals(StaticStringHelper.TYPE_CASHIER)) {
            // do something
        } else if (userType.toLowerCase().equals(StaticStringHelper.TYPE_WAITRESS)) {
            return ManageTableActivity.class;
        } else if (userType.toLowerCase().equals(StaticStringHelper.TYPE_CHEF)) {
            // do something
        }
        return null;
    }

    public boolean loginInvalid(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    public void clearCurrentUser() {
        SharedPreferences sharedPreferences = Contextor.getInstance().getContext()
                .getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
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

    public void getCurrentUser() {
        SharedPreferences sharedPreferences = Contextor.getInstance().getContext()
                .getSharedPreferences(StaticStringHelper.PREF_NAME, Context.MODE_PRIVATE);
        boolean loginStatus = sharedPreferences.getBoolean(StaticStringHelper.STATUS, false);
        String userType = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_TYPE, null);
        String userName = sharedPreferences.getString(StaticStringHelper.EMPLOYEE_NAME, null);
        String restaurantId = sharedPreferences.getString(StaticStringHelper.RESTAURANT_ID, null);
        String restaurantName = sharedPreferences.getString(StaticStringHelper.RESTAURANT_NAME, null);

        setCurrentStatus(loginStatus);
        setCurrentUserType(userType);
        setCurrentUserName(userName);
        setCurrentRestaurantId(restaurantId);
        setCurrentRestaurantName(restaurantName);
    }


    private void setCurrentStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    private void setCurrentUserType(String userType) {
        this.userType = userType;
    }

    private void setCurrentUserName(String userName) {
        this.userName = userName;
    }

    private void setCurrentRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    private void setCurrentRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public boolean getCurrentUserStatus() {
        return loginStatus;
    }

    public String getCurrentUserType() {
        return userType;
    }

    public String getCurrentUserName() {
        return userName;
    }

    public String getCurrentRestaurantId() {
        return restaurantId;
    }

    public String getCurrentRestaurantName() {
        return restaurantName;
    }
}
