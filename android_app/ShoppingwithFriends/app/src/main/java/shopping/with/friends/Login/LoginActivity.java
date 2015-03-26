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
public class LoginActivity extends ActionBarActivity {

    private EditText emailET, passwordET;
    private Button loginButton, registerButton;
    private CheckBox rememberCheck;
    private ArrayList<String> followersIdsList, followingIdsList;
    private boolean loginSuccessful;
    private SharedPreferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        followersIdsList = new ArrayList<>();
        followingIdsList = new ArrayList<>();

        emailET = (EditText) findViewById(R.id.al_email_et);
        passwordET = (EditText) findViewById(R.id.al_password_et);

        loginButton = (Button) findViewById(R.id.al_login_button);
        registerButton = (Button) findViewById(R.id.al_register_button);
        rememberCheck = (CheckBox) findViewById(R.id.al_remember_checkbox);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailET.getText().toString().trim().equals("") || emailET.getText().toString().trim().equals(null)) {
                    Toast.makeText(LoginActivity.this, "You didn't enter a username!", Toast.LENGTH_SHORT).show();
                } else if (passwordET.getText().toString().trim().equals("") || passwordET.getText().toString().trim().equals(null)) {
                    Toast.makeText(LoginActivity.this, "You didn't enter a password!", Toast.LENGTH_SHORT).show();
                } else {
                    loginButton.setEnabled(false);
                    emailET.setEnabled(false);
                    passwordET.setEnabled(false);
                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint("http://" + getString(R.string.server_address))
                            .build();

                    ApiInterface apiInterface = restAdapter.create(ApiInterface.class);
                    apiInterface.loginUser(emailET.getText().toString().trim(), passwordET.getText().toString().trim(), new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject s, Response response) {
                            Log.d("Response JSON", s.toString());
                            try {
                                JSONObject mainObject = new JSONObject(s.toString());
                                loginSuccessful = mainObject.getBoolean("status");

                                if(loginSuccessful) {
                                    JSONObject userObject = mainObject.getJSONObject("user");
                                    JSONArray followersArray = userObject.getJSONArray("followers");
                                    JSONArray followingArray = userObject.getJSONArray("following");
                                    for (int i = 0; i < followersArray.length(); i++) {
                                        String userId = followersArray.getString(i);

                                        followersIdsList.add(userId);
                                    }
                                    for (int i = 0; i < followingArray.length(); i++) {
                                        String userId = followingArray.getString(i);

                                        followingIdsList.add(userId);
                                    }
                                    Profile profile = new Profile();
                                    profile.setId(userObject.getString("_id"));
                                    profile.setEmail(userObject.getString("email"));
                                    profile.setPassword(userObject.getString("password"));
                                    profile.setUsername(userObject.getString("username"));
                                    profile.setName(userObject.getString("name"));
                                    profile.setThresholdPrice(Integer.parseInt(userObject.getString("threshold")));
                                    profile.setFollowers(followersIdsList);
                                    profile.setFollowing(followingIdsList);

                                    MainApplication mainApplication = (MainApplication) getApplicationContext();
                                    mainApplication.setProfile(profile);

                                    if (rememberCheck.isChecked()) {
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString(getString(R.string.email_pref), userObject.getString(getString(R.string.email_pref)));
                                        editor.putString(getString(R.string.password_pref), passwordET.getText().toString().trim());
                                        editor.commit();
                                    }

                                    Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(mainActivity);
                                    Toast.makeText(getBaseContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getBaseContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    loginButton.setEnabled(true);
                                    emailET.setEnabled(true);
                                    passwordET.setEnabled(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("Error", error.toString());
                        }
                    });
                }
            }
        });

        registerButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        });
    }
}
