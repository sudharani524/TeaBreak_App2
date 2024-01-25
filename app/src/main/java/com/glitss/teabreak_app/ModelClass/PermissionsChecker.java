package com.glitss.teabreak_app.ModelClass;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class PermissionsChecker {
    /**
     * Permission List
     */
    public static String[] REQUIRED_PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static String[] REQUIRED_PERMISSION13 = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO};

    public static String[] REQUIRED_PERMISSION132 = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
    };

    /**
     * GPS Permission
     */
    public static String[] GPS_PERMISSION = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    /**
     * Context
     */
    private final Context context;

    public PermissionsChecker(Context context) {
        this.context = context;
    }

    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }

}
