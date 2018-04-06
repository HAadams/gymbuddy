package gymbuddy.project.capstone.gymbuddy.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;

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
    private String location;

    private String fbUserID;
    private String userID;
    private List<Album> albums;
    private List<Photo> photos; // List of profile pictures that user chooses

    private ArrayList<String> likes;
    private ArrayList<String>liked;
    private Map<String, User> friends;
    private ArrayList<String> unlikes;

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


    public ArrayList<String> getLikes() {
        return likes;
    }

    public ArrayList<String> getLiked() {
        return liked;
    }

    public Map<String, User> getFriends() {
        return friends;
    }

    public ArrayList<String> getUnlikes() {
        return unlikes;
    }

    public void addToLikes(String id){
        this.likes.add(id);
    }
    public void addToLiked(String id){
        this.liked.add(id);
    }
    public void addToFriends(String id, User user){
        this.friends.put(id, user);
    }
    public void addToUnlikes(String id){
        this.unlikes.add(id);
    }
    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
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
        likes = new ArrayList<>();
        liked = new ArrayList<>();
        friends = new HashMap<>();
        unlikes = new ArrayList<>();
        birthday = "";
        gender = "";
        age = 0;
        email = "";
        name = "";
        longitude = "0.0";
        latitude = "0.0";
        altitude = "0.0";
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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


