package com.mobileacademy.NewsReader.activities;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.fragments.ArticleFragment;
import com.mobileacademy.NewsReader.models.Article;

public class ArticleListActivity extends AppCompatActivity implements ArticleFragment.OnListFragmentInteractionListener{
    public static String PUBLICATION_EXTRA = "publication_extra";
    public static String PUBLICATION_ID_EXTRA = "publication_id_extra";
    private static String NAME_EXTRA = "name";
    private static String ARTICLE_FRAGMENT_TAG = "article_tag";

    public static String TAG = ArticleListActivity.class.getSimpleName();
    private int publicationId = 0;


    @Override
    /**
     * In the onCreate method the publication name is got via the intent using the {@link PUBLICATION_EXTRA}
     * tag.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);


        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Intent i = getIntent();

        String publicationName;

        if (i != null && (publicationId = i.getIntExtra(PUBLICATION_ID_EXTRA, 0)) != 0
                && (publicationName = i.getStringExtra(PUBLICATION_EXTRA)) != null) {
            Log.d(TAG, "publicationId - " + publicationId);

            if (publicationId != -1) {
                ArticleFragment articleFragment;
                Bundle args = new Bundle();

                if(fragmentManager.findFragmentByTag(ARTICLE_FRAGMENT_TAG) == null) {
                    articleFragment = new ArticleFragment();
                } else {
                    articleFragment = (ArticleFragment) fragmentManager.findFragmentByTag(ARTICLE_FRAGMENT_TAG);
                }
                args.putString(NAME_EXTRA, publicationName);
                articleFragment.setArguments(args);

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                fragmentTransaction.replace(R.id.fragment_container, articleFragment, ARTICLE_FRAGMENT_TAG);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();

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
    }

    @Override
    public void onListFragmentArticleSelected(Article article) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(article.getUrl()));
        startActivity(i);
    }
}