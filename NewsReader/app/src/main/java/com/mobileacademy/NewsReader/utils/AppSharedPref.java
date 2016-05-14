package com.mobileacademy.NewsReader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Valerica Plesu on 4/16/2016.
 */
public class AppSharedPref {

    public static final String MyPrefName = "MyPrefs" ;
    private SharedPreferences sharedpreferences;

    public AppSharedPref(Context context) {
        // parse Preference file
        sharedpreferences = context.getSharedPreferences(MyPrefName, Context.MODE_PRIVATE);
    }

    public void addStringToSharePref(String key, String value) {
        // get Editor object
        SharedPreferences.Editor editor = sharedpreferences.edit();
        // put values in editor
        editor.putString(key, value);

        // commit your putted values to the SharedPreferences object synchronously
        // returns true if success
        boolean result = editor.commit();

        // do the same as commit() but asynchronously (faster but not safely)
        // returns nothing
        editor.apply();
    }


    public void saveLong(String key, long value) {
        // get Editor object
        SharedPreferences.Editor editor = sharedpreferences.edit();
        // put values in editor
        editor.putLong(key, value);
        editor.commit();
    }

    public String getValue(String key) {
        return sharedpreferences.getString(key, "");
    }

    public Long getLong(String key) {
        return sharedpreferences.getLong(key, 0);
    }


    public void remove(String key) {
        sharedpreferences.edit()
                .remove(key)
                .commit();
    }

    public boolean clear() {
        return sharedpreferences.edit()
                .clear()
                .commit();
    }

    private Map<String, ?> getAll() {
        // you can get all Map but be careful you must not modify the collection returned by this
        // method, or alter any of its contents.
        Map<String, ?> all = sharedpreferences.getAll();
        return all;
    }

}
