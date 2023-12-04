package com.example.teabreak_app.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.teabreak_app.R;
import com.example.teabreak_app.databinding.ActivityDashboardBinding;
import com.example.teabreak_app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityDashboardBinding binding;
    ProgressDialog progressDialog;
    List<Integer> images_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        images_list.add(R.drawable.img);
        images_list.add(R.drawable.img_1);
        images_list.add(R.drawable.tea_break_img);
        binding.viewPager.setAdapter(new SliderAdapter(this, images_list));
        binding.tabs.setupWithViewPager(binding.viewPager);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 5000);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle((Activity) this, binding.drawerLayout, (Toolbar)binding.toolbar, R.string.nav_open, R.string.nav_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

       toggle.setHomeAsUpIndicator(R.drawable.menu);
       binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        binding.navView.setNavigationItemSelectedListener(DashboardActivity.this);
        binding.navView.setItemIconTintList(null);
        View navHeaderView = binding.navView.getHeaderView(0);
        TextView nav_name = (TextView) navHeaderView.findViewById(R.id.nav_name);
        binding.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, ListItems_Activity.class));

            }
        });
       binding.bottomNavigation.setSelectedItemId(R.id.home);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.home) {
                    startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                }
                else if (id == R.id.orders){
                    startActivity(new Intent(DashboardActivity.this, Myorders.class));

                }
                else if (id == R.id.cart) {
                    startActivity(new Intent(DashboardActivity.this, Cart.class));

                }
                else if(id ==R.id.logout){

                }

                return true;
            }
        });



    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
        }
        else if (id == R.id.orders){
            startActivity(new Intent(DashboardActivity.this, Myorders.class));

        }
        else if (id == R.id.wallet) {
            startActivity(new Intent(DashboardActivity.this, Mywallet.class));
        } else if (id == R.id.cart) {
            startActivity(new Intent(DashboardActivity.this, Cart.class));

        }else if (id == R.id.AboutUs) {
            startActivity(new Intent(DashboardActivity.this, Aboutus.class));
        }
        else if(id == R.id.Faq){
            startActivity(new Intent(DashboardActivity.this, Faqs.class));
        }
        else if(id ==R.id.logout){

        }

        return true;

    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            DashboardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (binding.viewPager.getCurrentItem() < images_list.size() - 1) {
                        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                    } else {
                        binding.viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}