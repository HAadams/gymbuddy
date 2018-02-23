package gymbuddy.project.capstone.gymbuddy.UI.HomePage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.List;

import gymbuddy.project.capstone.gymbuddy.Adapters.Profile;
import gymbuddy.project.capstone.gymbuddy.Map.LocationHelper;
import gymbuddy.project.capstone.gymbuddy.R;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.AlbumsSelectAdapter;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.PictureSelectActivity;
import gymbuddy.project.capstone.gymbuddy.UI.LoginPage.MainActivity;
import gymbuddy.project.capstone.gymbuddy.Utilities.PhotosAPI;
import gymbuddy.project.capstone.gymbuddy.Utilities.Utils;

public class HomeActivity extends AppCompatActivity {

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(5)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));
        Utils utils = new Utils();
        List<Profile> arrayList = Utils.loadProfiles(mContext);
        for(int i = 0 ; i<arrayList.size(); i++){
            mSwipeView.addView(new TinderCard(mContext, arrayList.get(i), mSwipeView));
        }

//        for(Profile profile : Utils.loadProfiles(mContext)){
//            mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
//        }

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        Button logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startMainActivity();
            }
        });
        Button photos = findViewById(R.id.uploadPicsButton);
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFBPhotosSelectActivity();
            }
        });
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
            Intent accountIntent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(accountIntent);
            finish();
        }
    }
    private void startFBPhotosSelectActivity(){

        if(!(FirebaseAuth.getInstance().getCurrentUser() == null)){
            Log.w(getClass().toString(), "startMainActivity:starting main activity after logout");
            Intent accountIntent = new Intent(HomeActivity.this, PictureSelectActivity.class);
            startActivity(accountIntent);
            finish();
        }
    }
}

