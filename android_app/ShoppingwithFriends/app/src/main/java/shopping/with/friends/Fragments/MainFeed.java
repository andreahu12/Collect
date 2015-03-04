package shopping.with.friends.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import shopping.with.friends.Activities.CreatePost;
import shopping.with.friends.MainActivity;
import shopping.with.friends.MainApplication;
import shopping.with.friends.Objects.Profile;
import shopping.with.friends.R;

/**
 * Created by Ryan Brooks on 2/4/15.
 */
public class MainFeed extends Fragment {

    private ListView feedListView;
    private FloatingActionButton floatingActionButton;

    public MainFeed() {
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: Update listview
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_feed, container, false);

        feedListView = (ListView) view.findViewById(R.id.fmf_listview);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fmf_fab);

        floatingActionButton.attachToListView(feedListView);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreatePost.class);
                startActivity(i);
            }
        });

        return view;
    }
}
