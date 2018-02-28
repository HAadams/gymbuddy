package gymbuddy.project.capstone.gymbuddy.Database;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Album;

/**
 * Created by Sein on 2/15/18.
 */

public class User {
    final String FEMALE = "female";

    public String birthday;
    public String gender;
    public String email;
    public String name;
    public String longitude;
    public String latitude;
    public String fbUserID;
    public List<Album> albums;
    Uri photoURL;

    User(){
        albums = new ArrayList<>();
    }

    public boolean isFemale(){
        return gender.equalsIgnoreCase(FEMALE);
    }

    public void clearAlbums(){albums.clear();}

}


