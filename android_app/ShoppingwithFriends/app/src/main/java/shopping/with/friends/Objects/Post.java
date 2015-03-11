package shopping.with.friends.Objects;

import java.util.ArrayList;

/**
 * Created by Ryan Brooks on 3/4/15.
 */
public class Post {

    private String title;
    private String description;
    private String price;
    private String userID;
    private double longitude;
    private double latitiude;

    public Post() {
    }

    public Post(String title, String description, String price, String userID, double longitude, double latitiude) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.userID = userID;
        this.longitude = longitude;
        this.latitiude = latitiude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getLatitiude() {
        return latitiude;
    }

    public void setLatitiude(double latitiude) {
        this.latitiude = latitiude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
