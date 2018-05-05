package gymbuddy.project.capstone.gymbuddy.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.ArrayList;

import gymbuddy.project.capstone.gymbuddy.Adapters.Profile;
import gymbuddy.project.capstone.gymbuddy.Map.LocationHelper;

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
    private static final String PHOTOS = "photos";
    private static final String LIKES = "likes";
    private static final String LIKED = "liked";
    private static final String FRIENDS = "friends";
    private static final String LOCATIONS = "location";
    private static final String USERS = "users";
    private static final String UNLIKES = "unlikes";
    private static final String FEMALE = "female";
    private static final String MALE = "male";
    private static final String OTHER = "other";
    private static final String LOGIN_STATE = "login_state_gymbuddy";

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
    private boolean usersFetchComplete;
    private boolean currentUserUpdateComplete;
    private boolean firstLogin;

    private static FirebaseUser user;
    public ArrayList<Profile> users_from_database;

    private FirebaseDatabaseHelper(){
        initObjects();
        setListeners();
    }

    private void initObjects(){
        firstLogin = true;
        fetchComplete = false;
        errorOccured = false;
        usersFetchComplete = false;
        users_from_database = new ArrayList<>();
        rootRef = new Firebase(FIREBASE_DATABASE_URL);
        usersRef = rootRef.child(USERS);
        currentUserRef = usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        locationsRef = currentUserRef.child(LOCATIONS);
        gendersRef = currentUserRef.child(GENDER);
        birthdatesRef = currentUserRef.child(BIRTHDAY);
        likesRef = currentUserRef.child(LIKES);
        likedRef = currentUserRef.child(LIKED);
        friendsRef = currentUserRef.child(FRIENDS);
        photosRef = currentUserRef.child(PHOTOS);
        namesRef = currentUserRef.child(NAME);
        emailsRef = currentUserRef.child(EMAIL);
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

    public void setCurrentUserData(){
        currentUserUpdateComplete = false;
        usersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentUser.setCurrentUser(getUserFromSnapshot(dataSnapshot, "current_user"));
                CurrentUser.getInstance().setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                currentUserUpdateComplete = true;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                currentUserUpdateComplete = false;
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
                        NAME:       John Doe
                        Birthday:   birthday
                        Email:      email
                        Gender:     male
                        minAge:     18
                        maxAge:     100
                        p_dist:     100
                        p_gend:     female
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
            CurrentUser currentUser;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This iterates over the list of users
                for(DataSnapshot user: dataSnapshot.getChildren()){
                    currentUser = CurrentUser.getInstance();
                    // user.getKey() returns the USER_ID
                    // If user is current user, skip.
                    if(user.getKey().equalsIgnoreCase(currentUser.getUserID()))
                        continue;
                    // If user is on the current user's unlikes list, skip.
                    if(currentUser.getUnlikes().contains(user.getKey())) continue;
                    // If current user already likes this person (exists in likes list), skip.
                    if(currentUser.getLikes().contains(user.getKey())) continue;
                    // Get the new user
                    User u = getNewUser(user);
                    // If null, it means the user doesn't match the current user's criteria, skip.
                    if(u == null) continue;
                    Log.d("getUsersGroup()", "Adding user: "+u.getName());
                    Profile p = new Profile();
                    p.setUser(u);
                    users_from_database.add(p);
                }
                usersFetchComplete = true;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                usersFetchComplete = false;
            }
        });
    }

    private User getNewUser(DataSnapshot user){

        /*
        This method converts the DataSnapshot object to a user object.
        It also does the filtering based on min_age, max_age, preferred distance and preferred gender.
        */
        CurrentUser currentUser = CurrentUser.getInstance();
        Integer p_distance, min_age, max_age;
        String p_gender;
        p_distance = currentUser.getPerferredDistance();
        p_gender = currentUser.getPerferredGender();
        min_age = currentUser.getMinAge();
        max_age = currentUser.getMaxAge();

        Integer tmp_age;
        String tmp_birthday;
        String tmp_gender, tmp_preferred_gender;
        Integer tmp_minAge, tmp_maxAge;

        User n = new User(user.getKey());
        n.setUserID(user.getKey());

        for(DataSnapshot info: user.getChildren()){
            Log.d("getNewUser() User Info", info.getKey());
            switch(info.getKey()){
                case PHOTOS:
                    for(DataSnapshot photo: info.getChildren())
                        n.setPhotos(Integer.parseInt(photo.getKey()), photo.getValue().toString());
                    break;
                case NAME:
                    n.setName(info.getValue().toString());
                    break;
                case GENDER:
                    // filter by gender preference.
                    tmp_gender = info.getValue().toString();
                    Log.d("getNewUser():GENDER", "p_gender: "+p_gender+" tmp_gender: "+tmp_gender);
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
                            user_loc[1],
                            user_loc[2],
                            "M");
                    Log.d("CurrentUser:LOCATIONS", "LAT: "+currentUser.getLatitude()+" LONG: "+currentUser.getLongitude());
                    Log.d("OtherUser:LOCATIONS", "LAT: "+user_loc[1]+" LONG: "+user_loc[2]);
                    Log.d("getNewUser():LOCATIONS", "Distance: "+distance+" P_distance: "+p_distance);
                    if(Math.abs(distance) > p_distance) {
                        return null;
                    }

                    n.setAltitude(user_loc[0].toString());
                    n.setLatitude(user_loc[1].toString());
                    n.setLongitude(user_loc[2].toString());

                    break;

                case MIN_AGE:
                    // If the current user's age is not within the user's age limit, skip
                    tmp_minAge = Integer.parseInt(info.getValue().toString());
                    if(currentUser.getAge() < tmp_minAge) return null;
                    n.setMinAge(tmp_minAge);
                    break;

                case MAX_AGE:
                    // If the current user's age is not within the user's age limit, skip
                    tmp_maxAge = Integer.parseInt(info.getValue().toString());
                    if(currentUser.getAge() > tmp_maxAge) return null;
                    Log.d("CurrentUser Age", currentUser.getAge().toString());
                    n.setMaxAge(tmp_maxAge);
                    break;

                case PERFERRED_DISTANCE:
                    n.setPerferredDistance(Integer.parseInt(info.getValue().toString()));
                    break;

                case PERFERRED_GENDER:
                    tmp_preferred_gender = info.getValue().toString();
                    if(!tmp_preferred_gender.equalsIgnoreCase(currentUser.getGender())) return null;
                    n.setPerferredGender(tmp_preferred_gender);
                    break;

                case UNLIKES:
                    // If the current user exists on the user's unlikes list, don't show them
                    for(DataSnapshot u_id: info.getChildren())
                        if(currentUser.getUserID().equalsIgnoreCase(u_id.getKey())) return null;

                    break;
                default:
                    break;
            }

        }

        return n;
    }

    private User getUserFromSnapshot(DataSnapshot user, String u){

        /*
        This method converts the DataSnapshot object to a user object.
        */
        User tmp_user;
        if(u.equals("current_user"))
            tmp_user = new CurrentUser();
        else
            tmp_user = new User();

        for(DataSnapshot info: user.getChildren()){
            switch(info.getKey()){
                case PHOTOS:
                    for(DataSnapshot photo: info.getChildren())
                        tmp_user.setPhotos(Integer.parseInt(photo.getKey()), photo.getValue().toString());
                    break;
                case NAME:
                    tmp_user.setName(info.getValue().toString());
                    break;
                case EMAIL:
                    tmp_user.setEmail(info.getValue().toString());
                    break;
                case GENDER:
                    tmp_user.setGender(info.getValue().toString());
                    break;
                case BIRTHDAY:
                    tmp_user.setBirthday(info.getValue().toString());
                    tmp_user.setAge(User.getUserAge(tmp_user.getBirthday()));
                    break;
                case LOCATIONS:
                    // filter by distance
                    Double[] user_loc = LocationHelper.getLocationFromSnapshot(info);
                    tmp_user.setAltitude(user_loc[0].toString());
                    tmp_user.setLatitude(user_loc[1].toString());
                    tmp_user.setLongitude(user_loc[2].toString());
                    break;
                case MIN_AGE:
                    tmp_user.setMinAge(Integer.parseInt(info.getValue().toString()));
                    break;

                case MAX_AGE:
                    tmp_user.setMaxAge(Integer.parseInt(info.getValue().toString()));
                    break;

                case PERFERRED_DISTANCE:
                    tmp_user.setPerferredDistance(Integer.parseInt(info.getValue().toString()));
                    break;

                case PERFERRED_GENDER:
                    tmp_user.setPerferredGender(info.getValue().toString());
                    break;

                case UNLIKES:
                    for(DataSnapshot u_id: info.getChildren())
                        tmp_user.addToUnlikes(u_id.getValue().toString());
                    break;
                case LIKES:
                    for(DataSnapshot u_id: info.getChildren())
                        tmp_user.addToLikes(u_id.getValue().toString());
                    break;
                case LIKED:
                    for(DataSnapshot u_id: info.getChildren())
                        tmp_user.addToLiked(u_id.getValue().toString());
                    break;
                case FRIENDS:
                    for(DataSnapshot u_id: info.getChildren())
                        tmp_user.addToFriends(u_id.getValue().toString(), null);
                    break;
                default:
                    break;
            }
        }
        return tmp_user;
    }

    public void updateLikes(String id){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        likesRef.child(id).setValue(id);
    }

    public void updateUnlikes(String id){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        unlikesRef.child(id).setValue(id);
    }

    public void updateFriends(String id){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        friendsRef.child(id).setValue(id);
    }

    public void uploadUserDataToDatabase(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        firstLogin = false;
        FetchUserDataFromFB();
    }

    public void saveFirstLoginState(Context context, boolean state){
        SharedPreferences.Editor editor = context.getSharedPreferences("GYMBUDDY_DATA", MODE_PRIVATE).edit();
        editor.putBoolean(LOGIN_STATE, state);
        editor.apply();
    }

    public boolean isFirstLogin(Context context){
        SharedPreferences prefs = context.getSharedPreferences("GYMBUDDY_DATA", MODE_PRIVATE);
        return prefs.getBoolean(LOGIN_STATE, true);
    }

    public void updateLatitudeLocation(Double latitude){
        CurrentUser currentUser = CurrentUser.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.setLatitude(latitude.toString());;
        locationsRef.child(LATITUDE).setValue(currentUser.getLatitude());
    }

    public void updateLongitudeLocation(Double longitude){
        CurrentUser currentUser = CurrentUser.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.setLongitude(longitude.toString());
        locationsRef.child(LONGITUDE).setValue(currentUser.getLongitude());
    }

    public void updateAltitudeLocation(Double altitude){
        CurrentUser currentUser = CurrentUser.getInstance();
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

    public void updateUserBirthday(String birthdate){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        birthdatesRef.setValue(birthdate);
    }

    public void updateUserName(String name){
        namesRef.setValue(name);
    }

    public void updateUserSearchSettings(Integer minAge, Integer maxAge, Integer p_distance, String p_gender){
        updatePreferredAgeInterval(minAge, maxAge);
        updatePreferredGender(p_gender);
        updatePreferredDistance(p_distance);
    }

    public void updatePreferredDistance(Integer p_distance){
        currentUserRef.child(PERFERRED_DISTANCE).setValue(p_distance);
    }

    public void updatePreferredGender(String p_gender){
        currentUserRef.child(PERFERRED_GENDER).setValue(p_gender);
    }

    public void updatePreferredAgeInterval(Integer minAge, Integer maxAge){
        currentUserRef.child(MIN_AGE).setValue(minAge);
        currentUserRef.child(MAX_AGE).setValue(maxAge);
    }
    public void setDefaultUserSearchSettings(){
        CurrentUser currentUser = CurrentUser.getInstance();
        String perferred_gender;
        System.out.println("Current Gender: "+currentUser.getGender());
        if(currentUser.getGender().equalsIgnoreCase(FEMALE))
            perferred_gender = MALE;
        else if(currentUser.getGender().equalsIgnoreCase(MALE))
            perferred_gender = FEMALE;
        else
            perferred_gender = OTHER;

        System.out.println("Setting Gender To: "+perferred_gender);

        updateUserSearchSettings(18, 100, 100, perferred_gender);
    }

    public boolean isUsersFetchComplete(){return usersFetchComplete;}

    public boolean isCurrentUserUpdateComplete(){return currentUserUpdateComplete;}

    public void setFlags(boolean fetchComplete, boolean usersFetchComplete, boolean currentUserUpdateComplete){
        this.fetchComplete = fetchComplete;
        this.usersFetchComplete = usersFetchComplete;
        this.currentUserUpdateComplete = currentUserUpdateComplete;
    }

    private void FetchUserDataFromFB(){
        /*
        Fetches user's data from facebook and uploads it to Firebase
         */
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
                                CurrentUser currentUser = CurrentUser.getInstance();
                                FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
                                fdbh.updateUserBirthday(object.getString(BIRTHDAY));
                                currentUser.setBirthday(object.getString(BIRTHDAY));
                                fdbh.updateUserEmail(object.getString(EMAIL));
                                currentUser.setEmail(object.getString(EMAIL));
                                fdbh.updateUserGender(object.getString(GENDER));
                                currentUser.setGender(object.getString(GENDER));
                                fdbh.updateUserName(object.getString(NAME));
                                currentUser.setName(object.getString(NAME));
                                fdbh.setDefaultUserSearchSettings();
                                try {
                                    updateUserPhotos(0, FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                                }catch(Exception e){
                                    Log.e("Setting default picture", e.getMessage());
                                    e.printStackTrace();
                                }
                                fdbh.setCurrentUserData();
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
            e.printStackTrace();
            Log.e("FetchCurrentUserData", e.getMessage());
        }
    }

}
