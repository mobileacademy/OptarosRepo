package com.mobileacademy.NewsReader.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.events.NewArticlesEvent;
import com.mobileacademy.NewsReader.services.FetchArticlesService;
import com.mobileacademy.NewsReader.utils.LocationUtils;
import com.mobileacademy.NewsReader.utils.PermissionUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private static int DEFAULT_ZOOM_VALUE = 15;
    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //TODO: @Daniela delete
        startService(new Intent(this, FetchArticlesService.class));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isLocationPermissionsGranted()) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    PermissionUtils.wasPermissionRequested(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            } else {
                PermissionUtils.requestPermissionWithRationale(this, Manifest.permission.ACCESS_FINE_LOCATION,
                        PermissionUtils.REQUEST_LOCATION);
            }
        }

    }

    public boolean isLocationPermissionsGranted() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (isLocationPermissionsGranted()) {
            gotoCurrentLocation();
        }
    }

    private void gotoCurrentLocation() {
        // Add a marker in the current location and move the camera
        LatLng currentLocation = LocationUtils.getCurrentLocation(this);

        //for testing purposes in emulator, blv Magheru
        currentLocation = currentLocation == null ? new LatLng(44.442606, 26.098843) : currentLocation;

        if (currentLocation != null) {
            marker = mMap.addMarker(new MarkerOptions().position(currentLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM_VALUE));

            new GetAddressFromLatLongAsync(this).execute(currentLocation);
        } else {
            Toast.makeText(this, "Unable to get your location", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PermissionUtils.REQUEST_LOCATION) {
            if (mMap != null && grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoCurrentLocation();
            }
        }
    }

    private class GetAddressFromLatLongAsync extends AsyncTask<LatLng, Void, String> {

        WeakReference<MapsActivity> activityReference;

        public GetAddressFromLatLongAsync(MapsActivity activity) {
            activityReference = new WeakReference(activity);
        }

        @Override
        protected String doInBackground(LatLng... params) {

            if (activityReference == null || activityReference.get() == null) {
                return null;
            }

            MapsActivity activity = activityReference.get();

            Geocoder geocoder = new Geocoder(activity);
            LatLng latLng = params[0];
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {

                    Address address = addresses.get(0);
                    ArrayList<String> addressFragments = new ArrayList<String>();

                    // Fetch the address chunks
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        addressFragments.add(address.getAddressLine(i));
                    }

                    String addressString = TextUtils.join(System.getProperty("line.separator"),
                            addressFragments);
                    Log.d(TAG, "latLngToAddress: " + address.getMaxAddressLineIndex() + ":" + addressString);
                    return addressString;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String address) {
            if (activityReference == null && activityReference.get() == null) {
                return;
            }
            MapsActivity activity = activityReference.get();
            activity.marker.setTitle(address);
        }
    }
}
