package com.glitss.teabreak_app.Ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.glitss.teabreak_app.Adapter.CreditedListAdapter;
import com.glitss.teabreak_app.ModelClass.WallethistoryModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DebitedlistFragment extends Fragment {

    TeaBreakViewModel viewModel;
    RecyclerView Debited_wallet;
    ArrayList<WallethistoryModel> Debited_list_array = new ArrayList<>();

    CreditedListAdapter creditedListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_debited, container, false);
        Debited_wallet = root.findViewById(R.id.Debitedamount);
        viewModel = ViewModelProviders.of(this).get(TeaBreakViewModel.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        Debited_wallet.setLayoutManager(linearLayoutManager);
        Debited_wallet_api_call();
        return root;
    }

    private void Debited_wallet_api_call() {
        JsonObject object = new JsonObject();
        Log.d("Debited_wallet",object.toString());
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("type","0");
        viewModel.get_wallet_history(object).observe((LifecycleOwner) getContext(), new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                    if (jsonObject != null){
                        Log.d("orders","Text"+jsonObject);
                        try {
                            JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                            JSONArray jsonArray=new JSONArray();
                            jsonArray=jsonObject1.getJSONArray("data");
                            Log.d("dataorder",jsonArray.toString());
                            for(int i=0;i<jsonArray.length();i++){
                                Log.d("forloop","loop");
                                WallethistoryModel wallethistoryModel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<WallethistoryModel>() {
                                }.getType());
                                Debited_list_array.add(wallethistoryModel);
                                Log.d("list",String.valueOf(Debited_list_array.size()));
                            }
                            Log.d("list2",String.valueOf(Debited_list_array.size()));
                            creditedListAdapter=new CreditedListAdapter(getContext(), Debited_list_array, DebitedlistFragment.this);
                            Debited_wallet.setAdapter(creditedListAdapter);
                            creditedListAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.e("Exception", String.valueOf(e));
                       //     Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
                        }


                    }else{

                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

        });

    }
}