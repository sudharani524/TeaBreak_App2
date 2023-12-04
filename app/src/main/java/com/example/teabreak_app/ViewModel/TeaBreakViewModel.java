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



}