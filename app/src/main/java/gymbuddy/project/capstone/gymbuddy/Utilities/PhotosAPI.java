package gymbuddy.project.capstone.gymbuddy.Utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.firebase.client.Firebase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;


public class PhotosAPI {
    private String album_content_url = "https://graph.facebook.com/album_id/photos?access_token=token_id";
    private String access_token;
    private JSONArray response;
    protected FirebaseDatabaseHelper firebaseDatabaseHelper;
    private boolean fetchComplete;


    private static PhotosAPI initialInstance = null;

    public static synchronized PhotosAPI getInstance(){
        if(initialInstance == null)
        {
            initialInstance = new PhotosAPI();
        }
        return initialInstance;
    }


    public PhotosAPI(){
        firebaseDatabaseHelper = FirebaseDatabaseHelper.getInstance();
        access_token = firebaseDatabaseHelper.getAccessToken();
        fetchComplete = false;
    }

    public JSONArray getResponse(){return response;}

    protected void fetchPhotosFromAlbum(final String album_idٍ){
        assert access_token != null;
        fetchComplete = false;

        SyncHttpClient client = new SyncHttpClient();
        client.get(album_content_url.replace("album_id", album_idٍ).replace("token_id", access_token),
                new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    ArrayList<String> pics = new ArrayList<String>();
                    response = new JSONObject(new String(responseBody)).getJSONArray("data");
                    System.out.println("MA RESPONSE" + response.toString());
                    for(int i=0; i<response.length(); i++) {
                        pics.add(response.getJSONObject(i).get(firebaseDatabaseHelper.ID).toString());
                    }
                    firebaseDatabaseHelper.currentUser.photos.put(album_idٍ, pics);
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
    public void fetchMeStuff(){
        new AsyncPicturesFetcher().execute();
    }
    public boolean isFetchComplete(){return fetchComplete;}

    static class AsyncPicturesFetcher extends AsyncTask<Void, Void, Void>{
        PhotosAPI helper = PhotosAPI.getInstance();

        @Override
        protected Void doInBackground(Void... voids) {
            while(!helper.firebaseDatabaseHelper.isFetchComplete()){}
            for(String id: helper.firebaseDatabaseHelper.currentUser.albums.keySet()){
                helper.fetchPhotosFromAlbum(id);
                while(!helper.isFetchComplete()){}
            }
            helper.firebaseDatabaseHelper.updateUserPhotos();
            return null;
        }


    }
}
