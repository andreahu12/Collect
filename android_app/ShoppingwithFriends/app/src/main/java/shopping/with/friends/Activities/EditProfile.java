package shopping.with.friends.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.liuguangqiang.swipeback.SwipeBackLayout;

import shopping.with.friends.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ryan Brooks on 2/26/15.
 */
public class EditProfile extends ActionBarActivity {

    private Toolbar toolbar;
    private Button saveButton;
    private SwipeBackLayout swipeBackLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.aep_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeBackLayout = (SwipeBackLayout) findViewById(R.id.aep_swipeBackLayout);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        saveButton = (Button) findViewById(R.id.aep_save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
