package com.example.trw.maginder.manager;

import com.example.trw.maginder.service.dao.TableItemCollectionDao;
import com.example.trw.maginder.service.http_manger.HttpManagerTable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableManager {

    private static TableManager manager;

    public static TableManager getInstance() {
        if (manager == null) {
            manager = new TableManager();
        }
        return manager;
    }

    public void onCreateTableZone(String restaurantId, final TableManagerCallback callback) {
        Call<TableItemCollectionDao> call = HttpManagerTable.getInstance().getService().repos(restaurantId);
        call.enqueue(new Callback<TableItemCollectionDao>() {
            @Override
            public void onResponse(Call<TableItemCollectionDao> call, Response<TableItemCollectionDao> response) {
                if (response.isSuccessful()) {
                    TableItemCollectionDao dao = response.body();
                    callback.onCreateSuccess(dao);
                } else {
                    callback.onCreateFailure(String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<TableItemCollectionDao> call, Throwable t) {
                callback.onCreateFailure(t.toString());
            }
        });
    }

    public interface TableManagerCallback {
        void onCreateSuccess(TableItemCollectionDao dao);

        void onCreateFailure(String exception);
    }
}
