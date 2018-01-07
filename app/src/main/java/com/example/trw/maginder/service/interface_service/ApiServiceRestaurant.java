package com.example.trw.maginder.service.interface_service;

import com.example.trw.maginder.service.dao.RestaurantItemCollectionDao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by _TRW on 6/1/2561.
 */

public interface ApiServiceRestaurant {

    @GET("show_menu.php")
    Call<RestaurantItemCollectionDao> repos(@Query("ID_Restaurant") String idRestaurant);
}
