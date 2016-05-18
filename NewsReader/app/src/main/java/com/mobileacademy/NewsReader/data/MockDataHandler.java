package com.mobileacademy.NewsReader.data;

import android.util.Log;

import com.mobileacademy.NewsReader.NewsReaderApplication;
import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.models.Article;
import com.mobileacademy.NewsReader.models.Publication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by danielastamati on 21/04/16.
 *
 * class to be used for tests
 */
public class MockDataHandler {

    private static final String TAG = MockDataHandler.class.getSimpleName();
    private HashMap<String, Publication> publicationMap;
    private static MockDataHandler INSTANCE;

    public static final String HACKER_NEWS = "Hacker News";
    public static final String THE_VERGE = "The Verge";
    public static final String TECH_CRUNCH = "Tech Crunch";
    public static final String FAST_COMPANY = "Fast Company";


    private MockDataHandler() {
        generateDummyContent();
    }

    public static MockDataHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockDataHandler();
        }

        return INSTANCE;
    }

    public ArrayList<Publication> getPublicationsAsList(){
        ArrayList list = new ArrayList(publicationMap.values());
        return list;
    }

    public ArrayList<Article> getArticleListForPublication(String publicationName){
        return publicationMap.get(publicationName).getArticleList();
    }


    private void generateDummyContent() {
        publicationMap = new HashMap<>();

        ArrayList<Article> articlesForPub1 = new ArrayList<>();

        String name = "Hacker News";
        Publication hackerNews = new Publication(99, name, R.drawable.hn_logo);
        hackerNews.setArticleList(articlesForPub1);
        publicationMap.put(name, hackerNews);

        name = "Tech Crunch";
        Publication techCrunch = new Publication(98, name, R.drawable.tc_logo);
        techCrunch.setArticleList(articlesForPub1);
        publicationMap.put(name, techCrunch);

        name = "The Verge";
        Publication theVerge = new Publication(97, name, R.drawable.verge_logo);
        theVerge.setArticleList(articlesForPub1);
        publicationMap.put(name, theVerge);

        name = "Fast Company";
        Publication fastCo = new Publication(96, "Fast Company", R.drawable.fc_logo);
        fastCo.setArticleList(articlesForPub1);
        publicationMap.put(name, fastCo);
    }

    public void insertArticleDummyContentToDb() {
        Log.d(TAG, "insertArticleDummyContentToDb");

        ArrayList<Article> articlesForPub1 = getArticleListForPublication(HACKER_NEWS);

        if(articlesForPub1.size() > 0) {
            for (Article a: articlesForPub1) {
                NewsReaderApplication.getInstance().getDatasource().createArticle(a.getId(), a.getName(), a.getTime(), a.getUrl(), a.getPublicationId());
            }
        }

        ArrayList<Article> articlesForPub2 = getArticleListForPublication(TECH_CRUNCH);

        if(articlesForPub2.size() > 0) {
            for (Article a: articlesForPub2) {
                NewsReaderApplication.getInstance().getDatasource().createArticle(a.getId(), a.getName(), a.getTime(), a.getUrl(), a.getPublicationId());
            }
        }

    }
}
