package gymbuddy.project.capstone.gymbuddy.UI.EditPage;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.Database.FirebaseDatabaseHelper;
import gymbuddy.project.capstone.gymbuddy.R;
import gymbuddy.project.capstone.gymbuddy.UI.HomePage.HomeActivity;
import gymbuddy.project.capstone.gymbuddy.UI.LoginPage.MainActivity;

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


        Button logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startMainActivity();
            }
        });
        setProfileImages(view);
        setProfilePreferences(view);
        setProfileBiography(view);
        return view;
    }
    private void startMainActivity(){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.w(getClass().toString(), "startMainActivity:starting main activity after logout");
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
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
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                FirebaseDatabaseHelper.getInstance().updatePreferredDistance(i);
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
        String p_gend = CurrentUser.getInstance().getPerferredGender();
        if(p_gend.equalsIgnoreCase("female")) genderSwitch.setChecked(true);
        else genderSwitch.setChecked(false);
        genderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    FirebaseDatabaseHelper.getInstance().updatePreferredGender("female");
                    CurrentUser.getInstance().setPerferredGender("female");
                }
                if(!b) {
                    FirebaseDatabaseHelper.getInstance().updatePreferredGender("male");
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
    private void setProfileBiography(View view){
        EditText biography = view.findViewById(R.id.biographyEditText);
        biography.setText(CurrentUser.getInstance().getBiography());
        biography.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                FirebaseDatabaseHelper.getInstance().updateUserBiography(editable.toString());
                CurrentUser.getInstance().setBiography(editable.toString());
            }
        });
    }
}
