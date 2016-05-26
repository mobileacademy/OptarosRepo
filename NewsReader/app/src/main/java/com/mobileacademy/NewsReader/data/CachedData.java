package com.mobileacademy.NewsReader.data;

import com.mobileacademy.NewsReader.NewsReaderApplication;
import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.database.ArticlesDataSource;
import com.mobileacademy.NewsReader.models.Article;
import com.mobileacademy.NewsReader.models.Publication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by danielastamati on 21/04/16.
 * <p/>
 * class to be used for tests
 */
public class CachedData {

    private static final String TAG = CachedData.class.getSimpleName();
    private HashMap<String, Publication> publicationMap;
    private static CachedData INSTANCE;

    public static final String HACKER_NEWS = "Hacker News";
    public static final String THE_VERGE = "The Verge";
    public static final String TECH_CRUNCH = "Tech Crunch";
    public static final String FAST_COMPANY = "Fast Company";

    public static final int HACKER_NEWS_ID = 99;
    public static final int THE_VERGE_ID = 98;
    public static final int TECH_CRUNCH_ID = 96;
    public static final int FAST_COMPANY_ID = 97;


    private CachedData() {
        updateCache();
    }

    public static CachedData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CachedData();
        }

        return INSTANCE;
    }

    public ArrayList<Publication> getPublicationsAsList() {
        ArrayList list = new ArrayList(publicationMap.values());
        return list;
    }

    public ArrayList<Article> getArticleListForPublication(String publicationName) {
        return publicationMap.get(publicationName).getArticleList();
    }

    public void updateCache() {
        publicationMap = new HashMap<>();
        ArticlesDataSource db = NewsReaderApplication.getInstance()
                .getDatasource();

        ArrayList<Article> hackerNewsArticles = (ArrayList<Article>) db.getAllArticlesByPublication(HACKER_NEWS_ID);
        publicationMap.put(HACKER_NEWS, new Publication(HACKER_NEWS_ID, HACKER_NEWS,
                R.drawable.hn_logo, hackerNewsArticles));

        ArrayList<Article> fastCompanyArticles = (ArrayList<Article>) db.getAllArticlesByPublication(FAST_COMPANY_ID);
        publicationMap.put(FAST_COMPANY, new Publication(FAST_COMPANY_ID, FAST_COMPANY,
                R.drawable.fc_logo, fastCompanyArticles));

        ArrayList<Article> theVergeArticles = (ArrayList<Article>) db.getAllArticlesByPublication(THE_VERGE_ID);
        publicationMap.put(THE_VERGE, new Publication(THE_VERGE_ID, THE_VERGE,
                R.drawable.verge_logo, theVergeArticles));

        ArrayList<Article> techArticles = (ArrayList<Article>) db.getAllArticlesByPublication(TECH_CRUNCH_ID);
        publicationMap.put(TECH_CRUNCH, new Publication(TECH_CRUNCH_ID, TECH_CRUNCH,
                R.drawable.tc_logo, techArticles));
    }
}
