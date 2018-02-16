package gymbuddy.project.capstone.gymbuddy.Map;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.firebase.client.Firebase;
import com.google.android.gms.location.LocationListener;

import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Sein on 2/15/18.
 */

public class LocationHelper {

    private Context context;
    //private LocationManager locationManager;
    private LocationListener locationListener;
    private FirebaseDatabaseHelper firebaseDatabaseHelper;

    public LocationHelper(Context mainContext){
        context = mainContext;
        firebaseDatabaseHelper = FirebaseDatabaseHelper.getInstance();
        trackUserLocation();
    }

    private void trackUserLocation(){
        //locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        firebaseDatabaseHelper.updateLatitudeLocation(12345.5);
        firebaseDatabaseHelper.updateLongitudeLocation(7896.7);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                System.out.println("LOCATION DATA CHANGED");
                firebaseDatabaseHelper.updateLatitudeLocation(location.getLatitude());
                firebaseDatabaseHelper.updateLongitudeLocation(location.getLongitude());
            }

        };

    }
}
