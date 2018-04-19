package com.example.trw.maginder.service.http_manger;

import com.example.trw.maginder.service.interface_service.ApiServiceMenu;
import com.example.trw.maginder.service.interface_service.ApiServicePreOrderMenu;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by _TRW on 1/2/2561.
 */

public class HttpManagerMenu {

    private static HttpManagerMenu instances;

    public static HttpManagerMenu getInstance() {
        if (instances == null)
            instances = new HttpManagerMenu();
        return instances;
    }

    private ApiServiceMenu service;

    private HttpManagerMenu() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://it2.sut.ac.th/prj60_g14/Project/App/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(ApiServiceMenu.class);
    }

    public ApiServiceMenu getService() {
        return service;
    }
}
