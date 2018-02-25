package gymbuddy.project.capstone.gymbuddy.UI.LoginPage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.w3c.dom.Text;

import gymbuddy.project.capstone.gymbuddy.R;

/**
 * Created by arham on 2/25/18.
 */

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context context){
        this.context = context;
    }

    public String[] slide_anims = {
            "anim1.json",
            "map_animation.json",
            "anim2.json"
    };

    public String[] slide_heading = {
            "Welcome",
            "Find your heard",
            "Login"
    };

    public String[] slide_summary = {

            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "

    };

    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        LottieAnimationView animationView =view.findViewById(R.id.animation_view);
        TextView header =  view.findViewById(R.id.animationText);
        TextView summary =  view.findViewById(R.id.animationSummary);

        animationView.setAnimation(slide_anims[position]);
        animationView.playAnimation();

        header.setText(slide_heading[position]);

        summary.setText(slide_summary[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
