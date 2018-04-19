package com.example.trw.maginder.service.interface_service;

import com.example.trw.maginder.service.dao.OrderMenuItemDao;
import com.example.trw.maginder.service.dao.PreOrderMenuItemCollectionDao;
import com.example.trw.maginder.service.dao.RestaurantItemCollectionDao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by _TRW on 23/1/2561.
 */

public interface ApiServicePreOrderMenu {

    @GET("show_menu.php?")
    Call<PreOrderMenuItemCollectionDao> repos(@Query("ID_Restaurant") String idRestaurant);
}
