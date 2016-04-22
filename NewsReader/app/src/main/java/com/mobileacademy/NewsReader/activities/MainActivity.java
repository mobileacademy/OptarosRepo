package com.mobileacademy.NewsReader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileacademy.NewsReader.data.CachedData;
import com.mobileacademy.NewsReader.models.Publication;
import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.adapters.PublicationListAdapter;
import com.mobileacademy.NewsReader.utils.AppSharedPref;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private static final String KEY_NAME = "user_name";
    private ArrayList<Publication> list;
    private PublicationListAdapter adapter;
    private NavigationView navigationView;
    private AppSharedPref mySharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySharedPref = new AppSharedPref(this);
        mySharedPref.addStringToSharePref(KEY_NAME, "Valerica Plesu");

        // inflate activity toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // set the Toolbar to act as the ActionBar
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                drawer,/* DrawerLayout object */
                toolbar,/* nav drawer icon to replace 'Up' caret */
                R.string.navigation_drawer_open, /* "open drawer" description */
                R.string.navigation_drawer_close)/* "close drawer" description */ {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                Toast.makeText(MainActivity.this, "Drawer Closed", Toast.LENGTH_SHORT).show();
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Toast.makeText(MainActivity.this, "Drawer Open", Toast.LENGTH_SHORT).show();
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawer.setDrawerListener(toggle);

        // to sync the indicator to match the current state of the navigation drawer:
        //calling sync state is necessay or else your hamburger icon wont show up
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // add listener for the navigation item selected
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView drawerTitleView = (TextView) header.findViewById(R.id.user_name_tv);
        drawerTitleView.setText("User Name");


//        addProgramaticalyItemsToDrawerView();


        list = CachedData.getInstance().getPublicationsAsList();
        setupGridView();

    }

    private void setupGridView() {
        GridView gridView = (GridView) findViewById(R.id.gv_source);
        adapter = new PublicationListAdapter(this, list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, ArticleListActivity.class);
        i.putExtra(ArticleListActivity.PUBLICATION_EXTRA, list.get(position).getName());
        startActivity(i);
    }

    /**
     *  Add items to left navigation view
     */
    private void addProgramaticalyItemsToDrawerView() {
        final Menu menu = navigationView.getMenu();
        for (int i = 1; i <= 3; i++) {
            menu.add("Runtime item "+ i);
        }

        // adding a section and items into it
        final SubMenu subMenu = menu.addSubMenu("SubMenu Title");
        for (int i = 1; i <= 2; i++) {
            subMenu.add("SubMenu Item " + i);
        }

        //refresh the adapter
        for (int i = 0, count = navigationView.getChildCount(); i < count; i++) {
            final View child = navigationView.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged();
            }
        }
    }
}
