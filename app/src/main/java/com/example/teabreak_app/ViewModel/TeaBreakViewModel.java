package com.example.teabreak_app.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.repository.ApiClient;
import com.example.teabreak_app.repository.ApiInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeaBreakViewModel extends ViewModel {

    private MutableLiveData<JsonObject> list_items_status;
    private MutableLiveData<JsonObject> roles_list_status;
    private MutableLiveData<JsonObject> cart_list_status;
    private MutableLiveData<JsonObject> add_cart_status;
    private MutableLiveData<JsonObject> logout;


    public LiveData<JsonObject> get_list_items() {
        list_items_status = new MutableLiveData<JsonObject>();
        list_items_api_call();
        return list_items_status;
    }

    public LiveData<JsonObject> get_roles_list() {
        roles_list_status = new MutableLiveData<JsonObject>();
        roles_list_api_call();
        return roles_list_status;
    }

    public LiveData<JsonObject> get_cart_list(JsonObject jsonObject) {
        cart_list_status = new MutableLiveData<JsonObject>(jsonObject);
        cart_list_api_call(jsonObject);
        return cart_list_status;
    }

    public LiveData<JsonObject> add_cart_api(JsonObject jsonObject) {
        add_cart_status = new MutableLiveData<JsonObject>(jsonObject);
        add_cart_api_call(jsonObject);
        return add_cart_status;
    }
    public LiveData<JsonObject> logout_api(JsonObject jsonObject) {
        logout = new MutableLiveData<JsonObject>();
        logout_api_call(jsonObject);

        return logout;
    }




    private void add_cart_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> add_cart = apiInterface.add_cart(jsonObject);
        add_cart.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body()!= null){
                    add_cart_status.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });
    }



    private void cart_list_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> cart_list = apiInterface.cart_list(jsonObject);
        cart_list.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body()!= null){
                    cart_list_status.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });
    }

    private void roles_list_api_call() {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> roles_list = apiInterface.roles_list();
        roles_list.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body()!= null){
                    roles_list_status.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });
    }

    private void list_items_api_call() {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> list_items = apiInterface.list_items();
        list_items.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body()!= null){
                    list_items_status.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });
    }
    private void logout_api_call(JsonObject jsonObject) {

        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> log_out = apiInterface.logout(jsonObject);
        log_out.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response != null){
                    logout.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());

            }
        });

    }



}