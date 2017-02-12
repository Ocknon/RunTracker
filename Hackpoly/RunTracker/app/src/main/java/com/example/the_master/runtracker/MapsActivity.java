package com.example.the_master.runtracker;

import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.webkit.ConsoleMessage;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Console;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private GoogleMap mMap;
    private LineRenderer _line;

    private List<Node> _nodeList = new ArrayList<Node>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        Marker mark = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        _PlaceNode(mark);
        _line = new LineRenderer(mMap);
        _line.AddStartNode(_nodeList.get(0));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng point)
    {
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
            _line.DrawLine(node);
        }

    }
}
