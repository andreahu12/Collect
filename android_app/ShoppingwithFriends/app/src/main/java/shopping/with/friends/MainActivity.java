package shopping.with.friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import java.util.ArrayList;
import java.util.List;

import shopping.with.friends.Activities.Followers;
import shopping.with.friends.Activities.Following;
import shopping.with.friends.Drawer.DrawerMenuAdapter;
import shopping.with.friends.Drawer.DrawerMenuItem;
import shopping.with.friends.Fragments.MainFeed;
import shopping.with.friends.Fragments.ProfileFragment;
import shopping.with.friends.Fragments.Search;
import shopping.with.friends.Fragments.Settings;
import shopping.with.friends.Fragments.WishList;
import shopping.with.friends.Login.LoginSelectorActivity;
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
    private SharedPreferences preferences;

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

        drawerLayout = (DrawerLayout) findViewById(R.id.ma_drawer_layout);
        drawerMenuListview = (ListView) findViewById(R.id.ma_drawer_menu_listview);

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
                setFragment(1, ProfileFragment.class);
                break;
            case 2:
                setFragment(2, WishList.class);
                break;
            case 3:
                Intent followingIntent = new Intent(this, Following.class);
                startActivity(followingIntent);
                break;
            case 4:
                Intent followersIntent = new Intent(this, Followers.class);
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
        if (drawerLayout.isDrawerOpen(drawerMenuListview)) {
            drawerLayout.closeDrawer(drawerMenuListview);
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
            drawerLayout.closeDrawer(drawerMenuListview);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Logic behind determining which item is clicked:
        switch (id) {
            case R.id.action_settings:
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
