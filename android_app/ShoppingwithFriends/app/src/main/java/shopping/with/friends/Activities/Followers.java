package shopping.with.friends.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
import shopping.with.friends.Adapters.UserListviewAdapter;
import shopping.with.friends.Api.ApiInterface;
import shopping.with.friends.Objects.Profile;
import shopping.with.friends.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ryan Brooks on 2/19/15.
 */
public class Followers extends ActionBarActivity {

    private Toolbar toolbar;
    private ListView followersListView;
    private Profile profile;
    private ArrayList<Profile> followersList;
    private SwipeBackLayout swipeBackLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //TODO: Pass profile as serializable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        profile = (Profile) getIntent().getExtras().getSerializable("profile");

        followersList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.activity_followers_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeBackLayout = (SwipeBackLayout) findViewById(R.id.activity_followers_swipeBackLayout);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        followersListView = (ListView) findViewById(R.id.activity_followers_listview);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://" + getString(R.string.server_address))
                .build();

        ApiInterface apiInterface = restAdapter.create(ApiInterface.class);
        apiInterface.getFollowers(profile.getId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.d("JSON get-followers", jsonObject.toString());
                try {
                    JSONObject mainObject = new JSONObject(jsonObject.toString());
                    JSONArray userArray = mainObject.getJSONArray("followers");
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject user = userArray.getJSONObject(i);

                        Profile profile = new Profile();
                        profile.setId(user.getString("_id"));
                        profile.setEmail(user.getString("email"));
                        profile.setPassword(user.getString("password"));
                        profile.setUsername(user.getString("username"));
                        profile.setName(user.getString("name"));

                        followersList.add(profile);
                    }
                    UserListviewAdapter ulvw = new UserListviewAdapter(getApplicationContext(), followersList);
                    followersListView.setAdapter(ulvw);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Failure", error.getMessage());

            }
        });

        followersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Profile clickedProfile = followersList.get(position);
                Intent i = new Intent(Followers.this, ProfileActivity.class);
                i.putExtra("user_id", clickedProfile.getId());
                startActivity(i);
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
