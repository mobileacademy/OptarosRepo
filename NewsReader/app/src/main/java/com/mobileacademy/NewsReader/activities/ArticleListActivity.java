package com.mobileacademy.NewsReader.activities;

import android.app.ProgressDialog;
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

import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.data.MockDataHandler;
import com.mobileacademy.NewsReader.models.Article;
import com.mobileacademy.NewsReader.utils.HackerNewsAPI;

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
    ProgressDialog loadingDialog;

    @Override
    /**
     * In the onCreate method the publication name is got via the intent using the {@link PUBLICATION_EXTRA}
     * tag.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        Intent i = getIntent();

        String publicationName;

        if (i != null && (publicationId = i.getIntExtra(PUBLICATION_ID_EXTRA, 0)) != 0
                && (publicationName = i.getStringExtra(PUBLICATION_EXTRA)) != null) {
            Log.d(TAG, "publicationId - " + publicationId);

            if (publicationId != -1) {

                loadingDialog = new ProgressDialog(this);
                loadingDialog.setCancelable(false);
                loadingDialog.setMessage("Loading");


                // get articles from db by publication id
                //articleList = (ArrayList) NewsReaderApplication.getInstance().getDatasource().getAllArticlesByPublication(publicationId);
                listView = (ListView) findViewById(R.id.lv_articles);
                loadArticlesFromServer(publicationName);
            }
        } else {
            Log.e(TAG, "The publication id && publication name should have been passed in the intent");
            //finish the activity
            finish();
            return;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
    }

    private void setupList(ArrayList<Article> articleList) {
        this.articleList = articleList;
        Log.d(TAG, "articles size: " + articleList.size());

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, articleList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void loadArticlesFromServer(String publicationName) {
        switch (publicationName) {
            case MockDataHandler.HACKER_NEWS:
                loadingDialog.show();
                retrieveByUrl(HackerNewsAPI.TOP_STORIES_ENDPOINT);
                return;
            case MockDataHandler.FAST_COMPANY:
                loadingDialog.show();
                retrieveByUrl(HackerNewsAPI.NEW_STORIES_ENDPOINT);
                return;
            default:
                return;
        }
    }

    private void retrieveByUrl(String url) {
        try {
            HackerNewsAPI.retrieveStories(url, this);
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
        loadingDialog.dismiss();
        finish();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String articleListJson = response.body().string();
        Log.d(TAG, "Received articles array " + articleListJson);

        new LoadArticlesAsync().execute(articleListJson);

    }

    private class LoadArticlesAsync extends AsyncTask<String, Void, ArrayList<Article>> {

        protected ArrayList<Article> doInBackground(String... ids) {
            ArrayList<Article> articles = new ArrayList<>();
            try {
            JSONArray jsonArticlesArray = new JSONArray(ids[0]);
                //take the first NO_OF_ARTICLES articles
                for (int i = 0; i < Math.min(NO_OF_ARTICLES,jsonArticlesArray.length()); i++) {
                    String articleURL = HackerNewsAPI.getArticleById(jsonArticlesArray.getString(i));
                    String articleString = HackerNewsAPI.retrieveStories(articleURL);
                    if(articleString == null) continue;
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
            loadingDialog.dismiss();
        }
    }

}
