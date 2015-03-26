package shopping.with.friends.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ryan Brooks on 2/19/15.
 */
public class Profile implements Serializable {

    private String id;
    private String name;
    private String email;
    private String password;
    private String username;
    private int thresholdPrice;
    private ArrayList<String> followersIds;
    private ArrayList<String> followingIds;

    /**
     * Profile Object that stores all user data
     */
    public Profile() {
    }

    public Profile(String id, String name, String email, String password, String username) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public Profile(String id, String name, String email, String password, String username,
                   ArrayList<String> followersIds, ArrayList<String> followingIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
        this.followersIds = followersIds;
        this.followingIds = followingIds;
    }

    public ArrayList<String> getFollowers() {
        return followersIds;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followersIds = followers;
    }

    public ArrayList<String> getFollowing() {
        return followingIds;
    }

    public void setFollowing(ArrayList<String> following) {
        this.followingIds = following;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getThresholdPrice() {
        return thresholdPrice;
    }

    public void setThresholdPrice(int thresholdPrice) {
        this.thresholdPrice = thresholdPrice;
    }
}
