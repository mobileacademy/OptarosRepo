package com.mobileacademy.NewsReader.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.utils.LocationUtils;
import com.mobileacademy.NewsReader.utils.PermissionUtils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static int DEFAULT_ZOOM_VALUE = 15;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        if(isLocationPermissionsGranted()){
            gotoCurrentLocation();
        }
    }

    private void gotoCurrentLocation() {
        // Add a marker in the current location and move the camera
        LatLng currentLocation = LocationUtils.getCurrentLocation(this);
        if (currentLocation != null) {
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM_VALUE));
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
}
