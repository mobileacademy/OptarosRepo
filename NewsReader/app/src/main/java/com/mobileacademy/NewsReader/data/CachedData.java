package com.mobileacademy.NewsReader.data;

import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.models.Article;
import com.mobileacademy.NewsReader.models.Publication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by danielastamati on 21/04/16.
 *
 * Singleton class that acts as a cache in the app holding info about available data sources and articles.
 */
public class CachedData {

    private HashMap<String, Publication> publicationMap;
    private static CachedData INSTANCE;


    private CachedData() {
        generateDummyContent();
    }

    public static CachedData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CachedData();
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

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(new Article("article1"));
        articles.add(new Article("article2"));
        articles.add(new Article("article3"));
        articles.add(new Article("article4"));
        articles.add(new Article("article5"));

        String name = "Hacker News";
        Publication hackerNews = new Publication(name, R.drawable.hn_logo);
        hackerNews.setArticleList(articles);
        publicationMap.put(name, hackerNews);

        name = "Tech Crunch";
        Publication techCrunch = new Publication(name, R.drawable.tc_logo);
        techCrunch.setArticleList(articles);
        publicationMap.put(name, techCrunch);

        name = "The Verge";
        Publication theVerge = new Publication(name, R.drawable.verge_logo);
        theVerge.setArticleList(articles);
        publicationMap.put(name, theVerge);

        name = "Fast Company";
        Publication fastCo = new Publication("Fast Company", R.drawable.fc_logo);
        fastCo.setArticleList(articles);
        publicationMap.put(name, fastCo);
    }
}
