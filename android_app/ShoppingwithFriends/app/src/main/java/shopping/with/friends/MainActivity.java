package shopping.with.friends;

import android.content.Context;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shopping.with.friends.Activities.Followers;
import shopping.with.friends.Activities.Following;
import shopping.with.friends.Drawer.DrawerMenuAdapter;
import shopping.with.friends.Drawer.DrawerMenuItem;
import shopping.with.friends.Fragments.MainFeed;
import shopping.with.friends.Fragments.MyProfileFragment;
import shopping.with.friends.Fragments.Search;
import shopping.with.friends.Fragments.Settings;
import shopping.with.friends.Fragments.WishList;
import shopping.with.friends.Login.LoginSelectorActivity;
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
    private TextView drawerProfileName, drawerProfileUsername;
    private MainApplication mainApplication;
    private Profile userProfile;
    private RelativeLayout profileRelativeLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        toolbar = (Toolbar) findViewById(R.id.ma_toolbar);
        setSupportActionBar(toolbar);

        mainApplication = (MainApplication) getApplicationContext();
        userProfile = mainApplication.getProfile();

        drawerLayout = (DrawerLayout) findViewById(R.id.ma_drawer_wrapper);
        drawerLinearLayout = (RelativeLayout) findViewById(R.id.ma_drawer_main_layout);
        drawerMenuListview = (ListView) findViewById(R.id.ma_drawer_menu_listview);
        drawerProfileName = (TextView) findViewById(R.id.am_drawer_name_textview);
        drawerProfileUsername = (TextView) findViewById(R.id.am_drawer_username_textview);
        profileRelativeLayout = (RelativeLayout) findViewById(R.id.drawer_profile_layout);

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
        }

        profileRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(1, MyProfileFragment.class);
            }
        });

        drawerProfileName.setText(userProfile.getName());
        drawerProfileUsername.setText("@" + userProfile.getUsername());

        if (savedInstanceState == null) {
            setFragment(0, MainFeed.class);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                setFragment(0, MainFeed.class);
                break;
            case 1:
                setFragment(1, MyProfileFragment.class);
                break;
            case 2:
                setFragment(2, WishList.class);
                break;
            case 3:
                Intent followingIntent = new Intent(this, Following.class);
                followingIntent.putExtra("user_id", userProfile.getId());
                startActivity(followingIntent);
                break;
            case 4:
                Intent followersIntent = new Intent(this, Followers.class);
                followersIntent.putExtra("user_id", userProfile.getId());
                startActivity(followersIntent);
                break;
            case 5:
                setFragment(5, Search.class);
                break;
            case 6:
                setFragment(6, Settings.class);
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
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.ma_drawer_frame_container, fragment, fragmentClass.getSimpleName());
            fragmentTransaction.commit();

            drawerMenuListview.setItemChecked(position, true);
            drawerLayout.closeDrawer(drawerLinearLayout);
            drawerMenuListview.invalidateViews();
        }
        catch (Exception ex){
            Log.e("setFragment", ex.getMessage());
        }
    }

    private List<DrawerMenuItem> generateDrawerMenuItems() {
        String[] itemsText = getResources().getStringArray(R.array.ma_slide_drawer_items);
        List<DrawerMenuItem> result = new ArrayList<DrawerMenuItem>();
        for (int i = 0; i < itemsText.length; i++) {
            DrawerMenuItem item = new DrawerMenuItem();
            item.setItemText(itemsText[i]);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
