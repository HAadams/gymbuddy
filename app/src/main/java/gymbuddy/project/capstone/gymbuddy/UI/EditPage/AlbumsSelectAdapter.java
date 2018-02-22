package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.util.List;

import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.R;

/**
 * Created by Sein on 2/22/18.
 */

public class AlbumsSelectAdapter extends RecyclerView.Adapter<AlbumsSelectAdapter.AlbumCardViewHolder>{

    private Context context;
    private FirebaseDatabaseHelper firebaseDatabaseHelper;
    private List<Album> albums;

    public AlbumsSelectAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
        firebaseDatabaseHelper = FirebaseDatabaseHelper.getInstance();
    }

    @Override
    public AlbumCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.album_select_view_card, null);
        return new AlbumCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumCardViewHolder holder, int position) {
        Album album = albums.get(position);
        holder.album_name.setText(album.getName());
        Picasso.with(context)
                .load(albums.get(position).getPictures().get(0).getURL()).into(holder.logo);

        try{
            holder.logo.setImageBitmap(album.getPictures().get(0).getBitmap());
        }catch(Exception e){e.printStackTrace();}
    }

    @Override
    public int getItemCount() {
        return firebaseDatabaseHelper.currentUser.albums.size();
    }



    class AlbumCardViewHolder extends RecyclerView.ViewHolder{

        ImageView logo;
        TextView album_name;

        AlbumCardViewHolder(View view){
            super(view);
            logo = view.findViewById(R.id.album_logo_imageView);
            album_name = view.findViewById(R.id.album_titile_textView);
        }

    }

}
