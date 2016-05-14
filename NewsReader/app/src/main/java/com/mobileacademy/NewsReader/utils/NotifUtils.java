package com.mobileacademy.NewsReader.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.activities.MainActivity;

/**
 * Created by Valerica Plesu on 4/22/2016.
 */
public class NotifUtils {

    private static final int NOTIF_ID = 1123;

    public static Notification getCustomNotif(Context context) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.tc_logo)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setAutoCancel(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // add action
        mBuilder.setContentIntent(resultPendingIntent);

        return mBuilder.build();


//        NotificationManager mNotificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
//        mNotificationManager.notify(NOTIF_ID, mBuilder.build());
    }


    public static void scheduleNotification(Context context, Notification notification, int delay) {

        // Create an intent that will be wrapped in PendingIntent
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        // Create the pending intent and wrap our intent
        // this (context) – This is the context in which the `PendingIntent` should start the activity.
        // 1 (requestCode) – This is a private request code for the sender. Use it later with the same method again to get back the same pending intent later. Then you can do various things like cancelling the pending intent with cancel(), etc.
        // intent (intent) – Explicit intent of the activity to be launched.
        // PendingIntent.FLAG_UPDATE_CURRENT (flags) – Flag indicating that if the described PendingIntent already exists,then keep it but replace its extra data with what is in this newIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;

        // Get the alarm manager service and schedule it to go off after 3s
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        Toast.makeText(context, "Alarm set in " + futureInMillis + " seconds", Toast.LENGTH_LONG).show();
    }

    public static Notification getNotification(Context context, String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_menu_camera);
        return builder.build();
    }
}
