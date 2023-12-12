package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.databinding.ActivitySingleListItemBinding;
import com.squareup.picasso.Picasso;

public class SingleList_Item extends AppCompatActivity {
    String img,name,price,qty;
    private ActivitySingleListItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySingleListItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        img=getIntent().getStringExtra("img");
        name=getIntent().getStringExtra("name");
        price=getIntent().getStringExtra("price");
        qty=getIntent().getStringExtra("qty");

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
                startActivity(new Intent(SingleList_Item.this,Cartlist_Activity.class));
            }
        });

    }
}