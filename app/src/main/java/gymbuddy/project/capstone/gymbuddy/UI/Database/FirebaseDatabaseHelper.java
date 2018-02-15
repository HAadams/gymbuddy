package gymbuddy.project.capstone.gymbuddy.UI.Database;

import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

/**
 * Created by Sein on 2/15/18.
 */

public class FirebaseDatabaseHelper {

    final String FIREBASE_DATABASE_URL_USERS = "https://gymbuddy-a1579.firebaseio.com/Users";

    private boolean fetchComplete;
    private boolean errorOccured;
    private Firebase rootRef;
    private CurrentUser currentUser;

    private static FirebaseDatabaseHelper initialInstance = null;

    public static synchronized FirebaseDatabaseHelper getInstance(){
        if(initialInstance == null)
        {
            initialInstance = new FirebaseDatabaseHelper();
        }
        return initialInstance;
    }

    FirebaseDatabaseHelper(){
        fetchComplete = false;
        errorOccured = false;
        currentUser = CurrentUser.getInstance();
    }

    public void UploadUserDataToDatabase(FirebaseUser user, AccessToken accessToken) throws NullUserTokensException{
        if(user == null || accessToken == null)
            throw new NullUserTokensException("FirebaseUser and AccessToken inside UserProfileDBHelper cannot be null.");

        currentUser.user = user;
        currentUser.accessToken = accessToken;
        currentUser.name = user.getDisplayName();
        currentUser.email = user.getEmail();
        FetchCurrentUserData(accessToken);
        new UserDataUpdater().execute();

    }

    public User[] DownloadAllUserData(){
        /*

        Helper function to download all users in the database and return an array of some sorts.

         */
        return new User[1];
    }

    private void FetchCurrentUserData(AccessToken accessToken) throws NullUserTokensException{
        if(accessToken == null)
            throw new NullUserTokensException("AccessToken cannot be null inside UserProfileDBHelper");

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            currentUser.birthday = object.getString(currentUser.BIRTHDAY);
                            currentUser.gender = object.getString(currentUser.GENDER);
                            fetchComplete = true;
                        }catch(Exception e){
                            System.out.println(e.toString());
                            fetchComplete = false;
                            errorOccured = true;
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email");
        request.setParameters(parameters);

        request.executeAsync();
    }

    private class UserDataUpdater extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while(!fetchComplete && !errorOccured){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(errorOccured) return;

            rootRef = new Firebase(FIREBASE_DATABASE_URL_USERS);

            Firebase userRef = rootRef.child(currentUser.user.getUid());
            userRef.setValue(currentUser.user.getDisplayName());
            userRef.child(currentUser.NAME).setValue(currentUser.name);
            userRef.child(currentUser.BIRTHDAY).setValue(currentUser.birthday);
            userRef.child(currentUser.GENDER).setValue(currentUser.gender);
            userRef.child(currentUser.EMAIL).setValue(currentUser.email);
            errorOccured = false;
            fetchComplete = false;
        }
    }

    class NullUserTokensException extends Exception{
        NullUserTokensException(String msg){
            super(msg);
        }
    }
}
