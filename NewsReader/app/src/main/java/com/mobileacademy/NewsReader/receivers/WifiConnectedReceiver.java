package com.mobileacademy.NewsReader.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.mobileacademy.NewsReader.utils.AppSharedPref;

/**
 * Created by danielastamati on 14/05/16.
 */
public class WifiConnectedReceiver extends BroadcastReceiver {

    private static String TAG = WifiConnectedReceiver.class.getSimpleName();
    private static String LAST_UPDATE = WifiConnectedReceiver.class.getSimpleName();
    private static long UPDATE_SEQUENCE = 24/*h*/ * 60/*m*/ * 60/*s*/ * 1000/*ms*/;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(mWifi!=null && mWifi.isConnected()) {

            AppSharedPref sharedPrefs = new AppSharedPref(context);
            long lastUpdate = sharedPrefs.getLong(LAST_UPDATE);
            long currentTime = System.currentTimeMillis();
            if(currentTime - lastUpdate >=UPDATE_SEQUENCE) {
                sharedPrefs.saveLong(LAST_UPDATE, currentTime);
                Log.d(TAG, "onReceive: ");


                //todo: retrieve articles once a day
            }
        }
    }
}
