package com.example.the_master.runtracker;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hackathon on 2/12/2017.
 */

public class LineRenderer
{
    private GoogleMap _map;

    private Node _startNode = null;
    private Node _endNode = null;

    private Polyline _line;

    private List<LatLng> _latLngList = new ArrayList<LatLng>();

    LineRenderer(GoogleMap map)
    {
        _map = map;
    }

    public void AddStartNode(Node startNode)
    {
        _startNode = startNode;
        _latLngList.add(startNode.GetLatLng());
    }

    public void DrawLine(Node node)
    {
        _latLngList.add(node.GetLatLng());
        if (_latLngList.size() > 1)
        {
            _line = _map.addPolyline(new PolylineOptions()
                    .add(_latLngList.get(_latLngList.size() - 2), node.GetLatLng())
                    .width(2)
                    .color(Color.RED));
        }
    }
}
