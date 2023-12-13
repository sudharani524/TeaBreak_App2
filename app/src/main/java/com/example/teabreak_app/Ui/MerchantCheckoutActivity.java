package com.example.teabreak_app.Ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teabreak_app.ModelClass.Order_delivery_type;
import com.example.teabreak_app.ModelClass.orderDetails;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.repository.ApiClient;
import com.example.teabreak_app.repository.ApiInterface;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MerchantCheckoutActivity extends AppCompatActivity {
    private EditText edtAmount;
    Context mContext;
    ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_checkout);

        mContext = MerchantCheckoutActivity.this;
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(true);
        edtAmount = findViewById(R.id.edt_amount);

    }

    public void onClick(View view) {


        if (edtAmount.getText().toString().length() == 0) {
            Toast.makeText(MerchantCheckoutActivity.this, "Enter Amount", Toast.LENGTH_LONG).show();
        } else if (Integer.parseInt(edtAmount.getText().toString()) > 0) {

            initiatePayment(edtAmount.getText().toString());

        } else {
            Toast.makeText(MerchantCheckoutActivity.this, "Enter Amount", Toast.LENGTH_LONG).show();
        }
    }



// This API is implemented by merchant
    private void initiatePayment(String amount) {

        ApiInterface apiInterface = ApiClient.getClient(Constant.MERCHANT_SERVER_URL).create(ApiInterface.class);

        Call<JsonObject> call = apiInterface.initiatePayment(amount);
        show();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                hide();


                if (response != null) {

                    Log.d("TAG","rsponse payment : "+response.body());
                    try {

                        JSONObject job = new JSONObject(response.body().toString());
                        Log.d("TAG","rsponse payment1 : "+job);
                        if (job.getString("status").equalsIgnoreCase("0")) {

                            orderDetails order = new orderDetails();
                            order.setEnc_val(job.getString("enc_val"));
                            order.setRedirect_url(job.getString("redirect_url"));
                            order.setCancel_url(job.getString("cancel_url"));
                            order.setOrder_id(job.getString("order_id"));
                            order.setAccess_code(job.getString("access_code"));

                            Intent intent = new Intent(mContext, CCAvenueActivity.class);
                            intent.putExtra("order", order);
                            startActivity(intent);
                        } else {
                            show_alert(job.getString("status_message"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        show_alert("Please try Again!!!");
                    }

                }
                else
                {
                    show_alert("Please try Again!!!");

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hide();
                Log.d("TAG","rsponse payment error : "+t.toString());
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void show() {
        if (!((Activity) mContext).isFinishing())
            if (progress != null && !progress.isShowing())
                progress.show();

    }

    public void hide() {
        if (!((Activity) mContext).isFinishing())
            if (progress != null && progress.isShowing())
                progress.dismiss();

    }

    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                MerchantCheckoutActivity.this).create();

        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");

        alertDialog.setMessage(msg);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.show();
    }

}