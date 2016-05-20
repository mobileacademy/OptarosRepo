package com.mobileacademy.NewsReader.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationUpdatedReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationUpdatedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = intent.getExtras().getParcelable("location");

        Log.d(TAG, "onReceive:" + location.getLatitude() + " / " + location.getLongitude());
    }
}
