package gymbuddy.project.capstone.gymbuddy.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Album;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Photo;

import static android.content.Context.MODE_PRIVATE;

public class User {
    private static final int MAX_PROFILE_PHOTOS_NUMBER = 5;
    public static final String PROFILE_PICTURE = "profile_picture_";
    public static final String USER_LIKES = "user_likes_";
    public static final String USER_LIKED = "user_liked_";
    public static final String USER_FRIENDS = "user_liked_";
    public static final String USER_UNLIKES = "user_unlikes_";
    public static final String SIZE = "size";


    private String birthday;
    private Integer age;
    private Integer minAge;
    private Integer maxAge;
    private String  perferredGender;
    private Integer perferredDistance;

    private String gender;
    private String email;
    private String name;
    private String longitude;
    private String latitude;
    private String altitude;


    private String fbUserID;
    private String userID;
    private List<Album> albums;
    private List<Photo> photos; // List of profile pictures that user chooses

    private Map<String, User> likes;
    private Map<String, User> liked;
    private Map<String, User> friends;
    private Map<String, User> unlikes;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public User(String id){
        this.userID = id;
        initUserObject();
        initPhotoArray();
    }
    public User(){
        initUserObject();
        initPhotoArray();
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Map<String, User> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, User> likes) {
        this.likes = likes;
    }

    public Map<String, User> getLiked() {
        return liked;
    }

    public void setLiked(Map<String, User> liked) {
        this.liked = liked;
    }

    public Map<String, User> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, User> friends) {
        this.friends = friends;
    }

    public Map<String, User> getUnlikes() {
        return unlikes;
    }

    public void setUnlikes(Map<String, User> unlikes) {
        this.unlikes = unlikes;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getPerferredGender() {
        return perferredGender;
    }

    public void setPerferredGender(String perferredGender) {
        this.perferredGender = perferredGender;
    }

    public Integer getPerferredDistance() {
        return perferredDistance;
    }

    public void setPerferredDistance(Integer perferredDistance) {
        this.perferredDistance = perferredDistance;
    }

    private void initUserObject(){
        albums = new ArrayList<>();
        likes = new HashMap<>();
        liked = new HashMap<>();
        friends = new HashMap<>();
        unlikes = new HashMap<>();
        birthday = "";
        gender = "";

        email = "";
        name = "";
        longitude = "";
        latitude = "";
        altitude = "";
        fbUserID = "";
        userID = "";
        perferredDistance = 100;
        perferredGender = "";
        minAge = 18;
        maxAge = 100;

    }
    private void initPhotoArray(){
        photos = new ArrayList<>(MAX_PROFILE_PHOTOS_NUMBER);
        for(int i=0; i<MAX_PROFILE_PHOTOS_NUMBER; i++)
            photos.add(new Photo());
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPhotos(int position, String url) {
        this.photos.get(position).setUrl(url);
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setFbUserID(String fbUserID) {
        this.fbUserID = fbUserID;
    }

    public void setPhotoURL(Uri photoURL) {
        this.photos.get(0).setUrl(photoURL.toString());
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getFbUserID() {
        return fbUserID;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void saveUserDataToDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString((fdbh.GENDER+getUserID()), getGender());
        editor.putString((fdbh.NAME+getUserID()), getName());
        editor.putString((fdbh.BIRTHDAY+getUserID()), getBirthday());
        if(getPhotoURL() != null)
            editor.putString((fdbh.PROFILE_PICTURE+getUserID()), getPhotoURL().toString());
        editor.apply();
    }

    public void saveUserPhotosToDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<Photo> photos = getPhotos();
        String url = "";
        String key = "";
        for(Integer i=0; i<photos.size(); i++){
            key = PROFILE_PICTURE + i.toString() + getUserID();
            url = photos.get(i).getURL();
            editor.putString(key, url);
        }
        editor.apply();
    }

    public void getUserLocationFromDevice(Context context){
        getUserLongitudeFromDevice(context);
        getUserLatitudeFromDevice(context);
        getUserAltitudeFromDevice(context);
    }

    public void getUserPhotosFromDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        String url = "";
        String key = "";
        for(Integer i=0; i<getPhotos().size(); i++){
            key = PROFILE_PICTURE + i.toString() + getUserID();
            url = sharedPreferences.getString(key, getPhotos().get(i).getURL());
            getPhotos().get(i).setUrl(url);
        }
    }

    public void saveUserLocationToDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fdbh.LATITUDE, getLatitude());
        editor.putString(fdbh.LONGITUDE, getLongitude());
        editor.putString(fdbh.ALTITUDE, getAltitude());
        editor.apply();
    }

    public void getUserDataFromDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        if(getPhotoURL() != null)
            setPhotoURL(Uri.parse(sharedPreferences.getString((fdbh.PROFILE_PICTURE+getUserID()), getPhotoURL().toString())));
        setFbUserID(sharedPreferences.getString((fdbh.ID+getUserID()), getFbUserID()));
        setBirthday(sharedPreferences.getString((fdbh.BIRTHDAY+getUserID()), getBirthday()));
        setGender(sharedPreferences.getString((fdbh.GENDER+getUserID()), getGender()));
        setName(sharedPreferences.getString((fdbh.NAME+getUserID()), getName()));
        setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public void getUserAltitudeFromDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        setAltitude(sharedPreferences.getString((fdbh.ALTITUDE + getUserID()), getAltitude()));
    }

    public void getUserLatitudeFromDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        setLatitude(sharedPreferences.getString((fdbh.LATITUDE + getUserID()), getLatitude()));
    }

    public void getUserLongitudeFromDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        setLongitude(sharedPreferences.getString((fdbh.LONGITUDE+ getUserID()), getLongitude()));
    }

    public void saveUserLikesToDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String val = "";
        String key = "";
        Integer counter = 0;
        for(String k: getLikes().keySet()){
            key = USER_LIKES + counter.toString() + getUserID();
            val = k;
            editor.putString(key, val);
            ++counter;
        }
        editor.putString(USER_LIKES + getUserID() + SIZE, counter.toString());
        editor.apply();
    }

    public void getUserLikesFromDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        String val = "";
        String key = "";
        Integer size = Integer.parseInt(sharedPreferences.getString(USER_LIKES + getUserID() + SIZE, "0"));
        for(Integer i=0; i<size; i++){
            key = USER_LIKES + i.toString() + getUserID();
            val = sharedPreferences.getString(key, null);
            if(val == null) continue;
            getLikes().put(val, new User(val));
        }
    }

    public void saveUserLikedToDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String val = "";
        String key = "";
        Integer counter = 0;
        for(String k: getLiked().keySet()){
            key = USER_LIKED + counter.toString() + getUserID();
            val = k;
            editor.putString(key, val);
            ++counter;
        }
        editor.putString(USER_LIKED + getUserID() + SIZE, counter.toString());
        editor.apply();
    }

    public void getUserLikedFromDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        String val = "";
        String key = "";
        Integer size = Integer.parseInt(sharedPreferences.getString(USER_LIKED + getUserID() + SIZE, "0"));
        for(Integer i=0; i<size; i++){
            key = USER_LIKED + i.toString() + getUserID();
            val = sharedPreferences.getString(key, null);
            if(val == null) continue;
            getLiked().put(val, new User(val));
        }
    }

    public void saveUserFriendsToDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String val = "";
        String key = "";
        Integer counter = 0;
        for(String k: getFriends().keySet()){
            key = USER_FRIENDS + counter.toString() + getUserID();
            val = k;
            editor.putString(key, val);
            ++counter;
        }
        editor.putString(USER_FRIENDS + getUserID() + SIZE, counter.toString());
        editor.apply();
    }

    public void getUserFriendsFromDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        String val = "";
        String key = "";
        Integer size = Integer.parseInt(sharedPreferences.getString(USER_FRIENDS + getUserID() + SIZE, "0"));
        for(Integer i=0; i<size; i++){
            key = USER_FRIENDS + i.toString() + getUserID();
            val = sharedPreferences.getString(key, null);
            if(val == null) continue;
            getFriends().put(val, new User(val));
        }
    }

    public void saveUserUnlikesToDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String val = "";
        String key = "";
        Integer counter = 0;
        for(String k: getUnlikes().keySet()){
            key = USER_UNLIKES + counter.toString() + getUserID();
            val = k;
            editor.putString(key, val);
            ++counter;
        }
        editor.putString(USER_UNLIKES + getUserID() + SIZE, counter.toString());
        editor.apply();
    }

    public void getUserUnlikesFromDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        String val = "";
        String key = "";
        Integer size = Integer.parseInt(sharedPreferences.getString(USER_UNLIKES + getUserID() + SIZE, "0"));
        for(Integer i=0; i<size; i++){
            key = USER_FRIENDS + i.toString() + getUserID();
            val = sharedPreferences.getString(key, null);
            if(val == null) continue;
            getUnlikes().put(val, new User(val));
        }
    }


    public Uri getPhotoURL() {
        if(photos.get(0).getURL() != null)
            return Uri.parse(photos.get(0).getURL());
        return null;
    }

    public void clearAlbums(){albums.clear();}

    public static Integer getUserAge(String birthday){
        String[] bday_array = birthday.split("/");
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        int year = Integer.parseInt(bday_array[2]);
        int month = Integer.parseInt(bday_array[0]);
        int day = Integer.parseInt(bday_array[1]);

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;

    }
}


