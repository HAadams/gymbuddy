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

import org.json.JSONObject;

import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Album;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Photo;

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
    public final String PHOTOS = "photos";


    private boolean fetchComplete;
    private boolean errorOccured;
    private Firebase rootRef;
    public User currentUser;
    private Firebase userRef;
    private FirebaseUser user;

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

    public boolean isFetchComplete(){return fetchComplete;}

    public void UploadUserDataToDatabase(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        currentUser.name = user.getDisplayName();
        currentUser.email = user.getEmail();
        currentUser.photoURL = user.getPhotoUrl();
        FetchCurrentUserData();
        new UserDataUpdater().execute();

    }

    private void FetchCurrentUserData(){
        fetchComplete = false;
        errorOccured = false;

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            currentUser.birthday = object.getString(BIRTHDAY);
                            currentUser.gender = object.getString(GENDER);
                            currentUser.fbUserID = object.getString(ID);
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
        parameters.putString("fields", "id,name,link,gender,birthday,email");
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
        Firebase picRef = userRef.child(PHOTOS);
        Firebase inRef;
        for(Album album: currentUser.albums) {
            inRef = picRef.child(album.getID());
            for(Photo pic: album.getPictures())
                inRef.child(pic.getID()).setValue(pic.getURL());
        }
    }
    private boolean isErrorOccured(){return errorOccured;}

    private static class UserDataUpdater extends AsyncTask<Void, Void, Void> {
        FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
        Firebase userRef = fdbh.rootRef.child(fdbh.user.getUid());

        @Override
        protected Void doInBackground(Void... voids) {
            while(!fdbh.isFetchComplete() && !fdbh.isErrorOccured()){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(fdbh.isErrorOccured()) {
                Log.e("UserDataUpdater", "Error occurred in graph request that updates user data.");
                return;
            }
            userRef.child(fdbh.NAME).setValue(fdbh.currentUser.name);
            userRef.child(fdbh.BIRTHDAY).setValue(fdbh.currentUser.birthday);
            userRef.child(fdbh.GENDER).setValue(fdbh.currentUser.gender);
            userRef.child(fdbh.EMAIL).setValue(fdbh.currentUser.email);
            userRef.child(fdbh.PROFILE_PIC).setValue(fdbh.currentUser.photoURL.toString());
        }
    }
}
