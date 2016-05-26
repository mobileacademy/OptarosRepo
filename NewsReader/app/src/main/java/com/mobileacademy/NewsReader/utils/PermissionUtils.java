package com.mobileacademy.NewsReader.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by danielastamati on 18/05/16.
 */
@TargetApi(23)
public class PermissionUtils {

    public static final int REQUEST_LOCATION = 475;
    private static final String POSTFIX_PERM = "_permRequested";


    //(Manifest.permission.ACCESS_FINE_LOCATION
    public static void requestPermissionWithRationale(Activity activity, String permission,
                                                      int resultCode) {

        if (activity.shouldShowRequestPermissionRationale(permission)) {
            displayRationale(activity, permission, resultCode);
        } else {
            if (wasPermissionRequested(activity, permission)) {
                Toast.makeText(activity, "needs permission", Toast.LENGTH_LONG).show();
            } else {
                requestPermissions(activity, permission, resultCode);
            }
        }

    }

    public static void displayRationale(final Activity activity, final String permission, final int resultCode){
        new AlertDialog.Builder(activity)
                .setTitle("Permission required")
                .setMessage("The functionality requires a permission")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(activity, permission, resultCode);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public static void requestPermissions(Activity activity, String permission, int resultCode) {
        activity.requestPermissions(new String[]{permission },
                resultCode);
        setPermissionRequested(activity, permission);
    }

    public static boolean wasPermissionRequested (Context context, String permission){
        AppSharedPref sharedPref = new AppSharedPref(context);
        return sharedPref.getBoolean(permission+POSTFIX_PERM);
    }

    private static void setPermissionRequested(Context context, String permission){
        AppSharedPref sharedPref = new AppSharedPref(context);
        sharedPref.setBoolean(permission+POSTFIX_PERM, true);
    }
}