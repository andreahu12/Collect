package shopping.with.friends.Drawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import shopping.with.friends.Objects.DrawerMenuItem;
import shopping.with.friends.R;

/**
 * Created by Ryan Brooks on 2/4/15.
 */
public class DrawerMenuAdapter extends BaseAdapter {

    private List<DrawerMenuItem> drawerItems;
    private Context context;

    public DrawerMenuAdapter(Context context, List<DrawerMenuItem> drawerItems) {
        this.drawerItems = drawerItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_main_drawer_menu_item, null);
        }

        TextView itemTitle = (TextView) convertView.findViewById(R.id.drawer_menu_item_text);

        DrawerMenuItem item = drawerItems.get(position);

        itemTitle.setText(item.getItemText());

        return convertView;
    }
}
