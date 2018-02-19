package gymbuddy.project.capstone.gymbuddy.Database;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Sein on 2/15/18.
 **/

public class FirebaseDatabaseHelper {

    private final String FIREBASE_DATABASE_URL_USERS = "https://gymbuddy-a1579.firebaseio.com/Users";
    public final String GENDER = "gender";
    public final String BIRTHDAY = "birthday";
    public final String EMAIL = "email";
    public final String LATITUDE = "latitude";
    public final String LONGITUDE = "longitude";
    public final String NAME = "name";
    public final String PROFILE_PIC = "profile_picture";
    public final String ID = "id";
    private String picture_url = "https://graph.facebook.com/photo_id/picture?access_token=token_id";

    private boolean fetchComplete;
    private boolean errorOccured;
    private Firebase rootRef;
    public User currentUser;
    private Firebase userRef;
    private FirebaseUser user;
    private AccessToken accessToken;

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
    public String getAccessToken(){return accessToken.getToken();}
    public boolean isFetchComplete(){return fetchComplete;}
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
        fetchComplete = false;
        errorOccured = false;

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            currentUser.birthday = object.getString(BIRTHDAY);
                            currentUser.gender = object.getString(GENDER);
                            currentUser.fbUserID = object.getString(ID);
                            JSONArray array = object.getJSONObject("albums").getJSONArray("data");
                            for(int i=0; i<array.length(); i++)
                                currentUser.albums.put(
                                        array.getJSONObject(i).get(ID).toString(),
                                        array.getJSONObject(i).get(NAME).toString());

                            fetchComplete = true;
                        }catch(Exception e){
                            Log.e(getClass().toString(), "Error fetching user information");
                            Log.e(getClass().getName(), e.toString());
                            fetchComplete = false;
                            errorOccured = true;
                        }
                    }
                });

        Bundle parameters = new Bundle();
        // The information the request will fetch is defined in parameters
        parameters.putString("fields", "id,name,link,gender,birthday,email,albums");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void updateLatitudeLocation(Double latitude){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.latitude = latitude.toString();;
        rootRef.child(user.getUid()).child(LATITUDE).setValue(currentUser.latitude);
    }

    public void updateLongitudeLocation(Double longitude){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.longitude = longitude.toString();
        rootRef.child(user.getUid()).child(LONGITUDE).setValue(currentUser.longitude);
    }

    public void updateUserPhotos(){
        userRef = rootRef.child(user.getUid());
        Firebase picRef = userRef.child(currentUser.albums.get(currentUser.photos.get(0)));
        for(String pic: currentUser.photos)
            picRef.child(pic).setValue(picture_url.replace("photo_id", pic).replace("token_id", accessToken.getToken()));
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
            if(errorOccured) {
                // reset and return
                errorOccured = false;
                return;
            }

            userRef = rootRef.child(user.getUid());
            userRef.child(NAME).setValue(currentUser.name);
            userRef.child(BIRTHDAY).setValue(currentUser.birthday);
            userRef.child(GENDER).setValue(currentUser.gender);
            userRef.child(EMAIL).setValue(currentUser.email);
            userRef.child(PROFILE_PIC).setValue(currentUser.photoURL.toString());
            errorOccured = false;
        }
    }

    class NullUserTokensException extends Exception{
        NullUserTokensException(String msg){
            super(msg);
        }
    }
}
