package gymbuddy.project.capstone.gymbuddy.UI.EditPage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import gymbuddy.project.capstone.gymbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoZoomFragment extends Fragment {

    private static final String URL = "url";

    public PhotoZoomFragment() {
        // Required empty public constructor
    }

    public static PhotoZoomFragment newInstance(String url) {
        PhotoZoomFragment fragment = new PhotoZoomFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_photo_zoom, container, false);

        ImageView image = view.findViewById(R.id.zoomed_imageView);

        Picasso.with(getActivity())
                .load(getArguments().getString(URL)).into(image);

        return view;
    }

}
