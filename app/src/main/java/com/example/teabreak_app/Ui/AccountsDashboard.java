package com.example.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.teabreak_app.Adapter.Accountant_Orderlist_Adapter;
import com.example.teabreak_app.Adapter.Orderdetailsadapter;
import com.example.teabreak_app.Fragments.Approved_Order_List_Fragment;
import com.example.teabreak_app.Fragments.Pending_Orders_List_Fragment;
import com.example.teabreak_app.ModelClass.OrderdetailsModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityAccountsDashboardBinding;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AccountsDashboard extends AppCompatActivity {

    private ActivityAccountsDashboardBinding binding;
    LinearLayoutManager linearLayoutManager;
    private TeaBreakViewModel viewModel;
    ArrayList<OrderdetailsModel> account_order_list=new ArrayList<>();
    Accountant_Orderlist_Adapter accountantOrderlistAdapter;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    AppCompatButton edit_btn;
    ViewPager viewPager;
    List<Integer> images_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAccountsDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(AccountsDashboard.this).get(TeaBreakViewModel.class);

        progressDialog=new ProgressDialog(AccountsDashboard.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);

/*

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
*/

        images_list.add(R.drawable.slider_img);
        images_list.add(R.drawable.img);
        images_list.add(R.drawable.img_1);


        binding.viewPager.setAdapter(new SliderAdapter(this, images_list));
        binding.tabs.setupWithViewPager(binding.viewPager);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 5000);


        // binding.rvListItems.setLayoutManager(linearLayoutManager);


        binding.pendingOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("pending","pending");
                startActivity(new Intent(AccountsDashboard.this,Pending_order_Activity.class));
            }
        });


        binding.approvedOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountsDashboard.this,Approved_order_Activity.class));
            }
        });

         binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AlertDialog logoutDialog = new AlertDialog.Builder(AccountsDashboard.this).setTitle("logout")
                         .setMessage("Are you sure you want to logout")
                         .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                                 logout_api_call();
                             }
                         })
                         .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which) {
                                 dialog.dismiss();
                             }
                         })
                         .setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false).show();
             }
         });

         
    }

    private void logout_api_call() {
        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.logout_api(object).observe(AccountsDashboard.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("Logout","logout"+jsonObject);


                    if (jsonObject.get("message").getAsString().equalsIgnoreCase("Successfully Logout")){
                        SaveAppData.saveOperatorLoginData(null);
                        startActivity(new Intent(AccountsDashboard.this, MainActivity.class));
                        finish();

                    }
                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                 //   Toast.makeText(AccountsDashboard.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(AccountsDashboard.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });

    }

    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            AccountsDashboard.this.runOnUiThread(new Runnable() {
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

  /*  private static class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Pending_Orders_List_Fragment();
                case 1:
                    return new Approved_Order_List_Fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Pending List";
                case 1:
                    return "Approved List";
                default:
                    return null;
            }
        }
    }*/


}