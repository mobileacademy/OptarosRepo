package com.mobileacademy.NewsReader;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.mobileacademy.NewsReader.database.ArticlesDataSource;
import com.mobileacademy.NewsReader.services.FetchArticlesService;

/**
 * Created by Valerica Plesu on 4/23/2016.
 */
public class NewsReaderApplication extends Application {

    private static final String TAG = NewsReaderApplication.class.getSimpleName();
    private static ArticlesDataSource datasource;
    private static NewsReaderApplication sInstance;

    public static NewsReaderApplication getInstance() {
        if(sInstance == null) {
            sInstance = new NewsReaderApplication();
        }

        return  sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        Intent service = new Intent(this, FetchArticlesService.class);
        this.startService(service);

        // create database (create Articles table)
        datasource = new ArticlesDataSource(this);

        // open database
        datasource.open();

    }


    public ArticlesDataSource getDatasource() {
        return datasource;
    }
}
