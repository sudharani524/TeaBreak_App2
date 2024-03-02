package com.glitss.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ccavenue.indiasdk.AvenueOrder;
import com.ccavenue.indiasdk.AvenuesApplication;
import com.ccavenue.indiasdk.AvenuesTransactionCallback;
import com.glitss.teabreak_app.Adapter.DeliverydetailsAdapter;
import com.glitss.teabreak_app.ModelClass.ListItemsModel;
import com.glitss.teabreak_app.ModelClass.Order_delivery_type;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.Constant;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityCheckoutBinding;
import com.glitss.teabreak_app.repository.ApiClient;
import com.glitss.teabreak_app.repository.ApiInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Checkout extends AppCompatActivity  implements AvenuesTransactionCallback {
    private ActivityCheckoutBinding binding;
    LinearLayout paymentdetails,deliverydetails,submit;
    ImageView close_btn;
    AppCompatButton submit_btn;
    AlertDialog alertDialog;
    String Selected_deliverymode="",selected_delivery_mode_id="";
    ProgressDialog progressDialog;
    ArrayAdapter order_type_adapter;
    ArrayList<Order_delivery_type> order_list=new ArrayList<>();
    ArrayList<Order_delivery_type> transport_list=new ArrayList<>();
    ArrayList<String> transport_names=new ArrayList<>();
    ArrayList<ListItemsModel> cart_list=new ArrayList<>();
    DeliverydetailsAdapter deliverydetailsAdapter;

    ArrayList<String> order_type=new ArrayList<>();
    private TeaBreakViewModel viewModel;
    String t_amount="",Delivery_charges="";

    String order_no;
    String order_no2="";
    Float t_amt;
    static String availability_date="";
    ArrayList<String> as_dates;
    TextView delivery_date;
    static String wallet_amount="";

    static String wallet_status="0";


    boolean a=false;
    Context mContext;
    ProgressDialog progress;
    String Order_id;
    String  ba1,ba2;
    String request_hash="",req_id="";

    String gateway_id="",gateway_name="",payment_mode="",workingKey="",merchantId="",accessCode="",status="",last_update_date_time="",requestId="";
    String responseCode,responseStatus,responseMessage,secureToken,creationTimestamp,secureTokenExpiry,responseHash;
    private static DecimalFormat df = null;
    AppCompatButton cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_checkout);
        binding=ActivityCheckoutBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        df = new DecimalFormat("0.00");

        viewModel = ViewModelProviders.of(Checkout.this).get(TeaBreakViewModel.class);
        progressDialog=new ProgressDialog(Checkout.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        wallet_amount_api_call();

        Log.e("wallet_amt",wallet_amount);
        ordered_Items_list_api_call();
        delivery_mode_api_call();

        t_amount=getIntent().getStringExtra("t_amount");
        Delivery_charges=getIntent().getStringExtra("delivery_charges");
        Log.e("Delivery_charges",Delivery_charges);




        transport_type_api_call();

        binding.total.setText( "₹"+t_amount);
        binding.charges.setText( "₹"+Delivery_charges);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.orderedListItems.setLayoutManager(linearLayoutManager);

        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Checkout.this, Cartlist_Activity.class));
                finish();

            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        binding.Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(binding.Proceed.isEnabled()){
                    binding.Proceed.setEnabled(false);
                }
                if(selected_delivery_mode_id.equalsIgnoreCase("")){
                    //  Toast.makeText(Checkout.this, "Please Select the delivery mode", Toast.LENGTH_SHORT).show();
                    binding.Proceed.setEnabled(true);
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Please select the delivery mode",Snackbar.LENGTH_LONG).show();
                    return;
                }

                if(!selected_delivery_mode_id.equalsIgnoreCase("")){

                    binding.Proceed.setEnabled(true);

                    if(selected_delivery_mode_id.equalsIgnoreCase("2")){
                        for(int i=0;i<cart_list.size();i++){
                            if(cart_list.get(i).getLine_item_name().contains("Toast") || cart_list.get(i).getLine_item_name().contains("Cream Roll") || cart_list.get(i).getLine_item_name().contains("Cups")){
                                //  Toast.makeText(Checkout.this, "Please Remove the Items Toast,Cream Roll,Cups for courier delivery", Toast.LENGTH_SHORT).show();

                                AlertDialog.Builder dialog=new AlertDialog.Builder(Checkout.this);
                                dialog.setCancelable(false);
                                dialog.setMessage("Toast,Cream,Cups are not delivered by courier");

                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.create();
                                dialog.show();

                                return;

                            }/*else{
                                 pay_method();
                             }*/
                        }
                    }

                    pay_method();



                }
                else{
                    binding.Proceed.setEnabled(true);
                    //   Toast.makeText(Checkout.this, "Please Select the delivery mode", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Please select the delivery mode",Snackbar.LENGTH_LONG).show();
                }


            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void pay_method() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(Checkout.this);
        View view_alert= LayoutInflater.from(Checkout.this).inflate(R.layout.paymentdetails,null);
        paymentdetails=view_alert.findViewById(R.id.paymentdetails);
        TextView amount=view_alert.findViewById(R.id.amount);
        TextView total_amt=view_alert.findViewById(R.id.Total);
        TextView wallet_amt_txt=view_alert.findViewById(R.id.wallet_amt_txt);

        CheckBox checkBox=view_alert.findViewById(R.id.myCheckbox);
        TextView delivery_charges=view_alert.findViewById(R.id.tv_delivery_charges);

        amount.setText( "₹"+t_amount);

        if(Selected_deliverymode.equalsIgnoreCase("Courier")){
            delivery_charges.setText( "₹"+Delivery_charges);
            //cart_list.contains("Toast","Cream Roll","Cups",);

        }else{
            delivery_charges.setText( "0");
        }

        close_btn=view_alert.findViewById(R.id.close_btn);
        cancel_btn=view_alert.findViewById(R.id.cancel);
        submit_btn=view_alert.findViewById(R.id.submit_btn);


        wallet_amt_txt.setText("Wallet Amount is("+"₹"+""+wallet_amount+")");

        if(selected_delivery_mode_id.equalsIgnoreCase("2")){
            t_amt=Float.sum(Float.parseFloat(t_amount),Float.parseFloat(Delivery_charges));
        }else{
            t_amt=Float.sum(Float.parseFloat(t_amount),Float.parseFloat("0"));
        }

        Log.e("df_t_amt_format",df.format(t_amt));
        total_amt.setText(String.valueOf( "₹"+df.format(t_amt)));



        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("cancel","cancel_btn_click");
                alertDialog.dismiss();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()){
                    wallet_status="1";
                    if(selected_delivery_mode_id.equalsIgnoreCase("2")){
                        t_amt=Float.sum(Float.parseFloat(t_amount),Float.parseFloat(Delivery_charges))-Float.parseFloat(wallet_amount);
                        Log.e("t_amttt", String.valueOf(t_amt));
                        total_amt.setText(String.valueOf( "₹"+df.format(t_amt)));

                    }else{
                        t_amt=Float.sum(Float.parseFloat(t_amount),Float.parseFloat("0.00"))-Float.parseFloat(wallet_amount);

                        Log.e("t_amttt", String.valueOf(t_amt));
                        total_amt.setText(String.valueOf( "₹"+df.format(t_amt)));
                    }
                    //   t_amt=Float.sum(Float.parseFloat(t_amount),Float.parseFloat(Delivery_charges))-Float.parseFloat(wallet_amount);
                    // Float t_amt_with_wallet=t_amt-Float.parseFloat(wallet_amount);
                    // total_amt.setText(String.valueOf( "₹"+df.format(t_amt)));
                }else{
                    wallet_status="0";
                    if(selected_delivery_mode_id.equalsIgnoreCase("2")){
                        t_amt=Float.sum(Float.parseFloat(t_amount),Float.parseFloat(Delivery_charges));
                        Log.e("t_amt2", String.valueOf(t_amt));
                        total_amt.setText(String.valueOf( "₹"+df.format(t_amt)));
                    }else{
                        // t_amt=Float.sum(Float.parseFloat(t_amount),Float.parseFloat("0"));
                        t_amt=Float.sum(Float.parseFloat(t_amount),Float.parseFloat("0"));
                        total_amt.setText(String.valueOf( "₹"+df.format(t_amt)));
                        Log.e("t_amt2", String.valueOf(t_amt));

                    }
                    //    t_amt=Float.sum(Float.parseFloat(t_amount),Float.parseFloat(Delivery_charges));
                    //  total_amt.setText(String.valueOf( "₹"+df.format(t_amt)));
                }
            }
        });

      /*  Log.e("t_amt3", String.valueOf(t_amt));

        total_amt.setText(String.valueOf( "₹"+df.format(t_amt)));*/

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("close","close");
                alertDialog.dismiss();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if(Integer.valueOf(wallet_amount)>Integer.valueOf("0.00")){
                    if(!checkBox.isChecked()){
                        Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Please select the wallet amount",Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }*/

                if(selected_delivery_mode_id.equalsIgnoreCase("")){
                    //    Toast.makeText(Checkout.this, "Please Select the delivery mode", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Please Select the delivery mode",Snackbar.LENGTH_LONG).show();

                    return;
                }else{
                    create_order_api_call();
                }
            }
        });

        dialog.setView(view_alert);
        dialog.setCancelable(true);
        alertDialog = dialog.create();
        alertDialog.show();
    }

    private void transport_type_api_call() {

        viewModel.get_transport_type().observe(Checkout.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    Log.d("wallet_api_method","wallet_api "+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("response");
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");

                        Order_delivery_type order_delivery_type2=new Order_delivery_type();
                        order_delivery_type2.setTransport_name("");
                        order_delivery_type2.setTransport_id("");
                        order_delivery_type2.setTransport_address("");
                        transport_list.add(order_delivery_type2);
                        transport_names.clear();
                        transport_list.clear();

                        if(message.equalsIgnoreCase("success")){
                            for(int i=0;i<jsonArray.length();i++){
                                Order_delivery_type order_delivery_type = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<Order_delivery_type>() {
                                }.getType());

                                transport_list.add(order_delivery_type);
                                transport_names.add(order_delivery_type.getTransport_name());
                            }

                        }



                    } catch (JSONException e) {
                        //throw new RuntimeException(e);

                        Log.e("Exception", String.valueOf(e));

                        //  Toast.makeText(Checkout.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    //   Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });
    }


    public void wallet_amount_api_call() {

        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_wallet_amt(object).observe(Checkout.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    Log.d("wallet_api_method","wallet_api "+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        //String message=jsonObject1.getString("message");
                        wallet_amount=jsonObject1.getJSONObject("data").getString("wallet_amount");
                        Log.e("wallet_amt_money",wallet_amount);



                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                        // Toast.makeText(Checkout.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    //   Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();


                }

            }
        });
    }


    private void ordered_Items_list_api_call() {

        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        viewModel.get_cart_list(object).observe(Checkout.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (jsonObject != null){

                    Log.d("TAG","add_cart "+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");
                        JSONArray jsonArray1=new JSONArray();
                        jsonArray1=jsonObject1.getJSONArray("sub_totals");

                        String message=jsonObject1.getString("message");

                        //   Toast.makeText(Checkout.this, ""+message, Toast.LENGTH_SHORT).show();
                        for(int i=0;i<jsonArray.length();i++){
                            ListItemsModel listItemsModel = new Gson().fromJson(jsonArray.get(i).toString(), new TypeToken<ListItemsModel>() {
                            }.getType());
                            cart_list.add(listItemsModel);
                        }
                        deliverydetailsAdapter =new DeliverydetailsAdapter(cart_list, Checkout.this);
                        binding.orderedListItems.setAdapter(deliverydetailsAdapter);
                        deliverydetailsAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        Log.e("Exception", String.valueOf(e));
                        //   Toast.makeText(Checkout.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    //     Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });
    }

    private void create_order_api_call() {
        Log.e("create_order","create_order");
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_token", SaveAppData.getLoginData().getToken());
        jsonObject.addProperty("user_id",SaveAppData.getLoginData().getUser_id() );
        jsonObject.addProperty("delivery_type_id",selected_delivery_mode_id );
        jsonObject.addProperty("discount","0");
        jsonObject.addProperty("sub_total_amount",t_amount);
        jsonObject.addProperty("total_delivery_charges",Delivery_charges);

        if(Selected_deliverymode.equalsIgnoreCase("Courier")){
            jsonObject.addProperty("total_delivery_charges",Delivery_charges);

        }
        else {
            jsonObject.addProperty("total_delivery_charges","0");

        }

        jsonObject.addProperty("total_amount",Float.parseFloat(t_amount)+Float.parseFloat(Delivery_charges));
        jsonObject.addProperty("wallet_used_status",wallet_status);
        if(wallet_status.equalsIgnoreCase("1")){
            jsonObject.addProperty("used_wallet_amount",wallet_amount);
        }else{
            jsonObject.addProperty("used_wallet_amount","0");
        }
        jsonObject.addProperty("online_paid_amount",df.format(t_amt));

        viewModel.insert_order_api(jsonObject).observe(Checkout.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("TAG","complaint_names "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");

                        JSONObject data_object=new JSONObject();
                        data_object=jsonObject1.getJSONObject("data");
                        order_no2=data_object.getString("order_no");


                        //     Toast.makeText(Checkout.this, "data"+message, Toast.LENGTH_SHORT).show();
                        Log.e("message",message);

                        if(message.equalsIgnoreCase("success")){

                            Log.e("success","success");
                            alertDialog.dismiss();

                            payment_gateway_details_api_call();

                        }



                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                        //  Toast.makeText(Checkout.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    //     Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });
    }



    private void payment_gateway_details_api_call() {

        JsonObject object = new JsonObject();
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_payment_details(object).observe(Checkout.this, new Observer<JsonObject>() {
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
                    /*     request_hash=requestId+"320ECB91D6183CD5D65D9D91E2D2CF2B"+merchantId;
                         request_hash=requestId+"C84AF8924DB0262E1362AAD0BEBE59ED"+merchantId; //live
                         request_hash=requestId+"5D75051B7F577D861C9ECAD9B619804D"+merchantId; //live

                         request_hash=req_id+workingKey+merchantId; //live*/

                   /*     Log.e("request_hash",request_hash);
                        try {
                            ba1 =generateReqHash(request_hash);
                            Log.e("secure_token",ba1);
                        } catch (NoSuchAlgorithmException e) {
                            Log.e("Exception",""+e);
                            throw new RuntimeException(e);
                        }*/




                        //  secure_token_api_call();

                        genearate_request_hash();

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                        //  Toast.makeText(Checkout.this, ""+e, Toast.LENGTH_SHORT).show();
                    }

                }else{

                    // Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });
    }

  /*  private void secure_token_api_call() {
        Log.e("secure_token","secure_token_mthd");
       // JsonObject object = new JsonObject();
       // object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());


      //  viewModel.get_secure_token(object).observe(Checkout.this, new Observer<JsonObject>() {
        viewModel.get_secure_token().observe(Checkout.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    Log.d("secure_token_res","secure_token "+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        Log.e("secure_token_res",jsonObject.toString());

                        if(jsonObject1.getString("text").equalsIgnoreCase("Success")){

                            if(jsonObject1.getString("message")!=null){
                                Log.e("message","msg not null");
                                secureToken=jsonObject1.getJSONObject("message").getString("secureToken");
                                responseHash=jsonObject1.getJSONObject("message").getString("responseHash");
                                responseCode=jsonObject1.getJSONObject("message").getString("responseCode");
                               // order_no=jsonObject1.getJSONObject("order_id").toString();

                                Log.e("response_hash",responseHash);
                                request_hash=order_no2+"INR"+"1.00"+secureToken;
                               //  request_hash=order_no+"INR"+df.format(t_amt)+secureToken;
                                Log.e("s_token",request_hash);
                                try {
                                    ba2=generateReqHash(request_hash);
                                    Log.e("ba2",ba2);

                                } catch (NoSuchAlgorithmException e) {
                                    Log.e("ba2_Exception",ba2);
                                    throw new RuntimeException(e);
                                }

                                initiatePayment1();

                            }else{
                                Toast.makeText(mContext, "Secure token is null...please try again", Toast.LENGTH_SHORT).show();
                            }

                        }


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(Checkout.this, "EXception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }*/

    private void genearate_request_hash() {
        Log.e("generate_hash","generate_hash");
        JsonObject object = new JsonObject();
        object.addProperty("order_no", order_no2);
        object.addProperty("currency", "INR");
        object.addProperty("amount","1.00");
        //   object.addProperty("amount",df.format(t_amt));


        viewModel.get_request_hash(object).observe(Checkout.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    Log.d("generate_request_hash","generate_request_hash "+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());

                        if(jsonObject1.getString("message").equalsIgnoreCase("Success")){

                            if(jsonObject1.getString("message")!=null){

                                ba2=jsonObject1.getString("requestHash");
                                Log.e("request_hash_ba2",ba2);
                                initiatePayment1();

                            }else{
                                //  Toast.makeText(mContext, "RequestHash is null...please try again", Toast.LENGTH_SHORT).show();
                                Snackbar.make(Checkout.this,findViewById(android.R.id.content),"RequestHash is null...please try again",Snackbar.LENGTH_LONG).show();
                            }
                        }


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                        // Toast.makeText(Checkout.this, "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    //   Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });
    }

    private void initiatePayment1() {

    /*    AvenueOrder orderDetails = new AvenueOrder();
        orderDetails.setOrderId(order_no2);
        Log.e("order",order_no2);
        orderDetails.setRequestHash(ba2);
        orderDetails.setAccessCode(accessCode);
        orderDetails.setMerchantId(merchantId);
        orderDetails.setCurrency("INR");
      //  orderDetails.setAmount("1.00");

        orderDetails.setAmount(String.valueOf(df.format(t_amt)));

        orderDetails.setRedirectUrl(Constant.SERVER_BASE_URL+"response_handler_url.php");
        orderDetails.setCancelUrl(Constant.SERVER_BASE_URL+"response_handler_url.php");
        orderDetails.setCustomerId(SaveAppData.getLoginData().getUser_mobile());
        orderDetails.setPaymentType("all");
        orderDetails.setMerchantLogo(String.valueOf(R.drawable.teabreakicon));
        orderDetails.setBillingName(""+SaveAppData.getLoginData().getLogin_username());
        Log.e("Name",SaveAppData.getLoginData().getLogin_username());
        orderDetails.setBillingAddress(""+SaveAppData.getLoginData().getAddress());
        Log.e("address",SaveAppData.getLoginData().getAddress());

        orderDetails.setBillingCountry(""+SaveAppData.getLoginData().getCountry());
        Log.e("country",SaveAppData.getLoginData().getCountry());

        orderDetails.setBillingState(""+SaveAppData.getLoginData().getState());
        Log.e("state",SaveAppData.getLoginData().getState());

        orderDetails.setBillingCity(""+SaveAppData.getLoginData().getCity());
        Log.e("city",SaveAppData.getLoginData().getCity());

        orderDetails.setBillingZip(""+SaveAppData.getLoginData().getPincode());
        Log.e("pincode",SaveAppData.getLoginData().getPincode());

        orderDetails.setBillingTel(""+SaveAppData.getLoginData().getUser_mobile());
        Log.e("mobile",SaveAppData.getLoginData().getUser_mobile());

        orderDetails.setBillingEmail(""+SaveAppData.getLoginData().getUser_email());
        Log.e("email",SaveAppData.getLoginData().getUser_email());

        orderDetails.setDeliveryName(""+SaveAppData.getLoginData().getLogin_username());
        Log.e("name",SaveAppData.getLoginData().getLogin_username());

        orderDetails.setDeliveryAddress(""+SaveAppData.getLoginData().getAddress());
        Log.e("address",SaveAppData.getLoginData().getAddress());

        orderDetails.setDeliveryCountry(""+SaveAppData.getLoginData().getCountry());
        Log.e("country",SaveAppData.getLoginData().getCountry());

        orderDetails.setDeliveryState(""+SaveAppData.getLoginData().getState());
        Log.e("state",SaveAppData.getLoginData().getState());

        orderDetails.setDeliveryCity(""+SaveAppData.getLoginData().getCity());
        Log.e("city",SaveAppData.getLoginData().getCity());

        orderDetails.setDeliveryZip(SaveAppData.getLoginData().getPincode());
        orderDetails.setDeliveryTel(SaveAppData.getLoginData().getUser_mobile());
        orderDetails.setMerchant_param1("test"); //total 5 parameters
        orderDetails.setMerchant_param2("transaction"); //total 5 parameters
        orderDetails.setMerchant_param3("1"); //total 5 parameters
        orderDetails.setMerchant_param4("2"); //total 5 parameters
        orderDetails.setMerchant_param5("3"); //total 5 parameters
        orderDetails.setMobileNo(SaveAppData.getLoginData().getUser_mobile());
        orderDetails.setPaymentEnviroment("“app_staging"); //app_live - prod

//        orderDetails.setColorPrimary("#008000");
//        orderDetails.setColorAccent("#009688");
//        orderDetails.setColorFont("#fd5c63");

        orderDetails.setColorPrimary("color_primary");
        orderDetails.setColorAccent("color_accent");
        orderDetails.setColorFont("color_font");

        orderDetails.setBackgroundDrawable(0);
        // orderDetails.setBackgroundDrawable(R.drawable.img);
        //To begin transaction through SDK…


        AvenuesApplication.startTransaction(Checkout.this, orderDetails);
*/

        AvenueOrder orderDetails = new AvenueOrder();
        orderDetails.setOrderId(order_no2);
        Log.e("order",order_no2);
        orderDetails.setRequestHash(ba2);
        orderDetails.setAccessCode(accessCode);
        orderDetails.setMerchantId(merchantId);
        orderDetails.setCurrency("INR");
        //  orderDetails.setAmount(df.format(t_amt));
        orderDetails.setAmount("1.00");
        orderDetails.setRedirectUrl(Constant.SERVER_BASE_URL+"response_handler_url.php");
        orderDetails.setCancelUrl(Constant.SERVER_BASE_URL+"response_handler_url.php");
        orderDetails.setCustomerId(SaveAppData.getLoginData().getUser_mobile());
        orderDetails.setPaymentType("all");
        orderDetails.setMerchantLogo(String.valueOf(R.drawable.teabreakicon));
        orderDetails.setBillingName(SaveAppData.getLoginData().getName());
        Log.e("nameee",SaveAppData.getLoginData().getName());
        orderDetails.setBillingAddress(SaveAppData.getLoginData().getAddress());
        orderDetails.setBillingCountry(SaveAppData.getLoginData().getCountry());
        orderDetails.setBillingState(SaveAppData.getLoginData().getCountry());
        orderDetails.setBillingCity(SaveAppData.getLoginData().getCity());
        orderDetails.setBillingZip(SaveAppData.getLoginData().getPincode());
        orderDetails.setBillingTel(SaveAppData.getLoginData().getUser_mobile());
        orderDetails.setBillingEmail(SaveAppData.getLoginData().getUser_email());

        orderDetails.setDeliveryName(SaveAppData.getLoginData().getName());

        orderDetails.setDeliveryAddress(SaveAppData.getLoginData().getAddress());
        orderDetails.setDeliveryCountry(SaveAppData.getLoginData().getCountry());
        orderDetails.setDeliveryState(SaveAppData.getLoginData().getState());
        orderDetails.setDeliveryCity(SaveAppData.getLoginData().getCity());
        orderDetails.setDeliveryZip(SaveAppData.getLoginData().getPincode());
        orderDetails.setDeliveryTel(SaveAppData.getLoginData().getUser_mobile());
        orderDetails.setMerchant_param1("test"); //total 5 parameters
        orderDetails.setMerchant_param2("transaction"); //total 5 parameters
        orderDetails.setMerchant_param3("1"); //total 5 parameters
        orderDetails.setMerchant_param4("2"); //total 5 parameters
        orderDetails.setMerchant_param5("3"); //total 5 parameters
        orderDetails.setMobileNo(SaveAppData.getLoginData().getUser_mobile());
        orderDetails.setPaymentEnviroment("“app_staging"); //app_live - prod
//        orderDetails.setColorPrimary("#008000");
//        orderDetails.setColorAccent("#009688");
//        orderDetails.setColorFont("#fd5c63");
        orderDetails.setColorPrimary("color_primary");
        orderDetails.setColorAccent("color_accent");
        orderDetails.setColorFont("color_font");
        orderDetails.setBackgroundDrawable(0);
        //To begin transaction through SDK…
        AvenuesApplication.startTransaction(Checkout.this, orderDetails);

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

    private void delivery_mode_api_call() {
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_token", SaveAppData.getLoginData().getToken());
        jsonObject.addProperty("user_id",SaveAppData.getLoginData().getUser_id() );

        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> delivery_type = apiInterface.order_delivery_type(jsonObject);

        delivery_type.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

          /*      if(response.body()==null){
                    Toast.makeText(Checkout.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if(response.body()!=null){

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body().toString());
                        String message=jsonObj.getString("message");

                        JSONArray jsonArray=jsonObj.getJSONArray("data");

                        // Toast.makeText(Checkout.this, ""+message, Toast.LENGTH_SHORT).show();

                        order_list.clear();
                        order_type.clear();


                        Order_delivery_type order_delivery_type2=new Order_delivery_type();
                        order_delivery_type2.setCat_id("");
                        order_delivery_type2.setSub_cat_id("");

                        order_type.add("Select");
                        order_list.add(order_delivery_type2);

                        if(message.equalsIgnoreCase("success")){
                            for(int i=0;i<jsonArray.length();i++){
                                Order_delivery_type order_delivery_type = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<Order_delivery_type>() {
                                }.getType());

                                order_list.add(order_delivery_type);
                                order_type.add(order_delivery_type.getSub_cat_name());
                            }

                        }
                        order_type_adapter=new ArrayAdapter(Checkout.this,R.layout.spinner_text,order_type);
                        order_type_adapter.setDropDownViewResource(R.layout.spinner_text);
                        binding.deliverymode.setAdapter(order_type_adapter);


                        binding.deliverymode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Selected_deliverymode  = binding.deliverymode.getSelectedItem().toString();
                                selected_delivery_mode_id  =order_list.get(i).getSub_cat_id();
                                if(Selected_deliverymode.equalsIgnoreCase("Vehicle Delivery")){
                                    // if(selected_delivery_mode_id.equalsIgnoreCase("1")){

                                    Log.e("vehicle_delivery","vehicle_delivery");
                                    AlertDialog.Builder dialog=new AlertDialog.Builder(Checkout.this);
                                    View view_alert= LayoutInflater.from(Checkout.this).inflate(R.layout.vehicledeliveryalert,null);
                                    deliverydetails=view_alert.findViewById(R.id.deliverydetails);
                                    close_btn=view_alert.findViewById(R.id.close_btn);
                                    delivery_date=view_alert.findViewById(R.id.deliverydate);
                                    order_delivery_date_api_call();
                                    Log.e("future_date",availability_date);



                                    submit=view_alert.findViewById(R.id.submit_btn);
                                    submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    close_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    dialog.setView(view_alert);
                                    dialog.setCancelable(false);
                                    alertDialog = dialog.create();
                                    alertDialog.show();

                                }
                                if(Selected_deliverymode.equalsIgnoreCase("Courier")){
                                    binding.type.setVisibility(View.VISIBLE);
                                    binding.deliverycharges.setVisibility(View.VISIBLE);
                                    binding.charges.setText(Delivery_charges);
                                  /*  if(cart_list.get(i).getLine_item_name().contains("Toast") || cart_list.get(i).getLine_item_name().contains("Cream Roll") || cart_list.get(i).getLine_item_name().contains("Cups")){
                                        Toast.makeText(Checkout.this, "Please Remove the Items(Toast) for courier", Toast.LENGTH_SHORT).show();
                                    }*/
                                }
                                else {
                                    binding.type.setVisibility(View.GONE);
                                    binding.deliverycharges.setVisibility(View.GONE);
                                    binding.charges.setText("0");

                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });



                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Snackbar.make(Checkout.this,findViewById(android.R.id.content),""+e,Snackbar.LENGTH_LONG).show();
                    }

                }else{
                    //   Toast.makeText(Checkout.this, "null", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //   Toast.makeText(Checkout.this, ""+t, Toast.LENGTH_SHORT).show();
                Snackbar.make(Checkout.this,findViewById(android.R.id.content),""+t,Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void order_delivery_date_api_call() {

        progressDialog.show();

        Log.e("transport_date","transport_api_call");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_token", SaveAppData.getLoginData().getToken());
        jsonObject.addProperty("user_id",SaveAppData.getLoginData().getUser_id() );
        jsonObject.addProperty("delivery_type",selected_delivery_mode_id );


        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> order_delivery_date = apiInterface.order_delivery_dates(jsonObject);

        order_delivery_date.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if(response.body()!=null){
                    try {
                        JSONObject jsonObject1=new JSONObject(response.body().toString());
                        String message=jsonObject1.getString("message");

                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("availability_date");


                        // availability_date=jsonObject1.getJSONArray("availability_date").toString();
                        Log.e("dates_array",""+jsonArray.length());
                        as_dates=new ArrayList<>();


                   /*   for(int i=0;i<=jsonArray.length();i++){
                          as_dates.add((String) jsonArray.get(i));
                          availability_date=availability_date+jsonArray.get(i);
                          Log.e("availability_date",availability_date);
                      }*/

                        availability_date= jsonArray.get(0).toString();

                        delivery_date.setText(availability_date);


                        Log.e("availability_dateeeee",availability_date);
                        Log.e("dates",""+as_dates.size());


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                        // Toast.makeText(Checkout.this, "Exception"+e, Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //  Toast.makeText(Checkout.this, ""+t, Toast.LENGTH_SHORT).show();
                Snackbar.make(Checkout.this,findViewById(android.R.id.content),""+t,Snackbar.LENGTH_LONG).show();

            }
        });
    }


    private JSONObject convertBundleToJsonObject(Bundle bundle) {
        JSONObject jsonObject = new JSONObject();

        if (bundle != null) {
            for (String key : bundle.keySet()) {
                try {
                    // Add key-value pairs to the JSONObject
                    jsonObject.put(key, bundle.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.e("jsonObject", String.valueOf(jsonObject));
        return jsonObject;
    }
    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("transaction_res","onTransactionResponse"+bundle.toString());
        Log.e("transaction_res","onTransactionResponse22"+bundle);
        //  Log.e("order_status",bundle.getString("order_status"));

        JSONObject jsonObject = convertBundleToJsonObject(bundle);

        insert_payment_api_call(jsonObject);

     /*   Intent intent=new Intent(Checkout.this,StatusActivity.class);
        intent.putExtra("orderno",bundle.getString("orderno"));
        intent.putExtra("tracking_id",bundle.getString("tracking_id"));
        intent.putExtra("trans_date",bundle.getString("trans_date"));
        intent.putExtra("billing_name",bundle.getString("billing_name"));
        intent.putExtra("amount",bundle.getString("amount"));
        intent.putExtra("bank_ref_no",bundle.getString("bank_ref_no"));
        intent.putExtra("order_status",bundle.getString("order_status"));
        startActivity(intent);*/

    }

    private void
    insert_payment_api_call(JSONObject bundle) {
        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());

        try {
            object.addProperty("trans_date", bundle.getString("trans_date"));
            object.addProperty("delivery_state", bundle.getString("delivery_state"));
            object.addProperty("delivery_country", bundle.getString("delivery_country"));
            object.addProperty("billing_city", bundle.getString("billing_city"));
            object.addProperty("amount", bundle.getString("amount"));
            object.addProperty("billing_name", bundle.getString("billing_name"));
            object.addProperty("bin_country", bundle.getString("bin_country"));
            object.addProperty("billing_email", bundle.getString("billing_email"));
            object.addProperty("billing_notes", bundle.getString("billing_notes"));
            object.addProperty("billing_state", bundle.getString("billing_state"));
            object.addProperty("status_code", bundle.getString("status_code"));
            object.addProperty("billing_address", bundle.getString("billing_address"));
            object.addProperty("payment_mode", bundle.getString("payment_mode"));
            object.addProperty("delivery_city", bundle.getString("delivery_city"));
            object.addProperty("delivery_name", bundle.getString("delivery_name"));
            object.addProperty("card_name", bundle.getString("card_name"));
            object.addProperty("status_message", bundle.getString("status_message"));
            object.addProperty("billing_tel", bundle.getString("billing_tel"));
            object.addProperty("billing_zip", bundle.getString("billing_zip"));
            object.addProperty("mer_amount", bundle.getString("mer_amount"));
            object.addProperty("bank_ref_no", bundle.getString("bank_ref_no"));
            object.addProperty("delivery_address", bundle.getString("delivery_address"));
            object.addProperty("currency", bundle.getString("currency"));
            object.addProperty("delivery_tel", bundle.getString("delivery_tel"));
            object.addProperty("delivery_zip", bundle.getString("delivery_zip"));
            object.addProperty("order_status", bundle.getString("order_status"));
            object.addProperty("order_id", bundle.getString("order_id"));
            object.addProperty("billing_country", bundle.getString("billing_country"));
            object.addProperty("tracking_id", bundle.getString("tracking_id"));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


      /*  String s=String.valueOf(bundle).replace("\"","");
        object.addProperty("msg", s.replace(":","="));
*/

        viewModel.insert_payments(object).observe(Checkout.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("Checkout","logout"+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        Log.e("insrt_message",jsonObject1.getString("message"));
                        if(jsonObject1.getString("order_status").equalsIgnoreCase("Success")){
                            Log.e("insert_api","insert_api");
                            Intent intent=new Intent(Checkout.this,StatusActivity.class);
                            intent.putExtra("orderno",""+jsonObject1.getJSONObject("insrtMsg").getString("order_no"));
                            intent.putExtra("tracking_id",""+jsonObject1.getJSONObject("insrtMsg").getString("transaction_id"));
                            intent.putExtra("trans_date",""+jsonObject1.getJSONObject("insrtMsg").getString("payment_date_time"));
                            intent.putExtra("billing_name",""+jsonObject1.getString("vendor_name"));
                            intent.putExtra("amount",""+jsonObject1.getJSONObject("insrtMsg").getString("paid_amount"));
                            intent.putExtra("bank_ref_no",""+jsonObject1.getString("bank_ref_no"));
                            intent.putExtra("order_status",""+jsonObject1.getString("order_status"));

                            Log.e("orderno",jsonObject1.getJSONObject("insrtMsg").getString("order_no"));
                            Log.e("tracking_id",jsonObject1.getJSONObject("insrtMsg").getString("transaction_id"));
                            Log.e("trans_date",jsonObject1.getJSONObject("insrtMsg").getString("payment_date_time"));
                            Log.e("billing_name",jsonObject1.getString("vendor_name"));
                            Log.e("amount",jsonObject1.getJSONObject("insrtMsg").getString("paid_amount"));
                            Log.e("bank_ref_no",jsonObject1.getString("bank_ref_no"));
                            Log.e("order_status",jsonObject1.getString("order_status"));

                            startActivity(intent);
                            finish();

                           /* intent.putExtra("tracking_id",bundle.getString("tracking_id"));
                            intent.putExtra("trans_date",bundle.getString("trans_date"));
                            intent.putExtra("billing_name",bundle.getString("billing_name"));
                            intent.putExtra("amount",bundle.getString("amount"));
                            intent.putExtra("bank_ref_no",bundle.getString("bank_ref_no"));
                            intent.putExtra("order_status",bundle.getString("order_status"));*/

                        }

                    } catch (JSONException e) {
                        Log.e("Exception","Exception"+e.toString());
                        //   throw new RuntimeException(e);
                    }

                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    //     Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });
    }

/*    private void redirect_api_call() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("encResp", "");




        ApiInterface apiInterface = ApiClient.getClient( Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> redirect_res = apiInterface.redirect(jsonObject);

        redirect_res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if(response.body()==null){
                    Toast.makeText(Checkout.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.body()!=null){

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body().toString());
                        String response1=jsonObj.getString("response");
                        Toast.makeText(mContext, ""+response1, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Snackbar.make(Checkout.this,findViewById(android.R.id.content),""+e,Snackbar.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(Checkout.this, "null", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(Checkout.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    @Override
    public void onErrorOccurred(String s) {
        Log.e("transaction_res","onErrorOccurred "+s);
        insert_payment_api_call2(s);
    }



    @Override
    public void onCancelTransaction(String s) {
        Log.e("transaction_res","onCancelTransaction"+s);
        insert_payment_api_call2(s);
    }

    private void insert_payment_api_call2(String s) {
        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();


        try {
            object.addProperty("trans_date",""+dtf.format(now) );
            object.addProperty("delivery_state",SaveAppData.getLoginData().getState());
            object.addProperty("delivery_country",SaveAppData.getLoginData().getCountry());
            object.addProperty("billing_city",SaveAppData.getLoginData().getCity());
            object.addProperty("amount",t_amt);
            object.addProperty("billing_name",SaveAppData.getLoginData().getName());
            object.addProperty("bin_country",SaveAppData.getLoginData().getCountry() );
            object.addProperty("billing_email","");
            object.addProperty("billing_notes","");
            object.addProperty("billing_state", "");
            object.addProperty("status_code","");
            object.addProperty("billing_address",SaveAppData.getLoginData().getAddress());
            object.addProperty("payment_mode","" );
            object.addProperty("delivery_city",SaveAppData.getLoginData().getCity());
            object.addProperty("delivery_name", SaveAppData.getLoginData().getName());
            object.addProperty("card_name","" );
            object.addProperty("status_message",s);
            object.addProperty("billing_tel",SaveAppData.getLoginData().getUser_mobile() );
            object.addProperty("billing_zip", SaveAppData.getLoginData().getPincode());
            object.addProperty("mer_amount","" );
            object.addProperty("bank_ref_no","" );
            object.addProperty("delivery_address",SaveAppData.getLoginData().getAddress());
            object.addProperty("currency","INR" );
            object.addProperty("delivery_tel",SaveAppData.getLoginData().getUser_mobile());
            object.addProperty("delivery_zip",SaveAppData.getLoginData().getPincode());
            object.addProperty("order_status","");
            object.addProperty("order_id",order_no2 );
            object.addProperty("billing_country",SaveAppData.getLoginData().getCountry());
            object.addProperty("tracking_id","" );

        }
        catch (Exception e) {
            Log.e("Exception", String.valueOf(e));
            //  throw new RuntimeException(e);

        }


      /*  String s=String.valueOf(bundle).replace("\"","");
        object.addProperty("msg", s.replace(":","="));
*/

        viewModel.insert_payments(object).observe(Checkout.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("Checkout","logout"+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        Log.e("insrt_message",jsonObject1.getString("message"));
                        Log.e("order_status",jsonObject1.getString("order_status"));

                        if(jsonObject1.getString("order_status").equalsIgnoreCase("Success")){
                            Log.e("insert_api","insert_api");

                           /* intent.putExtra("tracking_id",bundle.getString("tracking_id"));
                            intent.putExtra("trans_date",bundle.getString("trans_date"));
                            intent.putExtra("billing_name",bundle.getString("billing_name"));
                            intent.putExtra("amount",bundle.getString("amount"));
                            intent.putExtra("bank_ref_no",bundle.getString("bank_ref_no"));
                            intent.putExtra("order_status",bundle.getString("order_status"));*/

                        }

                    } catch (JSONException e) {
                        Log.e("Exception","Exception"+e.toString());
                        //   throw new RuntimeException(e);
                    }

                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    //     Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Checkout.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });
    }

}





