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

    public Post() {
    }

    public Post(String title, String description, String price, String userID) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.userID = userID;
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
}
