package shopping.with.friends.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shopping.with.friends.Api.ApiInterface;
import shopping.with.friends.Objects.Profile;
import shopping.with.friends.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ryan Brooks on 2/25/15.
 */
public class ProfileActivity extends ActionBarActivity {

    private String user_id;
    private Profile profile;
    private ArrayList<String> followersIdsList, followingIdsList;
    private Toolbar toolbar;
    private TextView nameTextView;
    private TextView usernameTextView;
    private Button followersButton;
    private Button followingButton;
    private SwipeBackLayout swipeBackLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        followersIdsList = new ArrayList<>();
        followingIdsList = new ArrayList<>();
        profile = new Profile();

        nameTextView = (TextView) findViewById(R.id.af_name_textview);
        usernameTextView = (TextView) findViewById(R.id.af_username_textview);
        followersButton = (Button) findViewById(R.id.af_followers_button);
        followingButton = (Button) findViewById(R.id.af_following_button);
        followersButton.setEnabled(false);
        followingButton.setEnabled(false);

        Intent intent = getIntent();
        user_id = intent.getExtras().getString("user_id");

        toolbar = (Toolbar) findViewById(R.id.activity_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeBackLayout = (SwipeBackLayout) findViewById(R.id.ap_swipeBackLayout);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        getUserData(user_id);


        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, Followers.class);
                i.putExtra("profile", profile);
                startActivity(i);
            }
        });
        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, Following.class);
                i.putExtra("profile", profile);
                startActivity(i);
            }
        });

    }

    private void getUserData(String user_id) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://" + getString(R.string.server_address))
                .build();

        ApiInterface apiInterface = restAdapter.create(ApiInterface.class);
        apiInterface.getUser(user_id, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.d("Profile response", jsonObject.toString());
                try {
                    JSONObject mainObject = new JSONObject(jsonObject.toString());
                    if (mainObject.getBoolean("status")) {
                        JSONObject userObject = mainObject.getJSONObject("user");
                        JSONArray followersArray = userObject.getJSONArray("followers");
                        JSONArray followingArray = userObject.getJSONArray("following");
                        for (int i = 0; i < followersArray.length(); i++) {
                            String userId = followersArray.getString(i);

                            followersIdsList.add(userId);
                        }
                        for (int i = 0; i < followingArray.length(); i++) {
                            String userId = followingArray.getString(i);

                            followingIdsList.add(userId);
                        }
                        profile.setId(userObject.getString("_id"));
                        profile.setEmail(userObject.getString("email"));
                        profile.setPassword(userObject.getString("password"));
                        profile.setUsername(userObject.getString("username"));
                        profile.setName(userObject.getString("name"));
                        profile.setFollowers(followersIdsList);
                        profile.setFollowing(followingIdsList);

                        nameTextView.setText(profile.getName());
                        usernameTextView.setText("@" + profile.getUsername());
                        if (profile.getFollowers().size() == 1) {
                            followersButton.setText(profile.getFollowers().size() + " Follower");
                        } else {
                            followersButton.setText(profile.getFollowers().size() + " Followers");
                        }
                        followingButton.setText(profile.getFollowing().size() + " Following");
                        followersButton.setEnabled(true);
                        followingButton.setEnabled(true);
                    } else {
                        Toast.makeText(ProfileActivity.this, "Error finding profile", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    Toast.makeText(ProfileActivity.this, "Error finding profile", Toast.LENGTH_SHORT).show();
                    finish();
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Failure", error.getMessage());
                Toast.makeText(ProfileActivity.this, "Error finding profile", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
