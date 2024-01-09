package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivitySingleListItemBinding;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleList_Item extends AppCompatActivity {
    String img,name,price,qty,line_item_id;
    private ActivitySingleListItemBinding binding;
    private TeaBreakViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySingleListItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(SingleList_Item.this).get(TeaBreakViewModel.class);
        img=getIntent().getStringExtra("img");
        name=getIntent().getStringExtra("name");
        price=getIntent().getStringExtra("price");
        qty=getIntent().getStringExtra("qty");
        line_item_id=getIntent().getStringExtra("line_item_id");

        binding.singleItemName.setText(name);
        binding.singleItemPrice.setText("₹"+price);
        binding.singleItemQty.setText(qty);

        String img2= Constant.SERVER_BASE_URL+img;
//        Log.e("img",img);
        Picasso.get().load(img2).fit().centerInside().into(binding.singleListImg);
        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SingleList_Item.this,DashboardActivity.class));
            }
        });
        binding.Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    startActivity(new Intent(SingleList_Item.this,Cartlist_Activity.class));
                add_cart_api_call();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SingleList_Item.this,DashboardActivity.class));
                finish();
            }
        });

    }


    private void add_cart_api_call() {
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("line_item_id",line_item_id);
        object.addProperty("quantity",qty);
        object.addProperty("price",price);

        viewModel.add_cart_api(object).observe(SingleList_Item.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    Log.d("TAG","add_cart "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        Log.e("message",message);
                        String text=jsonObject1.getString("text");

                        Toast.makeText(SingleList_Item.this, ""+text, Toast.LENGTH_SHORT).show();
                        if(message.equalsIgnoreCase("Success")){
                           startActivity(new Intent(SingleList_Item.this,Cartlist_Activity.class));
                           finish();
                        }


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                       // Toast.makeText(SingleList_Item.this, ""+e, Toast.LENGTH_SHORT).show();
                        Log.e("Exception","Single_line_item"+e);
                    }


                }else{

                    Toast.makeText(SingleList_Item.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}