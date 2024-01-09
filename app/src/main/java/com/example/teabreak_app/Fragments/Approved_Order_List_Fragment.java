package com.example.teabreak_app.Fragments;

import android.os.Bundle;

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

import com.example.teabreak_app.Adapter.Accountant_Orderlist_Adapter;
import com.example.teabreak_app.ModelClass.OrderdetailsModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Approved_Order_List_Fragment extends Fragment {

    RecyclerView rv_pending_orders_list;
    private TeaBreakViewModel viewModel;
    LinearLayoutManager linearLayoutManager;
    Accountant_Orderlist_Adapter accountantOrderlistAdapter;
    ArrayList<OrderdetailsModel> account_order_list=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_approved__order__list_, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(TeaBreakViewModel.class);
        rv_pending_orders_list=root.findViewById(R.id.rvapprovedListItems);

        linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_pending_orders_list.setLayoutManager(linearLayoutManager);

        approved_order_list_api_call();

        return root;
    }

    private void approved_order_list_api_call() {

        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_accountant_approved_order_details(object).observe(getActivity(), new Observer<JsonObject>() {
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

                        accountantOrderlistAdapter=new Accountant_Orderlist_Adapter(getActivity(), account_order_list,"ApprovedList", new ListItemInterface() {
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
                        rv_pending_orders_list.setAdapter(accountantOrderlistAdapter);
                        accountantOrderlistAdapter.notifyDataSetChanged();


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