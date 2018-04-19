package com.example.trw.maginder.service.http_manger;

import com.example.trw.maginder.service.interface_service.ApiService;
import com.example.trw.maginder.service.interface_service.ApiServiceTable;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by _TRW on 16/1/2561.
 */

public class HttpManagerTable {

    private static HttpManagerTable instances;

    public static HttpManagerTable getInstance() {
        if (instances == null)
            instances = new HttpManagerTable();
        return instances;
    }

    private ApiServiceTable service;

    private HttpManagerTable() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://it2.sut.ac.th/prj60_g14/Project/App/")
                .baseUrl("http://it2.sut.ac.th/prj60_g14/Project/dashboard/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(ApiServiceTable.class);
    }

    public ApiServiceTable getService() {
        return service;
    }
}
