package com.example.trw.maginder.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;

import com.example.trw.maginder.db.callback.OnStateCallback;
import com.example.trw.maginder.db.callback.SendListDataCallback;
import com.example.trw.maginder.db.database.RestaurantDatabase;
import com.example.trw.maginder.db.entity.EmployeeEntity;
import com.example.trw.maginder.db.entity.MenuEntity;

import java.util.List;

/**
 * Created by _TRW on 25/12/2560.
 */

public class InsertData {

    public static String TAG = "InsertData";

    public static void onInsertMenu(final Context context,final MenuEntity menuEntity, final OnStateCallback stateCallback) {

        new insertMenu(context, menuEntity, stateCallback).execute();
    }

    public static class insertMenu extends AsyncTask<Void, Void, Void> {
        Context context;
        MenuEntity menuEntity;
        OnStateCallback stateCallback;

        public insertMenu(Context context, MenuEntity menuEntity , OnStateCallback stateCallback) {
            this.context = context;
            this.menuEntity = menuEntity;
            this.stateCallback = stateCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RestaurantDatabase.getAppDatabase(context).itemDao().insertMenu(menuEntity);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "MenuEntity: successful");
            QueryData.onLoadMenu(context, new SendListDataCallback() {
                @Override
                public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                    if (isSuccess) {
                        stateCallback.stateCallback(true);
                    } else {
                        stateCallback.stateCallback(false);
                    }
                }
            });
        }

        @Override
        protected void onPreExecute() {

        }
    }

}
