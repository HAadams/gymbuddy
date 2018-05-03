package gymbuddy.project.capstone.gymbuddy.UI.HomePage;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import gymbuddy.project.capstone.gymbuddy.Adapters.Profile;
import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.Map.LocationHelper;
import gymbuddy.project.capstone.gymbuddy.R;



public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1"; //isPro
    private static final String ARG_PARAM2 = "param2";


    private SwipePlaceHolderView mSwipeView;
    private Context mContext;


    // TODO: Rename and change types of parameters
    private Boolean mParam1;
    private String mParam2;
    LottieAnimationView unlike;
    LottieAnimationView like;


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Boolean param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mSwipeView = rootView.findViewById(R.id.swipeView);
        mContext = getContext();

        SimpleDraweeView v = rootView.findViewById(R.id.homeProfileButton);
        unlike = rootView.findViewById(R.id.rejectBtn);
        like = rootView.findViewById(R.id.acceptBtn);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onPhotosFragmentInteraction();
                }
            }
        });
        v.setImageURI(CurrentUser.getInstance().getPhotoURL());

        mSwipeView.getBuilder()
                .setDisplayViewCount(5)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        if (mSwipeView!=null) {
            for (Profile profile : FirebaseDatabaseHelper.getInstance().users_from_database) {

                mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView, new cardTapToProfileCallback() {
                    @Override
                    public void onTap(Profile profile) {
                        Toast.makeText(getContext(),"tap for dat ass: " + profile.getUser().getName(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReject(Profile profile) {
                        Toast.makeText(getContext(),"too fat: " + profile.getUser().getName(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAccept(Profile profile) {
                        Toast.makeText(getContext(),"short and white: " + profile.getUser().getName() ,
                                Toast.LENGTH_SHORT).show();
                    }
                }));
            }
        }
        rootView.findViewById(R.id.homeMapButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMapButtonPressed();
            }
        });

        rootView.findViewById(R.id.homeMessengerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMessengerButtonPressed();
            }
        });

        rootView.findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        rootView.findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        Button logout = rootView.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                onLogoutButtonPressed();
            }
        });
        Button photos = rootView.findViewById(R.id.uploadPicsButton);
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPhotosButtonPressed();
            }
        });

        return rootView;
    }




    public void onLogoutButtonPressed() {
        if (mListener != null) {
            mListener.onLogoutFragmentInteraction();
        }
    }

    public void onPhotosButtonPressed() {
        if (mListener != null) {
            mListener.onPhotosFragmentInteraction();
        }
    }

    public void onMessengerButtonPressed() {
        if (mListener != null) {
            mListener.onMessengerFragmentInteraction();
        }
    }

    public void onMapButtonPressed() {
        if (mListener != null) {
            mListener.onMapFragmentInteraction();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onLogoutFragmentInteraction();

        void onPhotosFragmentInteraction();

        void onMessengerFragmentInteraction();

        void onMapFragmentInteraction();
    }
}
