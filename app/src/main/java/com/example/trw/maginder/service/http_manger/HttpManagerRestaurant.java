package com.example.trw.maginder.service.http_manger;

import com.example.trw.maginder.service.interface_service.ApiServiceRestaurant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by _TRW on 6/1/2561.
 */

public class HttpManagerRestaurant {

    private static HttpManagerRestaurant instances;

    public static HttpManagerRestaurant getInstance() {
        if (instances == null)
            instances = new HttpManagerRestaurant();
        return instances;
    }

    private ApiServiceRestaurant service;

    private HttpManagerRestaurant() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suttest.atwebpages.com/Project/App/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(ApiServiceRestaurant.class);
    }

    public ApiServiceRestaurant getService() {
        return service;
    }
}