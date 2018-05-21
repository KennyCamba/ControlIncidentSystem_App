package com.app.computacionysociedad.systemcontrol;

import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

/**
 * Created by Municipio de Gye on 26/12/2017.
 */

public class QueryRouteFragment extends Fragment {
    SupportMapFragment mapFragment;;
    private GoogleMap mMap;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_route);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_query_route, container, false);
        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    mMap.setMinZoomPreference(10);
                    LatLng gyeLocation = new LatLng(-2.2058400, -79.9079500);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(gyeLocation));
                }
            });
        }

        getChildFragmentManager().beginTransaction().replace(R.id.map_route, mapFragment).commit();

        return rootView;
    }

}
