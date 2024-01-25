package com.glitss.teabreak_app.repository;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    static String API_URL = "";
   /* public static GsonConverterFactory gson = new GsonBuilder()
            .setLenient()
            .create();*/

    /* Initializing Retrofit object */
    public static Retrofit getClient(String url) {
        API_URL = url;
        Retrofit retrofit = null;

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                    .build();
        }

        return retrofit;
    }

}
