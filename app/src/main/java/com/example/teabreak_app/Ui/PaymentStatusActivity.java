package com.example.teabreak_app.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.teabreak_app.R;

public class PaymentStatusActivity extends AppCompatActivity {

//    @BindView(R.id.textView1)
    TextView txt_order_status;
//    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Intent mIntent;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_payment_status);
//        ButterKnife.bind(this);
        mIntent = getIntent();

        toolbar = findViewById(R.id.toolbar);
        txt_order_status = findViewById(R.id.textView1);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_order_status.setText(mIntent.getStringExtra("transStatus"));

    }

} 