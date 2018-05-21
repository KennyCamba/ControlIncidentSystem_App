package com.app.computacionysociedad.systemcontrol;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener{

    private final Context mContext;

    // flag for GPS Status
    private boolean isGPSEnabled = false;

    // flag for network status
    private boolean isNetworkEnabled = false;

    // flag for GPS Tracking is enabled
    private boolean isGPSTrackingEnabled = false;

    private Location location;
    private double latitude;
    private double longitude;

    // The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    // Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
    private String provider_info;

    public GPSTracker(Context context){
        this.mContext = context;
        getLocation();
    }

    public void getLocation()  {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            System.out.println("Prueba "+locationManager);
/*
            if(locationManager != null) {
                //getting GPS status
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                System.out.println("Prueba2 "+isGPSEnabled);

                //getting network status
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                System.out.println("Prueba3 "+isNetworkEnabled);
            }
            // Try to get location if you GPS Service is enabled
            if (isGPSEnabled) {
                this.isGPSTrackingEnabled = true;
                provider_info = LocationManager.GPS_PROVIDER;
                System.out.println("Prueba4 "+provider_info);
            } else if (isNetworkEnabled) {
                this.isGPSTrackingEnabled = true;
                provider_info = LocationManager.NETWORK_PROVIDER;
            }}*/
            Criteria criteria = new Criteria();
            provider_info = String.valueOf(locationManager.getBestProvider(criteria, true));
            if (!provider_info.isEmpty()) {
                locationManager.requestLocationUpdates(provider_info,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider_info);
                    System.out.println("Location: "+ location+", "+ location.getProvider());
                    updateGPSCoordinates();
                }
            }
        }catch (SecurityException|NullPointerException e){
            System.out.println(e.getMessage());
            Toast.makeText(mContext, "Imposible encontrar ubicación", Toast.LENGTH_LONG).show();
        }
    }

    public void updateGPSCoordinates() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }
    /**
     * GPSTracker latitude getter and setter
     * @return latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    /**
     * GPSTracker longitude getter and setter
     * @return longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    /**
     * GPSTracker isGPSTrackingEnabled getter.
     * Check GPS/wifi is enabled
     */
    public boolean getIsGPSTrackingEnabled() {

        return this.isGPSTrackingEnabled;
    }

    /**
     * Stop using GPS listener
     * Calling this method will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        //Setting Dialog Title
        alertDialog.setTitle("Conectar a GPS");

        //Setting Dialog Message
        alertDialog.setMessage("Permite que SIPECI active el GPS");

        //On Pressing Setting button
        alertDialog.setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        //On pressing cancel button
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
