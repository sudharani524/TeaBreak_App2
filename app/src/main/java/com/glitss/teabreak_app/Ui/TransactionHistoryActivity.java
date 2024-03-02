package com.glitss.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.glitss.teabreak_app.Adapter.OrderHistoryAdapter;
import com.glitss.teabreak_app.Adapter.TransactionHistoyAdapter;
import com.glitss.teabreak_app.ModelClass.OrderHistoryModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityTransactionHistoryBinding;
import com.glitss.teabreak_app.repository.OrderdetailsInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionHistoryActivity extends AppCompatActivity {

    private ActivityTransactionHistoryBinding binding;

    ProgressDialog progressDialog;
    ArrayList<OrderHistoryModel> list=new ArrayList<>();
    private TeaBreakViewModel viewModel;
    TransactionHistoyAdapter transactionHistoyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTransactionHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(TransactionHistoryActivity.this).get(TeaBreakViewModel.class);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvTransactionHistory.setLayoutManager(linearLayoutManager);

        transaction_history_api_call();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void transaction_history_api_call() {
        list.clear();
        JsonObject object = new JsonObject();
        Log.d("orderlistapicall",object.toString());
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        viewModel.get_transaction_history(object).observe(TransactionHistoryActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (jsonObject != null){
                    Log.d("orders","Text"+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");
                        String message=jsonObject1.getString("message");

                        if(message.equalsIgnoreCase("No Order List")){
//                            Snackbar.make(Orders_List_Activity.this,findViewById(android.R.id.content),"No Order List",Snackbar.LENGTH_LONG).show();

                            binding.tvNoDataFound.setVisibility(View.VISIBLE);
                            binding.rvTransactionHistory.setVisibility(View.GONE);
                            binding.tvNoDataFound.setText("No Data Found");
                        }
                        else{
                            binding.tvNoDataFound.setVisibility(View.GONE);
                            binding.rvTransactionHistory.setVisibility(View.VISIBLE);
                        }

                        Log.d("dataorder",jsonArray.toString());
                        for(int i=0;i<jsonArray.length();i++){
                            OrderHistoryModel orderHistoryModel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<OrderHistoryModel>() {
                            }.getType());
                            list.add(orderHistoryModel);
                          //  Delivery=list.get(i).getDelivery_type_name();
                        }


                        Log.d("list2",String.valueOf(list.size()));
                       transactionHistoyAdapter=new TransactionHistoyAdapter(TransactionHistoryActivity.this,list);
                        binding.rvTransactionHistory.setAdapter(transactionHistoyAdapter);
                        transactionHistoyAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("Exception", String.valueOf(e));
                        //      Toast.makeText(Orders_List_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }

                }else{

                    //    Toast.makeText(Orders_List_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(TransactionHistoryActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }
            }
        });
    }
}