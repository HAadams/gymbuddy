package gymbuddy.project.capstone.gymbuddy.UI.HomePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;

import gymbuddy.project.capstone.gymbuddy.Map.LocationHelper;
import gymbuddy.project.capstone.gymbuddy.R;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.AlbumsFragment;
import gymbuddy.project.capstone.gymbuddy.UI.LoginPage.MainActivity;

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    Context context;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Fresco.initialize(this);

        context = this;

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.homeFragmentContainer, new HomeFragment());

        fragmentTransaction.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            // Update user location after they re-open the app if they are already logged in
            LocationHelper.getInstance(this).updateUserLocation();
        }
    }

    private void startMainActivity(){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.w(getClass().toString(), "startMainActivity:starting main activity after logout");
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onLogoutFragmentInteraction() {
        startMainActivity();
    }

    @Override
    public void onPhotosFragmentInteraction() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.homeFragmentContainer, new AlbumsFragment()).addToBackStack(null);

        fragmentTransaction.commit();

    }

    @Override
    public void onMessengerFragmentInteraction() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.exit, R.anim.enter, R.anim.pop_exit, R.anim.pop_enter);
        //todo test on device and fix accordingly

        fragmentTransaction.replace(R.id.homeFragmentContainer, new MessengerFragment()).addToBackStack(null);

        fragmentTransaction.commit();

    }

    @Override
    public void onMapFragmentInteraction() {
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        fragmentTransaction.replace(R.id.homeFragmentContainer, new SupportMapFragment()).addToBackStack(null);

        fragmentTransaction.commit();

    }
}

