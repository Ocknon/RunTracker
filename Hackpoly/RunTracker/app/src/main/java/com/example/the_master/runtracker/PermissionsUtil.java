package com.example.the_master.runtracker;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by The_Master on 2/12/2017.
 */

public class PermissionsUtil
{
    public static final byte PERMISSION_ACCESS_FINE_LOCATION = 1;
    public static boolean checkPermission(Activity activity, String permission)
    {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity, String permission, int id)
    {
        if(!checkPermission(activity, permission))
        {
            ActivityCompat.requestPermissions(activity,new String[]{permission},id);
        }

    }

}
