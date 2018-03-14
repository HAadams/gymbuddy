package gymbuddy.project.capstone.gymbuddy.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.util.List;

import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Photo;

import static android.content.Context.MODE_PRIVATE;

public class CurrentUser extends User {

    public static final String PROFILE_PICTURE0 = "profile_picture_0";
    public static final String PROFILE_PICTURE1 = "profile_picture_1";
    public static final String PROFILE_PICTURE2 = "profile_picture_2";
    public static final String PROFILE_PICTURE3 = "profile_picture_3";
    public static final String PROFILE_PICTURE4 = "profile_picture_4";

    private static CurrentUser initialInstance = null;

    public static synchronized CurrentUser getInstance(){
        if(initialInstance == null)
        {
            initialInstance = new CurrentUser();
        }
        return initialInstance;
    }

    public void SaveUserDataToDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fdbh.GENDER, getGender());
        editor.putString(fdbh.NAME, getName());
        editor.putString(fdbh.EMAIL, getGender());
        editor.putString(fdbh.BIRTHDAY, getBirthday());
        if(getPhotoURL() != null)
            editor.putString(fdbh.PROFILE_PICTURE, getPhotoURL().toString());
        editor.apply();
    }

    public void SaveUserPhotosToDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<Photo> photos = getPhotos();
        String url = "";
        String key = "";
        for(int i=0; i<photos.size(); i++){
            switch(i){
                case 0:
                    key = PROFILE_PICTURE0;
                    break;

                case 1:
                    key = PROFILE_PICTURE1;
                    break;

                case 2:
                    key = PROFILE_PICTURE2;
                    break;

                case 3:
                    key = PROFILE_PICTURE3;
                    break;

                case 4:
                    key = PROFILE_PICTURE4;
                    break;
                default:
                    key = null;
                    break;
            }
            if(key == null) return;
            url = photos.get(i).getURL();
            editor.putString(key, url);
        }
        editor.apply();
    }

    public void getUserPhotosFromDevice(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        String url = "";
        String key = "";
        for(int i=0; i<getPhotos().size(); i++){
            switch(i){
                case 0:
                    key = PROFILE_PICTURE0;
                    break;

                case 1:
                    key = PROFILE_PICTURE1;
                    break;

                case 2:
                    key = PROFILE_PICTURE2;
                    break;

                case 3:
                    key = PROFILE_PICTURE3;
                    break;

                case 4:
                    key = PROFILE_PICTURE4;
                    break;
                default:
                    key = null;
                    break;
            }
            if(key == null) return;
            url = sharedPreferences.getString(key, getPhotos().get(i).getURL());
            getPhotos().get(i).setUrl(url);
        }


    }

    public void SaveUserLocationToDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fdbh.LATITUDE, getLatitude());
        editor.putString(fdbh.LONGITUDE, getLongitude());
        editor.apply();
    }

    public void getUserDataFromDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        if(getPhotoURL() != null)
            setPhotoURL(Uri.parse(sharedPreferences.getString(fdbh.PROFILE_PICTURE, getPhotoURL().toString())));
        setFbUserID(sharedPreferences.getString(fdbh.ID, getFbUserID()));
        setBirthday(sharedPreferences.getString(fdbh.BIRTHDAY, getBirthday()));
        setGender(sharedPreferences.getString(fdbh.GENDER, getGender()));
        setEmail(sharedPreferences.getString(fdbh.EMAIL, getEmail()));
        setName(sharedPreferences.getString(fdbh.NAME, getName()));
    }

    public void getUserLatitudeFromDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        setLatitude(sharedPreferences.getString(fdbh.LATITUDE, getLatitude()));
    }

    public void getUserLongitudeFromDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        setLongitude(sharedPreferences.getString(fdbh.LONGITUDE, getLongitude()));
    }
}
