package com.mobileacademy.NewsReader.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.data.MockDataHandler;
import com.mobileacademy.NewsReader.models.Article;
import com.mobileacademy.NewsReader.utils.HackerNewsApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ArticleListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, Callback {

    public static String PUBLICATION_EXTRA = "publication_extra";
    public static String PUBLICATION_ID_EXTRA = "publication_id_extra";
    private static final int NO_OF_ARTICLES = 20;

    public static String TAG = ArticleListActivity.class.getSimpleName();
    private ArrayList<Article> articleList;
    private int publicationId = 0;

    private ListView listView;

    @Override
    /**
     * In the onCreate method the publication name is got via the intent using the {@link PUBLICATION_EXTRA}
     * tag.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        listView = (ListView) findViewById(R.id.lv_articles);


        Intent i = getIntent();

        String publicationName;

        if (i != null && (publicationId = i.getIntExtra(PUBLICATION_ID_EXTRA, 0)) != 0
                && (publicationName = i.getStringExtra(PUBLICATION_EXTRA)) != null) {
            Log.d(TAG, "publicationId - " + publicationId);

            if (publicationId != -1) {
                // get articles from db by publication id
                //articleList = (ArrayList) NewsReaderApplication.getInstance().getDatasource().getAllArticlesByPublication(publicationId);
                loadArticlesFromServer(publicationName);
            }
        } else {
            Log.e(TAG, "The publication id && publication name should have been passed in the intent");
            //finish the activity
            finish();
            return;
        }

    }

    private void setupList(ArrayList<Article> articleList) {
        this.articleList = articleList;
        Log.d(TAG, "articles size: " + articleList.size());

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, articleList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void loadArticlesFromServer(String publicationId) {
        switch (publicationId) {
            case MockDataHandler.HACKER_NEWS:
                retrieveByUrl(HackerNewsApi.TOP_STORIES_ENDPOINT);
                return;
            case MockDataHandler.FAST_COMPANY:
                retrieveByUrl(HackerNewsApi.NEW_STORIES_ENDPOINT);
                return;
            default:
                new ArrayList<>();
                return;
        }
    }

    private void retrieveByUrl(String url) {
        try {
            HackerNewsApi.retrieveStories(url, this);
        } catch (IOException e) {
            Log.e(TAG, "ERROR retrieve by url", e);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Article article = articleList.get(position);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(article.getUrl()));
        startActivity(i);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, "failed to retrieve data", e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String articleListJson = response.body().string();
        Log.d(TAG, "YEEEEEY " + articleListJson);

        new LoadArticlesAsync().execute(articleListJson);

    }

    private class LoadArticlesAsync extends AsyncTask<String, Void, ArrayList<Article>> {

        protected ArrayList<Article> doInBackground(String... urls) {
            ArrayList<Article> articles = new ArrayList<>();
            try {
            JSONArray jsonArticlesArray = new JSONArray(urls[0]);
                //take the first NO_OF_ARTICLES articles
                for (int i = 0; i < NO_OF_ARTICLES; i++) {
                    String articleURL = HackerNewsApi.getArticleById(jsonArticlesArray.getString(i));
                    String articleString = HackerNewsApi.retrieveStories(articleURL);
                    JSONObject articleJson = new JSONObject(articleString);
                    articles.add(getNewsItemFromJSON(articleJson));
                }

            }catch (IOException|JSONException e){
                Log.e(TAG, "doInBackground: ", e);
            }
            return articles;

        }

        protected void onPostExecute(ArrayList<Article> result) {
            setupList(result);
        }
    }

}
