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
    static AlbumsSelectAdapter adapter;
    RecyclerView rv;
    PhotosAPI photosHelper = PhotosAPI.getInstance();

    List<Album> albumList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);

        albumList = FirebaseDatabaseHelper.getInstance().currentUser.albums;
        rv = findViewById(R.id.AlbumSelectRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlbumsSelectAdapter(this, albumList);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        FirebaseDatabaseHelper.getInstance().currentUser.clearAlbums();
        getUserAlbums();

    }

    public void getUserAlbums(){
        photosHelper.fetchUserAlbums();
        new AlbumsFetchedChecker().execute();
    }

    static class AlbumsFetchedChecker extends AsyncTask<Void, Void, Void>{
        PhotosAPI helper = PhotosAPI.getInstance();

        @Override
        protected Void doInBackground(Void... voids) {
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
            adapter.notifyDataSetChanged();
        }
    }

}

