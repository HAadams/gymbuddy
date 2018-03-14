package gymbuddy.project.capstone.gymbuddy.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Album;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Photo;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sein on 2/15/18.
 **/

public class FirebaseDatabaseHelper {

    private static FirebaseDatabaseHelper initialInstance = null;

    public static synchronized FirebaseDatabaseHelper getInstance(){
        if(initialInstance == null)
        {
            initialInstance = new FirebaseDatabaseHelper();
        }
        return initialInstance;
    }

    private static final String FIREBASE_DATABASE_URL = "https://gymbuddy-a1579.firebaseio.com";
    private static final String GENDERS = "Genders";
    private static final String PHOTOS = "Photos";
    private static final String BIRTHDATES = "Birthdates";
    private static final String LIKES = "Likes";
    private static final String LIKED = "Liked";
    private static final String FRIENDS = "Friends";
    private static final String LOCATIONS = "Locations";
    private static final String NAMES = "Names";
    private static final String EMAILS = "Emails";
    private static final String USERS = "Users";

    public final String GENDER = "gender";
    public final String BIRTHDAY = "birthday";
    public final String LATITUDE = "latitude";
    public final String LONGITUDE = "longitude";
    public final String ALTITUDE = "altitude";
    public final String NAME = "name";
    public final String ID = "id";
    public final String PROFILE_PICTURE = "profile_picture";
    public final String EMAIL = "email";

    private static Firebase rootRef, namesRef, locationsRef, gendersRef, birthdatesRef,
            likesRef, likedRef, friendsRef, photosRef, emailsRef, usersRef;

    private boolean fetchComplete;
    private boolean errorOccured;
    public CurrentUser currentUser;
    private static FirebaseUser user;

    private FirebaseDatabaseHelper(){
        fetchComplete = false;
        errorOccured = false;
        currentUser = CurrentUser.getInstance();
        rootRef = new Firebase(FIREBASE_DATABASE_URL);
        locationsRef = rootRef.child(LOCATIONS);
        gendersRef = rootRef.child(GENDERS);
        birthdatesRef = rootRef.child(BIRTHDATES);
        likesRef = rootRef.child(LIKES);
        likedRef = rootRef.child(LIKED);
        friendsRef = rootRef.child(FRIENDS);
        photosRef = rootRef.child(PHOTOS);
        namesRef = rootRef.child(NAMES);
        emailsRef = rootRef.child(EMAILS);
        usersRef = rootRef.child(USERS);

        setListeners();
    }

    public void setListeners(){
        likesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("VALUE CHANGED YO "+dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        likedRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void updateLikes(String id){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        likesRef.child(user.getUid()).child(id).setValue(id);
    }

    public void UploadUserDataToDatabase(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.setPhotoURL(user.getPhotoUrl());
        FetchCurrentUserData();
        new UserDataUpdater().execute();
    }

    public void updateLatitudeLocation(Double latitude){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.setLatitude(latitude.toString());;
        locationsRef.child(user.getUid()).child(LATITUDE).setValue(currentUser.getLatitude());
    }

    public void updateLongitudeLocation(Double longitude){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.setLongitude(longitude.toString());
        locationsRef.child(user.getUid()).child(LONGITUDE).setValue(currentUser.getLongitude());
    }

    public void updateAltitudeLocation(Double altitude){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.setAltitude(altitude.toString());
        locationsRef.child(user.getUid()).child(ALTITUDE).setValue(currentUser.getAltitude());
    }

    public void updateUserPhotos(Integer index, String url){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        Firebase deepPhotosRef = photosRef.child(user.getUid());
        deepPhotosRef.child(index.toString()).setValue(url);
    }

    public void updateUserEmail(String email){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        emailsRef.child(user.getUid()).setValue(email);
    }

    public void updateUserGender(String gender){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        gendersRef.child(user.getUid()).setValue(gender);
    }

    public void updateUserBirthdate(String birthdate){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        birthdatesRef.child(user.getUid()).setValue(birthdate);
    }

    public void addUserIdToDatabase(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        usersRef.child(user.getUid()).setValue(user.getUid());

    }
    public void updateUserName(String name){
        namesRef.child(user.getUid()).setValue(name);
    }

    private boolean isErrorOccured(){return errorOccured;}

    private boolean isFetchComplete(){return fetchComplete;}

    private void FetchCurrentUserData(){
        fetchComplete = false;
        errorOccured = false;
        try {
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try {
                                currentUser.setBirthday(object.getString(BIRTHDAY));
                                currentUser.setGender(object.getString(GENDER));
                                currentUser.setFbUserID(object.getString(ID));
                                currentUser.setName(object.getString(NAME));
                                currentUser.setEmail(object.getString(EMAIL));
                                fetchComplete = true;
                            } catch (Exception e) {
                                Log.e(getClass().toString(), "Error fetching user information");
                                Log.e(getClass().getName(), e.toString());
                                fetchComplete = false;
                                errorOccured = true;
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            // The information the request will fetch is defined in parameters
            parameters.putString("fields", "id,name,link,gender,birthday,email");
            request.setParameters(parameters);
            request.executeAsync();
        }catch(Exception e){
            Log.e("FetchCurrentUserData", e.getStackTrace().toString());
        }
    }

    private static class UserDataUpdater extends AsyncTask<Void, Void, Void> {
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();

        @Override
        protected Void doInBackground(Void... voids) {
            while(!fdbh.isFetchComplete() && !fdbh.isErrorOccured()){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(fdbh.isErrorOccured()) {
                Log.e("UserDataUpdater", "Error occurred in graph request that updates user data.");
                return;
            }
            fdbh.updateUserBirthdate(fdbh.currentUser.getBirthday());
            fdbh.updateUserEmail(fdbh.currentUser.getEmail());
            fdbh.updateUserGender(fdbh.currentUser.getGender());
            fdbh.updateUserName(fdbh.currentUser.getName());
            fdbh.addUserIdToDatabase();
        }
    }
}
