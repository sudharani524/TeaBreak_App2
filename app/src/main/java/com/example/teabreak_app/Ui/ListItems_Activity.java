package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.teabreak_app.R;
import com.example.teabreak_app.databinding.ActivityDashboardBinding;
import com.example.teabreak_app.databinding.ActivityListItemsBinding;

public class ListItems_Activity extends AppCompatActivity {
    private ActivityListItemsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityListItemsBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        binding.Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListItems_Activity.this, Checkout.class));

            }
        });

    }
}