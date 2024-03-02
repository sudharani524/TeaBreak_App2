package com.glitss.teabreak_app.Ui;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.glitss.teabreak_app.ModelClass.UsersRolesModel;
import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.Constant;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.glitss.teabreak_app.databinding.ActivityRegistrationFormBinding;
import com.glitss.teabreak_app.repository.ApiClient;
import com.glitss.teabreak_app.repository.ApiInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationForm extends AppCompatActivity {
    ImageView back_btn;
    ArrayAdapter route_type_adapter;
    ArrayList<UsersRolesModel> Route_list=new ArrayList<>();
    ArrayList<String> route_type=new ArrayList<>();
    ProgressDialog progressDialog;
    Spinner RouteId;
    private ActivityRegistrationFormBinding binding;
    String Selected_route="",selected_route_id="",selected_route_code="";
    Button submit;
    EditText vendor_name,vendor_mobile,vendor_email,pincode,address,city,district_name,outlet_name,outlet_code,outlet_address,outlet_id,lat_long,State;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        back_btn=findViewById(R.id.back_btn);
        RouteId=findViewById(R.id.Route_id);
        vendor_name=findViewById(R.id.FName);
        vendor_mobile=findViewById(R.id.Mnumber);
        vendor_email=findViewById(R.id.EMail);
        pincode=findViewById(R.id.pincode);
        address=findViewById(R.id.Address);
        submit=findViewById(R.id.submit);
        district_name=findViewById(R.id.Districtid);
        outlet_name=findViewById(R.id.et_outlet_name);
        outlet_code=findViewById(R.id.outletcode);
        outlet_address=findViewById(R.id.out_address);
        outlet_id=findViewById(R.id.outletid);
        city=findViewById(R.id.city);
        lat_long=findViewById(R.id.loglat);
        State=findViewById(R.id.state);
        progressDialog=new ProgressDialog(RegistrationForm.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        vendor_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                Pattern pattern;
                Matcher matcher;
                String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                pattern = Pattern.compile(EMAIL_PATTERN);
                CharSequence cs = (CharSequence) editable;
                matcher = pattern.matcher(cs);
                if (!(matcher.matches() == true)) {
                    vendor_email.setError("Invalid email");
                }


            }
        });





       /* vendor_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               String input=editable.toString();
               if(!input.matches("[a-zA-Z ]*")){
                   vendor_name.setText("");
                   vendor_name.setError("Only Letters are Allowed");
               }
            }
        });
*/
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationForm.this, AdminDashBoard.class));
            }
        });
        RoutesApiCall();
       submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(vendor_name.getText().toString().isEmpty()||vendor_email.getText().toString().isEmpty()||vendor_mobile.getText().toString().isEmpty()||outlet_name.getText().toString().isEmpty()||outlet_code.getText().toString().isEmpty()||outlet_id.getText().toString().isEmpty()||pincode.getText().toString().isEmpty()||city.getText().toString().isEmpty()||outlet_code.getText().toString().isEmpty()||district_name.getText().toString().isEmpty()||State.getText().toString().isEmpty()){
                    Toast.makeText(RegistrationForm.this, "Please Fill Required Fields", Toast.LENGTH_SHORT).show();
                    vendor_name.setBackgroundResource(R.drawable.editext);
                    vendor_email.setBackgroundResource(R.drawable.editext);
                    vendor_mobile.setBackgroundResource(R.drawable.editext);
                    outlet_code.setBackgroundResource(R.drawable.editext);
                    outlet_name.setBackgroundResource(R.drawable.editext);
                    pincode.setBackgroundResource(R.drawable.editext);
                    outlet_id.setBackgroundResource(R.drawable.editext);
                    city.setBackgroundResource(R.drawable.editext);
                    district_name.setBackgroundResource(R.drawable.editext);
                    State.setBackgroundResource(R.drawable.editext);
                }
                else{
                    vendor_name.setBackgroundResource(android.R.drawable.edit_text);
                    vendor_email.setBackgroundResource(android.R.drawable.edit_text);
                    vendor_mobile.setBackgroundResource(android.R.drawable.edit_text);
                    outlet_code.setBackgroundResource(android.R.drawable.edit_text);
                    outlet_name.setBackgroundResource(android.R.drawable.edit_text);
                    pincode.setBackgroundResource(android.R.drawable.edit_text);
                    outlet_id.setBackgroundResource(android.R.drawable.edit_text);
                    city.setBackgroundResource(android.R.drawable.edit_text);
                    district_name.setBackgroundResource(android.R.drawable.edit_text);
                   State.setBackgroundResource(android.R.drawable.edit_text);
                    SubmitApi_call();
                }
            }
        });

    }



    private void SubmitApi_call() {
        progressDialog.show();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_token", SaveAppData.getLoginData().getToken());
        jsonObject.addProperty("user_id",SaveAppData.getLoginData().getUser_id() );
        jsonObject.addProperty("vendor_name",vendor_name.getText().toString());
        jsonObject.addProperty("vendor_email",vendor_email.getText().toString());
        jsonObject.addProperty("vendor_mobile",vendor_mobile.getText().toString());
        jsonObject.addProperty("pincode",pincode.getText().toString());
        jsonObject.addProperty("address",address.getText().toString());
        jsonObject.addProperty("city",city.getText().toString());
        jsonObject.addProperty("district_name",district_name.getText().toString());
        jsonObject.addProperty("outlet_name",outlet_name.getText().toString());
        jsonObject.addProperty("outlet_code",outlet_code.getText().toString());
        jsonObject.addProperty("outlet_address",outlet_address.getText().toString());
        jsonObject.addProperty("lat_long",lat_long.getText().toString());
        jsonObject.addProperty("outlet_id",outlet_id.getText().toString());
        jsonObject.addProperty("route_code",selected_route_code);
        jsonObject.addProperty("state",State.getText().toString());
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> vendor_registration = apiInterface.vendor_registration(jsonObject);
        vendor_registration.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(response.body()!=null){
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body().toString());
                        String message=jsonObj.getString("message");
                        String text=jsonObj.getString("text");
                        JSONArray jsonArray=jsonObj.getJSONArray("data");
                        if(message.equalsIgnoreCase("success")){
                            Snackbar.make(RegistrationForm.this,findViewById(android.R.id.content),""+text,Snackbar.LENGTH_LONG).show();
                            finish();
                        }
                        else{
                            Toast.makeText(RegistrationForm.this, "Please Fill required fields", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error", String.valueOf(e));
                        Snackbar.make(RegistrationForm.this,findViewById(android.R.id.content),""+e,Snackbar.LENGTH_LONG).show();
                        finish();
                    }

                }
                else{
                    Toast.makeText(RegistrationForm.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void RoutesApiCall() {
        progressDialog.show();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_token", SaveAppData.getLoginData().getToken());
        jsonObject.addProperty("user_id",SaveAppData.getLoginData().getUser_id() );
        ApiInterface apiInterface = ApiClient.getClient(Constant.SERVER_BASE_URL).create(ApiInterface.class);
        Call<JsonObject> routes_type = apiInterface.routes(jsonObject);
        routes_type.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(response.body()!=null){

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(response.body().toString());
                        String message=jsonObj.getString("message");

                        JSONArray jsonArray=jsonObj.getJSONArray("data");
                        Route_list.clear();
                        route_type.clear();
                        UsersRolesModel usersRolesModel1=new UsersRolesModel();
                        usersRolesModel1.setRoute_id("");
                        usersRolesModel1.setRoute_code("");

                        route_type.add("Select");
                        Route_list.add(usersRolesModel1);

                        if(message.equalsIgnoreCase("Route Codes List")){
                            for(int i=0;i<jsonArray.length();i++){
                                UsersRolesModel usersRolesModel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), new TypeToken<UsersRolesModel>() {
                                }.getType());
                                Route_list.add(usersRolesModel);
                                route_type.add(usersRolesModel.getRoute_name());
                            }
                        }
                        route_type_adapter=new ArrayAdapter(RegistrationForm.this,R.layout.spinner_text,route_type);
                        route_type_adapter.setDropDownViewResource(R.layout.spinner_text);
                        RouteId.setAdapter(route_type_adapter);

                        RouteId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Selected_route  = RouteId.getSelectedItem().toString();
                                selected_route_id  =Route_list.get(i).getRoute_id();
                                selected_route_code=Route_list.get(i).getRoute_code();
                                Log.d("Route_id",selected_route_id);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                Snackbar.make(RegistrationForm.this,findViewById(android.R.id.content),"Please Select the Route",Snackbar.LENGTH_LONG).show();

                            }
                        });
                    } catch (JSONException e) {

                        Snackbar.make(RegistrationForm.this,findViewById(android.R.id.content),""+e,Snackbar.LENGTH_LONG).show();
                    }

                }else{
                    Snackbar.make(RegistrationForm.this,findViewById(android.R.id.content),"Something went wrong",Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(RegistrationForm.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }
}