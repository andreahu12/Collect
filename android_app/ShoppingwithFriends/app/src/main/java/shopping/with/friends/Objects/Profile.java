package shopping.with.friends.Objects;

import java.util.ArrayList;

/**
 * Created by Ryan Brooks on 2/19/15.
 */
public class Profile {

    private String id;
    private String name;
    private String email;
    private String password;
    private String username;
    private ArrayList<Profile> followers;
    private ArrayList<Profile> following;

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
                   ArrayList<Profile> followers, ArrayList<Profile> following) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
        this.followers = followers;
        this.following = following;
    }

    public ArrayList<Profile> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Profile> followers) {
        this.followers = followers;
    }

    public ArrayList<Profile> getFollowing() {
        return following;
    }

    public void setFollowing(ArrayList<Profile> following) {
        this.following = following;
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
}
