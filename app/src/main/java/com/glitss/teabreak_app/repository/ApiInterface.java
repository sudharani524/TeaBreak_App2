package com.glitss.teabreak_app.repository;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET("login/line_items")
    Call<JsonObject> list_items();

    @Headers("Content-Type: application/json")
    @GET("apis/user_roles_list")
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
    @Headers("Content-Type: application/json")
    @POST("apis/get_user_order_history")
    Call<JsonObject>orderslist(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("apis/get_order_delivery_types")
    Call<JsonObject>order_delivery_type(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("apis/user_create_order_details")
    Call<JsonObject>create_order(@Body JsonObject jsonObject);
    @Headers("Content-Type: application/json")
    @POST("apis/get_user_order_items_by_order_id")
    Call<JsonObject>Order_history(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("login/delete_cart_items")
    Call<JsonObject>dlt_itm_api(@Body JsonObject jsonObject);
    @Headers("Content-Type: application/json")
    @POST("login/get_dispatcher_order_list")
    Call<JsonObject>ordered_list_items(@Body JsonObject jsonObject);
    @Headers("Content-Type: application/json")
    @POST("login/get_dispatcher_order_item_list")
    Call<JsonObject>Items_ordered_list(@Body JsonObject jsonObject);

    @POST("MobPHPKit/india/init_payment.php")
    Call<JsonObject> initiatePayment(@Query("amount") String amount);

    @Headers("Content-Type: application/json")
    @POST("apis/check_token_status")
    Call<JsonObject>check_token_status(@Body JsonObject jsonObject);
    @Headers("Content-Type: application/json")
    @POST("apis/get_user_wallet_history")
    Call<JsonObject>Wallet_history(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("login/get_order_delivery_dates")
    Call<JsonObject>order_delivery_dates(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("apis/get_current_wallet_amount")
    Call<JsonObject>current_wallet_amt(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("apis/get_payment_gateway_details")
    Call<JsonObject>payment_gateway_details(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("apis/edit_dispatcher_order_item_list")
    Call<JsonObject>edit_dispatcher_order_items_list(@Body JsonObject jsonObject);


    @Headers("Content-Type: application/json")
    @POST("apis/get_accountant_pending_orders_list")
    Call<JsonObject>get_account_pending_orders_list(@Body JsonObject jsonObject);


    @Headers("Content-Type: application/json")
    @POST("apis/get_accountant_approved_orders_list")
    Call<JsonObject>get_account_approved_orders_list(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("apis/accountant_update_to_approve_order")
    Call<JsonObject>accountant_update_to_approve_order(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("apis/update_to_dispatched_order")
    Call<JsonObject>update_to_dispatch_order(@Body JsonObject jsonObject);


 /* @FormUrlEncoded
    @POST("login/create_secure_token")
  //  @POST("apis/request_secure_token_generation")
    Call<JsonObject>secure_token_generation_api(@Field("requestId") String param1,
                                    @Field("accessCode") String param2,
                                    @Field("requestHash") String param3);*/

    @Headers("Content-Type: application/json")
    @GET("login/create_secure_token")
    Call<JsonObject> secure_token_generation_api();


  /*  @FormUrlEncoded
    @POST("mobile/service/getSecureToken")
    Call<JsonObject>secure_token_generation_api(@Field("requestId") String param1,
                                                @Field("accessCode") String param2,
                                                @Field("requestHash") String param3);*/


    @Headers("Content-Type: application/json")
    @POST("succcess_redirect_url.php")
    Call<JsonObject> redirect(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("login/insert_payments")
    Call<JsonObject> insert_payment(@Body JsonObject jsonObject);



    @Headers("Content-Type: application/json")
    @POST(" login/generate_request_hash")
    Call<JsonObject> generate_request_hash(@Body JsonObject jsonObject);



    @Headers("Content-Type: application/json")
    @GET("login/transportation_list")
    Call<JsonObject> transport_type();


    @Headers("Content-Type: application/json")
    @POST("login/get_dispatcher_order_closed_list")
    Call<JsonObject> closed_orders_list(@Body JsonObject jsonObject);
    @Headers("Content-Type: application/json")
    @POST("apis/delivery_route_codes")
    Call<JsonObject>routes(@Body JsonObject jsonObject);

    @Headers("Content-Type: application/json")
    @POST("apis/create_new_vendor_user")
    Call<JsonObject>vendor_registration(@Body JsonObject jsonObject);


    @Headers("Content-Type: application/json")
    @POST("apis/get_user_transaction_history")
    Call<JsonObject>transaction_history(@Body JsonObject jsonObject);

}
