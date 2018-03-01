package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.R;
import gymbuddy.project.capstone.gymbuddy.UI.HomePage.HomeFragment;


public class AlbumsFragment extends Fragment {


    public AlbumsFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    AlbumsSelectAdapter adapter;
    RecyclerView rv;
    PhotosAPI photosHelper;
    List<Album> albumList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        photosHelper = PhotosAPI.getInstance();
        albumList = FirebaseDatabaseHelper.getInstance().currentUser.albums;
        rv = view.findViewById(R.id.AlbumSelectRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AlbumsSelectAdapter(getActivity(), albumList);
        rv.setAdapter(adapter);
        // Clear album data so there won't be any duplicates
        FirebaseDatabaseHelper.getInstance().currentUser.clearAlbums();
        // Fetch user albums and update the adapter
        getUserAlbums();

        return view;
    }

    public void getUserAlbums(){
        // This executes a thread to fetch user albums from facebook
        photosHelper.fetchUserAlbums();
        // This waits for the albums to be fetched and notifies the adapter afterwards
        new AsyncPhotosFetcher(adapter).execute();
    }

    private static class CheckAlbumsFetchedAndUpdate extends AsyncTask<Void, Void, Void> {
        PhotosAPI helper;
        AlbumsSelectAdapter adapter;

        CheckAlbumsFetchedAndUpdate(AlbumsSelectAdapter adapter){
            this.adapter = adapter;
            helper= PhotosAPI.getInstance();
        }

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

    private static class AsyncPhotosFetcher extends AsyncTask<Void, Void, Void> {
        PhotosAPI helper;
        AlbumsSelectAdapter adapter;

        AsyncPhotosFetcher(AlbumsSelectAdapter adapter){
            this.adapter = adapter;
            helper= PhotosAPI.getInstance();
        }

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
                while(!helper.isPhotosFetchComplete()){}
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
