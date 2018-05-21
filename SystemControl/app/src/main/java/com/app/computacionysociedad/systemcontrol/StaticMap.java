package com.app.computacionysociedad.systemcontrol;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Municipio de Gye on 30/12/2017.
 */

public class StaticMap {
    private final String BASIC_URL = "https://maps.googleapis.com/maps/api/staticmap?";
    private final String SEP = "%7C";
    private int height;
    private int weight;
    private int scale;
    private LatLng center;
    private int zoom;
    private String url = "";
    private  String apiKey;

    public StaticMap(String apiKey, int height, int weight, int scale){
        this.height = height;
        this.weight = weight;
        this.scale = scale;
        this.apiKey = apiKey;
        url += BASIC_URL;
        url += "size="+height+"x"+weight;
        url += "&maptype=roadmap";
        url += "&scale="+scale;
    }

    public StaticMap(String apiKey, int height, int weight, int scale, LatLng center, int zoom){
        this(apiKey, height, weight, scale);
        this.zoom = zoom;
        this.center = center;
        url += "&center="+center.latitude+","+center.longitude;
        url += "&zoom="+zoom;
        addMarker(center, "red", null);
    }

    public void addMarker(LatLng marker, String color, String label){
        double lat = marker.latitude;
        double lng = marker.longitude;
        url += "&markers=color:"+color+SEP;
        if(label != null){
            url += "label:"+label+SEP;
        }
        url += lat+","+lng;
    }

    public void addRoute(LatLng[] route){
        url += "&path=weight:5";
        url += "%7Cgeodesic:true";
        for(LatLng point: route){
            url += SEP + point.longitude+","+point.longitude;
        }
    }

    public Uri getMapUrl(){
        url += "&format=png";
        url += "&key="+ apiKey;
        return Uri.parse(url);
    }

}
