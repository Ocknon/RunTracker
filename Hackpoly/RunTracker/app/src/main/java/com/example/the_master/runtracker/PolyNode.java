package com.example.the_master.runtracker;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by The_Master on 2/12/2017.
 */

public class PolyNode {

    Polyline mPolyline;
    public Node a,b;

    public static PolyNode create(Node a, Node b, GoogleMap map)
    {
        PolyNode instance = new PolyNode();

        instance.a = a;
        instance.b = b;
        instance.createPolyline(map);
        //create line from nodes
        return  instance;
    }

    public boolean contains(Node node){

        return  (a != null && node == a) || (b != null && node == b);
    }

    public boolean isExactLine(Node node1, Node node2){
        if(a == null || b == null){return false;}
        return (node1 == a && node2 == b) || (node1 == b && node2 == a);
    }

    public void createPolyline(GoogleMap map){
        mPolyline = map.addPolyline(new PolylineOptions().add(a.GetLatLng(),b.GetLatLng()).width(2).color(Color.RED));
    }

    public void clearPolyline(){
        if(mPolyline != null){
            mPolyline.remove();
        }
    }

}
