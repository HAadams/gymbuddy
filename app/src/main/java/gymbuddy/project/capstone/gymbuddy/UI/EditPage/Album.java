package gymbuddy.project.capstone.gymbuddy.UI.EditPage;

import java.util.ArrayList;
import java.util.List;

public class Album {

    private String id;
    private Photo logo;
    private String name;
    private List<Photo> pictures;

    public Album(String id, String name){
        this.id = id;
        this.name = name;
        this.logo = new Photo(id);
        pictures = new ArrayList<>();
    }
    public Photo getLogoPicture(){return logo;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getID(){return id;}
    public List<Photo> getPictures(){return pictures;}
    public void addPicture(Photo photo){pictures.add(photo);}
}
