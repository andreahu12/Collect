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
 * Created by Ryan Brooks on 2/4/15.
 */
public class MainFeed extends Fragment {

    private ListView feedListView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Post> posts;

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

        posts = new ArrayList<>();

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
                        for (int i = 0; i < postArray.length(); i++) {
                            JSONObject object = postArray.getJSONObject(i);
                            Post post = new Post();
                            post.setUserID(object.getString("user"));
                            post.setTitle(object.getString("title"));
                            post.setDescription(object.getString("description"));
                            post.setPrice(Integer.parseInt(object.getString("price")));
                            posts.add(post);
                        }
                        PostListviewAdapter postListviewAdapter = new PostListviewAdapter(getActivity().getApplicationContext(), posts);
                        feedListView.setAdapter(postListviewAdapter);
                    } else {
                        Toast.makeText(getActivity(), "Sorry, an error occured.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Error. " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
