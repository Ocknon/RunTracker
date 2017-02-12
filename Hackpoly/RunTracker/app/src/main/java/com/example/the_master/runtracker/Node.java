package com.example.the_master.runtracker;

import android.location.Location;
import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.geometry.Point;

import org.json.JSONObject;

/**
 * Created by Hackathon on 2/12/2017.
 */

public class Node
{
    private  Node _previousNode = null;
    private  Node _nextNode = null;
    private LatLng _point = null;

    public Node GetPreviousNode (Node currentNode)
    {
        return currentNode._previousNode;
    }
    public void SetPreviousNode (Node node)
    {
        _previousNode = node;
    }

    public Node GetNextNode (Node currentNode)
    {
        return currentNode._nextNode;
    }
    public void SetNextNode (Node node)
    {
        _nextNode = node;
    }

    public LatLng GetLatLng()
    {
        return _point;
    }
    public void SetLatLng(LatLng point)
    {
        _point = point;
    }

    public float GetDistanceToNextNode(Node currentNode)
    {
        float[] distance = new float[1];
        Location.distanceBetween(currentNode.GetLatLng().latitude, currentNode.GetLatLng().longitude,
                                currentNode.GetNextNode(currentNode).GetLatLng().latitude, currentNode.GetNextNode(currentNode).GetLatLng().longitude, distance);
        return distance[0];
    }
}
