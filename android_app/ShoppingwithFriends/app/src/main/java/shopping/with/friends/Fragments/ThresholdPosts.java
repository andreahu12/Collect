package shopping.with.friends.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shopping.with.friends.Activities.CreatePost;
import shopping.with.friends.Adapters.PostListviewAdapter;
import shopping.with.friends.Api.ApiInterface;
import shopping.with.friends.Objects.Post;
import shopping.with.friends.R;

/**
 * Created by Ryan Brooks on 3/26/15.
 */
public class ThresholdPosts extends Fragment {
    private ListView feedListView;
    private ArrayList<Post> posts;

    public ThresholdPosts() {
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public void onResume() {
        super.onResume();

        //TODO: Update listview
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_threshold_posts, container, false);

        feedListView = (ListView) view.findViewById(R.id.ftp_listview);
        PostListviewAdapter postListviewAdapter = new PostListviewAdapter(getActivity().getApplicationContext(), posts);
        feedListView.setAdapter(postListviewAdapter);

        return view;
    }
}
