package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teabreak_app.Adapter.ItemsOrderAdapter;
import com.example.teabreak_app.ModelClass.ItemsorderedModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityOrderitemscheckBinding;
import com.example.teabreak_app.repository.OrderListitems;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Orderitemscheck extends AppCompatActivity {

    private ActivityOrderitemscheckBinding binding;
    ProgressDialog progressDialog;
    private TeaBreakViewModel viewModel;
    String order_no,order_id;
    ItemsOrderAdapter itemsOrderAdapter;
    ArrayList<ItemsorderedModel> list=new ArrayList<>();
    LinearLayout paymentdetails;
    ImageView close_btn,clear_btn;
    AlertDialog alertDialog;
    LinearLayout submit_btn,cancel;
    EditText quanity;
    String ordecount;
    TextView tv_qty;
    String total_amt="",delivery_charges="",available_qty="";
    Float t_amt;
    String selected_line_item_id="",selected_price="",selected_qty="",selected_order_item_id="";
    String used_amount="",amount_refunded="",balanced_amount="",order_date_time="",date_created="",wallet_amount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderitemscheckBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        order_no=getIntent().getStringExtra("order_no");
        order_id=getIntent().getStringExtra("orderid");
        viewModel = ViewModelProviders.of(Orderitemscheck.this).get(TeaBreakViewModel.class);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.orderedListItems.setLayoutManager(linearLayoutManager);

        order_items_list_api_call();

        binding.total.setText(total_amt);
        binding.deliveryCharges.setText(delivery_charges);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_dispatched_order_api_call();
            }
        });



        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orderitemscheck.this, VendorOrderlist.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void update_dispatched_order_api_call() {
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("order_id",order_id);
        object.addProperty("dispatched_amount",total_amt);
        Log.e("t_amt",total_amt);
        object.addProperty("dispatched_status","1");

        viewModel.update_dispatch_order(object).observe(Orderitemscheck.this, new Observer<JsonObject>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject != null){

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");

                        if(message.equalsIgnoreCase("success")){
                            binding.Proceed.setEnabled(false);
                            binding.Proceed.setClickable(false);
                            Toast.makeText(Orderitemscheck.this, ""+jsonObject1.getString("text"), Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }

                    } catch (JSONException e) {
                        Log.e("Exception", String.valueOf(e));
                      //  Toast.makeText(Orderitemscheck.this, "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

               //     Toast.makeText(Orderitemscheck.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Orderitemscheck.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }
            }
        });
    }

    private void order_items_list_api_call() {
        progressDialog.show();

        JsonObject object = new JsonObject();
        Log.d("orderdetailsapicall",object.toString());
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("order_id",order_id);
        list.clear();

        viewModel.get_Ordered_items_list(object).observe(Orderitemscheck.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if (jsonObject != null){
                    Log.d("ordersDetails","Text"+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("order_list");
                        Log.d("dataorderdetails",jsonArray.toString());
                        for(int i=0;i<jsonArray.length();i++){
                            Log.d("forloop","loop");
                            ItemsorderedModel itemsorderedModel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<ItemsorderedModel>() {
                            }.getType());
                            list.add(itemsorderedModel);
                            Log.d("orderdetailslist",String.valueOf(list.size()));
                        }

                        total_amt=jsonObject1.getString("total_line_items_price");
                        delivery_charges=jsonObject1.getString("total_delivery_charges");

                    //    t_amt=Float.sum(Float.parseFloat(total_amt),Float.parseFloat(delivery_charges))-Float.parseFloat(wallet_amount);

                        binding.total.setText("₹"+total_amt);
                        binding.deliveryCharges.setText("₹"+delivery_charges);

                        Log.e("t_amount_deliver_charges",total_amt+" "+delivery_charges);
                        Log.d("orderdetailslist2",String.valueOf(list.size()));
                        itemsOrderAdapter=new ItemsOrderAdapter(list, Orderitemscheck.this, "ordered_list", new OrderListitems() {
                            @Override
                            public void OnItemClick(int position, ItemsOrderAdapter.ViewHolder holder, String s) {
                                Log.d("itemclick","itemclick");
                                selected_line_item_id=list.get(position).getLine_item_id();
                                Log.e("Selected_line_item",selected_line_item_id);
                                selected_order_item_id=list.get(position).getOrder_item_id();
                                selected_price=list.get(position).getSub_total_price();
                                TextView textView=holder.itemView.findViewById(R.id.D_price);
                                tv_qty=holder.itemView.findViewById(R.id.D_Quantity);
                                tv_qty.setText(list.get(position).getQuantity());

                                available_qty=list.get(position).getAvailable_quantity();

                                Log.d("quantityordered",tv_qty.toString());
//                                textView.setText("₹"+""+Float.parseFloat(list.get(position).getSub_total_price())*String.valueOf(s));
                                LinearLayout add_cart=holder.itemView.findViewById(R.id.D_add_cart);
                                add_cart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        AlertDialog.Builder dialog=new AlertDialog.Builder(Orderitemscheck.this);
                                        View view_alert= LayoutInflater.from(Orderitemscheck.this).inflate(R.layout.quantityupdate,null);
                                        paymentdetails=view_alert.findViewById(R.id.quantitydetails);
                                        close_btn=view_alert.findViewById(R.id.close_btn);
                                        submit_btn=view_alert.findViewById(R.id.submit_btn);
                                        clear_btn=view_alert.findViewById(R.id.clearButton);
                                        quanity=view_alert.findViewById(R.id.quanity);
                                        cancel=view_alert.findViewById(R.id.Cancel);
                                        submit_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ordecount = quanity.getText().toString();
                                               // tv_qty.setText(ordecount);
                                                Log.e("ordercount", ordecount.toString());
//                                                order_list_api_call(ordecount, holder.itemView.findViewById(R.id.price), position);

                                                Log.e("order_cnt",ordecount);
                                                Log.e("text",tv_qty.getText().toString());
                                              //  if(Integer.parseInt(ordecount)>Integer.parseInt(list.get(position).getQuantity())){
                                                if(Integer.parseInt(ordecount)>Integer.parseInt(list.get(position).getAvailable_quantity())){
                                            //        Toast.makeText(Orderitemscheck.this, "Insufficient Quantity.", Toast.LENGTH_SHORT).show();

                                                    Snackbar.make(Orderitemscheck.this,findViewById(android.R.id.content),"Insufficient Quantity.",Snackbar.LENGTH_LONG).show();

                                                }
                                                else{
                                                    edit_dispatcher_order_item_api_call(ordecount);
                                                  //  order_items_list_api_call();
                                                    alertDialog.dismiss();
                                                }

                                            }
                                        });

                                        clear_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                quanity.setText("");
                                            }
                                        });
                                        close_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                alertDialog.dismiss();
                                            }
                                        });
                                        cancel.setOnClickListener(new View.OnClickListener() {
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
                                });
                            }
                        });
                        Log.d("Adapter",String.valueOf(jsonArray.length()));
                        binding.orderedListItems.setAdapter(itemsOrderAdapter);
                        Log.d("recycleview",String.valueOf(jsonArray.length()));
                        itemsOrderAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("Exception", String.valueOf(e));
                      //  Toast.makeText(Orderitemscheck.this, "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                //    Toast.makeText(Orderitemscheck.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    Snackbar.make(Orderitemscheck.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }

    private void edit_dispatcher_order_item_api_call(String ordecount) {
        progressDialog.show();
        JsonObject object = new JsonObject();
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token", SaveAppData.getLoginData().getToken());
        object.addProperty("order_id",order_id);
        object.addProperty("line_item_id", selected_line_item_id);
        object.addProperty("order_item_id",selected_order_item_id);
        object.addProperty("qty",ordecount);

        viewModel.edit_dipatcher_items_order(object).observe(Orderitemscheck.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject!= null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    try {
                        Log.d("ordersDetails","Text"+jsonObject);
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());

                        String message=jsonObject1.getString("message");
                     //   Toast.makeText(Orderitemscheck.this, ""+message, Toast.LENGTH_SHORT).show();
                        if(message.equalsIgnoreCase("success")){
                            alertDialog.dismiss();
                            tv_qty.setText(ordecount);
                            used_amount=jsonObject1.getJSONObject("wallet_history").getString("used_amount");
                            amount_refunded=jsonObject1.getJSONObject("wallet_history").getString("amount_refunded");
                            balanced_amount=jsonObject1.getJSONObject("wallet_history").getString("balanced_amount");
                            wallet_amount=jsonObject1.getJSONObject("vendor_wallet").getString("wallet_amount");
                            order_items_list_api_call();
                        }



                    } catch (JSONException e) {
                        Log.e("Exception", String.valueOf(e));
                      //  Toast.makeText(Orderitemscheck.this, ""+e, Toast.LENGTH_SHORT).show();
                    }

                }else{
                //    Toast.makeText(Orderitemscheck.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Orderitemscheck.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }
            }
        });
    }



}