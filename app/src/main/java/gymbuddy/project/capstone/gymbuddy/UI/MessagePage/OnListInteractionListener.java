package gymbuddy.project.capstone.gymbuddy.UI.MessagePage;

import gymbuddy.project.capstone.gymbuddy.Database.User;

/**
 * Created by arhamakbar on 3/12/18.
 */

public interface  OnListInteractionListener {
     void onListFragmentInteraction(User mItem);

     void onNavListFragmentInteraction(User item);
}
