package gymbuddy.project.capstone.gymbuddy.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Sein on 2/15/18.
 **/

public class LocationHelper implements android.location.LocationListener{

    @SuppressLint("StaticFieldLeak")
    private static LocationHelper initialInstance = null;

    public static synchronized LocationHelper getInstance(Activity activity){
        if(initialInstance == null)
            initialInstance = new LocationHelper(activity);
        else{
            initialInstance.activity = activity;
        }
        return initialInstance;
    }

    private Activity activity;

    private FirebaseDatabaseHelper firebaseDatabaseHelper;

    // flag for GPS status
    private boolean GPSEnabled = false;

    // flag for network status
    private boolean NetworkEnabled = false;

    private Location location; // location

    private double latitude; // latitude
    private double longitude; // longitude

    // Updates will trigger once user location changes by this distance
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // Meters

    // Time to wait until next location update
    private static final long MIN_UPDATE_TIME = 1000 * 60; // Milliseconds

    private LocationManager locationManager;

    private LocationHelper(Activity activity){
        this.activity = activity;
        firebaseDatabaseHelper = FirebaseDatabaseHelper.getInstance();
        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
    }

    private void setUserLocationInDB(){

        firebaseDatabaseHelper.updateLatitudeLocation(latitude);
        firebaseDatabaseHelper.updateLongitudeLocation(longitude);

    }

    public Location updateUserLocation() {
        /*
        NOTE: Need to execute requestLocationPermission first
         */
        String locationProvider = null;

        if (locationManager == null) {
            Log.e(this.getClass().toString(), "Location Manager is Null.");
        }


        // getting GPS status
        GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        NetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            requestLocationPermission();
        }

        if(NetworkEnabled) locationProvider = LocationManager.NETWORK_PROVIDER;
        else if(GPSEnabled) locationProvider = LocationManager.GPS_PROVIDER;


        if (locationProvider == null) {
            // no network provider is enabled
            Log.e(this.getClass().toString(), "No location provider found.");

        } else {
            Log.d(this.getClass().toString(), "Using Location provider: "+locationProvider);

            // First get location from specific provider
            locationManager.requestLocationUpdates(locationProvider, MIN_UPDATE_TIME, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            location = locationManager.getLastKnownLocation(locationProvider);

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }else
                Log.e(this.getClass().toString(), "Location object is null");
        }


        return location;
    }

    public void requestLocationPermission(){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            setUserLocationInDB();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
