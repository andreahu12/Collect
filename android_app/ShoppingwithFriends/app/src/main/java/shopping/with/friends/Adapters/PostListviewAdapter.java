package shopping.with.friends.Adapters;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import shopping.with.friends.Objects.Post;
import shopping.with.friends.Objects.Profile;
import shopping.with.friends.R;

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
        LayoutInflater layoutInflater;
        final Post post = posts.get(position);
        if (convertView == null) {
            holder = new Holder();
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_item_post, null);
            holder.image = (ImageView) convertView.findViewById(R.id.lip_imageview);
            holder.profilePicture = (CircleImageView) convertView.findViewById(R.id.lip_profile_picture);
            holder.titleText = (TextView) convertView.findViewById(R.id.lip_title_text);
            holder.descriptionText = (TextView) convertView.findViewById(R.id.lip_description_text);
            holder.priceText = (TextView) convertView.findViewById(R.id.lip_price_text);

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            int swidth = display.getWidth();
            ViewGroup.LayoutParams params = holder.image.getLayoutParams();
            params.width = ViewGroup.LayoutParams.FILL_PARENT;
            params.height = swidth ;
            holder.image.setLayoutParams(params);

            if (position == 2) {
                holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.stock2));
            }
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.titleText.setText(post.getTitle());
        holder.descriptionText.setText(post.getDescription());
        holder.priceText.setText("$" + post.getPrice());


        convertView.setTag(holder);
        return convertView;
    }

    private class Holder {
        //Views
        ImageView image;
        CircleImageView profilePicture;
        TextView titleText;
        TextView descriptionText;
        TextView priceText;
    }
}
