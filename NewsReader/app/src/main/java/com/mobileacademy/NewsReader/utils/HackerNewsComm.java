package com.mobileacademy.NewsReader.utils;


import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cornelbalaban on 06/04/16.
 */
public class HackerNewsComm {

    private static final String TAG = HackerNewsComm.class.getSimpleName();

    //no need for JSON_TYPE right now buuuut we might need it in the future
    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=urf-8");
    private static OkHttpClient httpClientInstance;

    static OkHttpClient getInstance() {

        if (httpClientInstance == null) {
            httpClientInstance = new OkHttpClient();
        }

        return httpClientInstance;
    }

    /**
     * @param url is the url to which the call needs to be made
     *            http operation is GET
     *            in case of failures we either throw an exception or return null
     */
    public static String retrieveStories(String url) throws IOException {

        Request getRequest = new Request.Builder()
                .url(url)
                .build();

        Response getResponse = getInstance().newCall(getRequest).execute();
        return getResponse.body().string();
    }
}
