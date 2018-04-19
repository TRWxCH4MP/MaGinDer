package com.example.trw.maginder.service.interface_service;

import com.example.trw.maginder.service.dao.RestaurantMenuTypeItemCollectionDao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by _TRW on 9/1/2561.
 */

public interface ApiServiceRestaurantMenuType {

    @GET("show_menu_type.php")
    Call<RestaurantMenuTypeItemCollectionDao> repos(@Query("ID_Restaurant") String idRestaurant);
}
