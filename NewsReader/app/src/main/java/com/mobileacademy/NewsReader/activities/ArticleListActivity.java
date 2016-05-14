package com.mobileacademy.NewsReader.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mobileacademy.NewsReader.NewsReaderApplication;
import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.data.CachedData;
import com.mobileacademy.NewsReader.models.Article;

import java.util.ArrayList;

public class ArticleListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public static String PUBLICATION_EXTRA = "publication_extra";
    public static String PUBLICATION_ID_EXTRA = "publication_id_extra";
    private static final String EXTRA_ARTICLE = "article_extra";

    public static String TAG = ArticleListActivity.class.getSimpleName();
    private ArrayList articleList;
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

        if(i!=null && (publicationId = i.getIntExtra(PUBLICATION_ID_EXTRA, 0))!=0){
            Log.d(TAG, "publicationId - " + publicationId);

            if(publicationId != -1) {
                // get articles from db by publication id
                 articleList = (ArrayList) NewsReaderApplication.getInstance().getDatasource().getAllArticlesByPublication(publicationId);
                Log.d(TAG, "articles size: " + articleList.size());

                ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, articleList);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);

            }
//            renderArticleList(publicationName);
        }else{
            Log.e(TAG, "The publication id should have been passed in the intent");
            //finish the activity
            finish();
            return;
        }

    }

    /**
     * Method for rendering the list of articles for a given publication.
     * <p>The list of articles is took from the {@link CachedData} singleton</>
     * <p>The adapter will use {@link Article#toString()} method to get the name of the articles it
     * needs to display in the ListView </>
     * @param publicationName
     */
    private void renderArticleList(String publicationName) {

        articleList = CachedData.getInstance().getArticleListForPublication(publicationName);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, articleList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String article = articleList.get(position)+" was clicked";
        Toast.makeText(this, article, Toast.LENGTH_SHORT).show();
        returnSelectedArticle(article);
    }

    private void returnSelectedArticle(String article) {
        Intent result = new Intent();
        result.putExtra(EXTRA_ARTICLE, article);
        setResult(RESULT_OK, result);
        finish();
    }
}
