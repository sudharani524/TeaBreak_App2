package com.example.teabreak_app.repository;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET("login/line_items")
    Call<JsonObject> list_items();

    @Headers("Content-Type: application/json")
    @GET("login/user_roles_list")
    Call<JsonObject> roles_list();

    @Headers("Content-Type: application/json")
    @POST("apis/get_cart_details")
    Call<JsonObject> cart_list(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("apis/update_line_item_to_cart")
    Call<JsonObject> add_cart(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<JsonObject> login(@Body JsonObject jsonObject);
    @Headers("Content-Type: application/json")
    @POST("login/user_logout")
    Call<JsonObject>logout (@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("apis/change_pwd")
    Call<JsonObject>change_password(@Body JsonObject jsonObject);

}
