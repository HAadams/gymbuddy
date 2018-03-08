package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
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
    private AlbumsFragment.AlbumListInteractionListener listener;

    public AlbumsSelectAdapter(Context context, List<Album> albums, AlbumsFragment.AlbumListInteractionListener listener) {
        this.context = context;
        this.albums = albums;
        this.listener = listener;
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
        holder.photos_number.setText(Integer.toString(album.getPictures().size()));
        holder.logo.setImageURI(albums.get(position).getLogoPicture().getURL());

    }

    @Override
    public int getItemCount() {
        return firebaseDatabaseHelper.currentUser.albums.size();
    }


    class AlbumCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SimpleDraweeView logo;
        TextView album_name;
        TextView photos_number;

        AlbumCardViewHolder(View view){
            super(view);
            logo = view.findViewById(R.id.album_logo_imageView);
            album_name = view.findViewById(R.id.album_name_textView);
            photos_number = view.findViewById(R.id.photos_number_textView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onAlbumSelectedInteraction(getLayoutPosition());
        }
    }

}
