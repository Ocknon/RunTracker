package com.example.the_master.runtracker;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.maps.android.ui.IconGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.example.the_master.runtracker.R.id.map;


public class MapsFactory extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, ActivityCompat.OnRequestPermissionsResultCallback, AdapterView.OnItemSelectedListener, GoogleMap.OnMarkerDragListener {

    private LineRenderer mLineRenderer;
    private float mTotalDistance = 0f;
    public IconGenerator mGenerator;
    private Bitmap mIconBitmap;

    private static final String TAG = "Debug";
    private Map<Integer, Node> mNodeDict = new Hashtable<Integer, Node>();
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
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        setUpButtons();

        mGenerator = new IconGenerator(this);
        mGenerator.setStyle(IconGenerator.STYLE_GREEN);
        mIconBitmap = mGenerator.makeIcon("START");
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
        mMap = googleMap;

        mLineRenderer = new LineRenderer(mMap, mGenerator);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);
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

        if (_nodeList.size() > 0)
        {
            Node previousNode = _nodeList.get(_nodeList.size() - 1);
            float dist = previousNode.GetDistanceToNextNode(point);
            dist /= 1609.344;
            BigDecimal bd = new BigDecimal(dist);
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);

            mIconBitmap = mGenerator.makeIcon(df.format(bd).toString() + " miles");
        }

        Marker mark = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(mIconBitmap)).position(point));

        mark.setDraggable(true);
        _PlaceNode(mark);
    }

    @Override
    public void onMarkerDragStart (Marker mark)
    {

    }

    @Override
    public void onMarkerDrag (Marker mark)
    {
        Node node = mNodeDict.get(mark.hashCode());
        mLineRenderer.movePolyNode(node);
    }

    @Override
    public void onMarkerDragEnd (Marker mark)
    {

    }



    private void _PlaceNode(Marker mark)
    {
        Node node = new Node();
        node.SetMarker(mark);
        mNodeDict.put(mark.hashCode(), node);
        _nodeList.add(node);
        if (_nodeList.size() > 1)
        {
            Node previousNode = _nodeList.get(_nodeList.size()-2);
            node.SetPreviousNode(previousNode);
            previousNode.SetNextNode(node);
            float distance = previousNode.GetDistanceToNextNode();
            mTotalDistance += distance;
            mLineRenderer.createPolyNode(node);
        }

    }

    public void clearMapData(){
        mMap.clear();
        mTotalDistance = 0;
        mNodeDict = new Hashtable<Integer, Node>();
        _nodeList = new ArrayList<Node>();
        mIconBitmap = mGenerator.makeIcon("START");
        LatLng currentPos =  new LatLng(mLocationFactory.mLastLocation.getLatitude(),mLocationFactory.mLastLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 20));
        onMapClick(currentPos);
        mLineRenderer.reset();
        mLineRenderer.AddStartNode(_nodeList.get(0));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        pos++;
        mMap.setMapType(pos);
        Log.d(TAG, "Chose selection");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

    public void setUpButtons(){
        final Button button = (Button) findViewById(R.id.clear);
        final Spinner spinner = (Spinner) findViewById(R.id.map_selection);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.map_types_a, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMapData();
            }
        });
        spinner.setOnItemSelectedListener(this);
    }
}
