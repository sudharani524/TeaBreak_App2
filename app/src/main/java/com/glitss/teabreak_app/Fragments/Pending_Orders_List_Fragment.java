package com.glitss.teabreak_app.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.glitss.teabreak_app.Adapter.Accountant_Orderlist_Adapter;
import com.glitss.teabreak_app.ModelClass.OrderdetailsModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.repository.ListItemInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Pending_Orders_List_Fragment extends Fragment {

    private TeaBreakViewModel viewModel;
    Accountant_Orderlist_Adapter accountantOrderlistAdapter;
    ArrayList<OrderdetailsModel> account_order_list=new ArrayList<>();
    AppCompatButton edit_btn;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv_pending_orders_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_pending__orders__list_, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(TeaBreakViewModel.class);
        rv_pending_orders_list=root.findViewById(R.id.rvPendingListItems);

        linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_pending_orders_list.setLayoutManager(linearLayoutManager);

        pending_order_list_api_call();

        return root;

    }

    private void pending_order_list_api_call() {

        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_accountant_pending_order_details(object).observe(Pending_Orders_List_Fragment.this, new Observer<JsonObject>() {
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

                        accountantOrderlistAdapter=new Accountant_Orderlist_Adapter(getActivity(), account_order_list,"PendingList", new ListItemInterface() {
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

                                AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
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
                        rv_pending_orders_list.setAdapter(accountantOrderlistAdapter);
                        accountantOrderlistAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        Log.e("Exception", String.valueOf(e));
                    //    Toast.makeText(getActivity(), "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private void approved_api_call(int position) {
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("order_id",account_order_list.get(position).getOrder_id());
        object.addProperty("accounts_approve_status","1");

        viewModel.accountant_update_approve_order(object).observe(getActivity(), new Observer<JsonObject>() {
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
                            pending_order_list_api_call();
                            Toast.makeText(getActivity(), ""+jsonObject1.getString("text"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Exception"+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}