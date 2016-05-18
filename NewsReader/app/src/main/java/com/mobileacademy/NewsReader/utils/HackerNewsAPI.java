package com.mobileacademy.NewsReader.utils;


import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cornelbalaban on 06/04/16.
 */
public class HackerNewsAPI {

    private static final String TAG = HackerNewsAPI.class.getSimpleName();

    public static final String BASE_ENDPOINT = "https://hacker-news.firebaseio.com/v0/";
    public static final String TOP_STORIES_ENDPOINT = BASE_ENDPOINT + "topstories.json";
    public static final String NEW_STORIES_ENDPOINT = BASE_ENDPOINT + "newstories.json";
    public static final String ITEM_ENDPOINT = BASE_ENDPOINT + "item/";

    public static String getArticleById(String id) {
        return ITEM_ENDPOINT + id + ".json";
    }

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
        Response response = getInstance().newCall(getRequest).execute();

        if(response.isSuccessful()){
            return response.body().string();
        }else{
            return null;
        }
    }

    /**
     * @param callback - callback which will be used when the call is done
     * @param url is the url to which the call needs to be made
     *            http operation is GET
     *            in case of failures we either throw an exception or return null
     *
     */
    public static void retrieveStories(String url, Callback callback) throws IOException {

        Request getRequest = new Request.Builder()
                .url(url)
                .build();
        getInstance().newCall(getRequest).enqueue(callback);
    }
}
