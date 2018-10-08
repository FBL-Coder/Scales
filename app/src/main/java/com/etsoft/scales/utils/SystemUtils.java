package com.etsoft.scales.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import static android.Manifest.permission.CALL_PHONE;

/**
 * Author：FBL  Time： 2018/9/28.
 * 系统功能工具
 */
public class SystemUtils {
    public static void callPhone(Activity activity) {
        permissionIsOk(activity, CALL_PHONE);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:$phoneNum"));
        activity.startActivity(intent);
    }


    public static void permissionIsOk(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);
            }
        }
    }

    public static String convertHexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();      //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {          //grab the hex in pairs
            String output = hex.substring(i, (i + 2));          //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);          //convert the decimal to character
            sb.append((char) decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }


}
