package com.mobileacademy.NewsReader.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.mobileacademy.NewsReader.activities.MainActivity;

/**
 * Created by cornelbalaban on 29/03/16.
 */
public class CounterService extends Service {

    private static final String TAG = CounterService.class.getSimpleName();
    public static final String ACTION_COUNT = "count";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }
        String action = intent.getAction();

        Log.i(TAG, "onStartCommand() has action" + action);

        if (action == null || !action.equalsIgnoreCase(ACTION_COUNT)) {
            stopSelf();
            return START_NOT_STICKY;
        }

        startCountingThread();
        return START_STICKY | START_REDELIVER_INTENT;
    }


    //Method for creating and running a counter thread
    private void startCountingThread() {

        //the code the will run() inside the thread
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                count();
                Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                LocalBroadcastManager.getInstance(CounterService.this).sendBroadcast(intent);
                stopSelf();
            }
        };

        new Thread(runnable).start();

    }

    private void count() {
        int i = 0;
        while (i < 3) {
            i++;
            Log.d(TAG, "second: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        Toast.makeText(CounterService.this, "Service stopped itself", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}