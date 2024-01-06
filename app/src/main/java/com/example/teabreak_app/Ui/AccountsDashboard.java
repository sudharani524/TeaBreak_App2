package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.teabreak_app.Adapter.Accountant_Orderlist_Adapter;
import com.example.teabreak_app.Adapter.Orderdetailsadapter;
import com.example.teabreak_app.Fragments.Approved_Order_List_Fragment;
import com.example.teabreak_app.Fragments.Pending_Orders_List_Fragment;
import com.example.teabreak_app.ModelClass.OrderdetailsModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityAccountsDashboardBinding;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountsDashboard extends AppCompatActivity {

    private ActivityAccountsDashboardBinding binding;
    LinearLayoutManager linearLayoutManager;
    private TeaBreakViewModel viewModel;
    ArrayList<OrderdetailsModel> account_order_list=new ArrayList<>();
    Accountant_Orderlist_Adapter accountantOrderlistAdapter;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    AppCompatButton edit_btn;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAccountsDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(AccountsDashboard.this).get(TeaBreakViewModel.class);

        progressDialog=new ProgressDialog(AccountsDashboard.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

         linearLayoutManager=new LinearLayoutManager(AccountsDashboard.this);
         linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // binding.rvListItems.setLayoutManager(linearLayoutManager);

         binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 logout_api_call();
             }
         });

       //  ordered_list_api_call_accounts();
         
    }

    private void logout_api_call() {
        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.logout_api(object).observe(AccountsDashboard.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("Logout","logout"+jsonObject);


                    if (jsonObject.get("message").getAsString().equalsIgnoreCase("Successfully Logout")){
                        SaveAppData.saveOperatorLoginData(null);
                        startActivity(new Intent(AccountsDashboard.this, MainActivity.class));
                        finish();
                    }
                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(AccountsDashboard.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


 /*   private void ordered_list_api_call_accounts() {

        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_accountant_verifying_order_details(object).observe(AccountsDashboard.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject != null){

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("order_list");
                        Log.d("order_list",jsonArray.toString());

                        for(int i=0;i<jsonArray.length();i++){
                            OrderdetailsModel orderdetailsModel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<OrderdetailsModel>() {
                            }.getType());
                            account_order_list.add(orderdetailsModel);
                            Log.d("orderdetailslist",String.valueOf(account_order_list.size()));
                        }

                        accountantOrderlistAdapter=new Accountant_Orderlist_Adapter(AccountsDashboard.this, account_order_list, new ListItemInterface() {
                            @Override
                            public void OnItemClick(int position, View v, String s) {
                               *//* AlertDialog.Builder dialog=new AlertDialog.Builder(AccountsDashboard.this);
                                View view_alert= LayoutInflater.from(AccountsDashboard.this).inflate(R.layout.custom_card_approve_order,null);

                                edit_btn=v.findViewById(R.id.orders_edit_btn);
                                AppCompatButton submit_btn=view_alert.findViewById(R.id.submit_btn);
                                ImageView close_btn=view_alert.findViewById(R.id.close_btn);

                                submit_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if
                                        approved_api_call(position);
                                    }
                                });

                                close_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });




                                dialog.setView(view_alert);
                                dialog.setCancelable(true);
                                alertDialog = dialog.create();
                                alertDialog.show();*//*

                                edit_btn=v.findViewById(R.id.orders_edit_btn);
                                approved_api_call(position);
                            }
                        });
                       // binding.rvListItems.setAdapter(accountantOrderlistAdapter);
                        accountantOrderlistAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        Toast.makeText(AccountsDashboard.this, "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(AccountsDashboard.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }*/

   /* private void approved_api_call(int position) {
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("order_id",account_order_list.get(position).getOrder_id());
        object.addProperty("accounts_approve_status","1");

        viewModel.accountant_update_approve_order(object).observe(AccountsDashboard.this, new Observer<JsonObject>() {
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
                            Toast.makeText(AccountsDashboard.this, ""+jsonObject1.getString("text"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(AccountsDashboard.this, "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(AccountsDashboard.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/


    private static class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Pending_Orders_List_Fragment();
                case 1:
                    return new Approved_Order_List_Fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Pending List";
                case 1:
                    return "Approved List";
                default:
                    return null;
            }
        }
    }


}