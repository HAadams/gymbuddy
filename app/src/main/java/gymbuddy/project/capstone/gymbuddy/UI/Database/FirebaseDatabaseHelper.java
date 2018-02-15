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

    private FirebaseUser user;
    AccessToken accessToken;

    String birthday;
    String gender;
    String email;

    private static FirebaseDatabaseHelper initialInstance = null;

    public static synchronized FirebaseDatabaseHelper getInstance(){
        if(initialInstance == null)
        {
            initialInstance = new FirebaseDatabaseHelper();
        }
        return initialInstance;
    }

    FirebaseDatabaseHelper(){
        user = null;
        accessToken = null;
        fetchComplete = false;
        errorOccured = false;

    }

    public void setFirebaseUser(FirebaseUser user){
        this.user = user;
    }

    public void setAccessToken(AccessToken accessToken){
        this.accessToken = accessToken;
    }

    public void UpdateUserData() throws NullUserTokensException {
        if(user == null || accessToken == null)
            throw new NullUserTokensException("FirebaseUser and AccessToken inside UserProfileDBHelper cannot be null.");

        FetchUserData();
        new UserDataUpdater().execute();

    }

    public void FetchUserData() throws NullUserTokensException{
        if(accessToken == null)
            throw new NullUserTokensException("AccessToken cannot be null inside UserProfileDBHelper");

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        System.out.println(object.toString());
                        try {
                            birthday = object.getString("birthday");
                            gender = object.getString("gender");
                            email = object.getString("email");
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

            Firebase userRef = rootRef.child(user.getUid());
            userRef.setValue(user.getDisplayName());
            userRef.child("Birthday").setValue(birthday);
            userRef.child("Gender").setValue(gender);
            userRef.child("Email").setValue(email);

        }
    }

    class NullUserTokensException extends Exception{
        NullUserTokensException(String msg){
            super(msg);
        }
    }
}
