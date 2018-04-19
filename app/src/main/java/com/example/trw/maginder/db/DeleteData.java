package com.example.trw.maginder.db;

import android.content.Context;
import android.os.AsyncTask;

import com.example.trw.maginder.db.callback.OnStateCallback;
import com.example.trw.maginder.db.callback.SendListDataCallback;
import com.example.trw.maginder.db.database.RestaurantDatabase;
import com.example.trw.maginder.db.entity.MenuEntity;

import java.util.List;

/**
 * Created by _TRW on 13/2/2561.
 */

public class DeleteData {

    public static void onDeleteMenu(final Context context, final OnStateCallback stateCallback) {
        new deleteMenu(context, stateCallback).execute();
    }

    public static class deleteMenu extends AsyncTask<Void, Void, Void> {
        Context context;
        OnStateCallback stateCallback;


        public deleteMenu(Context context, OnStateCallback stateCallback) {
            this.context = context;
            this.stateCallback = stateCallback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
           RestaurantDatabase.getAppDatabase(context).itemDao().deleteAllMenu();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stateCallback.stateCallback(true);
        }
    }

    public static void onDeleteMenuById(final Context context, final String primaryId, final OnStateCallback stateCallback) {
        new deleteMenuById(context, stateCallback, primaryId).execute();
    }

    public static class deleteMenuById extends AsyncTask<Void, Void, Void> {
        Context context;
        OnStateCallback stateCallback;
        String primaryId;


        public deleteMenuById(Context context, OnStateCallback stateCallback, String primaryId) {
            this.context = context;
            this.stateCallback = stateCallback;
            this.primaryId = primaryId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RestaurantDatabase.getAppDatabase(context).itemDao().deleteMenuByPrimaryId(primaryId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stateCallback.stateCallback(true);
        }
    }
}
