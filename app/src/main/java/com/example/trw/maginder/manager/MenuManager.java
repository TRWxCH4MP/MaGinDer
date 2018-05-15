package com.example.trw.maginder.manager;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.trw.maginder.R;
import com.example.trw.maginder.adapter.item.BaseItem;
import com.example.trw.maginder.adapter.item.MenuItem;
import com.example.trw.maginder.db.InsertData;
import com.example.trw.maginder.db.QueryData;
import com.example.trw.maginder.db.callback.OnStateCallback;
import com.example.trw.maginder.db.callback.SendListDataCallback;
import com.example.trw.maginder.db.entity.MenuEntity;
import com.example.trw.maginder.model.Contextor;
import com.example.trw.maginder.model.StaticStringHelper;
import com.example.trw.maginder.service.dao.RestaurantItemCollectionDao;
import com.example.trw.maginder.service.dao.RestaurantItemDao;
import com.example.trw.maginder.service.dao.RestaurantMenuTypeItemCollectionDao;
import com.example.trw.maginder.service.http_manger.HttpManagerMenu;
import com.example.trw.maginder.service.http_manger.HttpManagerRestaurant;
import com.example.trw.maginder.service.http_manger.HttpManagerRestaurantMenuType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuManager {

    private static MenuManager manager;
    private Context context;

    public static MenuManager getInstance() {
        if (manager == null) {
            manager = new MenuManager();
        }
        return manager;
    }

    private MenuManager() {
        context = Contextor.getInstance().getContext();
    }

    public void onGetMenuAmount(final MenuAmountCallback callback) {
        QueryData.onLoadMenu(context, new SendListDataCallback() {
            @Override
            public void loadMenuCallback(List<MenuEntity> menuEntities, boolean isSuccess) {
                if (isSuccess) {
                    if (menuEntities.size() <= 0) {
                        callback.onMenuAmount("0");
                    } else {
                        callback.onMenuAmount(String.valueOf(menuEntities.size()));
                    }
                } else {
                    callback.onMenuAmount("0");
                }
            }
        });
    }

    public void onGetMenuAllDetail(String menuId, final MenuDetailCallback callback) {
//        Log.d(TAG, "getMenuAllDetail: ");
        retrofit2.Call<RestaurantItemCollectionDao> call = HttpManagerMenu.getInstance().getService().repos(menuId);
        call.enqueue(new Callback<RestaurantItemCollectionDao>() {
            @Override
            public void onResponse(retrofit2.Call<RestaurantItemCollectionDao> call, Response<RestaurantItemCollectionDao> response) {
                if (response.isSuccessful()) {
                    RestaurantItemCollectionDao dao = response.body();
//                    Log.d(TAG, "onResponse: success");
                    callback.onComplete(dao.getData());
//                    chooseMenuCallback.chooseMenuCallback(dao.getData(), timeStampOrderMenu);
                } else {
                    callback.onFailure(String.valueOf(response.errorBody()));
//                    Log.d(TAG, "onResponse: failure");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<RestaurantItemCollectionDao> call, Throwable t) {
                callback.onFailure(t.toString());
//                Log.d(TAG, "onZoneIdIsNotMatch: " + t.toString());
            }
        });
    }

    public void onSetMenuToSQLite(List<RestaurantItemDao> data, final SetupMenuToSQLiteCallback callback) {
        MenuEntity menuEntity = new MenuEntity();
        for (int index = 0; index < data.size(); index++) {
            menuEntity.setDate(data.get(index).getDate());
            menuEntity.setIdMenu(data.get(index).getIdMenu());
            menuEntity.setIdKitchen(data.get(index).getIdKitchen());
            menuEntity.setImg(data.get(index).getImg());
            menuEntity.setName(data.get(index).getName());
            menuEntity.setPrice(data.get(index).getPrice());
        }

        InsertData.onInsertMenu(context, menuEntity, new OnStateCallback() {
            @Override
            public void stateCallback(boolean isSuccess) {
                if (isSuccess) {
//                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    callback.onComplete(true);
                } else {
                    callback.onFailure("Failure");
                }
            }
        });
    }

    public List<BaseItem> onCreateMenu(List<RestaurantItemDao> listRestaurant, String menuType) {
        List<BaseItem> itemList = new ArrayList<>();

        if (!listRestaurant.isEmpty() && !menuType.isEmpty()) {
            for (int index = 0; index < listRestaurant.size(); index++) {
                if (listRestaurant.get(index).getNameType().equals(menuType)) {
//                    Log.d(TAG, "createItemTotal: Item + " + String.valueOf(listRestaurant.get(index).getNameType()));
                    itemList.add(new MenuItem()
                            .setOrderDate(listRestaurant.get(index).getDate())
                            .setOrderMenuId(listRestaurant.get(index).getIdMenu())
                            .setOrderKitchenId(listRestaurant.get(index).getIdKitchen())
                            .setOrderImg(StaticStringHelper.IMAGE_URL + listRestaurant.get(index).getImg())
                            .setOrderName(listRestaurant.get(index).getName())
                            .setOrderPrice(listRestaurant.get(index).getPrice() + " " + context.getString(R.string.baht))
                    );
                }
            }
        }
        return itemList;
    }

    public void onGetMenuType(String restaurantId, final GetMenuTypeCallback callback) {
        Call<RestaurantMenuTypeItemCollectionDao> call = HttpManagerRestaurantMenuType.getInstance().getService().repos(restaurantId);
        call.enqueue(new Callback<RestaurantMenuTypeItemCollectionDao>() {
            @Override
            public void onResponse(Call<RestaurantMenuTypeItemCollectionDao> call, Response<RestaurantMenuTypeItemCollectionDao> response) {
                if (response.isSuccessful()) {
//                    Log.d(TAG, "Connect service is successful");
                    callback.onComplete(response.body());
                } else {
                    callback.onFailure("Connect service is failure");
//                    Log.d(TAG, "Connect service is failure");
                }
            }

            @Override
            public void onFailure(Call<RestaurantMenuTypeItemCollectionDao> call, Throwable t) {
//                Log.d(TAG, t.toString());
                callback.onFailure(t.toString());
            }
        });
    }

    public void onGetMenu(String restaurantId, final GetMenuCallback callback) {
        Call<RestaurantItemCollectionDao> call = HttpManagerRestaurant.getInstance().getService().repos(restaurantId);
        call.enqueue(new Callback<RestaurantItemCollectionDao>() {
            @Override
            public void onResponse(Call<RestaurantItemCollectionDao> call, Response<RestaurantItemCollectionDao> response) {
                if (response.isSuccessful()) {
//                    Log.d(TAG, "Connect service is successful");
                  callback.onComplete(response.body().getData());
                } else {
                    callback.onFailure("Connect service is failure");
//                    Log.d(TAG, "Connect service is failure");
                }
            }

            @Override
            public void onFailure(Call<RestaurantItemCollectionDao> call, Throwable t) {
//                Log.d(TAG, t.toString());
                callback.onFailure(t.toString());
            }
        });
    }

    public interface MenuDetailCallback {
        void onComplete(List<RestaurantItemDao> data);

        void onFailure(String error);
    }

    public interface MenuAmountCallback {
        void onMenuAmount(String size);
    }

    public interface SetupMenuToSQLiteCallback {
        void onComplete(boolean isSuccess);

        void onFailure(String error);
    }

    public interface GetMenuTypeCallback {
        void onComplete(RestaurantMenuTypeItemCollectionDao daoMenuType);

        void onFailure(String error);
    }

    public interface GetMenuCallback {
        void onComplete(List<RestaurantItemDao> data);

        void onFailure(String error);
    }
}
