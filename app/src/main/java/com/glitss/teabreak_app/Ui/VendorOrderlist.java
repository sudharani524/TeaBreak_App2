package com.glitss.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glitss.teabreak_app.Adapter.OrderedlistAdapter;
import com.glitss.teabreak_app.ModelClass.OrderedlistModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityVendorOrderlistBinding;
import com.glitss.teabreak_app.repository.itemsorderInterface;
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
    AlertDialog alertDialog;
    LinearLayout ll_change_pswd,ll_logout;


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

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.etFromDate.setText(from_date);
        binding.etToDate.setText(to_date);

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


        Orderlist_api_call();

   /*     binding.ivScreen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(VendorOrderlist.this);
                View view_alert= LayoutInflater.from(VendorOrderlist.this).inflate(R.layout.custom_layout,null);

               LinearLayout ll_change_pswd=(LinearLayout) view_alert.findViewById(R.id.ll_change_pswd);
              LinearLayout  ll_logout=(LinearLayout) view_alert.findViewById(R.id.ll_logout);
                ImageView close_btn=(ImageView) view_alert.findViewById(R.id.close_btn);

                ll_change_pswd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        startActivity(new Intent(VendorOrderlist.this,ChangePasswordActivity.class));
                    }
                });
                ll_logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        logout_api_call();
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
                alertDialog.show();
            }
        });*/



     /*   binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog logoutDialog = new AlertDialog.Builder(VendorOrderlist.this).setTitle("logout")
                        .setMessage("Are you sure you want to logout")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                logout_api_call();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false).show();
            }
        });*/

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
                 //   Toast.makeText(VendorOrderlist.this, "Please Select From Date", Toast.LENGTH_SHORT).show();
                    Snackbar.make(VendorOrderlist.this,findViewById(android.R.id.content),"Please Select From Date",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(binding.etToDate.getText().toString().equals("")){
                 //   Toast.makeText(VendorOrderlist.this, "Please Select To Date", Toast.LENGTH_SHORT).show();
                    Snackbar.make(VendorOrderlist.this,findViewById(android.R.id.content),"Please Select To Date",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(checkDates(from_date,to_date)){
                   Orderlist_api_call();

                }

            }


        });

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

                        if(jsonArray.length()==0){
                            binding.tvNoDataFound.setVisibility(View.VISIBLE);
                            binding.rv.setVisibility(View.GONE);
                            binding.tvNoDataFound.setText("No Data Found");
                            return;
                        }
                        else{
                            binding.tvNoDataFound.setVisibility(View.GONE);
                            binding.rv.setVisibility(View.VISIBLE);
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
                        Log.e("Exception", String.valueOf(e));
                        // Toast.makeText(VendorOrderlist.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    //     Toast.makeText(VendorOrderlist.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(VendorOrderlist.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();
                }
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

    private void logout_api_call() {
        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.logout_api(object).observe(VendorOrderlist.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("Logout","logout"+jsonObject);


                    if (jsonObject.get("message").getAsString().equalsIgnoreCase("Successfully Logout")){
                        SaveAppData.saveOperatorLoginData(null);
                        startActivity(new Intent(VendorOrderlist.this, MainActivity.class));
                        finish();
                    }
                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                 //   Toast.makeText(VendorOrderlist.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(VendorOrderlist.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();
               }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}