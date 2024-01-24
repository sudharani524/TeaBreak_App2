package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.teabreak_app.R;
import com.example.teabreak_app.databinding.ActivityStatusBinding;

public class StatusActivity extends AppCompatActivity {

    String orderno,tracking_id,trans_date,billing_name,amount,bank_ref_no,order_status;
    private ActivityStatusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

     /*   setSupportActionBar(binding.tBar.toolbar);
        binding.tBar.tlbarTitle.setText("Status Activitym");*/

        tracking_id=getIntent().getStringExtra("tracking_id");
        trans_date=getIntent().getStringExtra("trans_date");
        billing_name=getIntent().getStringExtra("billing_name");
        amount=getIntent().getStringExtra("amount");
        bank_ref_no=getIntent().getStringExtra("bank_ref_no");
        order_status=getIntent().getStringExtra("order_status");
        orderno=getIntent().getStringExtra("orderno");

        Log.e("tracking_id",tracking_id);
        Log.e("trans_date",trans_date);
        Log.e("billing_name",billing_name);
        Log.e("amount",amount);
        Log.e("bank_ref_no",bank_ref_no);
        Log.e("order_status",order_status);
        Log.e("orderno",orderno);


        binding.redirectScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatusActivity.this,DashboardActivity.class));
                finish();
            }
        });

        binding.tvOrderNum.setText(orderno);
        binding.tvTransactionId.setText(tracking_id);
        binding.tvBillingName.setText(billing_name);
        binding.tvAmount.setText(amount);
        binding.tvBankReferenceNo.setText(bank_ref_no);
        binding.tvStatus.setText(order_status);



    }
}