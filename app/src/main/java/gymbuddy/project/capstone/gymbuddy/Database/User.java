package gymbuddy.project.capstone.gymbuddy.Database;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Sein on 2/15/18.
 */

public class User {
    final String FEMALE = "female";

    String birthday;
    String gender;
    String email;
    String name;
    String longitude;
    String latitude;
    String fbUserID;

    //Contains set of albums in the form of <ID, NAME>
    public HashMap<String, String> albums;
    //Contains the list of pictures inside each album
    public HashMap<String, ArrayList<String>> photos;
    Uri photoURL;

    User(){
        albums = new HashMap<String, String>();
        photos = new HashMap<String, ArrayList<String>>();
    }

    public boolean isFemale(){
        return gender.equalsIgnoreCase(FEMALE);
    }
}


