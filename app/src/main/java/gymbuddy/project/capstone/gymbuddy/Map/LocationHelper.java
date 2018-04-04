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

import com.firebase.client.DataSnapshot;

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
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ALTITUDE = "altitude";

    private Activity activity;

    private FirebaseDatabaseHelper firebaseDatabaseHelper;

    // flag for GPS status
    private boolean GPSEnabled = false;

    // flag for network status
    private boolean NetworkEnabled = false;

    private Location location; // location

    private double latitude; // latitude
    private double longitude; // longitude
    private double altitude;

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
        firebaseDatabaseHelper.updateAltitudeLocation(altitude);
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
            return null;
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
                altitude = location.getAltitude();
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
            altitude = location.getAltitude();
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

//    /**
//     * Calculate distance between two points in latitude and longitude taking
//     * into account height difference. If you are not interested in height
//     * difference pass 0.0. Uses Haversine method as its base.
//     *
//     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
//     * el2 End altitude in meters
//     * @returns Distance in Meters
//     */
//    public static double distance(double lat1, double lat2, double lon1,
//                                  double lon2, double el1, double el2) {
//
//        final int R = 6371; // Radius of the earth
//
//        double latDistance = Math.toRadians(lat2 - lat1);
//        double lonDistance = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double distance = R * c * 1000; // convert to meters
//
//        double height = el1 - el2;
//
//        distance = Math.pow(distance, 2) + Math.pow(height, 2);
//
//        return Math.sqrt(distance);
//    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static Double[] getLocationFromSnapshot(DataSnapshot snapshot){
        /*
        Returns an array of size 3

            Index:
                0 : Altitude
                1 : Latitude
                2 : Longitude

         */
        Double[] location = new Double[3];
        for(DataSnapshot data: snapshot.getChildren()){
            switch (data.getKey()){
                case ALTITUDE:
                    location[0] = Double.valueOf(data.getValue().toString());
                    break;
                case LATITUDE:
                    location[1] = Double.valueOf(data.getValue().toString());
                    break;
                case LONGITUDE:
                    location[2] = Double.valueOf(data.getValue().toString());
                    break;
                default:
                    break;
            }
        }
        return location;
    }
}
