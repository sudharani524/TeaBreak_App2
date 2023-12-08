package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.teabreak_app.Adapter.Orderdetailsadapter;
import com.example.teabreak_app.ModelClass.OrderdetailsModel;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityOrderdetailsBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Orderdetails extends AppCompatActivity {
    private ActivityOrderdetailsBinding binding;
    String order_no,Amount,order_date,delivery_mode,order_id;
    ProgressDialog progressDialog;
    private TeaBreakViewModel viewModel;
   Orderdetailsadapter orderdetailsadapter;

    ArrayList<OrderdetailsModel> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderdetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        order_no=getIntent().getStringExtra("order_no");
        Amount=getIntent().getStringExtra("Amount");
        Log.d("amount",Amount);
        order_date=getIntent().getStringExtra("order_date");
        delivery_mode=getIntent().getStringExtra("delivery_mode");
        order_id=getIntent().getStringExtra("order_id");
        binding.orderNum.setText(order_no);
        binding.tvAmount.setText(Amount);
        binding.tvOrderDate.setText(order_date);
        binding.tvVehicle.setText(delivery_mode);
        binding.tBar.tlbarTitle.setText("Order Details");
        viewModel = ViewModelProviders.of(Orderdetails.this).get(TeaBreakViewModel.class);
        order_details_list_api_call();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvlist.setLayoutManager(linearLayoutManager);
    }

    private void order_details_list_api_call() {
        JsonObject object = new JsonObject();
        Log.d("orderdetailsapicall",object.toString());
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("order_id",order_id);
        viewModel.get_order_history(object).observe(Orderdetails.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject != null){
                    Log.d("ordersDetails","Text"+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");
                        Log.d("dataorderdetails",jsonArray.toString());
                        for(int i=0;i<jsonArray.length();i++){
                            Log.d("forloop","loop");
                            OrderdetailsModel orderdetailsModel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<OrderdetailsModel>() {
                            }.getType());
                            list.add(orderdetailsModel);
                            Log.d("orderdetailslist",String.valueOf(list.size()));
                        }
                        Log.d("orderdetailslist2",String.valueOf(list.size()));
                        orderdetailsadapter=new Orderdetailsadapter(list, Orderdetails.this);
                        Log.d("Adapter",String.valueOf(jsonArray.length()));
                        binding.rvlist.setAdapter(orderdetailsadapter);
                        Log.d("recycleview",String.valueOf(jsonArray.length()));
                        orderdetailsadapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Toast.makeText(Orderdetails.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(Orderdetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}