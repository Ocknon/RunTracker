package com.example.the_master.runtracker;

import android.location.Location;
import android.os.Parcel;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.geometry.Point;

import org.json.JSONObject;

/**
 * Created by Hackathon on 2/12/2017.
 */

public class Node
{
    private  Node _previousNode = null;
    private  Node _nextNode = null;
    private Marker mMarker = null;

    public Node GetPreviousNode () { return _previousNode; }
    public void SetPreviousNode (Node node)
    {
        _previousNode = node;
    }

    public Marker GetMarker(){ return  mMarker; }
    public void SetMarker(Marker value){ mMarker = value;}

    public Node GetNextNode ()
    {
        return _nextNode;
    }
    public void SetNextNode (Node node)
    {
        _nextNode = node;
    }

    public LatLng GetLatLng()
    {
        return mMarker.getPosition();
    }
    public void SetLatLng(LatLng point)
    {
        mMarker.setPosition(point);
    }

    public float GetDistanceToNextNode()
    {
        float[] distance = new float[1];
        Location.distanceBetween(GetLatLng().latitude, GetLatLng().longitude,
                                _nextNode.GetLatLng().latitude, _nextNode.GetLatLng().longitude, distance);
        return distance[0];
    }
    public float GetDistanceToNextNode(LatLng point)
    {
        float[] distance = new float[1];
        Location.distanceBetween(GetLatLng().latitude, GetLatLng().longitude,
                                point.latitude, point.longitude, distance);
        return distance[0];
    }
}
