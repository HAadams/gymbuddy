package gymbuddy.project.capstone.gymbuddy.Database;

import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

/**
 * Created by Sein on 2/15/18.
 **/

public class FirebaseDatabaseHelper {

    private final String FIREBASE_DATABASE_URL_USERS = "https://gymbuddy-a1579.firebaseio.com/Users";

    private boolean fetchComplete;
    private boolean errorOccured;
    private Firebase rootRef;
    public User currentUser;
    private Firebase userRef;
    private FirebaseUser user;
    AccessToken accessToken;

    private static FirebaseDatabaseHelper initialInstance = null;

    public static synchronized FirebaseDatabaseHelper getInstance(){
        if(initialInstance == null)
        {
            initialInstance = new FirebaseDatabaseHelper();
        }
        return initialInstance;
    }

    private FirebaseDatabaseHelper(){
        fetchComplete = false;
        errorOccured = false;
        currentUser = new User();
        rootRef = new Firebase(FIREBASE_DATABASE_URL_USERS);
    }

    public void setAccessToken(AccessToken accessToken){
        this.accessToken = accessToken;
    }

    public void UploadUserDataToDatabase() throws NullUserTokensException{
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null || accessToken == null)
            throw new NullUserTokensException("FirebaseUser and AccessToken inside UserProfileDBHelper cannot be null.");

        currentUser.name = user.getDisplayName();
        currentUser.email = user.getEmail();
        currentUser.photoURL = user.getPhotoUrl();
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

    public void updateLatitudeLocation(Double latitude){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.latitude = latitude.toString();;
        rootRef.child(user.getUid()).child(currentUser.LATITUDE).setValue(currentUser.latitude);
    }

    public void updateLongitudeLocation(Double longitude){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.longitude = longitude.toString();
        rootRef.child(user.getUid()).child(currentUser.LONGITUDE).setValue(currentUser.longitude);
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

            userRef = rootRef.child(user.getUid());
            userRef.child(currentUser.NAME).setValue(currentUser.name);
            userRef.child(currentUser.BIRTHDAY).setValue(currentUser.birthday);
            userRef.child(currentUser.GENDER).setValue(currentUser.gender);
            userRef.child(currentUser.EMAIL).setValue(currentUser.email);
            userRef.child(currentUser.PROFILE_PIC).setValue(currentUser.photoURL.toString());
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
