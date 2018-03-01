package gymbuddy.project.capstone.gymbuddy.UI.EditPage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.R;
import gymbuddy.project.capstone.gymbuddy.UI.HomePage.HomeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment {

    private static final String POSITION = "position";
    private FirebaseDatabaseHelper fdbh;
    public PhotosFragment() {
        // Required empty public constructor
    }

    public static PhotosFragment newInstance(int position) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        fdbh = FirebaseDatabaseHelper.getInstance();
        GridView gridView = view.findViewById(R.id.photosList_gridView);
        gridView.setAdapter(new PhotosSelectAdapter(getActivity(), fdbh.currentUser.albums.get(getArguments().getInt(POSITION))));

        return view;
    }

}
