package com.glitss.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.glitss.teabreak_app.Adapter.OrderedlistAdapter;
import com.glitss.teabreak_app.ModelClass.OrderedlistModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityClosedOrdersListBinding;
import com.glitss.teabreak_app.repository.itemsorderInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ClosedOrdersListActivity extends AppCompatActivity {

    private ActivityClosedOrdersListBinding binding;
    OrderedlistAdapter orderedlistAdapter;
    private TeaBreakViewModel viewModel;
    ArrayList<OrderedlistModel> list=new ArrayList<>();

    ProgressDialog progressDialog;
    String from_date = "", to_date = "";
    DatePickerDialog dialog;

    private DatePickerDialog.OnDateSetListener mDatasetlistner;
    private DatePickerDialog.OnDateSetListener mDatasetlistner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityClosedOrdersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel= ViewModelProviders.of(ClosedOrdersListActivity.this).get(TeaBreakViewModel.class);

        progressDialog=new ProgressDialog(ClosedOrdersListActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ClosedOrdersListActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvClosedOrdersList.setLayoutManager(linearLayoutManager);


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String d=String.valueOf(day);
        String m=String.valueOf(month+1);
        String y=String.valueOf(year);

        to_date=y+"-"+m+"-"+d;

        c.add(Calendar.DATE,-1);
        int day1 = c.get(Calendar.DAY_OF_MONTH);
        int month1 = c.get(Calendar.MONTH);
        int year1 = c.get(Calendar.YEAR);
        String d1=String.valueOf(day1);
        String m1=String.valueOf(month1+1);
        String y1=String.valueOf(year1);
        from_date=y1+"-"+m1+"-"+d1;
        Log.e("from_date",from_date);
        Log.e("to_date",to_date);


        binding.etFromDate.setText(from_date);
        binding.etToDate.setText(to_date);


        binding.etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateselect();
            }
        });
        binding.etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                to_dateselect();
            }
        });


        mDatasetlistner=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                if(month<10){
                    from_date=String.valueOf(year)+"-"+"0"+String.valueOf(month)+"-"+String.valueOf(dayOfMonth);
                }else{
                    from_date=String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(dayOfMonth);
                }
                Log.e("listorder","Listorder");
                binding.etFromDate.setText(from_date);
            }
        };

        mDatasetlistner1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                if(month<10){
                    to_date=String.valueOf(year)+"-"+"0"+String.valueOf(month)+"-"+String.valueOf(dayOfMonth);
                }else{
                    to_date=String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(dayOfMonth);
                }
                Log.e("Orderedlist","Orderedlist");
                binding.etToDate.setText(to_date);
            }
        };

        closed_orders_list_api_call();

        binding.getDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.etFromDate.getText().toString().equals("")){
                    //   Toast.makeText(VendorOrderlist.this, "Please Select From Date", Toast.LENGTH_SHORT).show();
                    Snackbar.make(ClosedOrdersListActivity.this,findViewById(android.R.id.content),"Please Select From Date",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(binding.etToDate.getText().toString().equals("")){
                    //   Toast.makeText(VendorOrderlist.this, "Please Select To Date", Toast.LENGTH_SHORT).show();
                    Snackbar.make(ClosedOrdersListActivity.this,findViewById(android.R.id.content),"Please Select To Date",Snackbar.LENGTH_LONG).show();
                    return;
                }

                closed_orders_list_api_call();
            }
        });

    }

    private void to_dateselect() {
        Log.e("date_select"," To date select");
        Calendar cal= Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        dialog=new DatePickerDialog(ClosedOrdersListActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDatasetlistner1,year,month,day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window= dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.show();
    }

    private void dateselect() {
        Log.e("date_select","From Date select");
        Calendar cal= Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        dialog=new DatePickerDialog(ClosedOrdersListActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDatasetlistner,year,month,day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window= dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.show();
    }


    private void closed_orders_list_api_call() {

        progressDialog.show();
        JsonObject object = new JsonObject();
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token", SaveAppData.getLoginData().getToken());
        object.addProperty("from_date",""+binding.etFromDate.getText().toString());
        object.addProperty("to_date",""+binding.etToDate.getText().toString());
        viewModel.closed_orders_list(object).observe(ClosedOrdersListActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject!= null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("ordersDetails","Text"+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("order_list");

                        if(jsonArray.length()==0){
                            binding.tvNoDataFound.setVisibility(View.VISIBLE);
                            binding.rvClosedOrdersList.setVisibility(View.GONE);
                            binding.tvNoDataFound.setText("No Data Found");
                            return;
                        }
                        else{
                            binding.tvNoDataFound.setVisibility(View.GONE);
                            binding.rvClosedOrdersList.setVisibility(View.VISIBLE);
                        }

                        Log.d("dataorderdetails",jsonArray.toString());
                        list.clear();
                        for(int i=0;i<jsonArray.length();i++){
                            Log.d("forloop","loop");
                            OrderedlistModel orderedlistModel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<OrderedlistModel>() {
                            }.getType());
                            list.add(orderedlistModel);
                            Log.d("orderdetailslist",String.valueOf(list.size()));
                        }
                        Log.d("orderdetailslist2",String.valueOf(list.size()));
//                                orderedlistAdapter=new OrderedlistAdapter(list, VendorOrderlist.this);
                        orderedlistAdapter=new OrderedlistAdapter(list, ClosedOrdersListActivity.this, new itemsorderInterface() {
                            @Override
                            public void OnItemClick(int position, OrderedlistAdapter.ViewHolder holder, String o) {

                               /* Intent intent=new Intent(ClosedOrdersListActivity.this,Orderitemscheck.class);
                                intent.putExtra("order_no",list.get(position).getOrder_no());
                                intent.putExtra("Amount",list.get(position).getTotal_amount());
                                Log.d("Amount",list.get(position).getTotal_amount());
                                intent.putExtra("order_date",list.get(position).getOrder_date_time());
                                intent.putExtra("orderid",list.get(position).getOrder_id());
                                startActivity(intent);*/

                            }
                        });

                        Log.d("Adapter",String.valueOf(jsonArray.length()));
                        binding.rvClosedOrdersList.setAdapter(orderedlistAdapter);
                        Log.d("recycleview",String.valueOf(jsonArray.length()));
                        orderedlistAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("Exception", String.valueOf(e));
                        // Toast.makeText(VendorOrderlist.this, ""+e, Toast.LENGTH_SHORT).show();
                    }

                }else{

                    //     Toast.makeText(VendorOrderlist.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(ClosedOrdersListActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}