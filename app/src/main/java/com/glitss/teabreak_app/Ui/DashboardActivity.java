package com.glitss.teabreak_app.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.glitss.teabreak_app.Adapter.ListItemsAdapter;
import com.glitss.teabreak_app.ModelClass.ListItemsModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.Constant;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityDashboardBinding;
import com.glitss.teabreak_app.repository.ListItemInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
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
    static String wallet_amount="";
    TextView wallet_amt;

    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

//        View navHeaderView= binding.navView.getHeaderView(0);
//        TextView wallet_amount = (TextView) navHeaderView.findViewById(R.id.wallet);
//        wallet_amount.setText(""+SaveAppData.getLoginData().getWallet_amount());
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

     //   checkForAppUpdate();
     //   checkfor_App_update();


        Constant.check_token_status_api_call(DashboardActivity.this);

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            binding.newDashboard.vesrionName.setText("v "+packageInfo.versionName);
            binding.versionName.setText("Version: "+packageInfo.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            // throw new RuntimeException(e);
            Log.e("Excption", String.valueOf(e));
          //  Toast.makeText(this, "version_name"+e, Toast.LENGTH_SHORT).show();

        }

        viewModel = ViewModelProviders.of(DashboardActivity.this).get(TeaBreakViewModel.class);
        list_items_api_call();

        wallet_amount_api_call();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);


        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        binding.newDashboarddd.rvListItems.setLayoutManager(gridLayoutManager);

        images_list.add(R.drawable.teabreak_slider);
        images_list.add(R.drawable.teabreak_slider2);
        images_list.add(R.drawable.slider12);
        images_list.add(R.drawable.teabreak_slider_img2);

       /* images_list.add(R.drawable.img);
        images_list.add(R.drawable.img_1);*/


        binding.newDashboarddd.viewPager.setAdapter(new SliderAdapter(this, images_list));
        binding.newDashboarddd.tabs.setupWithViewPager(binding.newDashboarddd.viewPager);


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 5000);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle((Activity) this, binding.drawerLayout, (Toolbar)binding.toolbar, R.string.nav_open, R.string.nav_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wallet_amount_api_call();
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
        TextView outlet_code = (TextView) navHeaderView.findViewById(R.id.tv_outlet_code);
        wallet_amt=(TextView) navHeaderView.findViewById(R.id.wallet_amt);
        wallet_amt.setText("₹"+wallet_amount);
        Log.e("wallet_amounttttttt",wallet_amount);
        nav_name.setText(SaveAppData.getLoginData().getName());
        outlet_code.setText(SaveAppData.getLoginData().getOutlet_code());

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

                if (id == R.id.orders){
                    startActivity(new Intent(DashboardActivity.this,Orders_List_Activity.class));
                }
                else if (id == R.id.cart) {
                    startActivity(new Intent(DashboardActivity.this, Cartlist_Activity.class));
               //     startActivity(new Intent(DashboardActivity.this, MerchantCheckoutActivity.class));
                }
                else if(id ==R.id.logout){
                    AlertDialog logoutDialog = new AlertDialog.Builder(DashboardActivity.this).setTitle("logout")
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

                return true;
            }
        });

    }

    private void checkfor_App_update() {
        Log.e("check_for_update_mthd","check for update method");
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(DashboardActivity.this);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                try {

                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // an activity result launcher registered via registerForActivityResult
                            AppUpdateType.IMMEDIATE,
                            // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                            // flexible updates.
                            this,101);
                  /*  appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // an activity result launcher registered via registerForActivityResult
                            activityResultLauncher,
                            // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                            // flexible updates.
                            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                                    .setAllowAssetPackDeletion(true)
                                    .build());*/

                } catch (IntentSender.SendIntentException e) {
                    Log.e("Exception","Exception");
                    throw new RuntimeException(e);

                    // Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void wallet_amount_api_call() {

        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());

        viewModel.get_wallet_amt(object).observe(DashboardActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){
                    Log.d("wallet_api_method","wallet_api "+jsonObject);
                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        //String message=jsonObject1.getString("message");
                        wallet_amount=jsonObject1.getJSONObject("data").getString("wallet_amount");
                        Log.e("wallet_amt_money",wallet_amount);
                        wallet_amt.setText("₹"+wallet_amount);


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                       // Toast.makeText(DashboardActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                 //   Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(DashboardActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }



    private void list_items_api_call() {

        progressDialog.show();
        viewModel.get_list_items().observe(DashboardActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (jsonObject != null){


                    Log.d("TAG","complaint_names "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        String text=jsonObject1.getString("text");

                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");

                    //    Toast.makeText(DashboardActivity.this, ""+text, Toast.LENGTH_SHORT).show();

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
                                    intent.putExtra("line_item_id",list.get(position).getLine_item_id());
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
                        Log.e("Excption", String.valueOf(e));
                        //Toast.makeText(DashboardActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }

                }else{
                //    Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(DashboardActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode != RESULT_OK) {
                // Log an error
                unregisterInstallStateUpdListener();
            }
        }
    }



    private void add_cart_api_call() {
        progressDialog.show();
        JsonObject object = new JsonObject();

        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("line_item_id",selected_line_item_id);
        object.addProperty("quantity","1");
        object.addProperty("price",selected_price);

        viewModel.add_cart_api(object).observe(DashboardActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if (jsonObject != null){

                    Log.d("TAG","add_cart "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("message");
                        String text=jsonObject1.getString("text");

                     //   Toast.makeText(DashboardActivity.this, ""+text, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                     //   Toast.makeText(DashboardActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{

                 //   Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(DashboardActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
          binding.drawerLayout.close();

        }
        else if (id == R.id.orders){
            startActivity(new Intent(DashboardActivity.this, Orders_List_Activity.class));
            //finish();
        }
        else if(id==R.id.transaction_history){
            startActivity(new Intent(DashboardActivity.this,TransactionHistoryActivity.class));
        }

        else if (id == R.id.wallet) {
            startActivity(new Intent(DashboardActivity.this, Mywallet.class));
        //    finish();

        } else if (id == R.id.cart) {
            startActivity(new Intent(DashboardActivity.this, Cartlist_Activity.class));
          //  finish();
        }
        else if(id==R.id.change_pswd){
            startActivity(new Intent(DashboardActivity.this,ChangePasswordActivity.class));
            //finish();

        }
        else if(id ==R.id.logout){
            AlertDialog logoutDialog = new AlertDialog.Builder(DashboardActivity.this).setTitle("logout")
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
                 //   Toast.makeText(DashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(DashboardActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Constant.check_token_status_api_call(DashboardActivity.this);
        wallet_amount_api_call();
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



    private void checkForAppUpdate() {
        // Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(DashboardActivity.this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister();
            }
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    // Request the update.
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                        // Before starting an update, register a listener for updates.
                        appUpdateManager.registerListener(installStateUpdatedListener);
                        // Start an update.
                        DashboardActivity.this.startAppUpdateFlexible(appUpdateInfo);
                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Start an update.
                        DashboardActivity.this.startAppUpdateImmediate(appUpdateInfo);
                    }
                }
            }
        });
    }


    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    101);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    101);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    private void popupSnackbarForCompleteUpdateAndUnregister() {
        //  Snackbar snackbar = Snackbar.make(drawer, getString("update downloaded"), Snackbar.LENGTH_INDEFINITE);

        Snackbar snackbar = Snackbar.make(DashboardActivity.this,findViewById(android.R.id.content),"Update Downloaded",Snackbar.LENGTH_LONG);
        snackbar.setAction("Restart", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.black));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }



}