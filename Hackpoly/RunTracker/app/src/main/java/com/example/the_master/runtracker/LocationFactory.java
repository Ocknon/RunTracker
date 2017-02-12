package com.example.the_master.runtracker;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by The_Master on 2/12/2017.
 */

public class LocationFactory implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int INTERVAL = 500000;
    private static final int FASTEST_INTERVAL = 30000;
    private static final String TAG = "Location";


    public boolean connected = false;
    public boolean locationPermissionGranted = false;

    LocationRequest mLocationRequest;
    MapsActivity mMapsActivity;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private boolean mSendingLocationRequests;


    public LocationFactory(MapsActivity mapsActivity) {
        mMapsActivity = mapsActivity;
        locationPermissionGranted = mMapsActivity.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        mGoogleApiClient = new GoogleApiClient.Builder(mapsActivity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        Log.d(TAG, "Init Location Factory");
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        connected = true;
        if(!mSendingLocationRequests && locationPermissionGranted) {
            startLocationRequests();
        }
        //mMapsActivity.updateCurrentLocation(mLastLocation);
        Log.d(TAG, "Play services connection Succeeded");
    }



    private void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @SuppressWarnings("MissingPermission")
    public void startLocationRequests()
    {

        if(connected && !mSendingLocationRequests) {
            createLocationRequest();
            mSendingLocationRequests = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        mMapsActivity.updateCurrentLocation(mLastLocation);
        Log.d(TAG, "Updating Location");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        Log.d(TAG, "Play services connection Failed");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Play services connection suspended");
    }




}
