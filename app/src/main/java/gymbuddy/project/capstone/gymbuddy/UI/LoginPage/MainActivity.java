package gymbuddy.project.capstone.gymbuddy.UI.LoginPage;


import android.content.Intent;
import android.location.LocationListener;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.facebook.AccessToken;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.Map.LocationHelper;
import gymbuddy.project.capstone.gymbuddy.R;
import gymbuddy.project.capstone.gymbuddy.Adapters.FragmentSelectionPageAdapter;
import gymbuddy.project.capstone.gymbuddy.UI.HomePage.HomeActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private FragmentSelectionPageAdapter fragmentAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    LocationHelper lh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        fragmentAdapter = new FragmentSelectionPageAdapter(getSupportFragmentManager());

        LocationHelper.getInstance(this).requestLocationPermission();


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(fragmentAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if(fragment instanceof LoginFragment)
                fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();
        // After user re-opens the app, check if they were logged in
        // If they were logged in, take them straight to the HomeActivity
        // Also, update the user location
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startHomePageActivity();
            LocationHelper.getInstance(this).updateUserLocation();
        }
    }

    public void updateUserData(AccessToken accessToken){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        try {
            // Set the access token to access the user's profile data
            fdbh.setAccessToken(accessToken);
            fdbh.UploadUserDataToDatabase();
            // Update the user's location after they login
            LocationHelper.getInstance(this).updateUserLocation();
        }catch(Exception e){
            Log.e(getClass().toString(), e.toString());
        }
    }

    public void startHomePageActivity(){
        Log.w(getClass().toString(), "startHomepageActivity:starting Homepage activity");
        Intent accountIntent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(accountIntent);
    }

}
