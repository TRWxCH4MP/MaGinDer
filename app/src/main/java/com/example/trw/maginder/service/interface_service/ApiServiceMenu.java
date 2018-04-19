package com.example.trw.maginder.service.interface_service;

import com.example.trw.maginder.service.dao.PreOrderMenuItemCollectionDao;
import com.example.trw.maginder.service.dao.RestaurantItemCollectionDao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by _TRW on 1/2/2561.
 */

public interface ApiServiceMenu {

    @GET("show_menu_by_id.php?")
    Call<RestaurantItemCollectionDao> repos(@Query("ID_Menu") String idMenu);
}
