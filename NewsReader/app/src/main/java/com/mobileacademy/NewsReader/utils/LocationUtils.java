package com.mobileacademy.NewsReader.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by danielastamati on 19/05/16.
 */

@SuppressWarnings({"MissingPermission"})
public class LocationUtils {

    private static String LOCATION_UPDATE_BROADCAST = "com.mobileacademy.newsreader.LOCATION_UPDATE";

    public static void registerForLocationUpdates(Context context){
        long minTime = 1000; //ms
        float minDistance = 50; //m
        String provider;   //GPS_PROVIDER or NETWORK_PROVIDER


        Intent intent = new Intent(LOCATION_UPDATE_BROADCAST);
        PendingIntent launchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = manager.getBestProvider(criteria, false);
        manager.requestLocationUpdates(provider, minTime, minDistance, launchIntent);
    }

    public static LatLng getCurrentLocation(Context context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //use the default criteria on how to get the location

        //accuracy, power usage, ability to report altitude, speed, and bearing, and monetary cost.
        Criteria criteria = new Criteria();
        String provider = manager.getBestProvider(criteria, false);
        Location location =  manager.getLastKnownLocation(provider);


        if(location!=null) {
            return new LatLng(location.getLatitude(), location.getLongitude());
        }else {
            return null;
        }
    }
}
