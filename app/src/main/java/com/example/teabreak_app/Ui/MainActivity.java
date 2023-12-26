package com.example.teabreak_app.Ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.teabreak_app.Adapter.ListItemsAdapter;
import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.ModelClass.LoginUserModel;
import com.example.teabreak_app.ModelClass.UsersRolesModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.Constant;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityMainBinding;
import com.example.teabreak_app.repository.ApiClient;
import com.example.teabreak_app.repository.ApiInterface;
import com.example.teabreak_app.repository.ListItemInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    String Walletamount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(MainActivity.this).get(TeaBreakViewModel.class);
        
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
               if(binding.username.getText().toString().isEmpty()){
                   Snackbar.make(MainActivity.this,findViewById(android.R.id.content),"Please Enter Username",Snackbar.LENGTH_LONG).show();
               } else if (binding.OperatorPassword.getText().toString().isEmpty()) {
                   Snackbar.make(MainActivity.this,findViewById(android.R.id.content),"Please Enter Username",Snackbar.LENGTH_LONG).show();
               } else if (selected_user_role_id.equalsIgnoreCase("")) {
                   Snackbar.make(MainActivity.this,findViewById(android.R.id.content),"Please Select Role",Snackbar.LENGTH_LONG).show();
               }else{
                   login_api_call();
               }
            }
        });


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
                    Toast.makeText(MainActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.body()!=null){

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body().toString());
                        String response1=jsonObj.getString("response");
                         String msg=jsonObj.getString("msg");
                        JSONObject dataobject=jsonObj.getJSONObject("data");

                        Toast.makeText(MainActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                        if(response1.equalsIgnoreCase("Successful") && response1!=null){
                            Log.e("Login_res",response.toString());
                            Toast.makeText(MainActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
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
                            switch (selected_user_role_id) {
                                case "1":
                                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                    finish();
                                    break;
                                case "3":
                                    startActivity(new Intent(MainActivity.this, VendorOrderlist.class));
                                    finish();
                                    break;
                                default:
                                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                                    finish();
                                    break;
                            }


                        }
                    } catch (JSONException e) {
                        //throw new RuntimeException(e);
                        Snackbar.make(MainActivity.this,findViewById(android.R.id.content),""+e,Snackbar.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(MainActivity.this, ""+t, Toast.LENGTH_SHORT).show();
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
                        //String text=jsonObject1.getString("text");

                        JSONArray jsonArray=new JSONArray();
                        jsonArray=jsonObject1.getJSONArray("data");


                        UsersRolesModel usersRolesModel1=new UsersRolesModel();
                        usersRolesModel1.setRole_id("");
                        userrolename.add("Select");
                        usersrole_list.add(usersRolesModel1);


                        Toast.makeText(MainActivity.this, ""+message, Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }


                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}