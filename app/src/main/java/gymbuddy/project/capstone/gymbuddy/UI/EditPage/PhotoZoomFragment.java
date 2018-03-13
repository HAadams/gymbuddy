package gymbuddy.project.capstone.gymbuddy.UI.EditPage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoZoomFragment extends Fragment {

    private static final String URL = "url";
    private static final String POSITION = "position";

    public PhotoZoomFragment() {
        // Required empty public constructor
    }

    public static PhotoZoomFragment newInstance(String url, int position) {
        PhotoZoomFragment fragment = new PhotoZoomFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_photo_zoom, container, false);

        SimpleDraweeView image = view.findViewById(R.id.zoomed_imageView);

        image.setImageURI(getArguments().getString(URL));

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo tmp = new Photo();
                tmp.setUrl(getArguments().getString(URL));
                CurrentUser.getInstance().setPhotos(getArguments().getInt(POSITION), tmp);
                getFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

}
