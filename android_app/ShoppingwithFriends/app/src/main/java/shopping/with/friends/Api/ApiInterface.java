package shopping.with.friends.Api;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

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
}
