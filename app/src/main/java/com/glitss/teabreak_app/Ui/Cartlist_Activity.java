package com.glitss.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.glitss.teabreak_app.Adapter.ItemslistAdapter;
import com.glitss.teabreak_app.ModelClass.ListItemsModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.Constant;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityCartlistBinding;
import com.glitss.teabreak_app.repository.CartInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class Cartlist_Activity extends AppCompatActivity {

    private ActivityCartlistBinding binding;
    ProgressDialog progressDialog;
    private TeaBreakViewModel viewModel;
    ArrayList<ListItemsModel> cart_list=new ArrayList<>();
    ArrayList<ListItemsModel> Pricelist = new ArrayList<>();

    ArrayList<ListItemsModel> filteredList=new ArrayList<>();
    ItemslistAdapter itemslistAdapter,adapter;
    boolean Cartfilterlist=false;
    LinearLayout paymentdetails;
    ImageView close_btn,clear_btn;
    AlertDialog alertDialog;
    LinearLayout submit_btn,cancel;
    EditText quanity;
    String ordecount="";

    TextView tv_qty;
    String selected_line_item_id="",selected_price="",selected_qty="";
    String total="",Delivery_charges="";

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

     /*   if(cart_list.isEmpty() || cart_list.size()==0){
            Snackbar.make(Cartlist_Activity.this,findViewById(android.R.id.content),"No data found",Snackbar.LENGTH_LONG).show();
        }*/
        cart_list_api_call();

        binding.Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cart_list.isEmpty()){
                    AlertDialog.Builder dialoguee=new AlertDialog.Builder(Cartlist_Activity.this);
                    dialoguee.setTitle("Cart is empty");
                    dialoguee.setMessage("Please add the items to the cart list");
                    dialoguee.setCancelable(false);
                    dialoguee.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialoguee.create();
                    dialoguee.show();
                    return;
                }
                Intent intent=new Intent(Cartlist_Activity.this, Checkout.class);
                intent.putExtra("t_amount",total);
                Log.e("t_amount",total);
                intent.putExtra("delivery_charges",Delivery_charges);
                Log.e("delivery",Delivery_charges);
                startActivity(intent);
                finish();

            }
        });


        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
        binding.etSearchfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }
            @Override
            
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = binding.etSearchfilter.getText().toString().toLowerCase(Locale.getDefault());


                if(text.isEmpty()){
                    itemslistAdapter=new ItemslistAdapter(cart_list, Cartlist_Activity.this, "list_items", new CartInterface() {
                        @Override
                        public void OnItemClick(int position, ItemslistAdapter.ViewHolder holder, String s) {

                            adapterClickEvent(position,holder,s, cart_list);

                        }
                    });

                }else {

                    filteredList.clear();

                    for (ListItemsModel ts : cart_list) {

                        if (ts.getLine_item_name().trim().toLowerCase(Locale.getDefault()).contains(text)) {
                            filteredList.add(ts);
                        }
                    }
                    itemslistAdapter=new ItemslistAdapter(filteredList, Cartlist_Activity.this, "list_items", new CartInterface() {
                        @Override
                        public void OnItemClick(int position, ItemslistAdapter.ViewHolder holder, String s) {

                            adapterClickEvent(position,holder,s,filteredList);

                        }
                    });

                }
                binding.rvCartList.setAdapter(itemslistAdapter);
                itemslistAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void cart_list_api_call() {
//        cart_list.clear();

        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_cart_list(object).observe(Cartlist_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if (jsonObject != null){
                    Log.d("TAG","add_cart "+jsonObject);
                    cart_list.clear();
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");

                        Log.e("data_array", String.valueOf(jsonArray.length()));

                        if(jsonArray.length()==0){
                       //     Snackbar.make(Cartlist_Activity.this,findViewById(android.R.id.content),"No Order List",Snackbar.LENGTH_LONG).show();

                            binding.tvNoDataFound.setVisibility(View.VISIBLE);
                            binding.rvCartList.setVisibility(View.GONE);
                            binding.tvNoDataFound.setText("No Data Found");
                        }
                        else{
                            binding.tvNoDataFound.setVisibility(View.GONE);
                            binding.rvCartList.setVisibility(View.VISIBLE);
                        }
                        String message=jsonObject1.getString("message");
                        JSONArray jsonArray1=new JSONArray();
                        jsonArray1=jsonObject1.getJSONArray("sub_totals");

                     //   Toast.makeText(Cartlist_Activity.this, ""+message, Toast.LENGTH_SHORT).show();


                        for(int i=0;i<jsonArray.length();i++){
                            ListItemsModel listItemsModel = new Gson().fromJson(jsonArray.get(i).toString(), new TypeToken<ListItemsModel>() {
                            }.getType());
                            cart_list.add(listItemsModel);
                        }


                     /*   if(cart_list.isEmpty() || cart_list.size()==0){
                            Snackbar.make(Cartlist_Activity.this,findViewById(android.R.id.content),"No data found",Snackbar.LENGTH_LONG).show();
                        }*/

                        for(int i=0;i<jsonArray1.length();i++){
                            Log.e("array",jsonArray1.toString());
                            ListItemsModel listItemsModel = new Gson().fromJson(jsonArray1.get(i).toString(), new TypeToken<ListItemsModel>() {
                            }.getType());
                            Pricelist.add(listItemsModel);
                        }

                        for (int i = 0; i < Pricelist.size(); i++) {
                             total = Pricelist.get(i).getSub_total();
                            Log.d("subtotal", total);
                            binding.tAmount.setText("₹"+total);
                            Delivery_charges=Pricelist.get(i).getAll_sub_total_delivery_charges();
                            Log.d("Delivery Charges",Delivery_charges);
                        }


                   /*     itemslistAdapter=new ItemslistAdapter(cart_list, Cartlist_Activity.this, "cart_items", new CartInterface() {
                            @Override
                            public void OnItemClick(int position, ItemslistAdapter.ViewHolder holder, String s) {
                                selected_line_item_id=cart_list.get(position).getLine_item_id();
                                selected_price=cart_list.get(position).getPrice();


                                //errorMessage(holder.itemView.findViewById(R.id.sp_qty),s);
                                TextView textView=holder.itemView.findViewById(R.id.price);
                                textView.setText("₹"+""+Float.parseFloat(cart_list.get(position).getPrice())*Float.parseFloat(s));

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
*/

                        itemslistAdapter=new ItemslistAdapter(cart_list, Cartlist_Activity.this, "cart_items", new CartInterface() {
                            @Override
                            public void OnItemClick(int position, ItemslistAdapter.ViewHolder holder, String s) {

                                adapterClickEvent(position,holder,s, cart_list);


                            }
                        });

                        binding.rvCartList.setAdapter(itemslistAdapter);
                        itemslistAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception","CartList Activity"+e);
                     //   Toast.makeText(Cartlist_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }

                }else{
               //     Toast.makeText(Cartlist_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    Snackbar.make(Cartlist_Activity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });
    }

    private void adapterClickEvent(int position, ItemslistAdapter.ViewHolder holder, String s, ArrayList<ListItemsModel> filtered_List) {

        selected_line_item_id=filtered_List.get(position).getLine_item_id();
        selected_price=filtered_List.get(position).getPrice();

        Log.d("TAG","line_id : "+filtered_List.get(position).getLine_item_id());
        Log.d("TAG","line_id_price : "+filtered_List.get(position).getPrice());

        if(Objects.equals(s, "Del")){
            Log.e("dlt_click","dlt_click");
            dlt_item_api_call(position,filtered_List);
            return;
        }

        if(Objects.equals(s, "Add")){
            Log.e("add","add_click");
            AlertDialog.Builder dialog=new AlertDialog.Builder(Cartlist_Activity.this);
            View view_alert= LayoutInflater.from(Cartlist_Activity.this).inflate(R.layout.quantityupdate,null);
            paymentdetails=view_alert.findViewById(R.id.quantitydetails);
            close_btn=view_alert.findViewById(R.id.close_btn);
            submit_btn=view_alert.findViewById(R.id.submit_btn);
            clear_btn=view_alert.findViewById(R.id.clearButton);
            quanity=view_alert.findViewById(R.id.quanity);
            cancel=view_alert.findViewById(R.id.Cancel);
            submit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ordecount=quanity.getText().toString();
                    if (ordecount.isEmpty()){
                      //  Toast.makeText(Cartlist_Activity.this, "Enter Qty", Toast.LENGTH_SHORT).show();
                        Snackbar.make(Cartlist_Activity.this,findViewById(android.R.id.content),"Enter Qty",Snackbar.LENGTH_LONG).show();

                        return;
                    }
                    if(Integer.parseInt(ordecount)>99){
                        Snackbar.make(Cartlist_Activity.this,findViewById(android.R.id.content),"Please Enter the quantity below 99",Snackbar.LENGTH_LONG).show();

                        return;
                    }
                    else{
                        Log.e("ordercount",ordecount.toString());
                        add_cart_api_call(ordecount,holder.itemView.findViewById(R.id.price),position);
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
            return;
        }


        //errorMessage(holder.itemView.findViewById(R.id.sp_qty),s);
        TextView textView=holder.itemView.findViewById(R.id.price);
        tv_qty=holder.itemView.findViewById(R.id.Quantity);
        textView.setText("₹"+""+Float.parseFloat(cart_list.get(position).getPrice())*Float.parseFloat(s));

        tv_qty.setText(s);

        Log.e("priceee",""+Float.parseFloat(filtered_List.get(position).getPrice())*Float.parseFloat(s));
                              /*  if(s.equalsIgnoreCase("Select")){
                                    Toast.makeText(Cartlist_Activity.this, "Please select the quantity", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    add_cart_api_call(s,holder.itemView.findViewById(R.id.price),position);
                                }*/

        /*ImageView iv_dlt=holder.itemView.findViewById(R.id.iv_delete);
        Log.d("cartdelete","delete");

        iv_dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("dlt_click","dlt_click");
                dlt_item_api_call();
            }
        });
*/
       /* LinearLayout add_cart=holder.itemView.findViewById(R.id.add_cart);

                               *//* iv_dlt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.e("dlt_click","dlt_click");
                                        dlt_item_api_call();
                                    }
                                });*//*

        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/
    }

    private void dlt_item_api_call(int position, ArrayList<ListItemsModel> filtered_List) {

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

                     //   Toast.makeText(Cartlist_Activity.this, ""+message, Toast.LENGTH_SHORT).show();
                        if(message.equalsIgnoreCase("Success")){
                            Log.d("TAG","delete api call sucess : "+cart_list.size());
                            Log.d("TAG","delete api call sucess1 : "+filtered_List.size());
                            filtered_List.remove(position);
                            itemslistAdapter.notifyDataSetChanged();
                            Log.d("TAG","delete api call sucess2 : "+filtered_List.size());
                            if(position==0){
                                binding.tAmount.setText("0");
                            }

                           /* if(cart_list.isEmpty() || cart_list.size()==0){
                                Snackbar.make(Cartlist_Activity.this,findViewById(android.R.id.content),"No data found",Snackbar.LENGTH_LONG).show();
                            }*/

                            cart_list_api_call();
                        }

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                   //     Toast.makeText(Cartlist_Activity.this, ""+e, Toast.LENGTH_SHORT).show();

                    }

                }else{
               //     Toast.makeText(Cartlist_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Cartlist_Activity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

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

                      //  Toast.makeText(Cartlist_Activity.this, ""+text, Toast.LENGTH_SHORT).show();

                        if(message.equalsIgnoreCase("success")){

                            alertDialog.dismiss();
//                            Log.e("qty",s);
                            //  tv_qty.setText(s);
                            TextView textView= (TextView) v;
                            Log.e("s_qty",cart_list.get(position).getPrice());
                            Log.e("price",s);
                            textView.setText("₹"+""+Float.parseFloat(cart_list.get(position).getPrice())*Float.parseFloat(s));
                            Log.e("t_price",""+Float.parseFloat(cart_list.get(position).getPrice())*Float.parseFloat(s));

                            /*if(cart_list.isEmpty() || cart_list.size()==0){
                                Snackbar.make(Cartlist_Activity.this,findViewById(android.R.id.content),"No data found",Snackbar.LENGTH_LONG).show();
                            }*/
                            cart_list_api_call();
                        }

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                     //   Toast.makeText(Cartlist_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                //    Toast.makeText(Cartlist_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Cartlist_Activity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

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