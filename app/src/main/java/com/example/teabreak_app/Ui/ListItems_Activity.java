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
import android.widget.Toast;

import com.example.teabreak_app.Adapter.ItemslistAdapter;

import com.example.teabreak_app.ModelClass.ListItemsModel;

import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityListItemsBinding;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListItems_Activity extends AppCompatActivity {
    private ActivityListItemsBinding binding;
    ProgressDialog progressDialog;
    private TeaBreakViewModel viewModel;
    ItemslistAdapter ItemslistAdapter;

    ArrayList<ListItemsModel> list=new ArrayList<>();
    String selected_line_item_id="",selected_price="",selected_qty="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityListItemsBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        viewModel = ViewModelProviders.of(ListItems_Activity.this).get(TeaBreakViewModel.class);
        list_items_api_call();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvListItems.setLayoutManager(linearLayoutManager);

        Constant.check_token_status_api_call(ListItems_Activity.this);


        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListItems_Activity.this, DashboardActivity.class));

            }
        });
        binding.Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListItems_Activity.this,Cartlist_Activity.class));

            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListItems_Activity.this,DashboardActivity.class));
            }
        });

    }

    private void list_items_api_call() {

        viewModel.get_list_items().observe(ListItems_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        String text=jsonObject1.getString("text");

                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");


                        Toast.makeText(ListItems_Activity.this, ""+text, Toast.LENGTH_SHORT).show();

                        if(message.equalsIgnoreCase("success")){
                            for(int i=0;i<jsonArray.length();i++){
                                ListItemsModel listItemsModel = new Gson().fromJson(jsonArray.get(i).toString(), new TypeToken<ListItemsModel>() {
                                }.getType());
                                list.add(listItemsModel);
                            }
                        }

                        ItemslistAdapter= new ItemslistAdapter(ListItems_Activity.this, list,"list_items", new ListItemInterface() {
                            @Override
                            public void OnItemClick(int position, View v, String s) {

                                selected_line_item_id=list.get(position).getLine_item_id();
                                selected_price=list.get(position).getPrice();
                                selected_qty=list.get(position).getPack_of_qty();

                                if(s.equalsIgnoreCase("cart")){
                                    //startActivity(new Intent(DashboardActivity.this,Cartlist_Activity.class));
                                    add_cart_api_call();
                                }


                            }
                        });
                        binding.rvListItems.setAdapter(ItemslistAdapter);
                        ItemslistAdapter.notifyDataSetChanged();




                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(ListItems_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(ListItems_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void add_cart_api_call() {
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("line_item_id",selected_line_item_id);
        object.addProperty("quantity",selected_qty);
        object.addProperty("price",selected_price);

        viewModel.add_cart_api(object).observe(ListItems_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    Log.d("TAG","add_cart "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        String text=jsonObject1.getString("text");

                        Toast.makeText(ListItems_Activity.this, ""+text, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(ListItems_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(ListItems_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Constant.check_token_status_api_call(ListItems_Activity.this);
    }
}