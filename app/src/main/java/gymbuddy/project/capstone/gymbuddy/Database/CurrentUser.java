package gymbuddy.project.capstone.gymbuddy.Database;

/**
 * Created by Sein on 2/15/18.
 */

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