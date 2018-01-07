package com.example.trw.maginder.db;

import android.content.Context;
import android.os.AsyncTask;

import com.example.trw.maginder.db.database.RestaurantDatabase;
import com.example.trw.maginder.db.entity.EmployeeEntity;
import com.example.trw.maginder.db.entity.MenuEntity;
import com.example.trw.maginder.fragment.LoginFragment;

import java.util.List;

/**
 * Created by _TRW on 26/12/2560.
 */

public class QueryData {

    public static void onLoadEmployee(final Context context, final SendListDataCallback callback) {
        new loadEmployee(context, callback).execute();
    }

    public static void onLoadMenuType(final Context context, final  SendListDataCallback callback) {
        new loadMenuType(context, callback).execute();
    }

    public static class loadEmployee extends AsyncTask<Void, Void, Void> {
        Context context;
        SendListDataCallback callback;
        List<EmployeeEntity> listEmployee;

        public loadEmployee(Context context, SendListDataCallback callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            listEmployee = RestaurantDatabase.getAppDatabase(context).itemDao().loadAllEmployee();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!listEmployee.isEmpty()) {
                callback.loadEmployeeDataCallback(listEmployee, true);
            } else {
                callback.loadEmployeeDataCallback(listEmployee, false);
            }
        }
    }

    public static class loadMenuType extends AsyncTask<Void, Void, Void> {
        Context context;
        SendListDataCallback callback;
        List<MenuEntity> listMenuType;

        public loadMenuType(Context context, SendListDataCallback callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            listMenuType = RestaurantDatabase.getAppDatabase(context).itemDao().loadAllMenuType();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!listMenuType.isEmpty()) {
                callback.loadMenuTypeDataCallback(listMenuType, true);
            } else {
                callback.loadMenuTypeDataCallback(listMenuType, false);
            }
        }
    }
}
