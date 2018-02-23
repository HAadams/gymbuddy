package gymbuddy.project.capstone.gymbuddy.Utilities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Album;
import gymbuddy.project.capstone.gymbuddy.UI.EditPage.Photo;


public class PhotosAPI {
    private String album_content_url = "https://graph.facebook.com/album_id/photos?access_token=token_id";

    private JSONArray response;
    public FirebaseDatabaseHelper firebaseDatabaseHelper;
    private boolean fetchComplete;
    private boolean albumsFetchComplete;
    private boolean errorOccured;

    private static PhotosAPI initialInstance = null;

    public static synchronized PhotosAPI getInstance(){
        if(initialInstance == null)
        {
            initialInstance = new PhotosAPI();
        }
        return initialInstance;
    }


    PhotosAPI(){
        fetchComplete = false;
        errorOccured = false;
        albumsFetchComplete = false;
        firebaseDatabaseHelper = FirebaseDatabaseHelper.getInstance();
    }

    public void fetchPhotosFromAlbum(final String album_id, final int album_positionٍ){
        fetchComplete = false;
        if(firebaseDatabaseHelper.currentUser.albums.size() == 0){
            Log.e(getClass().toString(), "No albums found to fetch pictures from");
            return;
        }
        SyncHttpClient client = new SyncHttpClient();
        client.get(album_content_url.replace("album_id", album_id).replace("token_id", AccessToken.getCurrentAccessToken().getToken()),
                new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    response = new JSONObject(new String(responseBody)).getJSONArray("data");

                    // For each picture ID returned in the json object, add it to the photos list
                    for(int i=0; i<response.length(); i++) {
                        firebaseDatabaseHelper.currentUser.albums.get(album_positionٍ).addPicture(
                                new Photo(response.getJSONObject(i).get(firebaseDatabaseHelper.ID).toString())
                        );
                    }
                    fetchComplete = true;
                }catch(Exception e){
                    Log.e(getClass().getName(), "Error parsing get request responseBody");
                    e.printStackTrace();
                    fetchComplete = false;
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(getClass().getName(), "Error sending GET request");
                fetchComplete = false;
            }
        });
    }

    public boolean isFetchComplete(){return fetchComplete;}
    public boolean isAlbumsFetchComplete(){return albumsFetchComplete;}
    public boolean isErrorOccured(){return errorOccured;}



    public void fetchUserAlbums() {
        albumsFetchComplete = false;
        errorOccured = false;
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            JSONArray array = object.getJSONObject("albums").getJSONArray("data");
                            for(int i=0; i<array.length(); i++) {
                                firebaseDatabaseHelper.currentUser.albums.add(new Album(
                                        array.getJSONObject(i).get(firebaseDatabaseHelper.ID).toString(),
                                       array.getJSONObject(i).get(firebaseDatabaseHelper.NAME).toString()));

                            }

                            albumsFetchComplete = true;
                        }catch(Exception e){
                            Log.e(getClass().toString(), "Error fetching user information");
                            Log.e(getClass().getName(), e.toString());
                            e.printStackTrace();
                            albumsFetchComplete = false;
                            errorOccured = true;
                        }
                    }
                });

        Bundle parameters = new Bundle();
        // The information the request will fetch is defined in parameters
        parameters.putString("fields", "albums");
        request.setParameters(parameters);
        request.executeAsync();
    }


    static class AsyncPicturesFetcher extends AsyncTask<Void, Void, Void>{
        PhotosAPI helper = PhotosAPI.getInstance();

        @Override
        protected Void doInBackground(Void... voids) {
            while(!helper.isAlbumsFetchComplete()){
                if(helper.isErrorOccured()){
                    Log.e(getClass().toString(), "Error occurred while fetching user albums");
                    return null;
                }
            }
            for(int i=0; i<helper.firebaseDatabaseHelper.currentUser.albums.size(); i++){
                helper.fetchPhotosFromAlbum(helper.firebaseDatabaseHelper.currentUser.albums.get(i).getID(), i);
                while(!helper.isFetchComplete()){}
            }
            return null;
        }

    }

}
