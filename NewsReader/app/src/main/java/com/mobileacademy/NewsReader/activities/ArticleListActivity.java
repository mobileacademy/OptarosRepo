package com.mobileacademy.NewsReader.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.fragments.NewStoriesArticleFragment;
import com.mobileacademy.NewsReader.fragments.TopStoriesArticleFragment;
import com.mobileacademy.NewsReader.models.Article;

public class ArticleListActivity extends AppCompatActivity implements TopStoriesArticleFragment.OnListFragmentInteractionListener,
        NewStoriesArticleFragment.OnListFragmentInteractionListener {

    public static String TAG = ArticleListActivity.class.getSimpleName();
    public static String PUBLICATION_EXTRA = "publication_extra";
    public static String PUBLICATION_ID_EXTRA = "publication_id_extra";

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private int publicationId = 0;

    @Override
    /**
     * In the onCreate method the publication name is got via the intent using the {@link PUBLICATION_EXTRA}
     * tag.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_main);

        mPager = (ViewPager) findViewById(R.id.vpPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Top Stories"));
        tabLayout.addTab(tabLayout.newTab().setText("New Stories"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


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
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                if(fragmentManager.findFragmentByTag("top_stories_tag") == null) {
//                    topStoriesArticleFragment = new TopStoriesArticleFragment();
//                } else {
//                    topStoriesArticleFragment = (TopStoriesArticleFragment) fragmentManager.findFragmentByTag("top_stories_tag");
//                }
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack
//                fragmentTransaction.replace(R.id.fragment_container, topStoriesArticleFragment, "top_stories_tag");
//                fragmentTransaction.addToBackStack(null);
//
//                fragmentTransaction.commit();
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
        private int tabs;

        public MyPagerAdapter(FragmentManager fragmentManager, int NumOfTabs) {
            super(fragmentManager);
            tabs = NumOfTabs;
        }
        // Returns total number of pages
        @Override
        public int getCount() {
            return tabs;
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