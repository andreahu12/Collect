package shopping.with.friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shopping.with.friends.Api.ApiInterface;
import shopping.with.friends.Login.LoginSelectorActivity;
import shopping.with.friends.Objects.Profile;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ryan Brooks on 2/24/15.
 */
public class SplashScreen extends ActionBarActivity {

    private SharedPreferences preferences;
    private String emailPref, passwordPref;
    private ArrayList<Profile> followerList, followingList;
    private boolean loginSuccessful;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        followerList = new ArrayList<>();
        followingList = new ArrayList<>();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        emailPref = preferences.getString(getString(R.string.email_pref), null);
        passwordPref = preferences.getString(getString(R.string.password_pref), null);

        if (emailPref != null && passwordPref != null ) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://" + getString(R.string.server_address))
                    .build();

            ApiInterface apiInterface = restAdapter.create(ApiInterface.class);
            apiInterface.loginUser(emailPref, passwordPref, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject s, Response response) {
                    Log.d("Response JSON", s.toString());
                    try {
                        JSONObject mainObject = new JSONObject(s.toString());
                        loginSuccessful = mainObject.getBoolean("status");

                        if(loginSuccessful) {
                            JSONObject userObject = mainObject.getJSONObject("user");
                            JSONArray followersArray = userObject.getJSONArray("followers");
                            JSONArray followingArray = userObject.getJSONArray("following");
                            for (int i = 0; i < followersArray.length(); i++) {
                                JSONObject user = followersArray.getJSONObject(i);

                                Profile followerProfile = new Profile();
                                followerProfile.setId(user.getString("_id"));
                                followerProfile.setEmail(user.getString("email"));
                                followerProfile.setPassword(user.getString("password"));
                                followerProfile.setUsername(user.getString("username"));
                                followerProfile.setName(user.getString("name"));

                                followerList.add(followerProfile);
                            }
                            for (int i = 0; i < followingArray.length(); i++) {
                                JSONObject user = followingArray.getJSONObject(i);

                                Profile followingProfile = new Profile();
                                followingProfile.setId(user.getString("_id"));
                                followingProfile.setEmail(user.getString("email"));
                                followingProfile.setPassword(user.getString("password"));
                                followingProfile.setUsername(user.getString("username"));
                                followingProfile.setName(user.getString("name"));

                                followingList.add(followingProfile);
                            }
                            Profile profile = new Profile();
                            profile.setId(userObject.getString("_id"));
                            profile.setEmail(userObject.getString("email"));
                            profile.setPassword(userObject.getString("password"));
                            profile.setUsername(userObject.getString("username"));
                            profile.setName(userObject.getString("name"));
                            profile.setFollowers(followerList);
                            profile.setFollowing(followerList);

                            MainApplication mainApplication = (MainApplication) getApplicationContext();
                            mainApplication.setProfile(profile);

                            Intent mainActivity = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(mainActivity);
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Error logging in. Please login again.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SplashScreen.this, LoginSelectorActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("Error", error.toString());
                }
            });
        } else {
            Log.d("No ", "saved login data.");
            new CountDownTimer(2000, 2000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    Intent i = new Intent(SplashScreen.this, LoginSelectorActivity.class);
                    startActivity(i);
                    finish();
                }
            }.start();
        }
    }
}
