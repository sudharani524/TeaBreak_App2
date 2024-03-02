package com.glitss.teabreak_app.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.glitss.teabreak_app.Ui.MainActivity;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.repository.ApiClient;
import com.glitss.teabreak_app.repository.ApiInterface;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Constant {

    public static final String SERVER_BASE_URL = "https://teabreak.digitalrupay.com/webservices/";

  //  public static final String SERVER_BASE_URL = "https://teabreak.digitalrupay.com/dev/webservices/";
    public static final String ccavenue_test_url=" https://test.ccavenue.com";
    public static String token_status="";
    public static final String MERCHANT_SERVER_URL = "http://122.182.6.212:8080/";
    public static final String CCAVENUE_URL = "https://qasecure.ccavenue.com/transaction.do?command=initiateTransaction";

    public static void logout_api_call(Context context) {
        JsonObject object = new JsonObject();
        object.addProperty("user_id",SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> logout = apiInterface.logout(object);

        logout.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.body()!=null){
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body().toString());

                        if(jsonObj.getString("message").equalsIgnoreCase("Successfully Logout")){
                            SaveAppData.saveOperatorLoginData(null);
                            context.startActivity(new Intent(context, MainActivity.class));
                        }

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        //Snackbar.make(context,findViewById(android.R.id.content),""+e,Snackbar.LENGTH_LONG).show();
                        Toast.makeText(context, "Exception"+e, Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public static void check_token_status_api_call(Context context) {

        Log.e("check_tokens","check_token_status");
        TeaBreakViewModel viewModel= ViewModelProviders.of((FragmentActivity) context).get(TeaBreakViewModel.class);
        JsonObject object = new JsonObject();
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.check_token_status_api(object).observe((LifecycleOwner) context, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject != null){
                    Log.d("TAG","password "+jsonObject);
                    JSONObject jsonObject1= null;
                    try {
                        jsonObject1 = new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        token_status=jsonObject1.getString("token_status");
                        if(token_status.equalsIgnoreCase("0")){
                            logout_api_call(context);
                            Log.e("token_status","token_status_0");
                            context.startActivity(new Intent(context, MainActivity.class));
                            ((FragmentActivity) context).finish();
                        }
                    }
                    catch (JSONException e) {
                        //  throw new RuntimeException(e);
                        Toast.makeText(context, "Exception"+e, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
