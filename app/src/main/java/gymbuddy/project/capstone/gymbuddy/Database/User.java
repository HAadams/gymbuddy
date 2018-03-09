package gymbuddy.project.capstone.gymbuddy.Database;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Album;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Photo;

public class User {
    private static final int MAX_PROFILE_PHOTOS_NUMBER = 5;
    private String birthday;
    private String gender;
    private String email;
    private String name;
    private String longitude;
    private String latitude;
    private String fbUserID;
    private List<Album> albums;
    private List<Photo> photos; // List of profile pictures that user chooses
    private Uri photoURL;

    public User(){
        albums = new ArrayList<>();
        photos = new ArrayList<>(MAX_PROFILE_PHOTOS_NUMBER);
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(int position, Photo photo) {
        this.photos.set(position, photo);
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

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void setPhotoURL(Uri photoURL) {
        this.photoURL = photoURL;
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
        return photoURL;
    }

    public void clearAlbums(){albums.clear();}

}


