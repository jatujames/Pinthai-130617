package com.pinthai.fragment;

import android.Manifest;
import android.app.Fragment;

import android.content.pm.PackageManager;

import android.location.Location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.view.ContextMenu;
import android.view.LayoutInflater;

import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.pinthai.R;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by WE on 2/22/2017.
 */

public class GmapFragment extends Fragment implements OnMapReadyCallback {






    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gmap, container, false);

        /*Button button = (Button) view.findViewById(R.id.addpin);
        registerForContextMenu(button);*/
        return view;

    }

    @Override

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.activity_main);


    }



    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                // googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("My Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),16));
                googleMap.setOnMyLocationChangeListener(null);
            }
        });
        //LatLng marker = new LatLng(location.getLatitude(), location.getLongitude());
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 18));
        // googleMap.addMarker(new MarkerOptions().title("TEST จ้า").position(marker));
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {


            @Override

            public void onMapLongClick(LatLng point) {
               /* googleMap.setMapType( googleMap.getMapType() ==
                        GoogleMap.MAP_TYPE_NORMAL ?
                        GoogleMap.MAP_TYPE_HYBRID :
                        GoogleMap.MAP_TYPE_NORMAL);*/
                registerForContextMenu(getView());
                //addmarker(point);

                googleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(point.toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

            }
        });

    }

    public void addmarker(LatLng point){
        /*Button button = (Button) getActivity().findViewById(R.id.map);
        registerForContextMenu(button);*/

        Toast.makeText(getApplicationContext(),
                "เพิ่มหมุดใหม่ที่พิกัด" +"\n"+ point.toString(), Toast.LENGTH_LONG)
                .show();


    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info) {
        super.onCreateContextMenu(menu, v, info);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.longpress_gmap_menu, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.option1:
                return true;
            case R.id.option2:
                return true;
            default: return super.onContextItemSelected(item);
        }
    }
   /* public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.longpress_gmap_menu, menu);
        //return super.onCreateOptionsMenu(menu);
    }*/

}
