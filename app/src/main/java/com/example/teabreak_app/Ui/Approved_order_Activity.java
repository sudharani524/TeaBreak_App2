package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.teabreak_app.Adapter.Accountant_Orderlist_Adapter;
import com.example.teabreak_app.ModelClass.OrderdetailsModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityApprovedOrderBinding;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Approved_order_Activity extends AppCompatActivity {
    RecyclerView rv_pending_orders_list;
    private TeaBreakViewModel viewModel;
    LinearLayoutManager linearLayoutManager;
    Accountant_Orderlist_Adapter accountantOrderlistAdapter;
    ArrayList<OrderdetailsModel> account_order_list=new ArrayList<>();

    private ActivityApprovedOrderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityApprovedOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(Approved_order_Activity.this).get(TeaBreakViewModel.class);


        linearLayoutManager=new LinearLayoutManager(Approved_order_Activity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvapprovedListItems.setLayoutManager(linearLayoutManager);

        approved_order_list_api_call();

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

    private void approved_order_list_api_call() {

        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_accountant_approved_order_details(object).observe(Approved_order_Activity.this, new Observer<JsonObject>() {
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
                        }

                        Log.d("orderdetailslist",String.valueOf(account_order_list.size()));

                        accountantOrderlistAdapter=new Accountant_Orderlist_Adapter(Approved_order_Activity.this, account_order_list,"ApprovedList", new ListItemInterface() {
                            @Override
                            public void OnItemClick(int position, View v, String s) {
                               /* AlertDialog.Builder dialog=new AlertDialog.Builder(AccountsDashboard.this);
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
                                alertDialog.show();*/

                            }
                        });
                        binding.rvapprovedListItems.setAdapter(accountantOrderlistAdapter);
                        accountantOrderlistAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        Log.e("Exception", String.valueOf(e));
                        //    Toast.makeText(getActivity(), "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                  //  Toast.makeText(Approved_order_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(Approved_order_Activity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }
            }
        });

    }
}