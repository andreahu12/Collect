package shopping.with.friends.Activities;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shopping.with.friends.Api.ApiInterface;
import shopping.with.friends.MainApplication;
import shopping.with.friends.Objects.Post;
import shopping.with.friends.Objects.Profile;
import shopping.with.friends.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ryan Brooks on 3/4/15.
 */
public class CreatePost extends ActionBarActivity {

    private MainApplication mainApplication;
    private Profile profile;
    private Toolbar toolbar;
    private SwipeBackLayout swipeBackLayout;
    private EditText titleET, descriptionET, priceET;
    private ImageView imageView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mainApplication = (MainApplication) getApplicationContext();
        profile = mainApplication.getProfile();

        toolbar = (Toolbar) findViewById(R.id.acp_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.acp_square_image_view);
        Display display = getWindowManager().getDefaultDisplay();
        int swidth = display.getWidth();
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = swidth ;
        imageView.setLayoutParams(params);

        swipeBackLayout = (SwipeBackLayout) findViewById(R.id.acp_swipeBackLayout);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        if (Build.VERSION.SDK_INT >= 21) {
            swipeBackLayout.setElevation(1);
        }

        titleET = (EditText) findViewById(R.id.acp_title_et);
        descriptionET = (EditText) findViewById(R.id.acp_description_et);
        priceET = (EditText) findViewById(R.id.acp_price_et);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.acp_fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                Post post = new Post();
                post.setTitle(titleET.getText().toString().trim());
                post.setDescription(descriptionET.getText().toString().trim());
                post.setPrice(Integer.parseInt(priceET.getText().toString().trim()));
                post.setUserID(profile.getId());
                post.setLongitude(longitude);
                post.setLatitiude(latitude);

                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint("http://" + getString(R.string.server_address))
                        .build();

                ApiInterface apiInterface = restAdapter.create(ApiInterface.class);
                apiInterface.createPost(post.getTitle(), post.getPrice() + "", post.getDescription(), profile.getId(), latitude + "", longitude + "", new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        try {
                            JSONObject jsonObjectString = new JSONObject(jsonObject.toString());
                            if (jsonObjectString.getBoolean("status")) {
                                finish();
                            } else {
                                Toast.makeText(CreatePost.this, jsonObjectString.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(CreatePost.this, "Sorry there was an error, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
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
