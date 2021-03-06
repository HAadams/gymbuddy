package gymbuddy.project.capstone.gymbuddy.UI.HomePage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.Database.User;
import gymbuddy.project.capstone.gymbuddy.Map.LocationHelper;
import gymbuddy.project.capstone.gymbuddy.R;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.EditProfileFragment;
import gymbuddy.project.capstone.gymbuddy.UI.MapPage.MapActivity;
import gymbuddy.project.capstone.gymbuddy.UI.MessagePage.MessageFragment;
import gymbuddy.project.capstone.gymbuddy.UI.MessagePage.OnListInteractionListener;

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, OnListInteractionListener{

    Context context;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseDatabaseHelper fdbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Fresco.initialize(this);

        fdbh = FirebaseDatabaseHelper.getInstance();

        context = this;

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.homeFragmentContainer, new HomeFragment());

        fragmentTransaction.commit();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "gymbuddy.project.capstone.gymbuddy",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Log.d("HomeActivity:onResume", "Im in onResume()");
            // Update user location after they re-open the app if they are already logged in
            LocationHelper.getInstance(this).updateUserLocation();
        }
    }


    @Override
    public void onPhotosFragmentInteraction() {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.home_down, R.anim.home_up, R.anim.pop_home_up, R.anim.pop_home_down);

        fragmentTransaction.replace(R.id.homeFragmentContainer, new EditProfileFragment()).addToBackStack(null);

        fragmentTransaction.commit();

    }

    @Override
    public void onMessengerFragmentInteraction() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.exit_r, R.anim.enter_r, R.anim.pop_exit_r, R.anim.pop_enter_r);

        fragmentTransaction.replace(R.id.homeFragmentContainer, new MessageFragment()).addToBackStack(null);

        fragmentTransaction.commit();

    }

    @Override
    public void onMapFragmentInteraction() {
//        fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
//
//        fragmentTransaction.replace(R.id.homeFragmentContainer, new SupportMapFragment()).addToBackStack(null);
//
//        fragmentTransaction.commit();

        Intent intent = new Intent(HomeActivity.this, MapActivity.class);
        startActivity(intent);

    }

    @Override
    public void onUserProfilePressed(User user) {
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.exit_r, R.anim.enter_r, R.anim.pop_exit_r, R.anim.pop_enter_r);

        fragmentTransaction.replace(R.id.homeFragmentContainer, ProfileFragment.newInstance(user.getUserID(),user.getName())).addToBackStack(null);

        fragmentTransaction.commit();

    }

    @Override
    public void onListFragmentInteraction(User mItem) {

    }

    @Override
    public void onNavListFragmentInteraction(User item) {

    }
}

