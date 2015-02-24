package shopping.with.friends.Adapters;

/**
 * Created by Ryan Brooks on 2/19/15.
 */
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import shopping.with.friends.MainApplication;
import shopping.with.friends.Objects.Profile;
import shopping.with.friends.R;

public class UserListviewAdapter extends BaseAdapter {

    private ArrayList<Profile> users;
    private Context context;
    private Profile userProfile;
    private Profile followingProfile;

    /**
     * ListView Adapter for each item that is in the user listview
     * @param context
     * @param users
     */
    public UserListviewAdapter(Context context, ArrayList<Profile> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Profile getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int pos, View child, ViewGroup parent) {
        final Holder mHolder;
        MainApplication mainApplication = (MainApplication) context.getApplicationContext();
        userProfile = mainApplication.getProfile();
        LayoutInflater layoutInflater;
        final Profile profile = users.get(pos);
        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listview_item_users, null);
            mHolder = new Holder();
            mHolder.name = (TextView) child.findViewById(R.id.lvu_name_textview);
            mHolder.username = (TextView) child.findViewById(R.id.lvu_username_textview);
            mHolder.followButton = (Button) child.findViewById(R.id.lvu_follow_button);
            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }

        mHolder.name.setText(profile.getName());
        mHolder.username.setText(profile.getUsername());
        if (userProfile.getFollowing().contains(profile)) { //TODO: Fix this to work properly
            mHolder.followButton.setEnabled(false);
        }

        mHolder.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHolder.followButton.setEnabled(false);
                mHolder.followButton.setText("Following");
                followingProfile = profile;
                userProfile.addFollowing(profile);
                //new HttpAsyncTask().execute("http://128.61.76.103:3000/api/user/follow");
            }
        });

        return child;
    }

    public static String POST(String url, String userId, String followingId) {

        // Declarations
        InputStream inputStream;
        String result = "";

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user_id", userId));
            nameValuePairs.add(new BasicNameValuePair("followingId", followingId));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!"; // Error
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage()); // Error
        }

        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0], userProfile.getId(), followingProfile.getId());
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                readJSONResponse(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private void readJSONResponse(String result) throws JSONException {
        Log.d("Follow", result);
        JSONObject mainObject = new JSONObject(result);
        boolean status = mainObject.getBoolean("status");
        String message = mainObject.getString("message");
        if (status) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public class Holder {
        TextView name;
        TextView username;
        Button followButton;
    }
}
