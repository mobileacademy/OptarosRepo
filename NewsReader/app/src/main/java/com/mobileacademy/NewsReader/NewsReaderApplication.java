package com.mobileacademy.NewsReader;

import android.app.Application;
import android.util.Log;

import com.mobileacademy.NewsReader.data.CachedData;
import com.mobileacademy.NewsReader.database.ArticlesDataSource;

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

        // create database (create Articles table)
        datasource = new ArticlesDataSource(this);

        // open database
        datasource.open();

        // insert dummy data into Article table
        CachedData.getInstance().insertArticleDummyContentToDb();

    }


    public ArticlesDataSource getDatasource() {
        return datasource;
    }
}
