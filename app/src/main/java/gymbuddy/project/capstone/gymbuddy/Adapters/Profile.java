package gymbuddy.project.capstone.gymbuddy.Adapters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import gymbuddy.project.capstone.gymbuddy.Database.User;

/**
 * Created by New User on 2/15/2018.
 */



public class Profile {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String imageUrl;

    @SerializedName("age")
    @Expose
    private Integer age;

    @SerializedName("location")
    @Expose
    private String location;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.imageUrl = user.getPhotoURL().toString();
        System.out.println("USER NAME: "+user.getName());
    }

    private User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}