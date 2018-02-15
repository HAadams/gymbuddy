package gymbuddy.project.capstone.gymbuddy.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import gymbuddy.project.capstone.gymbuddy.R;


public class MainFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    public MainFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance() {
//        LoginFragment fragment = new LoginFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_discovery_01, container, false);

        return rootView;
    }

}

