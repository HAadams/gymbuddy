package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.R;


public class AlbumsFragment extends Fragment {

    public AlbumsFragment() {
        // Required empty public constructor
    }

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }


    AlbumsSelectAdapter adapter;
    RecyclerView rv;
    PhotosAPI photosHelper;
    List<Album> albumList;
    AlbumListInteractionListener albums_listener;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    static Activity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentManager = getActivity().getSupportFragmentManager();
        albums_listener = new AlbumListInteractionListener() {
            @Override
            public void onAlbumSelectedInteraction(int position) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.homeFragmentContainer, PhotosFragment.newInstance(position)).addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        mContext = getActivity();
        photosHelper = PhotosAPI.getInstance();
        albumList = CurrentUser.getInstance().getAlbums();

        rv = view.findViewById(R.id.AlbumSelectRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AlbumsSelectAdapter(getActivity(), albumList, albums_listener);
        rv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Clear album data so there won't be any duplicates
        CurrentUser.getInstance().clearAlbums();
        // Fetch user albums and update the adapter
        getUserAlbums();
    }

    public void getUserAlbums(){
        // This executes a thread to fetch user albums from facebook
        photosHelper.fetchUserAlbums();
        // This waits for the albums to be fetched and notifies the adapter afterwards
        new AsyncPhotosFetcher(adapter).execute();
    }

    private static void updatePhotosViewer(final AlbumsSelectAdapter ad){
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ad.notifyDataSetChanged();

            }
        });
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
            Looper.prepare();
            try {
                while (!helper.isAlbumsFetchComplete()) {
                    if (helper.isErrorOccured()) {
                        Log.e(getClass().toString(), "Error occurred while fetching user albums");
                        return null;
                    }
                }
                for (int i = 0; i < CurrentUser.getInstance().getAlbums().size(); i++) {
                    updatePhotosViewer(adapter);
                    helper.fetchPhotosFromAlbum(CurrentUser.getInstance().getAlbums().get(i).getID(), i);
                    while (!helper.isPhotosFetchComplete()) ;
                }
            }catch(Exception e){
                Log.e("AsyncPhotoFetcher", e.getStackTrace().toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("Thread is  DONE");
            // Once albums are fetched, notify the adapter to update the list view
            adapter.notifyDataSetChanged();
        }
    }

    public interface AlbumListInteractionListener {
        void onAlbumSelectedInteraction(int position);
    }

}
