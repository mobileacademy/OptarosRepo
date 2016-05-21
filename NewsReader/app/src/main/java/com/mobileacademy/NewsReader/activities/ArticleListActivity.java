package com.mobileacademy.NewsReader.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.data.MockDataHandler;
import com.mobileacademy.NewsReader.fragments.NewStoriesArticleFragment;
import com.mobileacademy.NewsReader.fragments.TopStoriesArticleFragment;
import com.mobileacademy.NewsReader.models.Article;

public class ArticleListActivity extends AppCompatActivity implements TopStoriesArticleFragment.OnListFragmentInteractionListener,
        NewStoriesArticleFragment.OnListFragmentInteractionListener {

    public static String TAG = ArticleListActivity.class.getSimpleName();
    public static String PUBLICATION_EXTRA = "publication_extra";
    public static String PUBLICATION_ID_EXTRA = "publication_id_extra";
    private static String NAME_EXTRA = "name";
    private static String ARTICLE_FRAGMENT_TAG = "article_tag";

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;

    private int publicationId = 0;

    @Override
    /**
     * In the onCreate method the publication name is got via the intent using the {@link PUBLICATION_EXTRA}
     * tag.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_main);

        mFragmentManager = getSupportFragmentManager();
        mPager = (ViewPager) findViewById(R.id.vpPager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


        // Attach the page change listener inside the activity
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(ArticleListActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });


        Intent i = getIntent();
        String publicationName;
        if (i != null && (publicationId = i.getIntExtra(PUBLICATION_ID_EXTRA, 0)) != 0
                && (publicationName = i.getStringExtra(PUBLICATION_EXTRA)) != null) {
            Log.d(TAG, "publicationId - " + publicationId);

//            if (publicationId != -1) {
//                TopStoriesArticleFragment topStoriesArticleFragment;
//                Bundle args = new Bundle();
//
//                if(fragmentManager.findFragmentByTag(ARTICLE_FRAGMENT_TAG) == null) {
//                    topStoriesArticleFragment = new TopStoriesArticleFragment();
//                } else {
//                    topStoriesArticleFragment = (TopStoriesArticleFragment) fragmentManager.findFragmentByTag(ARTICLE_FRAGMENT_TAG);
//                }
//                args.putString(NAME_EXTRA, publicationName);
//                topStoriesArticleFragment.setArguments(args);
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack
//                fragmentTransaction.replace(R.id.fragment_container, topStoriesArticleFragment, ARTICLE_FRAGMENT_TAG);
//                fragmentTransaction.addToBackStack(null);
//
//                fragmentTransaction.commit();
//
//            }
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
    public void onNewStoryArticleSelected(Article article) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(article.getUrl()));
        startActivity(i);
    }

    @Override
    public void onTopStoryArticleSelected(Article item) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(item.getUrl()));
        startActivity(i);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return TopStoriesArticleFragment.newInstance();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return NewStoriesArticleFragment.newInstance();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            switch (position) {
                case 0:
                    title = "TopStories";
                    break;
                case 1:
                    title = "NewStories";
                    break;
            }
            return title;
        }

    }
}