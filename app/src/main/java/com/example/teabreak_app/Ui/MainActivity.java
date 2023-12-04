package com.example.teabreak_app.Ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.teabreak_app.Adapter.ListItemsAdapter;
import com.example.teabreak_app.ModelClass.ListItemsModel;
import com.example.teabreak_app.ModelClass.UsersRolesModel;
import com.example.teabreak_app.R;
import com.example.teabreak_app.ViewModel.TeaBreakViewModel;
import com.example.teabreak_app.databinding.ActivityMainBinding;
import com.example.teabreak_app.repository.ListItemInterface;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        viewModel = ViewModelProviders.of(MainActivity.this).get(TeaBreakViewModel.class);
        
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        users_role_api_call();

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            }
        });


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