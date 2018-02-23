package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import com.facebook.AccessToken;

/**
 * Created by Sein on 2/22/18.
 */

public class Photo {
    private String id;
    private String url;
    private String picture_url = "https://graph.facebook.com/photo_id/picture?access_token=token_id";

    public Photo(String id){
        this.id = id;
        this.url = picture_url
                .replace("photo_id", this.id)
                .replace("token_id", AccessToken.getCurrentAccessToken().getToken());
    }

    public void setID(String id){this.id = id;}
    public String getID(){return id;}
    public String getURL(){return url;}
    public void setURL(String url){this.url = url;}
}
