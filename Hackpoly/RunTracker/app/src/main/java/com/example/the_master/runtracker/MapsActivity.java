package com.example.the_master.runtracker;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "Location";
    private static final byte PERMISSION_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private LocationFactory mLocationFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_ACCESS_FINE_LOCATION);
        mLocationFactory = new LocationFactory(this);
        setContentView(R.layout.fragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public boolean checkPermission(String permission)
    {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(String permission, int id)
    {
        if(!checkPermission(permission))
        {
            ActivityCompat.requestPermissions(this,new String[]{permission},id);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationFactory.locationPermissionGranted = true;
                    mLocationFactory.startLocationRequests();

                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Map Ready");
        mMap = googleMap;

        LatLng currentPos;
        if(mLocationFactory.mLastLocation != null)
        {
            currentPos =  new LatLng(mLocationFactory.mLastLocation.getLatitude(),mLocationFactory.mLastLocation.getLongitude());
        }else
        {
            currentPos = new LatLng(-34, 151);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
        mMap.setOnMapClickListener(this);
    }

    public void updateCurrentLocation(Location data)
    {
        Log.d(TAG, " Location Callback");
        if(data != null)
        {
            Log.d(TAG, "Setting Location");
            LatLng currentPos =  new LatLng(mLocationFactory.mLastLocation.getLatitude(),mLocationFactory.mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
            onMapClick(currentPos);

        }

    }

    @Override
    public void onMapClick(LatLng point)
    {
        mMap.addMarker(new MarkerOptions().position(point).title("we did it boyz"));

    }
}
