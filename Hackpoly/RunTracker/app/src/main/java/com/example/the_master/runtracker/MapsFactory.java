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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MapsFactory extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private LineRenderer mLineRenderer;

    private static final String TAG = "Debug";

    private GoogleMap mMap;
    private LocationFactory mLocationFactory;

    private List<Node> _nodeList = new ArrayList<Node>();

    private boolean mLocationSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        PermissionsUtil.requestPermission(this,Manifest.permission.ACCESS_FINE_LOCATION, PermissionsUtil.PERMISSION_ACCESS_FINE_LOCATION);
        mLocationFactory = new LocationFactory(this);
        setContentView(R.layout.fragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtil.PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationFactory.locationPermissionGranted = true;
                    mLocationFactory.startLocationRequests();
                } else {

                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Map Ready");
        mMap = googleMap;

        mLineRenderer = new LineRenderer(mMap);
        mMap.setOnMapClickListener(this);
    }

    public void updateCurrentLocation(Location data)
    {
        if(data != null)
        {
            if(!mLocationSet)
            {
                mLocationSet = true;
                LatLng currentPos =  new LatLng(mLocationFactory.mLastLocation.getLatitude(),mLocationFactory.mLastLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 20));
                onMapClick(currentPos);
                mLineRenderer.AddStartNode(_nodeList.get(0));
            }
        }

    }

    @Override
    public void onMapClick(LatLng point)
    {
        if(!mLocationSet){return;}
        Marker mark = mMap.addMarker(new MarkerOptions().position(point).title("we did it boyz"));
        _PlaceNode(mark);
    }

    private void _PlaceNode(Marker mark)
    {
        Node node = new Node();
        node.SetLatLng(mark.getPosition());

        _nodeList.add(node);
        if (_nodeList.size() > 1)
        {
            Node previousNode = _nodeList.get(_nodeList.size()-2);
            node.SetPreviousNode(previousNode);
            previousNode.SetNextNode(node);
            float distance = previousNode.GetDistanceToNextNode();
            mLineRenderer.DrawLine(node);
        }

    }
}
