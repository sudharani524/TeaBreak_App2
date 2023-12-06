package com.example.teabreak_app.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teabreak_app.Adapter.ListItemsAdapter;
import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityDashboardBinding;
import com.example.teabreak_app.databinding.ActivityMainBinding;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityDashboardBinding binding;
    ProgressDialog progressDialog;
    List<Integer> images_list = new ArrayList<>();
    private TeaBreakViewModel viewModel;
    ListItemsAdapter listItemsAdapter;

    ArrayList<ListItemsModel> list=new ArrayList<>();
    String selected_line_item_id="",selected_price="",selected_qty="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


        if (ContextCompat.checkSelfPermission(DashboardActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)  {
            // Give first an explanation, if needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this,
                    Manifest.permission.READ_MEDIA_IMAGES)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(DashboardActivity.this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        1);
            }
        }


        viewModel = ViewModelProviders.of(DashboardActivity.this).get(TeaBreakViewModel.class);
        list_items_api_call();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);



        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,RecyclerView.HORIZONTAL,false);
        binding.newDashboarddd.rvListItems.setLayoutManager(gridLayoutManager);

        images_list.add(R.drawable.slider_img);
        images_list.add(R.drawable.img);
        images_list.add(R.drawable.img_1);


        binding.newDashboarddd.viewPager.setAdapter(new SliderAdapter(this, images_list));
        binding.newDashboarddd.tabs.setupWithViewPager(binding.newDashboarddd.viewPager);

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
        binding.newDashboarddd.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, ListItems_Activity.class));

            }
        });
        binding.newDashboarddd.bottomNavigation.setSelectedItemId(R.id.home);
        binding.newDashboarddd.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                    startActivity(new Intent(DashboardActivity.this, Cartlist_Activity.class));

                }
                else if(id ==R.id.logout){
                    AlertDialog logoutDialog = new AlertDialog.Builder(DashboardActivity.this).setTitle("logout")
                            .setMessage("Are you really want to logout")
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

                return true;
            }
        });



    }

    private void list_items_api_call() {

        viewModel.get_list_items().observe(DashboardActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    Log.d("TAG","complaint_names "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        String text=jsonObject1.getString("text");

                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");


                        Toast.makeText(DashboardActivity.this, ""+text, Toast.LENGTH_SHORT).show();

                        if(message.equalsIgnoreCase("success")){
                            for(int i=0;i<jsonArray.length();i++){
                                ListItemsModel listItemsModel = new Gson().fromJson(jsonArray.get(i).toString(), new TypeToken<ListItemsModel>() {
                                }.getType());
                                list.add(listItemsModel);
                            }
                        }

                       // listItemsAdapter=new ListItemsAdapter(DashboardActivity.this,list);
                        listItemsAdapter=new ListItemsAdapter(DashboardActivity.this, list, new ListItemInterface() {
                            @Override
                            public void OnItemClick(int position, View v,String s) {
                                selected_line_item_id=list.get(position).getLine_item_id();
                                selected_price=list.get(position).getPrice();
                                selected_qty=list.get(position).getPack_of_qty();
                                if(s.equalsIgnoreCase("card")){
                                    Intent intent=new Intent(DashboardActivity.this, SingleList_Item.class);
                                    intent.putExtra("img",list.get(position).getImage());
                                    intent.putExtra("name",list.get(position).getLine_item_name());
                                    intent.putExtra("price",list.get(position).getPrice());
                                    intent.putExtra("qty",list.get(position).getPack_of_qty());
                                    startActivity(intent);
                                }

                                if(s.equalsIgnoreCase("cart")){
                                    //startActivity(new Intent(DashboardActivity.this,Cartlist_Activity.class));
                                    add_cart_api_call();
                                }

                            }
                        });
                        binding.newDashboarddd.rvListItems.setAdapter(listItemsAdapter);
                        listItemsAdapter.notifyDataSetChanged();






                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(DashboardActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void add_cart_api_call() {
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("line_item_id",selected_line_item_id);
        object.addProperty("quantity",selected_qty);
        object.addProperty("price",selected_price);

        viewModel.add_cart_api(object).observe(DashboardActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    Log.d("TAG","add_cart "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        String text=jsonObject1.getString("text");

                        Toast.makeText(DashboardActivity.this, ""+text, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Toast.makeText(DashboardActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                    Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

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
            startActivity(new Intent(DashboardActivity.this, Cartlist_Activity.class));

        }else if (id == R.id.AboutUs) {
            startActivity(new Intent(DashboardActivity.this, Aboutus.class));
        }
        else if(id == R.id.Faq){
            startActivity(new Intent(DashboardActivity.this, Faqs.class));
        }
        else if(id ==R.id.logout){
            AlertDialog logoutDialog = new AlertDialog.Builder(DashboardActivity.this).setTitle("logout")
                    .setMessage("Are you really want to logout")
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

        return true;

    }

    private void logout_api_call() {
        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.logout_api(object).observe(DashboardActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("Logout","logout"+jsonObject);


                    if (jsonObject.get("message").getAsString().equalsIgnoreCase("Successfully Logout")){
                        SaveAppData.saveOperatorLoginData(null);
                        startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                        finish();
                    }
                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            DashboardActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (binding.newDashboarddd.viewPager.getCurrentItem() < images_list.size() - 1) {
                        binding.newDashboarddd.viewPager.setCurrentItem(binding.newDashboarddd.viewPager.getCurrentItem() + 1);
                    } else {
                        binding.newDashboarddd.viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
}