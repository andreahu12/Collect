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
import shopping.with.friends.MainApplication;
import shopping.with.friends.Objects.Profile;
import shopping.with.friends.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ryan Brooks on 2/19/15.
 */
public class Following extends ActionBarActivity {

    private Toolbar toolbar;
    private ListView followingListView;
    private ArrayList<Profile> followingList;
    private Profile profile;
    private SwipeBackLayout swipeBackLayout;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //TODO: Pass profile as serializable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        profile = (Profile) getIntent().getExtras().getSerializable("profile");

        followingList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.activity_following_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeBackLayout = (SwipeBackLayout) findViewById(R.id.activity_following_swipeBackLayout);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        followingListView = (ListView) findViewById(R.id.activity_following_listview);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://" + getString(R.string.server_address))
                .build();

        ApiInterface apiInterface = restAdapter.create(ApiInterface.class);
        apiInterface.getFollowing(profile.getId(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.d("JSON get-following", jsonObject.toString());
                try {
                    JSONObject mainObject = new JSONObject(jsonObject.toString());
                    JSONArray userArray = mainObject.getJSONArray("following");
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject user = userArray.getJSONObject(i);

                        Profile profile = new Profile();
                        profile.setId(user.getString("_id"));
                        profile.setEmail(user.getString("email"));
                        profile.setPassword(user.getString("password"));
                        profile.setUsername(user.getString("username"));
                        profile.setName(user.getString("name"));

                        followingList.add(profile);
                    }
                    UserListviewAdapter ulvw = new UserListviewAdapter(getApplicationContext(), followingList);
                    followingListView.setAdapter(ulvw);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Failure", error.getMessage());

            }
        });

        followingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Profile clickedProfile = followingList.get(position);
                Intent i = new Intent(Following.this, ProfileActivity.class);
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
