package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.facebook.drawee.view.SimpleDraweeView;

import gymbuddy.project.capstone.gymbuddy.R;


public class PhotosSelectAdapter extends BaseAdapter{

    Album album;
    Context context;
    OnListFragmentInteractionListener mListener;

    PhotosSelectAdapter(Context context, Album album, OnListFragmentInteractionListener mListener){
        this.context = context;
        this.album = album;
        this.mListener = mListener;
    }

    @Override
    public int getCount() {
        return album.getPictures().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        SimpleDraweeView draweeView = new SimpleDraweeView(context);
        draweeView.setImageURI(album.getPictures().get(i).getURL());
        draweeView.setLayoutParams(new GridView.LayoutParams(
                  (int)context.getResources().getDimension(R.dimen.picture_size),
                 (int)context.getResources().getDimension(R.dimen.picture_size)
        ));
        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPicturePressedInteraction(album.getPictures().get(i));
            }
        });
        return draweeView;

    }

}
