package gymbuddy.project.capstone.gymbuddy.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.android.gms.wearable.DataApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gymbuddy.project.capstone.gymbuddy.Map.LocationHelper;
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
    private static final String GENDERS = "Gender";
    private static final String PHOTOS = "Photos";
    private static final String BIRTHDATES = "Birthdate";
    private static final String LIKES = "Likes";
    private static final String LIKED = "Liked";
    private static final String FRIENDS = "Friends";
    private static final String LOCATIONS = "Location";
    private static final String NAMES = "Name";
    private static final String EMAILS = "Email";
    private static final String USERS = "Users";
    private static final String UNLIKES = "Unlikes";
    private static final String FEMALE = "female";
    private static final String MALE = "male";
    private static final String OTHER = "other";


    public final String GENDER = "gender";
    public final String BIRTHDAY = "birthday";
    public final String LATITUDE = "latitude";
    public final String LONGITUDE = "longitude";
    public final String ALTITUDE = "altitude";
    public final String NAME = "name";
    public final String ID = "id";
    public final String PROFILE_PICTURE = "profile_picture";
    public final String EMAIL = "email";
    public final String MIN_AGE = "min_age";
    public final String MAX_AGE = "max_age";
    public final String PERFERRED_DISTANCE = "perferred_distance";
    public final String PERFERRED_GENDER = "perferred_gender";

    private static Firebase rootRef, namesRef, locationsRef, gendersRef, birthdatesRef,
            likesRef, likedRef, friendsRef, photosRef, emailsRef, usersRef, unlikesRef, currentUserRef;

    private boolean fetchComplete;
    private boolean errorOccured;
    public CurrentUser currentUser;
    private static FirebaseUser user;
    public Map<String, User> users_from_database;
    public Map<String, User> users_from_device;

    private FirebaseDatabaseHelper(){
        initObjects();
        setListeners();
        getUsersGroup();
    }

    private void initObjects(){
        fetchComplete = false;
        errorOccured = false;
        users_from_database = new HashMap<String, User>();
        users_from_device = new HashMap<String, User>();
        currentUser = CurrentUser.getInstance();
        rootRef = new Firebase(FIREBASE_DATABASE_URL);
        usersRef = rootRef.child(USERS);
        currentUserRef = usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        locationsRef = currentUserRef.child(LOCATIONS);
        gendersRef = currentUserRef.child(GENDERS);
        birthdatesRef = currentUserRef.child(BIRTHDATES);
        likesRef = currentUserRef.child(LIKES);
        likedRef = currentUserRef.child(LIKED);
        friendsRef = currentUserRef.child(FRIENDS);
        photosRef = currentUserRef.child(PHOTOS);
        namesRef = currentUserRef.child(NAMES);
        emailsRef = currentUserRef.child(EMAILS);
        unlikesRef = currentUserRef.child(UNLIKES);
    }

    private void setListeners(){
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

    public void getUsersGroup(){
        /*
        Here, we get all the users to display to the current user.
        Filter based on following cases:
            1. Must be within age limit set by user
            2. Must be within distance limit set by user
            3. Must be of the gender the user requests (Male, Female, Both)

        Additional filtering should be done based on the following
            1. If the user is on the current user's "unlike" list (includes blocked)
            2. If the user is on the other user's "unlike" list

        Objects are of the following JSON format:

        Users:
                USER_ID
                        NAME:       name
                        Birthday:   birthday
                        Email:      email
                        Gender:     gender
                        Likes:
                                USER_ID0, UserID1...etc
                        Liked:
                                USER_ID0, UserID2...etc
                        Friends:
                                USER_ID4...etc
                        Unlikes:
                                USER_ID7...etc
                        Location:
                                Altitude:   0.0
                                Latitude:   0.0
                                Longitude:  0.0
                        Photos:
                            1.  url
                            2.  url
                            .
                            .
                            4.  url

         */

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This iterates over the list of users
                for(DataSnapshot user: dataSnapshot.getChildren()){
                    // user.getKey() returns the USER_ID
                    // If user is current user, skip.
                    if(user.getKey().equalsIgnoreCase(currentUser.getUserID())) continue;
                    // If user is on the current user's unlikes list, skip.
                    if(currentUser.getUnlikes().get(user.getKey()) != null) continue;
                    // If user already liked this person (exists in liked list), skip.
                    if(currentUser.getLikes().get(user.getKey()) != null) continue;
                    // Get the new user
                    User u = getNewUser(user);
                    // If null, it means the user doesn't match the current user's criteria, skip.
                    if(u == null) continue;
                    // If current user is on the user's liked list
                    // means current user already liked this person, skip.
                    if(u.getLiked().get(currentUser.getUserID()) != null) continue;
                    // If current user is on the other user's unlikes list, skip.
                    if(u.getUnlikes().get(currentUser.getUserID()) != null) continue;
                    // Otherwise, add this person to the list to be displayed
                    users_from_database.put(u.getUserID(), u);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
    private User getNewUser(DataSnapshot user){

        // First, get user's criteria
        Integer p_distance, min_age, max_age;
        String p_gender;
        p_distance = currentUser.getPerferredDistance();
        p_gender = currentUser.getPerferredGender();
        min_age = currentUser.getMinAge();
        max_age = currentUser.getMaxAge();

        Integer tmp_age;
        String tmp_birthday;
        String tmp_gender;

        User n = new User(user.getKey());

        for(DataSnapshot info: user.getChildren()){
            n.setUserID(info.getKey());
            switch(info.getKey()){
                case PHOTOS:
                    for(DataSnapshot photo: info.getChildren())
                        n.setPhotos(Integer.parseInt(photo.getKey()), photo.getValue().toString());
                case NAME:
                    n.setName(info.getValue().toString());
                    break;
                case GENDER:
                    // filter by gender preference.
                    tmp_gender = info.getValue().toString();
                    if (!tmp_gender.equalsIgnoreCase(p_gender))
                        return null;

                    n.setGender(tmp_gender);
                    break;
                case BIRTHDAY:
                    // filter by age limits
                    tmp_birthday = info.getValue().toString();
                    tmp_age = User.getUserAge(tmp_birthday);
                    if( !(tmp_age >= min_age && tmp_age <= max_age) )
                        return null;

                    n.setBirthday(tmp_birthday);
                    n.setAge(tmp_age);

                    break;
                case LOCATIONS:
                    // filter by distance
                    Double[] user_loc = LocationHelper.getLocationFromSnapshot(info);
                    Double distance = LocationHelper.distance(
                            Double.valueOf(currentUser.getLatitude()),
                            Double.valueOf(currentUser.getLongitude()),
                            Double.valueOf(currentUser.getAltitude()),
                            user_loc[1],
                            user_loc[2],
                            user_loc[0]);

                    if( !(Math.abs(distance) <= p_distance) )
                        return null;

                    n.setAltitude(user_loc[0].toString());
                    n.setLatitude(user_loc[1].toString());
                    n.setLongitude(user_loc[2].toString());

                    break;


                default:
                    break;
            }

        }

        return n;
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
        currentUser.setUserID(user.getUid());
        FetchCurrentUserData();
        new UserDataUpdater().execute();
    }

    public void updateLatitudeLocation(Double latitude){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.setLatitude(latitude.toString());;
        locationsRef.child(LATITUDE).setValue(currentUser.getLatitude());
    }

    public void updateLongitudeLocation(Double longitude){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.setLongitude(longitude.toString());
        locationsRef.child(LONGITUDE).setValue(currentUser.getLongitude());
    }

    public void updateAltitudeLocation(Double altitude){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.setAltitude(altitude.toString());
        locationsRef.child(ALTITUDE).setValue(currentUser.getAltitude());
    }

    public void updateUserPhotos(Integer index, String url){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        photosRef.child(index.toString()).setValue(url);
    }

    public void updateUserEmail(String email){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        emailsRef.setValue(email);
    }

    public void updateUserGender(String gender){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        gendersRef.setValue(gender);
    }

    public void updateUserBirthdate(String birthdate){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        birthdatesRef.setValue(birthdate);
    }

    public void updateUserName(String name){
        namesRef.setValue(name);
    }

    public void updateUserSearchSettings(Integer minAge, Integer maxAge, Integer p_distance, String p_gender){
        currentUserRef.child(PERFERRED_GENDER).setValue(p_gender);
        currentUserRef.child(PERFERRED_DISTANCE).setValue(p_distance);
        currentUserRef.child(MIN_AGE).setValue(minAge);
        currentUserRef.child(MAX_AGE).setValue(maxAge);
    }

    public void setDefaultUserSearchSettings(){
        String perferred_gender;
        if(currentUser.getGender().equalsIgnoreCase(FEMALE))
            perferred_gender = MALE;
        else if(currentUser.getGender().equalsIgnoreCase(MALE))
            perferred_gender = FEMALE;
        else
            perferred_gender = OTHER;

        updateUserSearchSettings(18, 100, 100, perferred_gender);
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
            fdbh.setDefaultUserSearchSettings();
        }
    }
}
