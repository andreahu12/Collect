package shopping.with.friends.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import shopping.with.friends.Adapters.UserListviewAdapter;
import shopping.with.friends.Objects.Profile;
import shopping.with.friends.R;

/**
 * Created by Ryan Brooks on 2/19/15.
 */
public class Search extends Fragment {

    private Button searchButton;
    private ListView userListView;
    private ArrayList<Profile> userList;


    public Search() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        userList = new ArrayList<>();

        userListView = (ListView) view.findViewById(R.id.sf_search_listview);
        searchButton = (Button) view.findViewById(R.id.sf_all_users_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpAsyncTask().execute("http://" + getString(R.string.server_address) + "/api/get-all-users");
            }
        });

        return view;
    }

    public static String GET(String url) {

        // Declarations
        InputStream inputStream;
        String result = "";

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpclient.execute(httpGet);
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

            return GET(urls[0]);
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
        Log.d("JSON", result);
        JSONObject mainObject = new JSONObject(result);
        JSONArray userArray = mainObject.getJSONArray("users");
        for (int i = 0; i < userArray.length(); i++) {
            JSONObject user = userArray.getJSONObject(i);

            Profile profile = new Profile();
            profile.setId(user.getString("_id"));
            profile.setEmail(user.getString("email"));
            profile.setPassword(user.getString("password"));
            profile.setUsername(user.getString("username"));
            profile.setName(user.getString("name"));

            userList.add(profile);
        }
        UserListviewAdapter ulvw = new UserListviewAdapter(getActivity().getApplicationContext(), userList);
        userListView.setAdapter(ulvw);
    }
}