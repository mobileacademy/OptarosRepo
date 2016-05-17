package com.mobileacademy.NewsReader.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.data.CachedData;
import com.mobileacademy.NewsReader.models.Article;

import java.util.ArrayList;


public class ArticleListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static String PUBLICATION_EXTRA = "publication_extra";
    public static String PUBLICATION_ID_EXTRA = "publication_id_extra";

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
                articleList = CachedData.getInstance().getArticleListForPublication(publicationName);
                setupList(articleList);

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
        listView = (ListView) findViewById(R.id.lv_articles);
        Log.d(TAG, "articles size: " + articleList.size());

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, articleList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Article article = articleList.get(position);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(article.getUrl()));
        startActivity(i);
    }

    //    Code not used anymore...
    //    Can be started using new LoadArticlesAsync().execute(articleListJson);
//
//    private class LoadArticlesAsync extends AsyncTask<String, Void, ArrayList<Article>> {
//
//        protected ArrayList<Article> doInBackground(String... ids) {
//            ArrayList<Article> articles = new ArrayList<>();
//            try {
//                JSONArray jsonArticlesArray = new JSONArray(ids[0]);
//                //take the first NO_OF_ARTICLES articles
//                for (int i = 0; i < Math.min(NO_OF_ARTICLES, jsonArticlesArray.length()); i++) {
//                    String articleURL = HackerNewsAPI.getArticleById(jsonArticlesArray.getString(i));
//                    String articleString = HackerNewsAPI.retrieveStories(articleURL);
//                    if (articleString == null) continue;
//                    JSONObject articleJson = new JSONObject(articleString);
//                    articles.add(getNewsItemFromJSON(articleJson));
//                }
//
//            } catch (IOException | JSONException e) {
//                Log.e(TAG, "doInBackground: ", e);
//            }
//            return articles;
//
//        }
//
//        protected void onPostExecute(ArrayList<Article> result) {
//            setupList(result);
//            loadingDialog.dismiss();
//        }
//    }
}
