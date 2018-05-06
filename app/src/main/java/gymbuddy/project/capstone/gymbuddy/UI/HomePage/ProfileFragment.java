package gymbuddy.project.capstone.gymbuddy.UI.HomePage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gymbuddy.project.capstone.gymbuddy.Adapters.Profile;
import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.Database.User;
import gymbuddy.project.capstone.gymbuddy.R;


public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView name = rootview.findViewById(R.id.nameTv);
        TextView age = rootview.findViewById(R.id.ageTV);
        TextView bio = rootview.findViewById(R.id.bioTV);
        ViewPager viewPager = rootview.findViewById(R.id.pictureVP);
        User currentProfile = null;

        for(Profile p: FirebaseDatabaseHelper.getInstance().users_from_database) {
            System.out.println("IN LOOP: "+p.getUser().getUserID());
            if (p.getUser().getUserID().equalsIgnoreCase(mParam1)) {
                System.out.println("MATCH: "+p.getUser().getUserID() + " "+mParam1);
                currentProfile = p.getUser();
            }
        }
        System.out.println("PARAMS "+mParam1+" "+FirebaseDatabaseHelper.getInstance().users_from_database.size());
        //System.out.println("USER FORM DB: "+ currentProfile.getName());
        System.out.println("NAME: "+currentProfile.getUserID());
        System.out.println("NAME: "+currentProfile.getName());
        name.setText(currentProfile.getName());

        age.setText(currentProfile.getAge().toString());
        bio.setText(currentProfile.getBiography());




        return rootview;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction();
    }
}
