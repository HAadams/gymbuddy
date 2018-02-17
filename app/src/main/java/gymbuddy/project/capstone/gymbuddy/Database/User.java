package gymbuddy.project.capstone.gymbuddy.Database;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;

import android.net.Uri;

/**
 * Created by Sein on 2/15/18.
 */

public class User {
    final String FEMALE = "female";
    final String GENDER = "gender";
    final String BIRTHDAY = "birthday";
    final String EMAIL = "email";
    final String LATITUDE = "latitude";
    final String LONGITUDE = "longitude";
    final String NAME = "name";
    final String PROFILE_PIC = "profile_picture";


    String birthday;
    String gender;
    String email;
    String name;
    String longitude;
    String latitude;
    Uri photoURL;

    User(){}

    public boolean isFemale(){
        return gender.equalsIgnoreCase(FEMALE);
    }
}


