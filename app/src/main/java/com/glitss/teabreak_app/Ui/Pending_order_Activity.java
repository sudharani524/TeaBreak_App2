package com.glitss.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.glitss.teabreak_app.Adapter.Accountant_Orderlist_Adapter;
import com.glitss.teabreak_app.ModelClass.OrderdetailsModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityPendingOrderBinding;
import com.glitss.teabreak_app.repository.ListItemInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Pending_order_Activity extends AppCompatActivity {

    private ActivityPendingOrderBinding binding;
    private TeaBreakViewModel viewModel;
    Accountant_Orderlist_Adapter accountantOrderlistAdapter;
    ArrayList<OrderdetailsModel> account_order_list=new ArrayList<>();
    AppCompatButton edit_btn;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv_pending_orders_list;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPendingOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(Pending_order_Activity.this).get(TeaBreakViewModel.class);

        progressDialog=new ProgressDialog(Pending_order_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        linearLayoutManager=new LinearLayoutManager(Pending_order_Activity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvPendingListItems.setLayoutManager(linearLayoutManager);

        pending_order_list_api_call();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }

    private void pending_order_list_api_call() {

        Log.e("pending_list","pending_list");
        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_accountant_pending_order_details(object).observe(Pending_order_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("order_list");
                        Log.d("order_list",jsonArray.toString());

                        if(jsonArray.length()==0){
                            binding.tvNoDataFound.setVisibility(View.VISIBLE);
                            binding.rvPendingListItems.setVisibility(View.GONE);
                            binding.tvNoDataFound.setText("No Data Found");
                        }
                        else{
                            binding.tvNoDataFound.setVisibility(View.GONE);
                            binding.rvPendingListItems.setVisibility(View.VISIBLE);

                        }

                        for(int i=0;i<jsonArray.length();i++){
                            OrderdetailsModel orderdetailsModel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<OrderdetailsModel>() {
                            }.getType());
                            account_order_list.add(orderdetailsModel);
                        }
                        Log.d("orderdetailslist",String.valueOf(account_order_list.size()));

                        accountantOrderlistAdapter=new Accountant_Orderlist_Adapter(Pending_order_Activity.this, account_order_list,"PendingList", new ListItemInterface() {
                            @Override
                            public void OnItemClick(int position, View v, String s) {


                                AlertDialog.Builder dialog=new AlertDialog.Builder(Pending_order_Activity.this);
                                dialog.setCancelable(false);
                                dialog.setMessage("Are you sure you want to approve this order");
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        edit_btn=v.findViewById(R.id.orders_edit_btn);
                                        approved_api_call(position);
                                    }
                                });

                                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                dialog.create();
                                dialog.show();


                            }
                        });
                        binding.rvPendingListItems.setAdapter(accountantOrderlistAdapter);
                        accountantOrderlistAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        Log.e("Exception", String.valueOf(e));
                        //    Toast.makeText(getActivity(), "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                  //  Toast.makeText(Pending_order_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Pending_order_Activity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void approved_api_call(int position) {
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("order_id",account_order_list.get(position).getOrder_id());
        object.addProperty("accounts_approve_status","1");

        viewModel.accountant_update_approve_order(object).observe(Pending_order_Activity.this, new Observer<JsonObject>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject != null){

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");

                        if(message.equalsIgnoreCase("success")){
                            edit_btn.setText("Approved");
                            edit_btn.setBackgroundColor(R.color.green);
                            edit_btn.setTextColor(R.color.black);
                            edit_btn.setEnabled(false);
                            edit_btn.setClickable(false);
                         //   Toast.makeText(Pending_order_Activity.this, ""+jsonObject1.getString("text"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Pending_order_Activity.this,AccountsDashboard.class));
                            finish();
                        }

                    } catch (JSONException e) {
                        Log.e("Exception",e.toString());
                      //  Toast.makeText(Pending_order_Activity.this, "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                  //  Toast.makeText(Pending_order_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Pending_order_Activity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}