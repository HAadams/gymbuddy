package gymbuddy.project.capstone.gymbuddy.UI.Adapters;

/**
 * Created by Sein on 2/15/18.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import gymbuddy.project.capstone.gymbuddy.UI.LoginPage.LoginFragment;
import gymbuddy.project.capstone.gymbuddy.UI.HomePage.MainFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class FragmentSelectionPageAdapter extends FragmentPagerAdapter {

    private final int MAX_PAGES = 3;
    private final int LOGIN_PAGE_INDEX = 2;

    public FragmentSelectionPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position){
            case LOGIN_PAGE_INDEX:
                return LoginFragment.newInstance();
            default:
                return MainFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return MAX_PAGES;
    }
}