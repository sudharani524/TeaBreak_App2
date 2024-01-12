package com.example.teabreak_app.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.teabreak_app.R;
import com.example.teabreak_app.databinding.ActivityMywalletBinding;
import com.google.android.material.tabs.TabLayout;

public class Mywallet extends AppCompatActivity {

    CreditedListFragment CreditedListFragment;
    DebitedlistFragment DebitedlistFragment;

    ViewPager viewPager;
    private ActivityMywalletBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
        binding = ActivityMywalletBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(binding.getRoot());
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });
    }


    private static class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CreditedListFragment();
                case 1:
                    return new DebitedlistFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Credited List";
                case 1:
                    return "Debited List";
                default:
                    return null;
            }
        }
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}