package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.Network.NetworkCallback;
import gymbuddy.project.capstone.gymbuddy.R;

public class PictureSelectActivity extends AppCompatActivity {
    AlbumsSelectAdapter adapter;
    RecyclerView rv;
    Context context;

    List<Album> albumList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);

context = this;
        rv = findViewById(R.id.AlbumSelectRecyclerView);
        //rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));



        downloadContent(new NetworkCallback() {
            @Override
            public void onSuccess() {

                adapter = new AlbumsSelectAdapter(context, albumList);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure() {

            }
        });



    }

    private void downloadContent(NetworkCallback networkCallback) {


        albumList= FirebaseDatabaseHelper.getInstance().currentUser.albums;
        networkCallback.onSuccess();

    }

}

