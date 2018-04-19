package com.example.trw.maginder.service.interface_service;

import com.example.trw.maginder.service.dao.RestaurantMenuTypeItemCollectionDao;
import com.example.trw.maginder.service.dao.TableItemCollectionDao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by _TRW on 16/1/2561.
 */

public interface ApiServiceTable {

    @GET("show_zone.php")
    Call<TableItemCollectionDao> repos(@Query("id") String idRestaurant);
}
