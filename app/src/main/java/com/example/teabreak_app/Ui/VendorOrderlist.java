package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import com.example.teabreak_app.Adapter.OrderHistoryAdapter;
import com.example.teabreak_app.Adapter.OrderedlistAdapter;
import com.example.teabreak_app.ModelClass.OrderedlistModel;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityVendorOrderlistBinding;
import com.example.teabreak_app.repository.OrderdetailsInterface;
import com.example.teabreak_app.repository.itemsorderInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VendorOrderlist extends AppCompatActivity {
    DatePickerDialog dialog;
    private ActivityVendorOrderlistBinding binding;
    String from_date = "", to_date = "";
    private DatePickerDialog.OnDateSetListener mDatasetlistner;
    private DatePickerDialog.OnDateSetListener mDatasetlistner1;
    ProgressDialog progressDialog;
    ArrayList<OrderedlistModel> list=new ArrayList<>();
    private  TeaBreakViewModel viewModel;
    OrderedlistAdapter orderedlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVendorOrderlistBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String d=String.valueOf(day);
        String m=String.valueOf(month+1);
        String y=String.valueOf(year);

        from_date=y+"-"+m+"-"+d;
        to_date=y+"-"+m+"-"+d;
        viewModel = ViewModelProviders.of(VendorOrderlist.this).get(TeaBreakViewModel.class);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(VendorOrderlist.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rv.setLayoutManager(linearLayoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
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
        binding.getDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etFromDate.getText().toString().equals("")){
                    Toast.makeText(VendorOrderlist.this, "Please Select From Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(binding.etToDate.getText().toString().equals("")){
                    Toast.makeText(VendorOrderlist.this, "Please Select To Date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkDates(from_date,to_date)){
                   Orderlist_api_call();

                }

            }

            private void Orderlist_api_call() {
                progressDialog.show();
                JsonObject object = new JsonObject();
                Log.d("object","Orderlistapicall");
                object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
                Log.d("userid",SaveAppData.getLoginData().getUser_id());
                object.addProperty("user_token", SaveAppData.getLoginData().getToken());
                Log.d("token",SaveAppData.getLoginData().getToken());
                object.addProperty("from_date",""+binding.etFromDate.getText().toString());
                Log.d("formdate",binding.etFromDate.getText().toString());
                object.addProperty("to_date",""+binding.etToDate.getText().toString());
                viewModel.get_ordered_list(object).observe(VendorOrderlist.this, new Observer<JsonObject>() {
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
                                orderedlistAdapter=new OrderedlistAdapter(list, VendorOrderlist.this, new itemsorderInterface() {
                                    @Override
                                    public void OnItemClick(int position, OrderedlistAdapter.ViewHolder holder, String o) {
                                        Intent intent=new Intent(VendorOrderlist.this,Orderitemscheck.class);
                                        intent.putExtra("order_no",list.get(position).getOrder_no());
                                        intent.putExtra("Amount",list.get(position).getTotal_amount());
                                        Log.d("Amount",list.get(position).getTotal_amount());
                                        intent.putExtra("order_date",list.get(position).getOrder_date_time());
                                        intent.putExtra("orderid",list.get(position).getOrder_id());
                                        startActivity(intent);
                                    }
                                });

                                Log.d("Adapter",String.valueOf(jsonArray.length()));
                                binding.rv.setAdapter(orderedlistAdapter);
                                Log.d("recycleview",String.valueOf(jsonArray.length()));
                                orderedlistAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                Toast.makeText(VendorOrderlist.this, ""+e, Toast.LENGTH_SHORT).show();
                            }


                        }else{

                            Toast.makeText(VendorOrderlist.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    public static String getCalculatedDate(String date, String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        try {
            Log.e("week_ago",""+s.format(new Date(s.parse(date).getTime())));
            return s.format(new Date(s.parse(date).getTime()));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("TAG", "Error in Parsing Date : " + e.getMessage());



        }
        return null;
    }
    private boolean checkDates(String toString, String toString1) {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        boolean b = false;
        try {
            //  b = dfDate.parse(d1).before(dfDate.parse(d2)) || dfDate.parse(d1).equals(dfDate.parse(d2));
            if(dfDate.parse(toString).before(dfDate.parse(toString1)))
            {
                b = true;//If start date is before end date
            }
            else if(dfDate.parse(toString).equals(dfDate.parse(toString1)))
            {
                b = true;//If two dates are equal
            }
            else
            {
                b = false;//If start date is after the end date
                Snackbar.make(VendorOrderlist.this,findViewById(android.R.id.content),"Invalid Dates",Snackbar.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private void dateselect() {
        Log.e("date_select","From Date select");
        Calendar cal= Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        dialog=new DatePickerDialog(VendorOrderlist.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDatasetlistner,year,month,day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window= dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void to_dateselect() {
        Log.e("date_select"," To date select");
        Calendar cal= Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        dialog=new DatePickerDialog(VendorOrderlist.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDatasetlistner1,year,month,day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window= dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.show();
    }

}