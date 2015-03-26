package shopping.with.friends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shopping.with.friends.Activities.Followers;
import shopping.with.friends.Activities.Following;
import shopping.with.friends.Adapters.PostListviewAdapter;
import shopping.with.friends.Api.ApiInterface;
import shopping.with.friends.Drawer.DrawerMenuAdapter;
import shopping.with.friends.Fragments.MainFeed;
import shopping.with.friends.Fragments.Map;
import shopping.with.friends.Fragments.MyCollections;
import shopping.with.friends.Fragments.MyProfileFragment;
import shopping.with.friends.Fragments.Search;
import shopping.with.friends.Fragments.Settings;
import shopping.with.friends.Fragments.ThresholdPosts;
import shopping.with.friends.Login.LoginSelectorActivity;
import shopping.with.friends.Objects.DrawerMenuItem;
import shopping.with.friends.Objects.Post;
import shopping.with.friends.Objects.Profile;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ryan Brooks on 1/24/15.
 */
public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerMenuListview;
    private DrawerMenuAdapter drawerMenuAdapter;
    private RelativeLayout drawerLinearLayout;
    private SharedPreferences preferences;
    private TextView drawerProfileName, followingAmount, followersAmount, thresholdText, thresholdCount;
    private MainApplication mainApplication;
    private Profile userProfile;
    private RelativeLayout profileRelativeLayout, followingButton, followersButton, thresholdLayout;
    private android.support.v4.app.FragmentManager fragmentManager;
    private ArrayList<Post> threshPosts;
    private int threshCount;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        threshPosts = new ArrayList<>();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        toolbar = (Toolbar) findViewById(R.id.ma_toolbar);
        setSupportActionBar(toolbar);

        mainApplication = (MainApplication) getApplicationContext();
        userProfile = mainApplication.getProfile();

        drawerLayout = (DrawerLayout) findViewById(R.id.ma_drawer_wrapper);
        drawerLinearLayout = (RelativeLayout) findViewById(R.id.ma_drawer_main_layout);
        drawerMenuListview = (ListView) findViewById(R.id.ma_drawer_menu_listview);
        drawerProfileName = (TextView) findViewById(R.id.am_drawer_name_textview);
        profileRelativeLayout = (RelativeLayout) findViewById(R.id.drawer_profile_layout);
        followingButton = (RelativeLayout) findViewById(R.id.am_drawer_following_button);
        followersButton = (RelativeLayout) findViewById(R.id.am_drawer_followers_button);
        thresholdLayout = (RelativeLayout) findViewById(R.id.ma_drawer_threshold_layout);
        followingAmount = (TextView) findViewById(R.id.am_drawer_following_count);
        followersAmount = (TextView) findViewById(R.id.am_drawer_followers_count);
        thresholdText = (TextView) findViewById(R.id.ma_drawer_threshold_text);
        thresholdCount = (TextView) findViewById(R.id.ma_drawer_threshold_count);

        List<DrawerMenuItem> menuItems = generateDrawerMenuItems();
        drawerMenuAdapter = new DrawerMenuAdapter(getApplicationContext(), menuItems);
        drawerMenuListview.setAdapter(drawerMenuAdapter);

        drawerMenuListview.setOnItemClickListener(this);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        if (Build.VERSION.SDK_INT >= 21) {
            profileRelativeLayout.setBackground(getResources().getDrawable(R.drawable.selector_button_blue_ripple));
            followersButton.setBackground(getResources().getDrawable(R.drawable.selector_button_blue_ripple));
            followingButton.setBackground(getResources().getDrawable(R.drawable.selector_button_blue_ripple));
            thresholdLayout.setBackground(getResources().getDrawable(R.drawable.selector_button_blue_inverse_ripple));
        }

        profileRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(1, MyProfileFragment.class);
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Following.class);
                i.putExtra("profile", userProfile);
                startActivity(i);
            }
        });

        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Followers.class);
                i.putExtra("profile", userProfile);
                startActivity(i);
            }
        });

        thresholdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThresholdPosts fragment = new ThresholdPosts();
                fragment.setPosts(threshPosts);
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ma_drawer_frame_container, fragment, "Under my Threshold");
                fragmentTransaction.commit();

                drawerLayout.closeDrawer(drawerLinearLayout);
                drawerMenuListview.invalidateViews();
            }
        });

        drawerProfileName.setText(userProfile.getName());
        followersAmount.setText(userProfile.getFollowers().size() + "");
        followingAmount.setText(userProfile.getFollowing().size() + "");
        thresholdText.setText("My Threshold: $" + userProfile.getThresholdPrice());
        checkPosts();



        if (savedInstanceState == null) {
            setFragment(0, MainFeed.class);
        }
    }

    private void checkPosts() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://" + getString(R.string.server_address))
                .build();

        ApiInterface apiInterface = restAdapter.create(ApiInterface.class);
        apiInterface.getAllPosts(new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                try {
                    Log.d("Success", jsonObject.toString());
                    JSONObject mainResponse = new JSONObject(jsonObject.toString());
                    if (mainResponse.getBoolean("status")) {
                        JSONArray postArray = mainResponse.getJSONArray("posts");
                        Log.d("Post Array", postArray.toString());
                        threshPosts.clear();
                        threshCount = 0;
                        for (int i = 0; i < postArray.length(); i++) {
                            JSONObject object = postArray.getJSONObject(i);
                            Post post = new Post();
                            post.setUserID(object.getString("user"));
                            post.setTitle(object.getString("title"));
                            post.setDescription(object.getString("description"));
                            post.setPrice(Integer.parseInt(object.getString("price")));
                            if (post.getPrice() <= userProfile.getThresholdPrice()) {
                                threshCount++;
                                threshPosts.add(post);
                            }
                        }
                        if (threshCount > 0) {
                            thresholdCount.setText("" + threshCount);
                        } else if (threshCount == 0) {
                            thresholdCount.setText("");
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Sorry, an error occured.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, "Error. " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                setFragment(0, MainFeed.class);
                break;
            case 1:
                setFragment(1, MyCollections.class);
                break;
            case 2:
                setFragment(2, Search.class);
                break;
            case 3:
                setFragment(3, Map.class);
                break;
            case 4:
                setFragment(4, Settings.class);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(drawerLinearLayout)) {
            drawerLayout.closeDrawer(drawerLinearLayout);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void setFragment(int position, Class<? extends Fragment> fragmentClass) {
        try {
            Fragment fragment = fragmentClass.newInstance();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.ma_drawer_frame_container, fragment, fragmentClass.getSimpleName());
            fragmentTransaction.commit();

            drawerMenuListview.setItemChecked(position, true);
            drawerLayout.closeDrawer(drawerLinearLayout);
            drawerMenuListview.invalidateViews();
        } catch (Exception ex){
            Log.e("setFragment", ex.getMessage());
        }
    }

    private List<DrawerMenuItem> generateDrawerMenuItems() {
        String[] itemsText = getResources().getStringArray(R.array.ma_slide_drawer_items);
        List<DrawerMenuItem> result = new ArrayList<>();
        for (String menuItem : itemsText) {
            DrawerMenuItem item = new DrawerMenuItem();
            item.setItemText(menuItem);
            result.add(item);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                setFragment(6, Settings.class);
                return true;
            case R.id.ma_action_logout:
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(getString(R.string.email_pref));
                editor.remove(getString(R.string.password_pref));
                editor.commit();
                Intent i = new Intent(this, LoginSelectorActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.ma_change_threshold:
                AlertDialog.Builder changeDialog = new AlertDialog.Builder(this);
                changeDialog.setTitle("Change threshold price");
                changeDialog.setMessage("Enter a new threshold price");

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(16, 0, 16, 0);
                input.setLayoutParams(lp);
                changeDialog.setView(input);

                changeDialog.setPositiveButton("Change",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int which) {
                                final String newThreshold = input.getText().toString().trim();
                                if (newThreshold == "") {
                                    Toast.makeText(MainActivity.this, "You didn't enter a number!", Toast.LENGTH_SHORT).show();
                                } else {
                                    RestAdapter restAdapter = new RestAdapter.Builder()
                                            .setEndpoint("http://" + getString(R.string.server_address))
                                            .build();

                                    ApiInterface apiInterface = restAdapter.create(ApiInterface.class);
                                    Log.d("ID", userProfile.getId());
                                    apiInterface.changeThreshold(userProfile.getId(), newThreshold, new Callback<JsonObject>() {
                                        @Override
                                        public void success(JsonObject jsonObject, Response response) {
                                            try {
                                                Log.d("Success", jsonObject.toString());
                                                JSONObject mainResponse = new JSONObject(jsonObject.toString());
                                                if (mainResponse.getBoolean("status")) {
                                                    JSONObject userObject = mainResponse.getJSONObject("user");
                                                    userProfile.setThresholdPrice(Integer.parseInt(newThreshold));

                                                    MainApplication mainApplication = (MainApplication) getApplicationContext();
                                                    mainApplication.setProfile(userProfile);
                                                    thresholdText.setText("My Threshold: $" + userProfile.getThresholdPrice());
                                                    checkPosts();
                                                    dialog.dismiss();
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Sorry, an error occured.", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.d("Change Threshold error", error.getMessage());
                                        }
                                    });
                                }
                            }
                        });

                changeDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                changeDialog.show();
                return true;
    }

        return super.onOptionsItemSelected(item);
    }
}
