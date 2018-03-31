package gymbuddy.project.capstone.gymbuddy.UI.HomePage;

import gymbuddy.project.capstone.gymbuddy.Adapters.Profile;

public interface cardTapToProfileCallback {

    void onTap(Profile profile);

    void onReject(Profile profile);

    void onAccept(Profile profile);

}
