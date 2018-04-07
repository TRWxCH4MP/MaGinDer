package com.example.trw.maginder.db;

import android.content.Context;
import android.os.AsyncTask;

import com.example.trw.maginder.db.callback.SendListDataCallback;
import com.example.trw.maginder.db.database.RestaurantDatabase;
import com.example.trw.maginder.db.entity.MenuEntity;

import java.util.List;

/**
 * Created by _TRW on 26/12/2560.
 */

public class QueryData {

    public static void onLoadMenu(final Context context, final SendListDataCallback callback) {
        new loadMenu(context, callback).execute();
    }

    public static class loadMenu extends AsyncTask<Void, Void, Void> {
        Context context;
        SendListDataCallback callback;
        List<MenuEntity> listMenu;


        public loadMenu(Context context, SendListDataCallback callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            listMenu = RestaurantDatabase.getAppDatabase(context).itemDao().loadAllMenu();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!listMenu.isEmpty()) {
                callback.loadMenuCallback(listMenu, true);
            } else {
                callback.loadMenuCallback(listMenu, false);
            }
        }
    }
}
