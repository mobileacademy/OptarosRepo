package com.mobileacademy.NewsReader.data;

import android.util.Log;

import com.mobileacademy.NewsReader.NewsReaderApplication;
import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.database.ArticlesDataSource;
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

    private static final String TAG = CachedData.class.getSimpleName();
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

        ArrayList<Article> articlesForPub1 = new ArrayList<>();
        ArrayList<Article> articlesForPub2 = new ArrayList<>();

        Article a1 = new Article();
        a1.setId(11);
        a1.setName("Ann Caracristi, who cracked codes, and the glass ceiling, at NSA, dies at 94");
        a1.setTime("1461393347");
        a1.setUrl("https://www.washingtonpost.com/national/ann-caracristi-who-excelled-at-code-breaking-and-management-dies-at-94/2016/01/11/b8187468-b80d-11e5-b682-4bb4dd403c7d_story.html");
        a1.setPublicationId(99);


        Article a2 = new Article();
        a2.setId(12);
        a2.setName("Why Google and car companies are about to spend billions mapping American roads");
        a2.setTime("1461393211");
        a2.setUrl("http://www.vox.com/2016/4/22/11476636/self-driving-car-maps");
        a2.setPublicationId(99);



        Article a3 = new Article();
        a3.setId(13);
        a3.setName("Afghan coding school for girls wins Google RISE Award");
        a3.setTime("1461392442");
        a3.setUrl("http://www.techrung.com/2016/04/22/229/");
        a3.setPublicationId(99);



        Article a4 = new Article();
        a4.setId(14);
        a4.setName("Q&A with Python Creator, Guido van Rossum");
        a4.setTime("1461391411");
        a4.setUrl("http://blog.techrocket.com/2016/04/21/python-creator-guido-van-rossum-on-how-he-got-his-start-in-programming");
        a4.setPublicationId(98);


        Article a5 = new Article();
        a5.setId(15);
        a5.setName("Tech Shares Fail to Join the Party");
        a5.setTime("1461391052");
        a5.setUrl("http://www.wsj.com/articles/tech-shares-fail-to-join-the-party-1461372935");
        a5.setPublicationId(98);

        articlesForPub1.add(a1);
        articlesForPub1.add(a2);
        articlesForPub1.add(a3);


        articlesForPub2.add(a4);
        articlesForPub2.add(a5);

        String name = "Hacker News";
        Publication hackerNews = new Publication(99, name, R.drawable.hn_logo);
        hackerNews.setArticleList(articlesForPub1);
        publicationMap.put(name, hackerNews);

        name = "Tech Crunch";
        Publication techCrunch = new Publication(98, name, R.drawable.tc_logo);
        techCrunch.setArticleList(articlesForPub2);
        publicationMap.put(name, techCrunch);

        name = "The Verge";
        Publication theVerge = new Publication(97, name, R.drawable.verge_logo);
        theVerge.setArticleList(articlesForPub1);
        publicationMap.put(name, theVerge);

        name = "Fast Company";
        Publication fastCo = new Publication(96, "Fast Company", R.drawable.fc_logo);
        fastCo.setArticleList(articlesForPub2);
        publicationMap.put(name, fastCo);
    }

    public void insertArticleDummyContentToDb() {
        Log.d(TAG, "insertArticleDummyContentToDb");

        ArrayList<Article> articlesForPub1 = getArticleListForPublication("Hacker News");

        if(articlesForPub1.size() > 0) {
            for (Article a: articlesForPub1) {
                NewsReaderApplication.getInstance().getDatasource().createArticle(a.getId(), a.getName(), a.getTime(), a.getUrl(), a.getPublicationId());
            }
        }

        ArrayList<Article> articlesForPub2 = getArticleListForPublication("Tech Crunch");

        if(articlesForPub2.size() > 0) {
            for (Article a: articlesForPub2) {
                NewsReaderApplication.getInstance().getDatasource().createArticle(a.getId(), a.getName(), a.getTime(), a.getUrl(), a.getPublicationId());
            }
        }

    }
}
