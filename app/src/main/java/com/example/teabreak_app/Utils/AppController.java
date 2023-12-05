package com.example.teabreak_app.Utils;

import android.app.Application;
import android.content.Context;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();
    private String baseURL;

    private static Context appContext;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        appContext = getApplicationContext();
        setAppContext(getApplicationContext());
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    private static void setAppContext(Context appContext) {
        AppController.appContext = appContext;
    }

    /*@Override
    public void onTerminate() {
        super.onTerminate();
        MyReceiver myReceiver = new MyReceiver();
//        unregisterReceiver(Constant.mGpsSwitchStateReceiver);
        Log.d("TAG","ONTERMINATE APP_CONTROLLER");
        unregisterReceiver(myReceiver);
    }
*/
}
