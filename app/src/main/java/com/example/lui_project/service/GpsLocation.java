package com.example.lui_project.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.lui_project.Fragment.SportFragment;
import com.example.lui_project.utils.SaveKeyValues;

public class GpsLocation extends Service {

    private LocationManager gpslocationManager;
    private LocationListener listener;
    public static Boolean FLAG = false;
    public static double Longitude = 0;
    public static double Latitude = 0;
    Boolean unable = false;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {

        gpslocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {
                Log.d("gps", "service update");

                Longitude = location.getLongitude();
                Latitude = location.getLatitude();
                FLAG = true;


            }


            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("gps", "updating");
        unable = true;
        while (unable == true) {
            Log.d("gps", "true");
            gpslocationManager.requestLocationUpdates(gpslocationManager.NETWORK_PROVIDER, 0, 100, listener);
            if (gpslocationManager != null) {
                unable = false;
                Log.d("gps", "false");
            }
            Log.d("gps", "done");
        }
    }

}