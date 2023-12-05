package com.example.teabreak_app.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.teabreak_app.ModelClass.LoginUserModel;
import com.google.gson.Gson;

public class SaveAppData{

    private static SaveAppData sessionData;
    public static String TeaBreak_App = "Paddy_App";
    public static String SHPREF_KEY_EMP_LOGIN="EMP_LOGIN";
    public static String TeaBreak_App_Token = "Token";

    private SaveAppData() {}
    public static SaveAppData getSessionDataInstance() {
        if (sessionData == null) {
            sessionData = new SaveAppData();
        }
        return sessionData;
    }

    public static void saveOperatorLoginData(LoginUserModel emplogin) {
        SharedPreferences.Editor e = AppController.getAppContext().getSharedPreferences(TeaBreak_App, Context.MODE_PRIVATE).edit();
        if (emplogin != null) {
            Gson gson = new Gson();
            String json = gson.toJson(emplogin);
            e.putString(SHPREF_KEY_EMP_LOGIN, json);
        } else {
            e.putString(SHPREF_KEY_EMP_LOGIN, null);
        }
        e.commit();
    }

    public static LoginUserModel getLoginData() {
        Gson gson = new Gson();
        String json = AppController.getAppContext().getSharedPreferences(TeaBreak_App, Context.MODE_PRIVATE).getString(SHPREF_KEY_EMP_LOGIN, null);
        LoginUserModel operatorLogin = gson.fromJson(json, LoginUserModel.class);
        return operatorLogin;
    }

  /*  public static void setLoginToken(String usename) {
        SharedPreferences.Editor e = AppController.getAppContext().getSharedPreferences(Paddy_App_TOKEN, Context.MODE_PRIVATE).edit();
        e.putString("login-token", usename).commit();
    }

    public static String getLoginToken() {
        String usename = AppController.getAppContext().getSharedPreferences(Paddy_App_TOKEN, Context.MODE_PRIVATE).getString("login-token","");
        return usename;
    }
*/


}

