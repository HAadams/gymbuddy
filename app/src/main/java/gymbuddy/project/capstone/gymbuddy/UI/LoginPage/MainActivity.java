package gymbuddy.project.capstone.gymbuddy.UI.LoginPage;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.Map.LocationHelper;
import gymbuddy.project.capstone.gymbuddy.R;
import gymbuddy.project.capstone.gymbuddy.UI.HomePage.HomeActivity;

public class MainActivity extends AppCompatActivity {


    private CallbackManager mCallbackManager;
    private final String TAG = "FACEBOOK_AUTH_LOGGER";
    private FirebaseAuth firebaseAuth;

    private SlideAdapter slideAdapter;
    private LinearLayout dotLayout;
    private TextView[] dots;
    private ViewPager mViewPager;
    LocationHelper lh;

    private Button prevButton;
    private Button nextButton;
    LoginButton loginButton;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        loginButton = findViewById(R.id.login_button);

        mViewPager = findViewById(R.id.slideContainer);
        dotLayout = findViewById(R.id.dotLayout);
        nextButton = findViewById(R.id.NextButton);
        prevButton = findViewById(R.id.PrevButton);

        slideAdapter = new SlideAdapter(this);

        loginButton.setVisibility(View.INVISIBLE);

        mViewPager.setAdapter(slideAdapter);

        initDotIndicator();

        mViewPager.addOnPageChangeListener(viewListener);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mViewPager.setCurrentItem(currentPage+1);
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(currentPage-1);

            }
        });


        // Initialize Facebook Login button
        firebaseAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile", "user_birthday", "user_photos");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }

    public void initDotIndicator(){
        dots = new TextView[3];
        // Set all the TextViews to default parameters
        for (int i = 0; i < dots.length; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorGrey));

            dotLayout.addView(dots[i]);

        }
        // Change the color of the first position to be white
        changeDotIndicatorColor(0);

    }
    public void changeDotIndicatorColor(int position){
        for(int i=0; i<dots.length; i++){
            if(position == i)
                dots[position].setTextColor(getResources().getColor(R.color.colorWhite));
            else
                dots[i].setTextColor(getResources().getColor(R.color.colorGrey));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
                changeDotIndicatorColor(position);
                currentPage = position;

                if (position == 1){
                    nextButton.setEnabled(true);
                    prevButton.setEnabled(false);
                    prevButton.setVisibility(View.INVISIBLE);
                }else if (position == dots.length){
                    nextButton.setEnabled(false);
                    prevButton.setEnabled(true);
                    nextButton.setVisibility(View.INVISIBLE);
                } else{
                    nextButton.setEnabled(true);
                    prevButton.setEnabled(true);
                    nextButton.setVisibility(View.VISIBLE);
                    prevButton.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.VISIBLE);

                }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart(){
        super.onStart();
        // After user re-opens the app, check if they were logged in
        // If they were logged in, take them straight to the HomeActivity
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startHomePageActivity();
        }
    }

    public void updateUserData(){
        try {
            FirebaseDatabaseHelper.getInstance().UploadUserDataToDatabase();
        }catch(Exception e){
            Log.e(getClass().toString(), e.toString());
        }
    }

    public void startHomePageActivity(){
        Log.w(getClass().toString(), "startHomepageActivity:starting Homepage activity");
        Intent homepageActivity = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(homepageActivity);
        finish();
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            updateUserData();
                            startHomePageActivity();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

}
