package gymbuddy.project.capstone.gymbuddy.UI.EditPage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private static final int PROFILE_IMAGE_1 = 0;
    private static final int PROFILE_IMAGE_2 = 1;
    private static final int PROFILE_IMAGE_3 = 2;
    private static final int PROFILE_IMAGE_4 = 3;
    private static final int PROFILE_IMAGE_5 = 4;

    public EditProfileFragment() {
        // Required empty public constructor
    }
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    SimpleDraweeView profile_image1, profile_image2, profile_image3, profile_image4, profile_image5;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        setProfileImages(view);
        setProfilePreferences(view);
        return view;
    }

    private void setProfileImages(View view){
        List<Photo> photos_list = CurrentUser.getInstance().getPhotos();

        profile_image1 =  view.findViewById(R.id.profile_imageView);
        profile_image1.setLayoutParams(new FrameLayout.LayoutParams(
                (int)getActivity().getResources().getDimension(R.dimen.main_profile_image_width),
                (int)getActivity().getResources().getDimension(R.dimen.main_profile_image_height)
        ));

        if(photos_list.get(PROFILE_IMAGE_1) != null)
            profile_image1.setImageURI(photos_list.get(PROFILE_IMAGE_1).getURL());

        profile_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.homeFragmentContainer, AlbumsFragment.newInstance(PROFILE_IMAGE_1)).addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

        profile_image2 =  view.findViewById(R.id.profile_imageView2);
        profile_image2.setLayoutParams(new FrameLayout.LayoutParams(
                (int)getActivity().getResources().getDimension(R.dimen.main_profile_image_width),
                (int)getActivity().getResources().getDimension(R.dimen.main_profile_image_height)
        ));
        if(photos_list.get(PROFILE_IMAGE_2) != null)
            profile_image2.setImageURI(photos_list.get(PROFILE_IMAGE_2).getURL());
        profile_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.homeFragmentContainer, AlbumsFragment.newInstance(PROFILE_IMAGE_2)).addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

        profile_image3 =  view.findViewById(R.id.profile_imageView3);
        profile_image3.setLayoutParams(new FrameLayout.LayoutParams(
                (int)getActivity().getResources().getDimension(R.dimen.secondary_profile_image_wdith),
                (int)getActivity().getResources().getDimension(R.dimen.secondary_profile_image_height)
        ));
        if(photos_list.get(PROFILE_IMAGE_3) != null)
            profile_image3.setImageURI(photos_list.get(PROFILE_IMAGE_3).getURL());
        profile_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.homeFragmentContainer, AlbumsFragment.newInstance(PROFILE_IMAGE_3)).addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

        profile_image4 =  view.findViewById(R.id.profile_imageView4);
        profile_image4.setLayoutParams(new FrameLayout.LayoutParams(
                (int)getActivity().getResources().getDimension(R.dimen.secondary_profile_image_wdith),
                (int)getActivity().getResources().getDimension(R.dimen.secondary_profile_image_height)
        ));
        if(photos_list.get(PROFILE_IMAGE_4) != null)
            profile_image4.setImageURI(photos_list.get(PROFILE_IMAGE_4).getURL());
        profile_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.homeFragmentContainer, AlbumsFragment.newInstance(PROFILE_IMAGE_4)).addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

        profile_image5 =  view.findViewById(R.id.profile_imageView5);
        profile_image5.setLayoutParams(new FrameLayout.LayoutParams(
                (int)getActivity().getResources().getDimension(R.dimen.secondary_profile_image_wdith),
                (int)getActivity().getResources().getDimension(R.dimen.secondary_profile_image_height)
        ));
        if(photos_list.get(PROFILE_IMAGE_5) != null)
            profile_image5.setImageURI(photos_list.get(PROFILE_IMAGE_5).getURL());
        profile_image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.homeFragmentContainer, AlbumsFragment.newInstance(PROFILE_IMAGE_5)).addToBackStack(null);

                fragmentTransaction.commit();
            }
        });
    }
    private void setProfilePreferences(final View view){
        SeekBar distanceSeekBar = view.findViewById(R.id.distanceSeekbar);
        distanceSeekBar.setProgress(CurrentUser.getInstance().getPerferredDistance());
        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                fdbh.updatePreferredDistance(i);
                TextView distance = view.findViewById(R.id.distanceTextView);
                distance.setText(String.valueOf(i)+" miles");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Switch genderSwitch = view.findViewById(R.id.genderSwitch);
        genderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            FirebaseDatabaseHelper fdbh = FirebaseDatabaseHelper.getInstance();
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    fdbh.updatePreferredGender("female");
                    CurrentUser.getInstance().setPerferredGender("female");
                }
                if(!b) {
                    fdbh.updatePreferredGender("male");
                    CurrentUser.getInstance().setPerferredGender("male");

                }
            }
        });

        RangeBar ageRangeBar = view.findViewById(R.id.rangeBar);
        ageRangeBar.setLeft(CurrentUser.getInstance().getMinAge());
        ageRangeBar.setRight(CurrentUser.getInstance().getMaxAge());
        ageRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                if(leftPinIndex <= rightPinIndex) {
                    CurrentUser.getInstance().setMinAge(leftPinIndex);
                    CurrentUser.getInstance().setMaxAge(rightPinIndex);
                    FirebaseDatabaseHelper.getInstance().updatePreferredAgeInterval(leftPinIndex, rightPinIndex);
                }
            }
        });


    }
}
