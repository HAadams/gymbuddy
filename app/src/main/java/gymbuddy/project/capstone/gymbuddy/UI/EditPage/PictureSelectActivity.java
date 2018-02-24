package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.Network.NetworkCallback;
import gymbuddy.project.capstone.gymbuddy.R;
import gymbuddy.project.capstone.gymbuddy.Utilities.PhotosAPI;

public class PictureSelectActivity extends AppCompatActivity {
    AlbumsSelectAdapter adapter;
    RecyclerView rv;
    PhotosAPI photosHelper;
    List<Album> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);

        photosHelper = PhotosAPI.getInstance();
        albumList = FirebaseDatabaseHelper.getInstance().currentUser.albums;
        rv = findViewById(R.id.AlbumSelectRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlbumsSelectAdapter(this, albumList);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        // Clear album data so there won't be any duplicates
        FirebaseDatabaseHelper.getInstance().currentUser.clearAlbums();
        // Fetch user albums and update the adapter
        getUserAlbums();

    }

    public void getUserAlbums(){
        // This executes a thread to fetch user albums from facebook
        photosHelper.fetchUserAlbums();
        // This waits for the albums to be fetched and notifies the adapter afterwards
        new CheckAlbumsFetchedAndUpdate(adapter).execute();
    }

    private static class CheckAlbumsFetchedAndUpdate extends AsyncTask<Void, Void, Void>{
        PhotosAPI helper = PhotosAPI.getInstance();
        AlbumsSelectAdapter adapter;
        CheckAlbumsFetchedAndUpdate(AlbumsSelectAdapter adapter){this.adapter = adapter;}

        @Override
        protected Void doInBackground(Void... voids) {
            // Wait for albums to be fetched, return on error
            while(!helper.isAlbumsFetchComplete()){
                if(helper.isErrorOccured()){
                    Log.e(getClass().toString(), "Error occurred while fetching user albums");
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Once albums are fetched, notify the adapter to update the list view
            adapter.notifyDataSetChanged();
        }
    }

}

