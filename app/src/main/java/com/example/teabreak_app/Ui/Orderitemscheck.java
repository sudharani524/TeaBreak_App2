package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.teabreak_app.Adapter.ItemslistAdapter;
import com.example.teabreak_app.Adapter.OrderHistoryAdapter;
import com.example.teabreak_app.Adapter.Orderdetailsadapter;
import com.example.teabreak_app.Adapter.OrderedlistAdapter;
import com.example.teabreak_app.ModelClass.ItemsorderedModel;
import com.example.teabreak_app.ModelClass.OrderHistoryModel;
import com.example.teabreak_app.ModelClass.OrderdetailsModel;
import com.example.teabreak_app.ModelClass.OrderedlistModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityOrderdetailsBinding;
import com.example.teabreak_app.databinding.ActivityOrderitemscheckBinding;
import com.example.teabreak_app.repository.OrderListitems;
import com.example.teabreak_app.repository.OrderdetailsInterface;
import com.example.teabreak_app.repository.itemsorderInterface;
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
    String selected_line_item_id="",selected_price="",selected_qty="";
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
        order_items_list_api_call();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.orderedListItems.setLayoutManager(linearLayoutManager);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orderitemscheck.this, VendorOrderlist.class));

            }
        });
        binding.Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Orderitemscheck.this, "Orders List Updated", Toast.LENGTH_SHORT).show();
            }
        });
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orderitemscheck.this, VendorOrderlist.class));
            }
        });

    }

    private void order_items_list_api_call() {
        JsonObject object = new JsonObject();
        Log.d("orderdetailsapicall",object.toString());
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("order_id",order_id);
        viewModel.get_Ordered_items_list(object).observe(Orderitemscheck.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
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
                        Log.d("orderdetailslist2",String.valueOf(list.size()));
                        itemsOrderAdapter=new ItemsOrderAdapter(list, Orderitemscheck.this, "ordered_list", new OrderListitems() {
                            @Override
                            public void OnItemClick(int position, ItemsOrderAdapter.ViewHolder holder, String s) {
                                Log.d("itemclick","itemclick");
                                selected_line_item_id=list.get(position).getLine_item_id();
                                selected_price=list.get(position).getSub_total_price();
                                TextView textView=holder.itemView.findViewById(R.id.D_price);
                                tv_qty=holder.itemView.findViewById(R.id.D_Quantity);
                                tv_qty.setText(list.get(position).getQuantity());
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
                                                tv_qty.setText(ordecount);
                                                Log.e("ordercount", ordecount.toString());
//                                                order_list_api_call(ordecount, holder.itemView.findViewById(R.id.price), position);

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
                        Toast.makeText(Orderitemscheck.this, "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(Orderitemscheck.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}