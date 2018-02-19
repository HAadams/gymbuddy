package gymbuddy.project.capstone.gymbuddy.Utilities;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;
import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;


public class PhotosAPI {
    private String album_content_url = "https://graph.facebook.com/album_id/photos?access_token=token_id";
    private String access_token;
    private JSONArray response;
    private FirebaseDatabaseHelper firebaseDatabaseHelper;
    private boolean fetchComplete;

    public PhotosAPI(){
        firebaseDatabaseHelper = FirebaseDatabaseHelper.getInstance();
        access_token = firebaseDatabaseHelper.getAccessToken();
        fetchComplete = false;
    }

    public JSONArray getResponse(){return response;}

    private void fetchPhotosFromAlbum(String album_idٍ){
        assert access_token != null;
        fetchComplete = false;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(album_content_url.replace("album_id", album_idٍ).replace("token_id", access_token),
                new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    response = new JSONObject(new String(responseBody)).getJSONArray("data");
                    System.out.println("MA RESPONSE" + response.toString());
                    for(int i=0; i<response.length(); i++) {
                        firebaseDatabaseHelper.currentUser.photos.add(response.getJSONObject(i).get(firebaseDatabaseHelper.ID).toString());
                        System.out.println(response.getJSONObject(i).get(firebaseDatabaseHelper.ID).toString());
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

    public void fetchPicturesFromAllAlbums(){
        while(!firebaseDatabaseHelper.isFetchComplete()){}
        for(String id: firebaseDatabaseHelper.currentUser.albums.keySet()){
            fetchPhotosFromAlbum(id);
            while(!fetchComplete){}
        }

        //firebaseDatabaseHelper.updateUserPhotos();
    }


}
