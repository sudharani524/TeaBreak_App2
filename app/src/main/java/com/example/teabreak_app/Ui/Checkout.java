package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.teabreak_app.R;
import com.example.teabreak_app.databinding.ActivityCheckoutBinding;
import com.example.teabreak_app.databinding.ActivityListItemsBinding;

public class Checkout extends AppCompatActivity {
    private ActivityCheckoutBinding binding;
    LinearLayout paymentdetails;
    ImageView close_btn;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        binding=ActivityCheckoutBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        binding.Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(Checkout.this);
                View view_alert= LayoutInflater.from(Checkout.this).inflate(R.layout.paymentdetails,null);
                paymentdetails=view_alert.findViewById(R.id.paymentdetails);
                close_btn=view_alert.findViewById(R.id.close_btn);
                dialog.setView(view_alert);
                dialog.setCancelable(true);
                alertDialog = dialog.create();
                alertDialog.show();
            }
        });

    }
}