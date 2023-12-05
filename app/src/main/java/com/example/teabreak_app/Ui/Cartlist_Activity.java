package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.teabreak_app.Adapter.ItemslistAdapter;
import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityCartlistBinding;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cartlist_Activity extends AppCompatActivity {

    private ActivityCartlistBinding binding;
    ProgressDialog progressDialog;
    private TeaBreakViewModel viewModel;
    ArrayList<ListItemsModel> cart_list=new ArrayList<>();
    ItemslistAdapter itemslistAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog=new ProgressDialog(Cartlist_Activity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        viewModel = ViewModelProviders.of(Cartlist_Activity.this).get(TeaBreakViewModel.class);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        binding.rvCartList.setLayoutManager(linearLayoutManager
        );

        cart_list_api_call();

    }

    private void cart_list_api_call() {

        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_cart_list(object).observe(Cartlist_Activity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    Log.d("TAG","add_cart "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");

                        String message=jsonObject1.getString("message");

                        Toast.makeText(Cartlist_Activity.this, ""+message, Toast.LENGTH_SHORT).show();
                        for(int i=0;i<jsonArray.length();i++){
                            ListItemsModel listItemsModel = new Gson().fromJson(jsonArray.get(i).toString(), new TypeToken<ListItemsModel>() {
                            }.getType());
                            cart_list.add(listItemsModel);
                        }
                        itemslistAdapter=new ItemslistAdapter(Cartlist_Activity.this, cart_list,"cart_items", new ListItemInterface() {
                            @Override
                            public void OnItemClick(int position, View v, String s) {

                            }
                        });
                        binding.rvCartList.setAdapter(itemslistAdapter);
                        itemslistAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(Cartlist_Activity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(Cartlist_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}