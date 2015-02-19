package shopping.with.friends.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import shopping.with.friends.Adapters.UserListviewAdapter;
import shopping.with.friends.MainApplication;
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //TODO: Pass profile as serializable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        MainApplication mainApplication = (MainApplication)this.getApplicationContext();
        profile = mainApplication.getProfile();

        toolbar = (Toolbar) findViewById(R.id.activity_followers_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //TODO: <--- Fix this up

        followersListView = (ListView) findViewById(R.id.activity_followers_listview);


        UserListviewAdapter ulvw = new UserListviewAdapter(this, profile.getFollowers());
        followersListView.setAdapter(ulvw);
    }
}
