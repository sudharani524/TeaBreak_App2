package com.glitss.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityVendorDashboardBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VendorDashboardActivity extends AppCompatActivity {

    private ActivityVendorDashboardBinding binding;
    List<Integer> images_list = new ArrayList<>();
    private TeaBreakViewModel viewModel;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVendorDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel= ViewModelProviders.of(VendorDashboardActivity.this).get(TeaBreakViewModel.class);

        images_list.add(R.drawable.teabreak_slider);
        images_list.add(R.drawable.teabreak_slider2);
        images_list.add(R.drawable.slider12);
        images_list.add(R.drawable.teabreak_slider_img2);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        binding.viewPager.setAdapter(new SliderAdapter(this, images_list));
        binding.tabs.setupWithViewPager(binding.viewPager);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),3000,5000);


        binding.pendingOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendorDashboardActivity.this,VendorOrderlist.class));
            }
        });


        binding.closedOrdersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendorDashboardActivity.this,ClosedOrdersListActivity.class));
            }
        });


        binding.changePswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendorDashboardActivity.this,ChangePasswordActivity.class));
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_api_call();
            }
        });


    }

    private void logout_api_call() {
        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.logout_api(object).observe(VendorDashboardActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("Logout","logout"+jsonObject);


                    if (jsonObject.get("message").getAsString().equalsIgnoreCase("Successfully Logout")){
                        SaveAppData.saveOperatorLoginData(null);
                        startActivity(new Intent(VendorDashboardActivity.this, MainActivity.class));
                        finish();
                    }
                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    //   Toast.makeText(VendorOrderlist.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(VendorDashboardActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();
                }

            }
        });

    }


    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            VendorDashboardActivity.this.runOnUiThread(new Runnable() {
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