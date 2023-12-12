package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teabreak_app.Adapter.ItemslistAdapter;
import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityCartlistBinding;
import com.example.teabreak_app.repository.CartInterface;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cartlist_Activity extends AppCompatActivity {

    private ActivityCartlistBinding binding;
    ProgressDialog progressDialog;
    private TeaBreakViewModel viewModel;
    ArrayList<ListItemsModel> cart_list=new ArrayList<>();
    ArrayList<ListItemsModel> Pricelist = new ArrayList<>();
    ItemslistAdapter itemslistAdapter;
    String selected_line_item_id="",selected_price="",selected_qty="";
    String total;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog=new ProgressDialog(Cartlist_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        viewModel = ViewModelProviders.of(Cartlist_Activity.this).get(TeaBreakViewModel.class);



        Constant.check_token_status_api_call(Cartlist_Activity.this);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        binding.rvCartList.setLayoutManager(linearLayoutManager);

        cart_list_api_call();
        binding.Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Cartlist_Activity.this, Checkout.class);
                intent.putExtra("t_amount",total);
                Log.e("t_amount",total);
                startActivity(intent);

            }
        });
        binding.tBar.tlbarTitle.setText("Cart List");

    }

    private void cart_list_api_call() {

        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_cart_list(object).observe(Cartlist_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    Log.d("TAG","add_cart "+jsonObject);

                    cart_list.clear();

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");
                        JSONArray jsonArray1=new JSONArray();
                        jsonArray1=jsonObject1.getJSONArray("sub_totals");

                        String message=jsonObject1.getString("message");

                        Toast.makeText(Cartlist_Activity.this, ""+message, Toast.LENGTH_SHORT).show();
                        for(int i=0;i<jsonArray.length();i++){
                            ListItemsModel listItemsModel = new Gson().fromJson(jsonArray.get(i).toString(), new TypeToken<ListItemsModel>() {
                            }.getType());
                            cart_list.add(listItemsModel);
                        }
                        for(int i=0;i<jsonArray1.length();i++){
                            Log.e("array",jsonArray1.toString());
                            ListItemsModel listItemsModel = new Gson().fromJson(jsonArray1.get(i).toString(), new TypeToken<ListItemsModel>() {
                            }.getType());
                            Pricelist.add(listItemsModel);
                        }
                        for (int i = 0; i < Pricelist.size(); i++) {

                             total = Pricelist.get(i).getSub_total();
                            Log.d("subtotal", total);
                            binding.tAmount.setText(total);

                        }

                        itemslistAdapter=new ItemslistAdapter(cart_list, Cartlist_Activity.this, "cart_items", new CartInterface() {
                            @Override
                            public void OnItemClick(int position, ItemslistAdapter.ViewHolder holder, String s) {
                                selected_line_item_id=cart_list.get(position).getLine_item_id();
                                selected_price=cart_list.get(position).getPrice();


                                errorMessage(holder.itemView.findViewById(R.id.sp_qty),s);
                                TextView textView=holder.itemView.findViewById(R.id.price);
                                textView.setText(""+Float.parseFloat(cart_list.get(position).getPrice())*Float.parseFloat(s));

                                if(s.equalsIgnoreCase("Select")){
                                    Toast.makeText(Cartlist_Activity.this, "Please select the quantity", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                     add_cart_api_call(s,holder.itemView.findViewById(R.id.price),position);
                                }

                                ImageView iv_dlt=holder.itemView.findViewById(R.id.iv_delete);
                                iv_dlt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.e("dlt_click","dlt_click");
                                        dlt_item_api_call();
                                    }
                                });

                            }
                        });

                      /*  itemslistAdapter=new ItemslistAdapter(Cartlist_Activity.this, cart_list,"cart_items", new ListItemInterface() {
                            @Override
                            public void OnItemClick(int position, View v, String s) {

                                selected_line_item_id=cart_list.get(position).getLine_item_id();
                                selected_price=cart_list.get(position).getPrice();

                                errorMessage((Spinner) v,s);
                                if(s.equalsIgnoreCase("Select")){
                                    Toast.makeText(Cartlist_Activity.this, "Please select the quantity", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                   // add_cart_api_call(s,v,position);
                                }


                            }
                        });*/

                        binding.rvCartList.setAdapter(itemslistAdapter);
                        itemslistAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(Cartlist_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(Cartlist_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void dlt_item_api_call() {

        Log.e("dlt_api","dlt_api");
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("line_item_id",selected_line_item_id);
        object.addProperty("status","0");


        viewModel.dlt_item_api(object).observe(Cartlist_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    Log.d("TAG","complaint_names "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        String text=jsonObject1.getString("text");

                        Toast.makeText(Cartlist_Activity.this, ""+message, Toast.LENGTH_SHORT).show();
                        if(message.equalsIgnoreCase("Success")){
                            cart_list_api_call();
                        }

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(Cartlist_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Cartlist_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    public void errorMessage(Spinner spinner, String s){

        TextView error = (TextView) spinner.getSelectedView();
        error.setText(s);
        error.requestFocus();

    }

    private void add_cart_api_call(String s, View v, int position) {
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("line_item_id",selected_line_item_id);
        object.addProperty("quantity",s);
        object.addProperty("price",selected_price);

        viewModel.add_cart_api(object).observe(Cartlist_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    Log.d("TAG","add_cart "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        String text=jsonObject1.getString("text");

                        Toast.makeText(Cartlist_Activity.this, ""+text, Toast.LENGTH_SHORT).show();

                        if(message.equalsIgnoreCase("success")){

                            TextView textView= (TextView) v;
                            Log.e("s_qty",cart_list.get(position).getPrice());
                            Log.e("price",s);
                            textView.setText(""+Float.parseFloat(cart_list.get(position).getPrice())*Float.parseFloat(s));
                            Log.e("t_price",""+Float.parseFloat(cart_list.get(position).getPrice())*Float.parseFloat(s));

                            //cart_list_api_call();
                        }

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(Cartlist_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(Cartlist_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Constant.check_token_status_api_call(Cartlist_Activity.this);
    }
}