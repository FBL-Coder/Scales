package com.etsoft.scales.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Author：FBL  Time： 2017/6/26.
 * 权限申请
 */

public class PermissionsUtli {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        boolean permission_Read = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean permission_Camera = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA);
        boolean permission_Call = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE);
        boolean permission_Audio = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        boolean permission_Location = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.LOCATION_HARDWARE);

        if (permission_Read && permission_Camera && permission_Call && permission_Audio && permission_Location) {

        } else  // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
    }

    public static void permissionIsOk(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);
            }
        }
    }
}
