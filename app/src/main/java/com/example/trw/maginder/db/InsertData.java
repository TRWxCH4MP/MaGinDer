package com.example.trw.maginder.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;

import com.example.trw.maginder.db.database.RestaurantDatabase;
import com.example.trw.maginder.db.entity.EmployeeEntity;
import com.example.trw.maginder.db.entity.MenuEntity;

/**
 * Created by _TRW on 25/12/2560.
 */

public class InsertData {

    public static String TAG = "InsertData";

    public static void onInsertEmployee(final Context context
            , final EmployeeEntity employeeEntity) {

        new insertEmployee(context, employeeEntity).execute();
    }

    public static class insertEmployee extends AsyncTask<Void, Void, Void> {
        Context context;
        EmployeeEntity employeeEntity;

        public insertEmployee(Context context, EmployeeEntity employeeEntity) {
            this.context = context;
            this.employeeEntity = employeeEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RestaurantDatabase.getAppDatabase(context).itemDao().insertEmployee(employeeEntity);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "Employee: successful");
        }

        @Override
        protected void onPreExecute() {
        }
    }

    public static void onInsertMenuType(final Context context
            , final MenuEntity menuEntity) {

        new insertMenuType(context, menuEntity).execute();
    }

    public static class insertMenuType extends AsyncTask<Void, Void, Void> {
        Context context;
        MenuEntity menuEntity;

        public insertMenuType(Context context, MenuEntity menuEntity) {
            this.context = context;
            this.menuEntity = menuEntity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RestaurantDatabase.getAppDatabase(context).itemDao().insertMenuType(menuEntity);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "MenuType: successful");
        }

        @Override
        protected void onPreExecute() {
        }
    }
}
