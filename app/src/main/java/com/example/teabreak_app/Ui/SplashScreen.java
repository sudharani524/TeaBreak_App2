package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                if(SaveAppData.getLoginData()!=null){
                    startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}