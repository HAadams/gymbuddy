package gymbuddy.project.capstone.gymbuddy.UI.HomePage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;

import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.Database.User;
import gymbuddy.project.capstone.gymbuddy.Map.LocationHelper;
import gymbuddy.project.capstone.gymbuddy.R;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.EditProfileFragment;
import gymbuddy.project.capstone.gymbuddy.UI.LoginPage.MainActivity;
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

        FirebaseDatabaseHelper.getInstance().getUsersGroup();

        fdbh = FirebaseDatabaseHelper.getInstance();

        context = this;

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.homeFragmentContainer, new HomeFragment());

        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("I AM IN onSTART");
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
    protected void onStop() {
        super.onStop();
        System.out.println("I AM IN onSTOP");
    }

    @Override
    public void onListFragmentInteraction(User mItem) {

    }

    @Override
    public void onNavListFragmentInteraction(User item) {

    }
}

