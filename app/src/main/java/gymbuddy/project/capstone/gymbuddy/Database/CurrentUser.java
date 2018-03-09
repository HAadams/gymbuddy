package gymbuddy.project.capstone.gymbuddy.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import static android.content.Context.MODE_PRIVATE;

public class CurrentUser extends User {

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
        editor.putString(fdbh.PROFILE_PICTURE, getPhotoURL().toString());
        editor.apply();
    }

    public void SaveUserLocationToDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(fdbh.LATITUDE, getLatitude());
        editor.putString(fdbh.LONGITUDE, getLongitude());
        editor.apply();
    }

    public void GetUserDataFromDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        setPhotoURL(Uri.parse(sharedPreferences.getString(fdbh.PROFILE_PICTURE, null)));
        setFbUserID(sharedPreferences.getString(fdbh.ID, null));
        setBirthday(sharedPreferences.getString(fdbh.BIRTHDAY, null));
        setGender(sharedPreferences.getString(fdbh.GENDER, null));
        setEmail(sharedPreferences.getString(fdbh.EMAIL, null));
        setName(sharedPreferences.getString(fdbh.NAME, null));
    }

    public void GetUserLocationFromDevice(Context context){
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("STRING", MODE_PRIVATE);
        setLatitude(sharedPreferences.getString(fdbh.LATITUDE, null));
        setLongitude(sharedPreferences.getString(fdbh.LONGITUDE, null));
    }
}
