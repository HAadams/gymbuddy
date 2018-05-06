package gymbuddy.project.capstone.gymbuddy.UI.HomePage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import gymbuddy.project.capstone.gymbuddy.Adapters.Profile;
import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.Map.LocationHelper;
import gymbuddy.project.capstone.gymbuddy.R;

/**
 * Created by New User on 2/15/2018.
 */
@Layout(R.layout.tinder_card_view)
public class TinderCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    private Profile mProfile;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    final cardTapToProfileCallback rootCallback;

    public TinderCard(Context context, Profile profile, SwipePlaceHolderView swipeView, cardTapToProfileCallback callback) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
        rootCallback = callback;
    }

    @Resolve
    private void onResolved(){
        if(mProfile == null) return;
        if(mProfile.getUser() == null) return;
        try {
            Glide.with(mContext).load(mProfile.getUser().getPhotoURL()).into(profileImageView);
        }catch(Exception e){}
        nameAgeTxt.setText(mProfile.getUser().getName() + ", " + mProfile.getUser().getAge());
        Double distance = LocationHelper.distance(
                Double.valueOf(CurrentUser.getInstance().getLatitude()),
                Double.valueOf(CurrentUser.getInstance().getLongitude()),
                Double.valueOf(mProfile.getUser().getLatitude()),
                Double.valueOf(mProfile.getUser().getLongitude()),
                "M");
        String away = "" + Math.floor(distance) + " miles away";
        locationNameTxt.setText(away);
        //locationNameTxt.setText(LocationHelper.getLocality(mContext, mProfile.getUser()));
    }

    @Click(R.id.tinderCard)
    public void onImageViewClick(){
    Log.v("testing", "clicks");
        rootCallback.onTap(mProfile);
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        rootCallback.onReject(mProfile);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
        rootCallback.onAccept(mProfile);
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}