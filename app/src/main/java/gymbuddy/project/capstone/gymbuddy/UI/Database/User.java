package gymbuddy.project.capstone.gymbuddy.UI.Database;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Sein on 2/15/18.
 */

public class User {
    FirebaseUser user;
    AccessToken accessToken;

    final String FEMALE = "female";
    final String GENDER = "gender";
    final String BIRTHDAY = "birthday";
    final String EMAIL = "email";
    final String NAME = "name";


    String birthday;
    String gender;
    String email;
    String name;

    User(){}

    public boolean isFemale(){
        return gender.equalsIgnoreCase(FEMALE);
    }
}


