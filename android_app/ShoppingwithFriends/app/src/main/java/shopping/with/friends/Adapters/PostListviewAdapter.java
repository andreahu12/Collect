package shopping.with.friends.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import shopping.with.friends.Objects.Post;

/**
 * Created by Ryan Brooks on 3/4/15.
 */
public class PostListviewAdapter extends BaseAdapter {

    private ArrayList<Post> posts;
    private Context context;

    public PostListviewAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }
    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        return null;
    }

    private class Holder {
        //Views
    }
}
