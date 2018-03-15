package gymbuddy.project.capstone.gymbuddy.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.util.List;
import java.util.Map;

import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Photo;

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

}
