package com.example.trw.maginder.service.interface_service;

import com.example.trw.maginder.service.dao.LoginItemDao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by _TRW on 4/1/2561.
 */

public interface ApiService {

    @GET("login.php")
    Call<LoginItemDao> repos(@Query("username") String username, @Query("password") String password);

}
