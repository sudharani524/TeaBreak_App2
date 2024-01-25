package com.glitss.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.glitss.teabreak_app.Adapter.ItemslistAdapter;

import com.glitss.teabreak_app.ModelClass.ListItemsModel;

import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.Constant;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityListItemsBinding;
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

public class ListItems_Activity extends AppCompatActivity {
    private ActivityListItemsBinding binding;
    ProgressDialog progressDialog;
    private TeaBreakViewModel viewModel;
    ItemslistAdapter ItemslistAdapter;
    LinearLayout paymentdetails;
    ImageView close_btn,clear_btn;
    AlertDialog alertDialog;
    LinearLayout submit_btn,cancel;
    EditText quanity;
    String ordecount;

    TextView tv_qty;

    ArrayList<ListItemsModel> list=new ArrayList<>();
    ArrayList<ListItemsModel> filteredList=new ArrayList<>();
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
                finish();

            }
        });
        binding.Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListItems_Activity.this,Cartlist_Activity.class));
                finish();


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
                    ItemslistAdapter=new ItemslistAdapter(list, ListItems_Activity.this, "list_items", new CartInterface() {
                        @Override
                        public void OnItemClick(int position, com.glitss.teabreak_app.Adapter.ItemslistAdapter.ViewHolder holder, String s) {

                            adapterClickEvent(position,holder,s, list);

                        }
                    });

                }else {

                    filteredList.clear();

                    for (ListItemsModel ts : list) {

                        if (ts.getLine_item_name().trim().toLowerCase(Locale.getDefault()).contains(text)) {
                            filteredList.add(ts);
                        }
                    }
                    ItemslistAdapter=new ItemslistAdapter(filteredList, ListItems_Activity.this, "list_items", new CartInterface() {
                        @Override
                        public void OnItemClick(int position, com.glitss.teabreak_app.Adapter.ItemslistAdapter.ViewHolder holder, String s) {

                            adapterClickEvent(position,holder,s, filteredList);

                        }
                    });

                }
                binding.rvListItems.setAdapter(ItemslistAdapter);
                ItemslistAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void list_items_api_call() {

        progressDialog.show();
        viewModel.get_list_items().observe(ListItems_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (jsonObject != null){

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        String text=jsonObject1.getString("text");

                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");


                     //   Toast.makeText(ListItems_Activity.this, ""+text, Toast.LENGTH_SHORT).show();

                        if(message.equalsIgnoreCase("success")){
                            list.clear();
                            for(int i=0;i<jsonArray.length();i++){
                                ListItemsModel listItemsModel = new Gson().fromJson(jsonArray.get(i).toString(), new TypeToken<ListItemsModel>() {
                                }.getType());
                                list.add(listItemsModel);
                            }
                        }

               /*         ItemslistAdapter= new ItemslistAdapter(ListItems_Activity.this, list,"list_items", new ListItemInterface() {
                            @SuppressLint("MissingInflatedId")
                            @Override
                            public void OnItemClick(int position, View v, String s) {

                                selected_line_item_id=list.get(position).getLine_item_id();
                                selected_price=list.get(position).getPrice();
                                selected_qty=list.get(position).getPack_of_qty();

                                if(s.equalsIgnoreCase("cart")){
                                    AlertDialog.Builder dialog=new AlertDialog.Builder(ListItems_Activity.this);
                                    View view_alert= LayoutInflater.from(ListItems_Activity.this).inflate(R.layout.quantityupdate,null);
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
                                            Log.e("ordercount",ordecount.toString());
                                            add_cart_api_call();

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

                                }
                        });
*/


                        ItemslistAdapter=new ItemslistAdapter(list, ListItems_Activity.this, "list_items", new CartInterface() {
                            @Override
                            public void OnItemClick(int position, com.glitss.teabreak_app.Adapter.ItemslistAdapter.ViewHolder holder, String s) {

                                adapterClickEvent(position,holder,s,list);

                            }
                        });

                        binding.rvListItems.setAdapter(ItemslistAdapter);
                        ItemslistAdapter.notifyDataSetChanged();




                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                      //  Toast.makeText(ListItems_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                  //  Toast.makeText(ListItems_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(ListItems_Activity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    private void adapterClickEvent(int position, com.glitss.teabreak_app.Adapter.ItemslistAdapter.ViewHolder holder, String s, ArrayList<ListItemsModel> filtered_List) {

        selected_line_item_id= filtered_List.get(position).getLine_item_id();
        selected_price= filtered_List.get(position).getPrice();
        //selected_qty=list.get(position).getPack_of_qty();


        Log.d("TAG","line_id : "+filtered_List.get(position).getLine_item_id());
        Log.d("TAG","line_id_price : "+filtered_List.get(position).getPrice());

        tv_qty=holder.itemView.findViewById(R.id.Quantity);

        AlertDialog.Builder dialog=new AlertDialog.Builder(ListItems_Activity.this);
        View view_alert= LayoutInflater.from(ListItems_Activity.this).inflate(R.layout.quantityupdate,null);
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
                  //  Toast.makeText(ListItems_Activity.this, "Enter Qty", Toast.LENGTH_SHORT).show();
                    Snackbar.make(ListItems_Activity.this,findViewById(android.R.id.content),"Enter Qty",Snackbar.LENGTH_LONG).show();


                    return;
                }
                if(Integer.parseInt(ordecount)>99){
                    Snackbar.make(ListItems_Activity.this,findViewById(android.R.id.content),"Please Enter the quantity below 99",Snackbar.LENGTH_LONG).show();
                    return;
                }
                else{
                    Log.e("ordercount",ordecount.toString());
                    add_cart_api_call();
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

    private void add_cart_api_call() {
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("line_item_id",selected_line_item_id);
        object.addProperty("quantity",ordecount);
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

                   //     Toast.makeText(ListItems_Activity.this, ""+text, Toast.LENGTH_SHORT).show();
                        if(message.equalsIgnoreCase("Success")){
                            alertDialog.dismiss();
                             tv_qty.setText(ordecount);
                        }


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                    //    Toast.makeText(ListItems_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                 //   Toast.makeText(ListItems_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(ListItems_Activity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

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