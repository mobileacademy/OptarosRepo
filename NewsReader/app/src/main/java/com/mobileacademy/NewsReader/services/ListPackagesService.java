package com.mobileacademy.NewsReader.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

/**
 * Created by danielastamati on 22/04/16.
 */
public class ListPackagesService extends IntentService {

    String TAG = ListPackagesService.class.getSimpleName();

    public ListPackagesService() {
        super("ListPackagesService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ListPackagesService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        listPackages();
    }

    private void listPackages() {

        PackageManager pckManager = getPackageManager();

        List<ApplicationInfo> listApp
                = pckManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : listApp) {
            Log.i(TAG, app.packageName);
        }

        stopSelf();
    }

}
