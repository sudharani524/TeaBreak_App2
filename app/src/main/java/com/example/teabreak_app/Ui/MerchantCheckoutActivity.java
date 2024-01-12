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
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ccavenue.indiasdk.AvenueOrder;
import com.ccavenue.indiasdk.AvenuesApplication;
import com.ccavenue.indiasdk.AvenuesTransactionCallback;
import com.example.teabreak_app.ModelClass.orderDetails;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.repository.ApiClient;
import com.example.teabreak_app.repository.ApiInterface;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MerchantCheckoutActivity extends AppCompatActivity implements AvenuesTransactionCallback {
    private EditText edtAmount;
    Context mContext;
    ProgressDialog progress;
    String Order_id;
    String Order_id_2;
    String  ba1,ba2;
    private TeaBreakViewModel viewModel;
    String request_hash="",req_id="";

    String gateway_id="",gateway_name="",payment_mode="",workingKey="",merchantId="",accessCode="",status="",last_update_date_time="",requestId="";
    String responseCode,responseStatus,responseMessage,secureToken,creationTimestamp,secureTokenExpiry,responseHash;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_checkout);

        viewModel = ViewModelProviders.of(MerchantCheckoutActivity.this).get(TeaBreakViewModel.class);
        payment_gateway_details_api_call();

      //  Order_id = String.valueOf(System.currentTimeMillis());
       Order_id=getIntent().getStringExtra("order_no");
       req_id=String.valueOf(System.currentTimeMillis());
       Log.e("req_id",req_id);

        mContext = MerchantCheckoutActivity.this;
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(true);
     //   edtAmount = findViewById(R.id.edt_amount);

    /*    byte[] byteArray = request_hash.getBytes();
         ba1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
         Log.e("req_hash",ba1);*/

        //String base64=Base64.encodeToString()


    }

    private void secure_token_api_call() {
        Log.e("secure_token","secure_token_mthd");
        JsonObject object = new JsonObject();
       // Long a= Long.valueOf(requestId);

/*        Long b= Long.valueOf(accessCode);
        Long c= Long.valueOf(ba1);
        object.addProperty("requestId",Order_id);*/

       // object.addProperty("requestId",requestId);
      //  object.addProperty("requestId",req_id);
      //  object.addProperty("accessCode",accessCode);
       // object.addProperty("requestHash",ba1);
       // object.addProperty("merchantId",merchantId);

       // String[] s_array=new String[3];
      //  s_array[0]=requestId;
       // s_array[1]="AVFB29KC10BD75BFDB";
       // s_array[1]=accessCode;//live
     //   s_array[1]="AVGV42KL73BP43VGPB";//live
      //  s_array[2]=ba1;


        viewModel.get_secure_token().observe(MerchantCheckoutActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    Log.d("secure_token_res","secure_token "+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                       Log.e("secure_token_res",jsonObject.toString());

                       if(jsonObject1.getString("text").equalsIgnoreCase("Success")){
                           secureToken=jsonObject1.getJSONObject("message").getString("secureToken");
                           responseHash=jsonObject1.getJSONObject("message").getString("responseHash");
                           responseCode=jsonObject1.getJSONObject("message").getString("responseHash");

                           Log.e("response_hash",responseHash);
                           request_hash=Order_id+"INR"+"1.00"+secureToken;
                           try {
                               ba2=generateReqHash(request_hash);
                               Log.e("ba2",ba2);
                           } catch (NoSuchAlgorithmException e) {
                               Log.e("ba2_Exception",ba2);
                               throw new RuntimeException(e);
                           }
                           initiatePayment1();
                       }


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(MerchantCheckoutActivity.this, "Exception.."+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(MerchantCheckoutActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void payment_gateway_details_api_call() {

        JsonObject object = new JsonObject();
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_payment_details(object).observe(MerchantCheckoutActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    Log.d("payment_details","payment_details "+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        //String message=jsonObject1.getString("message");
                        JSONObject data_object= new JSONObject();
                        data_object=jsonObject1.getJSONObject("data");
                        gateway_id=data_object.getString("gateway_id");
                        gateway_name=data_object.getString("gateway_name");
                        payment_mode=data_object.getString("payment_mode");
                        workingKey=data_object.getString("workingKey");
                        merchantId=data_object.getString("merchantId");
                        accessCode=data_object.getString("accessCode");
                        status=data_object.getString("status");
                        last_update_date_time=data_object.getString("last_update_date_time");

                        requestId=jsonObject1.getString("requestId");

                        Log.e("accessCode_for_secure_token",accessCode);
                        Log.e("working_key",workingKey);
                        Log.e("merchantId",merchantId);
                        Log.e("workingKey",workingKey);

                        request_hash=requestId+workingKey+merchantId;
                       // request_hash=requestId+"320ECB91D6183CD5D65D9D91E2D2CF2B"+merchantId;
                       // request_hash=requestId+"C84AF8924DB0262E1362AAD0BEBE59ED"+merchantId; //live
                       // request_hash=requestId+"5D75051B7F577D861C9ECAD9B619804D"+merchantId; //live

                       // request_hash=req_id+workingKey+merchantId; //live

                        Log.e("request_hash",request_hash);
                        try {
                            ba1 =generateReqHash(request_hash);
                            Log.e("secure_token",ba1);
                        } catch (NoSuchAlgorithmException e) {
                            Log.e("Exception",""+e);
                            throw new RuntimeException(e);
                        }


                      /*  byte[] byteArray = request_hash.getBytes();
                        ba1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        Log.e("req_hash",ba1);
*/

                        secure_token_api_call();

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(MerchantCheckoutActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(MerchantCheckoutActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private String generateReqHash(String str) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] digest = md.digest(str.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println(sb);
        return sb.toString();
    }


/*    public void onClick(View view) {


        if (edtAmount.getText().toString().length() == 0) {
            Toast.makeText(MerchantCheckoutActivity.this, "Enter Amount", Toast.LENGTH_LONG).show();
        } else if (Integer.parseInt(edtAmount.getText().toString()) > 0) {

//            initiatePayment(edtAmount.getText().toString());
            initiatePayment1();


        } else {
            Toast.makeText(MerchantCheckoutActivity.this, "Enter Amount", Toast.LENGTH_LONG).show();
        }
    }*/


//   String xmlResponse=bundle.getString("response");

/*    public static String convertXmlToJson(String xmlString) {
        try {
            // Parse XML using Jsoup
            Document document = Jsoup.parse(xmlString, "", Parser.xmlParser());

            // Convert XML to JSON using a recursive function
            JSONObject jsonObject = convertElementToJson((Element) document);

            // Return the JSON string
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JSONObject convertElementToJson(Element element) {
        JSONObject jsonObject = new JSONObject();

        for (Element child : element.children()) {
            // Recursively convert child elements to JSON
            jsonObject.put(child.tagName(), child.children().isEmpty()
                    ? child.text()
                    : convertElementToJson(child));
        }

        return jsonObject;
    }
    */


    private void initiatePayment1() {

        AvenueOrder orderDetails = new AvenueOrder();
        orderDetails.setOrderId(Order_id);
        orderDetails.setRequestHash(ba2);
        orderDetails.setAccessCode(accessCode);
        orderDetails.setMerchantId(merchantId);
        orderDetails.setCurrency("INR");
        orderDetails.setAmount("1.00");
       // orderDetails.setRedirectUrl("redirect_url");

     //   orderDetails.setRedirectUrl(Constant.SERVER_BASE_URL+"redirectPage.php");
       // orderDetails.setCancelUrl("cancel_url");
       // orderDetails.setCancelUrl("https://www.testserver.com/cancelPage.php");

     //   orderDetails.setCancelUrl(Constant.SERVER_BASE_URL+"cancelPage.php");


        orderDetails.setRedirectUrl("https://www.testserver.com/redirectPage.php");
        orderDetails.setCancelUrl("https://www.testserver.com/cancelPage.php");


        orderDetails.setCustomerId("9390126304");
        orderDetails.setPaymentType("all");
        orderDetails.setMerchantLogo(String.valueOf(R.drawable.logo));

        orderDetails.setBillingName(SaveAppData.getLoginData().getName());
        orderDetails.setBillingAddress("Hyderabad");
        orderDetails.setBillingCountry("India");
        orderDetails.setBillingState("Telangana");
        orderDetails.setBillingCity("Hyderabad");
        orderDetails.setBillingZip("500016");
        orderDetails.setBillingTel("9390126304");
        orderDetails.setBillingEmail("sudhak4585@gmail.com");


        orderDetails.setDeliveryName("test");
        orderDetails.setDeliveryAddress("Hyderabad");
        orderDetails.setDeliveryCountry("India");
        orderDetails.setDeliveryState("Telangana");
        orderDetails.setDeliveryCity("Hyderabad");
        orderDetails.setDeliveryZip("500016");
        orderDetails.setDeliveryTel("9390126304");
        orderDetails.setMerchant_param1("test"); //total 5 parameters
        orderDetails.setMerchant_param2("transaction"); //total 5 parameters
        orderDetails.setMerchant_param3("1"); //total 5 parameters
        orderDetails.setMerchant_param4("2"); //total 5 parameters
        orderDetails.setMerchant_param5("3"); //total 5 parameters
        orderDetails.setMobileNo("9390126304");
        orderDetails.setPaymentEnviroment("“app_staging"); //app_live - prod

        orderDetails.setColorPrimary("#008000");
      //  orderDetails.setColorPrimary("color_primary");
        orderDetails.setColorAccent("#009688");
      //  orderDetails.setColorAccent("color_accent");
        orderDetails.setColorFont("#fd5c63");
      //  orderDetails.setColorFont("color_font");
        orderDetails.setBackgroundDrawable(0);
       // orderDetails.setBackgroundDrawable(R.drawable.img);
        //To begin transaction through SDK…
        AvenuesApplication.startTransaction(MerchantCheckoutActivity.this, orderDetails);

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

    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("transaction_res","onTransactionResponse"+bundle.toString());
    }

    @Override
    public void onErrorOccurred(String s) {
        Log.e("transaction_res","onErrorOccurred "+s);
    }

    @Override
    public void onCancelTransaction(String s) {
        Log.e("transaction_res","onCancelTransaction"+s);
    }
}