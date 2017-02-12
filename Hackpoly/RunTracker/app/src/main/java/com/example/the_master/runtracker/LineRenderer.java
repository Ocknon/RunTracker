package com.example.the_master.runtracker;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Hackathon on 2/12/2017.
 */

public class LineRenderer
{
    private GoogleMap _map;

    private Node _startNode = null;

    private List<PolyNode> mPolyNodes = new ArrayList<>();

    LineRenderer(GoogleMap map)
    {
        _map = map;
    }

    public PolyNode[] getLinesFromNode(Node a)
    {
        PolyNode[] returnArray = new PolyNode[2];
        int j = 0;
        for(Iterator<PolyNode> i = mPolyNodes.iterator(); i.hasNext(); ){
            PolyNode temp = i.next();
            if(temp.contains(a)){
                returnArray[j++] = temp;
            }
            if(j > 2){
                return returnArray;
            }
        }
        return returnArray;
    }

    public PolyNode getLineFromNodes(Node a, Node b)
    {
        for(Iterator<PolyNode> i = mPolyNodes.iterator(); i.hasNext(); ){
            PolyNode temp = i.next();
            if(temp.isExactLine(a, b)){
                return temp;
            }
        }
        return  null;
    }

    public void AddStartNode(Node startNode)
    {
        _startNode = startNode;
    }

    public void createPolyNode(Node node)
    {
        if(mPolyNodes.size() == 0){
            mPolyNodes.add(PolyNode.create(_startNode, node,_map));
        } else{
            mPolyNodes.add(PolyNode.create(node.GetPreviousNode(), node, _map));
        }
    }

    public void movePolyNode(Node node)
    {
        PolyNode leadingNodeLine = getLineFromNodes(node.GetPreviousNode(), node);
        PolyNode trailingNodeLine = getLineFromNodes(node, node.GetNextNode());

        if(leadingNodeLine != null){
            leadingNodeLine.clearPolyline();
            leadingNodeLine.createPolyline(_map);
        }
        if(trailingNodeLine != null){
            trailingNodeLine.clearPolyline();
            trailingNodeLine.createPolyline(_map);
        }
    }

    public void reset(){
        mPolyNodes = new ArrayList<>();
        _startNode = null;
    }
}
