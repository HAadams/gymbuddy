package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sein on 2/22/18.
 */

public class Album {

    private String id;
    private String name;
    private List<Photo> pictures;

    public Album(String id, String name){
        this.id = id;
        this.name = name;
        pictures = new ArrayList<>();
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getID(){return id;}
    public List<Photo> getPictures(){return pictures;}
    public void addPicture(String id, String url){pictures.add(new Photo(id, url));}
}
