package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sein on 2/22/18.
 */

public class Photo {
    private String id;
    private String url;
    private Bitmap bitmap;

    public Photo(String id, String url){
        this.id = id;
        this.url = url;
//        try {
//            URL imageURL = new URL(url);
//
//
//
//            Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
//            bitmap.setHeight(100);
//            bitmap.setWidth(100);
//            this.bitmap = bitmap;
//
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
    }

    public Bitmap getBitmap(){return bitmap;}
    public void setID(String id){this.id = id;}
    public String getID(){return id;}
    public String getURL(){return url;}
    public void setURL(String url){this.url = url;}
}
