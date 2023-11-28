package com.example.teabreak_app.Ui;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.teabreak_app.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    ProgressDialog progressDialog;
    List<Integer> images_list = new ArrayList<>();
    TabLayout tabLayout;
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabs);
        view=findViewById(R.id.view);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        images_list.add(R.drawable.img);
        images_list.add(R.drawable.img_1);
        images_list.add(R.drawable.tea_break_img);
        viewPager.setAdapter(new SliderAdapter(this, images_list));
        tabLayout.setupWithViewPager(viewPager);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 5000);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle((Activity) this, drawerLayout, (Toolbar) toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, ListItems_Activity.class));

            }
        });


    }
    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            DashboardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < images_list.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}