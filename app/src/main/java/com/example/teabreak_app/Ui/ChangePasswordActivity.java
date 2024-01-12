package com.example.teabreak_app.Ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.teabreak_app.R;
import com.example.teabreak_app.Utils.SaveAppData;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityChangePasswordBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    ProgressDialog progressDialog;
    TeaBreakViewModel viewModel;
    private boolean isShowPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewModel = ViewModelProviders.of(this).get(TeaBreakViewModel.class);
        binding.ivVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowPassword) {
                    binding.tvOldpassword.setTransformationMethod(new PasswordTransformationMethod());

                    binding.ivVisible.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    isShowPassword = false;
                }else{
                    binding.tvOldpassword.setTransformationMethod(null);
                    binding.ivVisible.setImageDrawable(getResources().getDrawable(R.drawable.notvisible));
                    isShowPassword = true;
                }
            }
        });


        binding.ivVisible2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowPassword) {
                    binding.tvNewpassword.setTransformationMethod(new PasswordTransformationMethod());

                    binding.ivVisible2.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    isShowPassword = false;
                }else{
                    binding.tvNewpassword.setTransformationMethod(null);
                    binding.ivVisible2.setImageDrawable(getResources().getDrawable(R.drawable.notvisible));
                    isShowPassword = true;
                }
            }
        });

        binding.ivVisible3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowPassword) {
                    binding.tvCpassword.setTransformationMethod(new PasswordTransformationMethod());

                    binding.ivVisible3.setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    isShowPassword = false;
                }else{
                    binding.tvCpassword.setTransformationMethod(null);
                    binding.ivVisible3.setImageDrawable(getResources().getDrawable(R.drawable.notvisible));
                    isShowPassword = true;
                }
            }
        });





        binding.btPassowrdsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.tvOldpassword.getText().toString().isEmpty()){
                    Snackbar.make(ChangePasswordActivity.this,findViewById(android.R.id.content),"Please Enter the Old Password",Snackbar.LENGTH_LONG).show();
                }else if(binding.tvNewpassword.getText().toString().isEmpty()){
                    Snackbar.make(ChangePasswordActivity.this,findViewById(android.R.id.content),"Please Enter the New Password",Snackbar.LENGTH_LONG).show();
                } else if (binding.tvCpassword.getText().toString().isEmpty()) {
                    Snackbar.make(ChangePasswordActivity.this,findViewById(android.R.id.content),"Please Enter the Confirm Password",Snackbar.LENGTH_LONG).show();
                }
                else if(binding.tvOldpassword.getText().toString().equalsIgnoreCase(binding.tvNewpassword.getText().toString())){
                    Snackbar.make(ChangePasswordActivity.this,findViewById(android.R.id.content),"Old Password and New Password should not be Same",Snackbar.LENGTH_LONG).show();
                }
                else if(!binding.tvNewpassword.getText().toString().equalsIgnoreCase(binding.tvCpassword.getText().toString())){
                    Snackbar.make(ChangePasswordActivity.this,findViewById(android.R.id.content),"New Password and Confirm Password should be Same",Snackbar.LENGTH_LONG).show();
                }
                else{
                    changepasswordapi_call();
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void changepasswordapi_call() {
        progressDialog.show();
        JsonObject object = new JsonObject();
        object.addProperty("user_id", SaveAppData.getLoginData().getUser_id());
        object.addProperty("user_token",SaveAppData.getLoginData().getToken());
        object.addProperty("old_password",binding.tvOldpassword.getText().toString());
        object.addProperty("new_password",binding.tvNewpassword.getText().toString());
        object.addProperty("conf_password",binding.tvCpassword.getText().toString());
        viewModel.change_password(object).observe(ChangePasswordActivity.this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.d("TAG","password "+jsonObject);
                    Toast.makeText(ChangePasswordActivity.this, ""+jsonObject.get("text"), Toast.LENGTH_SHORT).show();

                    if (jsonObject.get("message").getAsString().equalsIgnoreCase("Success")){
                        Toast.makeText(ChangePasswordActivity.this, ""+jsonObject.get("text"), Toast.LENGTH_SHORT).show();

                        SaveAppData.saveOperatorLoginData(null);
                        startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                        finish();
                        return;


                    }
                }else{
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(ChangePasswordActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
    }*/
}