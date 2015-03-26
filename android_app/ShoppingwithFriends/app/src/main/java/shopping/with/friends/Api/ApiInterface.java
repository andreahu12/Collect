package shopping.with.friends.Api;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Ryan Brooks on 2/23/15.
 */
public interface ApiInterface {

    @FormUrlEncoded
    @POST("/api/user/login")
    void loginUser(@Field("email") String email, @Field("password") String password, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/api/user/signup")
    void registerUser(@Field("name") String name, @Field("username") String username, @Field("email") String email,
                      @Field("password") String password, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/api/user/add-follower") //Ex someone follows me, they're added to my followers
    void addFollower(@Field("id") String theirId, @Field("followerId") String myId, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/api/user/remove-follower")
    void removeFollower(@Field("user_id") String theirId, @Field("followerId") String myId, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/api/user/follow") //Ex I follow someone else, they're added to my following
    void followUser(@Field("user_id") String myId, @Field("followingId") String theirId, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/api/user/un-follow")
    void unFollowUser(@Field("user_id") String myId, @Field("followingId") String theirId, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/api/user/change-threshold")
    void changeThreshold(@Field("user_id") String myId, @Field("threshold") String newThreshold, Callback<JsonObject> callback);

    @FormUrlEncoded
    @POST("/api/post/create")
    void createPost(@Field("postTitle") String title, @Field("postPrice") String price, @Field("postDescription") String description,
                    @Field("userId") String userID, @Field("postLat") String latitude, @Field("postLong") String longitude, Callback<JsonObject> callback);

    @GET("/api/user/get-followers")
    void getFollowers(@Query("user_id") String id, Callback<JsonObject> callback);

    @GET("/api/user/get-following")
    void getFollowing(@Query("user_id") String id, Callback<JsonObject> callback);

    @GET("/api/user/get-all-users")
    void getAllUsers(Callback<JsonObject> callback);

    @GET("/api/user/get-user")
    void getUser(@Query("id") String userId, Callback<JsonObject> callback);

    @GET("/api/posts/all")
    void getAllPosts(Callback<JsonObject> callback);

    @GET("/api/posts/locations")
    void getAllLocations(Callback<JsonObject> callback);
}
