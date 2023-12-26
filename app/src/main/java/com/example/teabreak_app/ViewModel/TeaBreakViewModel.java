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
    private MutableLiveData<JsonObject> change_password_status;
    private MutableLiveData<JsonObject> insert_order_status;
    private MutableLiveData<JsonObject> list_order_status;
    private MutableLiveData<JsonObject> Order_history_details;
    private MutableLiveData<JsonObject> dlt_item_status;
    private MutableLiveData<JsonObject> check_token_status;
    private MutableLiveData<JsonObject> Wallet_history_details;
    private MutableLiveData<JsonObject> wallet_amt_status;
    private MutableLiveData<JsonObject> payment_gateway_details_status;
    private MutableLiveData<JsonObject> secure_token_status;
    private MutableLiveData<JsonObject> Order_list_details;
    private MutableLiveData<JsonObject> Orders_items_list;


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


    public LiveData<JsonObject> change_password(JsonObject jsonObject) {
        change_password_status = new MutableLiveData<JsonObject>();
        change_password_api_call(jsonObject);

        return change_password_status;
    }


    public LiveData<JsonObject> insert_order_api(JsonObject jsonObject) {
        insert_order_status = new MutableLiveData<JsonObject>();
        insert_order_api_call(jsonObject);

        return insert_order_status;
    }

    public LiveData<JsonObject> get_order_items(JsonObject jsonObject) {
        list_order_status = new MutableLiveData<JsonObject>(jsonObject);
        order_list_api_call(jsonObject);
        return list_order_status;
    }
    public LiveData<JsonObject> get_order_history(JsonObject jsonObject) {
        Order_history_details = new MutableLiveData<JsonObject>(jsonObject);
        order_history_api_call(jsonObject);
        return Order_history_details;
    }


    public LiveData<JsonObject> dlt_item_api(JsonObject jsonObject) {
        dlt_item_status = new MutableLiveData<JsonObject>(jsonObject);
        dlt_item_api_call(jsonObject);
        return dlt_item_status;
    }

    public LiveData<JsonObject> check_token_status_api(JsonObject jsonObject) {
        check_token_status = new MutableLiveData<JsonObject>();
        check_token_status_api_call(jsonObject);
        return check_token_status;
    }
    public LiveData<JsonObject> get_wallet_history(JsonObject jsonObject) {
        Wallet_history_details = new MutableLiveData<JsonObject>(jsonObject);
        wallet_histor_api_call(jsonObject);
        return Wallet_history_details;
    }



    public LiveData<JsonObject> get_wallet_amt(JsonObject jsonObject) {
        wallet_amt_status = new MutableLiveData<JsonObject>();
        wallet_amount_api_call(jsonObject);
        return wallet_amt_status;
    }


    public LiveData<JsonObject> get_payment_details(JsonObject jsonObject) {
        payment_gateway_details_status = new MutableLiveData<JsonObject>();
        payment_gateway_details_api(jsonObject);
        return payment_gateway_details_status;
    }

   /* public LiveData<JsonObject> get_secure_token(JsonObject jsonObject, String[] s_array) {
        secure_token_status = new MutableLiveData<JsonObject>();
        secure_token_api_call(jsonObject,s_array);
        return secure_token_status;
    }*/

    public LiveData<JsonObject> get_secure_token() {
        secure_token_status = new MutableLiveData<JsonObject>();
        secure_token_api_call();
        return secure_token_status;
    }
    public LiveData<JsonObject> get_ordered_list(JsonObject jsonObject) {
        Order_list_details = new MutableLiveData<JsonObject>(jsonObject);
        ordered_list_api_call(jsonObject);
        return Order_list_details;
    }
    public LiveData<JsonObject>get_Ordered_items_list(JsonObject jsonObject){
        Orders_items_list = new MutableLiveData<JsonObject>(jsonObject);
        ordered_list_items_api_call(jsonObject);
        return Orders_items_list;
    }
    private void ordered_list_items_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface=ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject>Items_list= apiInterface.Items_ordered_list(jsonObject);
        Items_list.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body()!= null){
                    Orders_items_list.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });
    }

    private void ordered_list_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface=ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject>Ordred_list=apiInterface.ordered_list_items(jsonObject);
        Ordred_list.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body()!= null){
                    Order_list_details.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });

    }
    private void payment_gateway_details_api(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> payment_details = apiInterface.payment_gateway_details(jsonObject);
        payment_details.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body()!= null){
                    payment_gateway_details_status.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });

    }


    private void secure_token_api_call() {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
      //  ApiInterface apiInterface = ApiClient.getClient("https://secure.ccavenue.com/").create(ApiInterface.class); //production
       // ApiInterface apiInterface = ApiClient.getClient("https://stgsecure.ccavenue.com/").create(ApiInterface.class); //test
        Call<JsonObject> secure_token = apiInterface.secure_token_generation_api();
       // Call<JsonObject> secure_token = apiInterface.secure_token_generation_api(s_array[0],s_array[1],s_array[2]);
        secure_token.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body()!= null){
                    secure_token_status.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });

    }

    private void wallet_amount_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> wallet_amount = apiInterface.current_wallet_amt(jsonObject);
        wallet_amount.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body()!= null){
                    wallet_amt_status.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });

    }




    private void wallet_histor_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> Wallet_history = apiInterface.Wallet_history(jsonObject);
        Wallet_history.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body()!= null){
                    Wallet_history_details.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });

    }


    private void check_token_status_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> token_status = apiInterface.check_token_status(jsonObject);
        token_status.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body()!= null){
                    check_token_status.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });

    }


    private void dlt_item_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> dlt_item = apiInterface.dlt_itm_api(jsonObject);
        dlt_item.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body()!= null){
                    dlt_item_status.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });
    }


    private void order_history_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> Order_history = apiInterface.Order_history(jsonObject);
        Order_history.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body()!= null){
                    Order_history_details.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());

            }
        });
    }

    private void order_list_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> orderslist = apiInterface.orderslist(jsonObject);
        orderslist.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body()!= null){
                    list_order_status.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });

    }

    private void insert_order_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> insert_api = apiInterface.create_order(jsonObject);
        insert_api.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body()!= null){
                    insert_order_status.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });
    }


    private void change_password_api_call(JsonObject jsonObject) {
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> change_passwod = apiInterface.change_password(jsonObject);
        change_passwod.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body()!= null){
                    change_password_status.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG_NAME", t.toString());
            }
        });
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