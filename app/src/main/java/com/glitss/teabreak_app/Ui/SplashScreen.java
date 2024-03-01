package com.glitss.teabreak_app.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.glitss.teabreak_app.R;
import com.glitss.teabreak_app.Utils.SaveAppData;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class SplashScreen extends AppCompatActivity {

    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

     //   checkfor_App_update();

       //    checkForAppUpdate();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(SaveAppData.getLoginData()!=null){


                    if(SaveAppData.getLoginData().getRole_id().equalsIgnoreCase("2")){

                        startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
                        finish();

                        /*if(SaveAppData.getLoginData().getPassword_status().equalsIgnoreCase("1")){
                            startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
                            finish();
                        }else{
                            startActivity(new Intent(SplashScreen.this, ChangePasswordActivity.class));
                            finish();
                        }*/

                    } else if (SaveAppData.getLoginData().getRole_id().equalsIgnoreCase("3")) {
                        startActivity(new Intent(SplashScreen.this, VendorDashboardActivity.class));
                        finish();

                     /*   if(SaveAppData.getLoginData().getPassword_status().equalsIgnoreCase("1")){
                            startActivity(new Intent(SplashScreen.this, VendorOrderlist.class));
                            finish();
                        }else{
                            startActivity(new Intent(SplashScreen.this, ChangePasswordActivity.class));
                            finish();
                        }*/

                    } else if (SaveAppData.getLoginData().getRole_id().equalsIgnoreCase("4")) {

                        startActivity(new Intent(SplashScreen.this, AccountsDashboard.class));
                        finish();

                      /*  if(SaveAppData.getLoginData().getPassword_status().equalsIgnoreCase("1")){
                            startActivity(new Intent(SplashScreen.this, AccountsDashboard.class));
                            finish();
                        }else{
                            startActivity(new Intent(SplashScreen.this, ChangePasswordActivity.class));
                            finish();
                        }*/

                    }
                    else if (SaveAppData.getLoginData().getRole_id().equalsIgnoreCase("1")) {

                        startActivity(new Intent(SplashScreen.this, AdminDashBoard.class));
                        finish();

                      /*  if(SaveAppData.getLoginData().getPassword_status().equalsIgnoreCase("1")){
                            startActivity(new Intent(SplashScreen.this, AccountsDashboard.class));
                            finish();
                        }else{
                            startActivity(new Intent(SplashScreen.this, ChangePasswordActivity.class));
                            finish();
                        }*/

                    }
                    else{

                        startActivity(new Intent(SplashScreen.this, VendorDashboardActivity.class));
                        finish();

                        /*if(SaveAppData.getLoginData().getPassword_status().equalsIgnoreCase("1")){
                            startActivity(new Intent(SplashScreen.this, VendorOrderlist.class));
                            finish();
                        }else{
                            startActivity(new Intent(SplashScreen.this, ChangePasswordActivity.class));
                            finish();
                        }*/
                    }

                }else{
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }
            }
        }, 3000);
    }

    private void checkfor_App_update() {
        Log.e("check_for_update_mthd","check for update method");
     AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(SplashScreen.this);

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



    private void checkForAppUpdate() {
        // Creates instance of the manager.
       AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(SplashScreen.this);

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
                        SplashScreen.this.startAppUpdateFlexible(appUpdateInfo);
                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Start an update.
                        SplashScreen.this.startAppUpdateImmediate(appUpdateInfo);
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

      Snackbar snackbar = Snackbar.make(SplashScreen.this,findViewById(android.R.id.content),"Update Downloaded",Snackbar.LENGTH_LONG);
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