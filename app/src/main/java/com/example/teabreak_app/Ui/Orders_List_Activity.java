package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.teabreak_app.R;
import com.example.teabreak_app.databinding.ActivityListItemsBinding;
import com.example.teabreak_app.databinding.ActivityOrdersListBinding;

public class Orders_List_Activity extends AppCompatActivity {
    private ActivityOrdersListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityOrdersListBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        binding.ordercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Orders_List_Activity.this, Orderdetails.class));

            }
        });


    }
}