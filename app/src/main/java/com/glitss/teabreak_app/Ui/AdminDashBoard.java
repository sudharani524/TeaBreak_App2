package com.glitss.teabreak_app.Ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityAdminDashBoardBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdminDashBoard extends AppCompatActivity {
    AppCompatButton vendorRegister;
    private ActivityAdminDashBoardBinding binding;
    private TeaBreakViewModel viewModel;
    List<Integer> images_list = new ArrayList<>();
    ViewPager viewPager;
    TabLayout TabLayout;
    ImageView Logout;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);
        vendorRegister=findViewById(R.id.Registration);
        viewPager=findViewById(R.id.viewPager);
        TabLayout=findViewById(R.id.slidertabs);
        Logout=findViewById(R.id.logout_btn);
        progressDialog=new ProgressDialog(AdminDashBoard.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        viewModel = ViewModelProviders.of(AdminDashBoard.this).get(TeaBreakViewModel.class);

        vendorRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashBoard.this, RegistrationForm.class));
            }
        });
        images_list.add(R.drawable.teabreak_slider);
        images_list.add(R.drawable.teabreak_slider2);
        images_list.add(R.drawable.slider12);
        images_list.add(R.drawable.teabreak_slider_img2);
        viewPager.setAdapter(new SliderAdapter(this, images_list));
        TabLayout.setupWithViewPager(viewPager);
//        binding.slidertabs.setupWithViewPager(viewPager);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 5000);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog logoutDialog = new AlertDialog.Builder(AdminDashBoard.this).setTitle("logout")
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

        viewModel.logout_api(object).observe(AdminDashBoard.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("Logout","logout"+jsonObject);


                    if (jsonObject.get("message").getAsString().equalsIgnoreCase("Successfully Logout")){
                        SaveAppData.saveOperatorLoginData(null);
                        startActivity(new Intent(AdminDashBoard.this, MainActivity.class));
                        finish();

                    }
                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    //   Toast.makeText(AccountsDashboard.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(AdminDashBoard.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });

    }
    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            AdminDashBoard.this.runOnUiThread(new Runnable() {
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