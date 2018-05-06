package gymbuddy.project.capstone.gymbuddy.UI.EditPage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment implements OnListFragmentInteractionListener {

   // private OnListFragmentInteractionListener mListener;

    private static final String POSITION = "position";
    private static final String PROFILE_PIC_POSITION = "profile_pic_position";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    public PhotosFragment() {
        // Required empty public constructor
    }

    public static PhotosFragment newInstance(int album_position, int profile_pic_position) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, album_position);
        args.putInt(PROFILE_PIC_POSITION, profile_pic_position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        GridView gridView = view.findViewById(R.id.photosList_gridView);
        gridView.setAdapter(new PhotosSelectAdapter(getActivity(),
                            CurrentUser.getInstance().getAlbums().get(getArguments().getInt(POSITION)),
                            this));

        return view;
    }


    @Override
    public void onPicturePressedInteraction(Photo photo) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeFragmentContainer,
                PhotoZoomFragment.newInstance(photo.getURL(), getArguments().getInt(PROFILE_PIC_POSITION))).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
interface OnListFragmentInteractionListener {
    void onPicturePressedInteraction(Photo photo);
}