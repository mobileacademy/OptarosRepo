package com.mobileacademy.NewsReader.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import android.widget.Toast;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mobileacademy.NewsReader.data.MockDataHandler;
import com.mobileacademy.NewsReader.models.Publication;
import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.adapters.PublicationListAdapter;
import com.mobileacademy.NewsReader.services.ListPackagesService;
import com.mobileacademy.NewsReader.services.CounterService;
import com.mobileacademy.NewsReader.services.MyTaskService;
import com.mobileacademy.NewsReader.services.RegistrationGCMIntentService;
import com.mobileacademy.NewsReader.utils.AppSharedPref;
import com.mobileacademy.NewsReader.utils.NotifUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private static String TAG = MainActivity.class.getSimpleName();
    public static String BROADCAST_ACTION = "TimeIsUp";

    private static final String KEY_NAME = "user_name";
    private static final String ARTICLE_EXTRA = "article_extra";

    private static final int PICK_IMAGE_REQUEST = 109;
    private static final int GO_TO_PUBLICATION_REQUEST = 110;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private ArrayList<Publication> list;
    private PublicationListAdapter adapter;
    private BroadcastReceiver countDownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "BroadcastReceived");
            Toast.makeText(MainActivity.this, "Time is up", Toast.LENGTH_SHORT).show();
        }
    };

    private NavigationView navigationView;
    private ImageView userImageView;

    private AppSharedPref mySharedPref;
    private String selectedArticleTitle;

    private boolean isReceiverRegistered;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        userImageView = (ImageView) header.findViewById(R.id.user_picture_view);
        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });


//        addProgramaticalyItemsToDrawerView();


        list = MockDataHandler.getInstance().getPublicationsAsList();

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(countDownReceiver, intentFilter);
        setupGridView();


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "mRegistrationBroadcastReceiver - onReceive");
                if (intent.getAction().equals(MyTaskService.ACTION_DONE)) {

                    String tag = intent.getStringExtra(MyTaskService.EXTRA_TAG);
                    int result = intent.getIntExtra(MyTaskService.EXTRA_RESULT, -1);

                    String msg = String.format("DONE: %s (%d)", tag, result);
                    Log.d(TAG, "msg: " + msg);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(RegistrationGCMIntentService.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        Log.d(TAG, "token sent!");
                    } else {
                        Log.d(TAG, "token not sent!");
                    }
                }
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            Log.d(TAG, "playservices OK - start RegistrationGCMIntentService!");
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationGCMIntentService.class);
            startService(intent);
        }

        MyTaskService.startChargingTask(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(countDownReceiver);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){

        IntentFilter myTaskServiceFilter = new IntentFilter();
        myTaskServiceFilter.addAction(MyTaskService.ACTION_DONE);

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                myTaskServiceFilter);

        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(RegistrationGCMIntentService.REG_COMPLETE_ACTION));
            isReceiverRegistered = true;
        }
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

        if (id == R.id.item_count) {
            Intent service = new Intent(this, CounterService.class);
            service.setAction(CounterService.ACTION_COUNT);
            this.startService(service);
        } else if (id == R.id.item_package_list) {
            Intent service = new Intent(this, ListPackagesService.class);
            this.startService(service);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            NotifUtils.scheduleNotification(MainActivity.this, NotifUtils.getCustomNotif(MainActivity.this), 5000);

        } else if (id == R.id.nav_send) {
            sendMessageTo();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, ArticleListActivity.class);
        i.putExtra(ArticleListActivity.PUBLICATION_EXTRA, list.get(position).getName());
        i.putExtra(ArticleListActivity.PUBLICATION_ID_EXTRA, list.get(position).getId());
        startActivityForResult(i, GO_TO_PUBLICATION_REQUEST);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PICK_IMAGE_REQUEST:
                    if (data.getData() != null) {
                        Uri uri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            userImageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            Log.e(TAG, "Error, " + e);
                        }
                    }
                    break;
                case GO_TO_PUBLICATION_REQUEST:
                    selectedArticleTitle = data.getStringExtra(ARTICLE_EXTRA);
                    break;
                default:
                    Log.e(TAG, "invalid request code!");
                    break;
            }
        }

    }

    /**
     * Method to create an Intent to allow user to pic a picture from a source
     */
    private void choosePicture() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // Always use string resources for UI text.
        // This says something like "Share this photo with"
        String title = getResources().getString(R.string.chooser_title);
        // Create intent to show the chooser dialog(if there are multiple options available)
        Intent chooser = Intent.createChooser(intent, title);

        // Verify the original intent will resolve to at least one activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, PICK_IMAGE_REQUEST);
        }
    }

    private void sendMessageTo() {
        if(!TextUtils.isEmpty(selectedArticleTitle)) {
            // Create the text message with a string
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, selectedArticleTitle);
            sendIntent.setType("text/plain");

            // Verify that the intent will resolve to an activity
            if (sendIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(sendIntent);
            }
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
