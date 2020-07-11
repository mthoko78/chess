package com.mthoko.mobile.service.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class LocationService {

    private static boolean locationRequested;

    private LocationManager locationManager;

    private LocationListener locationListener;

    private Context context;

    public LocationService(Context context) {
        this.context = context;
    }

    public void initLocationRequests(LocationListener locationListener) {
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        this.locationListener = locationListener;
    }

    public void startLocationRequests() {
        if(locationRequested)
            return;
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing PERMISSIONS, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] PERMISSIONS,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //-29.656267, 31.066761
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5, locationListener);
        showMessage("WELCOME: " + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        locationRequested = true;
    }

    public void stopLocationRequests() {
        if(!locationRequested)
            return;
        locationManager.removeUpdates(locationListener);
        showMessage("STOPPING LOCATION REQUESTS");
        locationRequested = false;
    }

    public void toggleLocationRequest() {
        if(locationRequested){
            stopLocationRequests();
        }
        else{
            startLocationRequests();
        }
    }

    public void showMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}