package shopping.with.friends.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shopping.with.friends.Api.ApiInterface;
import shopping.with.friends.MainActivity;
import shopping.with.friends.MainApplication;
import shopping.with.friends.Objects.Profile;
import shopping.with.friends.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Ryan Brooks on 1/24/15.
 */
public class RegisterActivity extends ActionBarActivity {

    private EditText emailET, usernameET, passwordET, nameET;
    private CheckBox rememberCheck;
    private Button signUpButton;
    private String returnUsername;
    private ArrayList<String> followersIdsList, followingIdsList;
    private SharedPreferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // TODO: Remove the actionbar
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        followersIdsList = new ArrayList<>();
        followingIdsList = new ArrayList<>();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        rememberCheck = (CheckBox) findViewById(R.id.ar_remember_checkbox);

        nameET = (EditText) findViewById(R.id.ar_name_et);
        emailET = (EditText) findViewById(R.id.ar_email_et);
        usernameET = (EditText) findViewById(R.id.ar_username_et);
        passwordET = (EditText) findViewById(R.id.ar_password_et);
        signUpButton = (Button) findViewById(R.id.ar_register_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailET.getText().toString().trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "You didn't enter an email!", Toast.LENGTH_SHORT).show();
                } else if (usernameET.getText().toString().trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "You didn't enter a username!", Toast.LENGTH_SHORT).show();
                } else if (passwordET.getText().toString().trim().equals("")) {
                    Toast.makeText(RegisterActivity.this, "You didn't enter a password!", Toast.LENGTH_SHORT).show();
                } else {
                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint("http://" + getString(R.string.server_address))
                            .build();

                    ApiInterface apiInterface = restAdapter.create(ApiInterface.class);
                    apiInterface.registerUser(nameET.getText().toString().trim(),
                            usernameET.getText().toString().trim(),
                            emailET.getText().toString().trim(),
                            passwordET.getText().toString().trim(), new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject s, Response response) {
                            Log.d("Response JSON", s.toString());
                            try {
                                JSONObject mainObject = new JSONObject(s.toString());
                                returnUsername = mainObject.getString("username");

                                if (returnUsername != null && returnUsername.equals(usernameET.getText().toString().trim())) {
                                    JSONArray followersArray = mainObject.getJSONArray("followers");
                                    JSONArray followingArray = mainObject.getJSONArray("following");
                                    for (int i = 0; i < followersArray.length(); i++) {
                                        String userId = followersArray.getString(i);

                                        followersIdsList.add(userId);
                                    }
                                    for (int i = 0; i < followingArray.length(); i++) {
                                        String userId = followingArray.getString(i);

                                        followingIdsList.add(userId);
                                    }

                                    Profile profile = new Profile();
                                    profile.setId(mainObject.getString("_id"));
                                    profile.setEmail(mainObject.getString("email"));
                                    profile.setPassword(mainObject.getString("password"));
                                    profile.setUsername(mainObject.getString("username"));
                                    profile.setName(mainObject.getString("name"));
                                    profile.setThresholdPrice(Integer.parseInt(mainObject.getString("threshold")));
                                    profile.setFollowers(followersIdsList);
                                    profile.setFollowing(followingIdsList);

                                    MainApplication mainApplication = (MainApplication) getApplicationContext();
                                    mainApplication.setProfile(profile);

                                    if (rememberCheck.isChecked()) {
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString(getString(R.string.email_pref), mainObject.getString(getString(R.string.email_pref)));
                                        editor.putString(getString(R.string.password_pref), passwordET.getText().toString().trim());
                                        editor.commit();
                                    }

                                    Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(mainActivity);
                                    Toast.makeText(getBaseContext(), "Registration Successful! Welcome to collect!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getBaseContext(), "Sorry there was an error, please try again!", Toast.LENGTH_SHORT).show();
                                    signUpButton.setEnabled(true);
                                    emailET.setEnabled(true);
                                    usernameET.setEnabled(true);
                                    passwordET.setEnabled(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("Error", error.getMessage());
                        }
                    });
                }
            }
        });
    }
}
