package com.glitss.teabreak_app.Ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.glitss.teabreak_app.ModelClass.LoginUserModel;
import com.glitss.teabreak_app.ModelClass.UsersRolesModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.Constant;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.ViewModel.TeaBreakViewModel;
import com.glitss.teabreak_app.databinding.ActivityMainBinding;
import com.glitss.teabreak_app.repository.ApiClient;
import com.glitss.teabreak_app.repository.ApiInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    
    private TeaBreakViewModel viewModel;
    ProgressDialog progressDialog;
    ArrayList<String> userrolename=new ArrayList<>();
    ArrayList<UsersRolesModel> usersrole_list=new ArrayList<>();
    private ActivityMainBinding binding;
    ArrayAdapter user_roles_list_adapter;
    String selected_user_role_id="";
    String FCMToken="token";
    private boolean isShowPassword = false;
    String Walletamount,app_version="",update_flag="",latest_apk_url="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(MainActivity.this).get(TeaBreakViewModel.class);

     //   checkfor_App_update();


        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        generate_fcm_token();
        users_role_api_call();

        binding.ivInvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowPassword) {
                    binding.OperatorPassword.setTransformationMethod(new PasswordTransformationMethod());

                    binding.ivInvisible.setImageDrawable(getResources().getDrawable(R.drawable.pvisible));
                    isShowPassword = false;
                }else{
                    binding.OperatorPassword.setTransformationMethod(null);
                    binding.ivInvisible.setImageDrawable(getResources().getDrawable(R.drawable.punvisible));
                    isShowPassword = true;
                }
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Objects.equals(app_version, "")){
                    try{
                        if(app_version.equalsIgnoreCase(getPackageManager().getPackageInfo(getPackageName(), 0).versionName) || update_flag.equalsIgnoreCase("1")){
                            if(binding.username.getText().toString().isEmpty()){
                                Snackbar.make(MainActivity.this,findViewById(android.R.id.content),"Please Enter Username",Snackbar.LENGTH_LONG).show();
                            } else if (binding.OperatorPassword.getText().toString().isEmpty()) {
                                Snackbar.make(MainActivity.this,findViewById(android.R.id.content),"Please Enter Username",Snackbar.LENGTH_LONG).show();
                            } else if (selected_user_role_id.equalsIgnoreCase("")) {
                                Snackbar.make(MainActivity.this,findViewById(android.R.id.content),"Please Select Role",Snackbar.LENGTH_LONG).show();
                            }else{
                                login_api_call();
                            }
                        }else{
                            alertDialogForUpdate();
                        }
                    }catch (PackageManager.NameNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });

    }


    private void checkfor_App_update() {
        Log.e("check_for_update_mthd","check for update method");
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

        // Returns an intent object that you use to check for an update.
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
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
                /*    appUpdateManager.startUpdateFlowForResult(
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


    private void alertDialogForUpdate() {

        String message = "Please update latest version from play store";

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Notification");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!Objects.equals(latest_apk_url, "")){
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(latest_apk_url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dialog.dismiss();
/*
                    String packageName = getPackageName();

                    try {
                       startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    }*/

                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }


    private void login_api_call() {

        progressDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", binding.username.getText().toString());
        jsonObject.addProperty("password", binding.OperatorPassword.getText().toString());
        jsonObject.addProperty("token", FCMToken);
        jsonObject.addProperty("role_id",selected_user_role_id );


        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> login = apiInterface.login(jsonObject);

        login.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if(response.body()==null){
                 //   Toast.makeText(MainActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(MainActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();
                    return;
                }

                if(response.body()!=null){

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body().toString());
                        String response1=jsonObj.getString("response");
                         String msg=jsonObj.getString("msg");
                        JSONObject dataobject=jsonObj.getJSONObject("data");

                      //  Toast.makeText(MainActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                        if(response1.equalsIgnoreCase("Successful") && response1!=null){
                            Log.e("Login_res",response.toString());
                         //   Toast.makeText(MainActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                            //  startActivity(new Intent(MainActivity.this,VAADashboardActivity.class));
                            LoginUserModel operatorLoginData = new Gson().fromJson(jsonObj.getJSONObject("data").toString(), new TypeToken<LoginUserModel>() {
                            }.getType());

                            SaveAppData.saveOperatorLoginData(operatorLoginData);


//                            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
//                            finish();

                           /* switch (selected_user_role_id) {
                                case "1":
                                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                    finish();
                                    break;

                                default:
                                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                    finish();
                                    break;
                            }*/

                            Log.e("Selected_role_id_login",selected_user_role_id);

                            /*switch (selected_user_role_id) {
                                case "1":
                                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                    finish();
                                    break;
                                case "3":
                                    startActivity(new Intent(MainActivity.this, VendorOrderlist.class));
                                    finish();
                                    break;
                                case "4":
                                    startActivity(new Intent(MainActivity.this, AccountsDashboard.class));
                                    finish();
                                    break;
                                default:
                                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                    finish();
                                    break;
                            }*/

                            if(SaveAppData.getLoginData().getRole_id().equalsIgnoreCase("2")){

                                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                finish();

                             /*   if(SaveAppData.getLoginData().getPassword_status().equalsIgnoreCase("1")){
                                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                    finish();
                                }else{
                                    startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                                    finish();
                                }*/

                            } else if (SaveAppData.getLoginData().getRole_id().equalsIgnoreCase("3")) {

                                startActivity(new Intent(MainActivity.this, VendorDashboardActivity.class));
                                finish();
                                /*if(SaveAppData.getLoginData().getPassword_status().equalsIgnoreCase("1")){
                                    startActivity(new Intent(MainActivity.this, VendorOrderlist.class));
                                    finish();
                                }else{
                                    startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                                    finish();
                                }*/

                            }
                            else if (SaveAppData.getLoginData().getRole_id().equalsIgnoreCase("1")) {

                                startActivity(new Intent(MainActivity.this, AdminDashBoard.class));
                                finish();

                            }
                            else if (SaveAppData.getLoginData().getRole_id().equalsIgnoreCase("4")) {
                                startActivity(new Intent(MainActivity.this, AccountsDashboard.class));
                                finish();
                            /*    if(SaveAppData.getLoginData().getPassword_status().equalsIgnoreCase("1")){
                                    startActivity(new Intent(MainActivity.this, AccountsDashboard.class));
                                    finish();
                                }else{
                                    startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                                    finish();
                                }*/

                            } else{
                                startActivity(new Intent(MainActivity.this, VendorDashboardActivity.class));
                                finish();
                               /* if(SaveAppData.getLoginData().getPassword_status().equalsIgnoreCase("1")){
                                    startActivity(new Intent(MainActivity.this, VendorOrderlist.class));
                                    finish();
                                }else{
                                    startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
                                    finish();
                                }*/
                            }

                        }
                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Snackbar.make(MainActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                users_role_api_call();
                            }
                        }).show();
                    }

                }else{
                 //   Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(MainActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                          users_role_api_call();
                        }
                    }).show();
                    /*Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout),
                            R.string.email_archived, Snackbar.LENGTH_SHORT);
                    mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
                    mySnackbar.show();*/
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
              //  Toast.makeText(MainActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                Snackbar.make(MainActivity.this,findViewById(android.R.id.content),""+t,Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void generate_fcm_token() {

        SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        FCMToken = pref.getString("FCMToken", null);

        if(FCMToken==null){
            Log.e("fcm","fcm");

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            Log.e("task","task");
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            FCMToken = task.getResult();
                            Log.e("token",FCMToken);
                            // Log and toast
                        }
                    });
        }
        SharedPreferences pref1 = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref1.edit();
        pref1.getString("FCMToken", FCMToken);
        editor.putString("FCM", FCMToken);
        Log.d("FCM","token fcm "+FCMToken);
        editor.commit();


    }

    private void users_role_api_call() {
        Log.e("role_api","role_api");
        progressDialog.show();
        usersrole_list.clear();
        userrolename.clear();
        binding.userlistSpinner.setAdapter(null);

        viewModel.get_roles_list().observe(MainActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

                if (jsonObject != null){

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("TAG","complaint_names "+jsonObject);

                    try {
                        JSONObject jsonObject1=new JSONObject(jsonObject.toString());
                        String message=jsonObject1.getString("response");
                        app_version=jsonObject1.getString("app_version");
                        update_flag=jsonObject1.getString("update_flag");
                        latest_apk_url=jsonObject1.getString("app_url");

                        //String text=jsonObject1.getString("text");

                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");


                     /*   UsersRolesModel usersRolesModel1=new UsersRolesModel();
                        usersRolesModel1.setRole_id("");
                        userrolename.add("Select");
                        usersrole_list.add(usersRolesModel1);*/


                      //  Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();

                        if(message.equalsIgnoreCase("Success")){
                            for(int i=0;i<jsonArray.length();i++){
                                UsersRolesModel usersRolesModel = new Gson().fromJson(jsonArray.get(i).toString(), new TypeToken<UsersRolesModel>() {
                                }.getType());
                                usersrole_list.add(usersRolesModel);
                                userrolename.add(usersRolesModel.getRole_full_name().toString());
                            }
                        }

                        user_roles_list_adapter=new ArrayAdapter(MainActivity.this,R.layout.spinner_text,userrolename);
                       user_roles_list_adapter.setDropDownViewResource(R.layout.spinner_text);
                       binding.userlistSpinner.setAdapter(user_roles_list_adapter);

                       binding.userlistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                           @Override
                           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                               selected_user_role_id=usersrole_list.get(position).getRole_id();
                           }

                           @Override
                           public void onNothingSelected(AdapterView<?> parent) {

                           }
                       });


                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Log.e("Exception", String.valueOf(e));
                       // Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                 //   Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Snackbar.make(MainActivity.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }

            }
        });

    }

}