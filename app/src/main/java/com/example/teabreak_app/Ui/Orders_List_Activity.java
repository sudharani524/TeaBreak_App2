package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teabreak_app.Adapter.ItemslistAdapter;
import com.example.teabreak_app.Adapter.OrderHistoryAdapter;
import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.ModelClass.OrderHistoryModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityListItemsBinding;
import com.example.teabreak_app.databinding.ActivityOrdersListBinding;
import com.example.teabreak_app.repository.CartInterface;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Orders_List_Activity extends AppCompatActivity {
    private ActivityOrdersListBinding binding;
    ProgressDialog progressDialog;
    private TeaBreakViewModel viewModel;
    OrderHistoryAdapter orderHistoryAdapter;

    ArrayList<OrderHistoryModel> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityOrdersListBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        viewModel = ViewModelProviders.of(Orders_List_Activity.this).get(TeaBreakViewModel.class);
        order_list_api_call();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvListItems.setLayoutManager(linearLayoutManager);
    }

    private void order_list_api_call() {
        JsonObject object = new JsonObject();
        Log.d("orderlistapicall",object.toString());
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        viewModel.get_order_items(object).observe(Orders_List_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject != null){
                    Log.d("orders","Text"+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");
                        Log.d("dataorder",jsonArray.toString());
                        for(int i=0;i<jsonArray.length();i++){
                            Log.d("forloop","loop");
                            OrderHistoryModel orderHistoryModel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<OrderHistoryModel>() {
                            }.getType());
                            list.add(orderHistoryModel);
                            Log.d("list",String.valueOf(list.size()));
                        }
                        Log.d("list2",String.valueOf(list.size()));


                        orderHistoryAdapter=new OrderHistoryAdapter(list, Orders_List_Activity.this);
                        binding.rvListItems.setAdapter(orderHistoryAdapter);
                        orderHistoryAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Toast.makeText(Orders_List_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(Orders_List_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}