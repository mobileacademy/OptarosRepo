package com.mobileacademy.NewsReader.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.mobileacademy.NewsReader.NewsReaderApplication;
import com.mobileacademy.NewsReader.data.CachedData;
import com.mobileacademy.NewsReader.models.Article;
import com.mobileacademy.NewsReader.utils.HackerNewsAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by danielastamati on 17/05/16.
 */
public class FetchArticlesService extends IntentService {

    private static final String TAG = "FetchArticlesService";
    private int NO_OF_ARTICLES = 20;

    public FetchArticlesService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ArrayList<Article> topStoriesList;
        ArrayList<Article> newStoriesList;

        try {
            String idsArrayHN = HackerNewsAPI.retrieveStories(HackerNewsAPI.TOP_STORIES_ENDPOINT);
            topStoriesList = fetchArticlesByIdsArray(idsArrayHN);
            String idsArrayFC = HackerNewsAPI.retrieveStories(HackerNewsAPI.NEW_STORIES_ENDPOINT);
            newStoriesList = fetchArticlesByIdsArray(idsArrayFC);

            saveToDb(CachedData.HACKER_NEWS_ID, topStoriesList);
            saveToDb(CachedData.FAST_COMPANY_ID, newStoriesList);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveToDb(int publicationId, ArrayList<Article> articles){
        for (Article article: articles) {
            //TODO: can be set in a single DB transaction
            article.setPublicationId(publicationId);
            NewsReaderApplication.getInstance().getDatasource().saveArticle(article);
        }
    }

    private Article getNewsItemFromJSON(JSONObject json) {
        Article item = new Article();

        String title = json.optString("title");
        String url = json.optString("url");
        int id = json.optInt("id");

        item.setId(id);
        item.setUrl(url);
        item.setName(title);

        return item;
    }



    private ArrayList<Article>  fetchArticlesByIdsArray(String idsArray){
        ArrayList<Article> articles = new ArrayList<>();
        try {
            JSONArray jsonArticlesArray = new JSONArray(idsArray);
            //take the first NO_OF_ARTICLES articles
            for (int i = 0; i < Math.min(NO_OF_ARTICLES,jsonArticlesArray.length()); i++) {
                String articleURL = HackerNewsAPI.getArticleById(jsonArticlesArray.getString(i));
                String articleString = HackerNewsAPI.retrieveStories(articleURL);
                if(articleString == null) continue;
                JSONObject articleJson = new JSONObject(articleString);
                articles.add(getNewsItemFromJSON(articleJson));
            }

        }catch (IOException |JSONException e){
            Log.e(TAG, "doInBackground: ", e);
        }
        return articles;
    }
}
