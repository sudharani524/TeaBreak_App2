package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.teabreak_app.Adapter.DeliverydetailsAdapter;
import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.ModelClass.LoginUserModel;
import com.example.teabreak_app.ModelClass.Order_delivery_type;
import com.example.teabreak_app.ModelClass.UsersRolesModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityCheckoutBinding;
import com.example.teabreak_app.databinding.ActivityListItemsBinding;
import com.example.teabreak_app.repository.ApiClient;
import com.example.teabreak_app.repository.ApiInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Checkout extends AppCompatActivity {
    private ActivityCheckoutBinding binding;
    LinearLayout paymentdetails,deliverydetails,submit;
    ImageView close_btn;
    AppCompatButton submit_btn;
    AlertDialog alertDialog;
    String Selected_deliverymode,selected_delivery_mode_id;
    ProgressDialog progressDialog;
    ArrayAdapter order_type_adapter;
    ArrayList<Order_delivery_type> order_list=new ArrayList<>();
    ArrayList<ListItemsModel> cart_list=new ArrayList<>();
    DeliverydetailsAdapter deliverydetailsAdapter;

    ArrayList<String> order_type=new ArrayList<>();
    private TeaBreakViewModel viewModel;
    String t_amount;

    String order_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_checkout);
        binding=ActivityCheckoutBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        viewModel = ViewModelProviders.of(Checkout.this).get(TeaBreakViewModel.class);
        progressDialog=new ProgressDialog(Checkout.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        delivery_mode_api_call();
        t_amount=getIntent().getStringExtra("t_amount");
        ordered_Items_list_api_call();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.orderedListItems.setLayoutManager(linearLayoutManager);
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Checkout.this, Cartlist_Activity.class));

            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Checkout.this, Cartlist_Activity.class));
            }
        });
        binding.Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(Checkout.this);
                View view_alert= LayoutInflater.from(Checkout.this).inflate(R.layout.paymentdetails,null);
                paymentdetails=view_alert.findViewById(R.id.paymentdetails);
                close_btn=view_alert.findViewById(R.id.close_btn);
                submit_btn=view_alert.findViewById(R.id.submit_btn);
                submit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selected_delivery_mode_id.equalsIgnoreCase("")){
                            Toast.makeText(Checkout.this, "Please Select the delivery mode", Toast.LENGTH_SHORT).show();
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
        });
    }

    private void ordered_Items_list_api_call() {
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        viewModel.get_cart_list(object).observe(Checkout.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    Log.d("TAG","add_cart "+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");
                        JSONArray jsonArray1=new JSONArray();
                        jsonArray1=jsonObject1.getJSONArray("sub_totals");

                        String message=jsonObject1.getString("message");

                        Toast.makeText(Checkout.this, ""+message, Toast.LENGTH_SHORT).show();
                        for(int i=0;i<jsonArray.length();i++){
                            ListItemsModel listItemsModel = new Gson().fromJson(jsonArray.get(i).toString(), new TypeToken<ListItemsModel>() {
                            }.getType());
                            cart_list.add(listItemsModel);
                        }
                        deliverydetailsAdapter =new DeliverydetailsAdapter(cart_list, Checkout.this);
                        binding.orderedListItems.setAdapter(deliverydetailsAdapter);
                        deliverydetailsAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        Toast.makeText(Checkout.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        jsonObject.addProperty("discount","0" );
        jsonObject.addProperty("paid_amount",t_amount);

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
                         order_no=jsonObject1.getString("order_no");

                        JSONObject data_object=new JSONObject();
                        data_object=jsonObject1.getJSONObject("data");


                        Toast.makeText(Checkout.this, ""+message, Toast.LENGTH_SHORT).show();
                        if(message.equalsIgnoreCase("success")){
                            startActivity(new Intent(Checkout.this,Cartlist_Activity.class));
                            finish();
                        }



                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(Checkout.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(Checkout.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
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

                        Toast.makeText(Checkout.this, ""+message, Toast.LENGTH_SHORT).show();

                        order_list.clear();
                        order_type.clear();
                        Order_delivery_type order_delivery_type2=new Order_delivery_type();
                        order_delivery_type2.setCat_id("");

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
                            @SuppressLint("MissingInflatedId")
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Selected_deliverymode  = binding.deliverymode.getSelectedItem().toString();
                                selected_delivery_mode_id  =order_list.get(i).getCat_id();
                                if(Selected_deliverymode.equalsIgnoreCase("Vehicle Delivery")){
                                    AlertDialog.Builder dialog=new AlertDialog.Builder(Checkout.this);
                                    View view_alert= LayoutInflater.from(Checkout.this).inflate(R.layout.vehicledeliveryalert,null);
                                    deliverydetails=view_alert.findViewById(R.id.deliverydetails);
                                    close_btn=view_alert.findViewById(R.id.close_btn);
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
                                    dialog.setCancelable(true);
                                    alertDialog = dialog.create();
                                    alertDialog.show();

                                }
                                if(Selected_deliverymode.equalsIgnoreCase("Courier")){
                                    binding.type.setVisibility(View.VISIBLE);
                                }
                                else {
                                    binding.type.setVisibility(View.GONE);
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
    }

}