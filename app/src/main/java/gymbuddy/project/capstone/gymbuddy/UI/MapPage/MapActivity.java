package gymbuddy.project.capstone.gymbuddy.UI.MapPage;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.Database.User;
import gymbuddy.project.capstone.gymbuddy.R;

public class MapActivity extends FragmentActivity implements com.google.android.gms.maps.OnMapReadyCallback {

    Context context;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        context = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat = Double.valueOf(CurrentUser.getInstance().getLatitude());
        double lon = Double.valueOf(CurrentUser.getInstance().getLongitude());

        LatLng currentLocation = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

        for(User user : CurrentUser.getInstance().getFriends().values()){
            double latitude = Double.valueOf(user.getLatitude());
            double longitude = Double.valueOf(user.getLongitude());

            LatLng friendLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(friendLocation).title(user.getName()));
        }
    }

}
