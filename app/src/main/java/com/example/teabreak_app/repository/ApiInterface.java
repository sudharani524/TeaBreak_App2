package com.example.teabreak_app.repository;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET(" login/line_items")
    Call<JsonObject> list_items();

    @Headers("Content-Type: application/json")
    @GET("login/user_roles_list")
    Call<JsonObject> roles_list();

}
