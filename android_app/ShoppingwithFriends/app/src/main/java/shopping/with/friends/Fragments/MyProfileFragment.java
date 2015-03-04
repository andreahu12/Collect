package shopping.with.friends.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import shopping.with.friends.Activities.EditProfile;
import shopping.with.friends.Activities.Followers;
import shopping.with.friends.Activities.Following;
import shopping.with.friends.MainApplication;
import shopping.with.friends.Objects.Profile;
import shopping.with.friends.R;

/**
 * Created by Ryan Brooks on 2/4/15.
 */
public class MyProfileFragment extends Fragment {

    private Profile profile;
    private MainApplication mainApplication;
    private TextView nameTextView;
    private TextView usernameTextView;
    private TextView editProfileIcon;
    private Button followersButton;
    private Button followingButton;

    public MyProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApplication = (MainApplication)getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile = mainApplication.getProfile();
        nameTextView = (TextView) view.findViewById(R.id.fp_name_textview);
        usernameTextView = (TextView) view.findViewById(R.id.fp_username_textview);
        editProfileIcon = (TextView) view.findViewById(R.id.fp_edit_profile);
        followersButton = (Button) view.findViewById(R.id.fp_followers_button);
        followingButton = (Button) view.findViewById(R.id.fp_following_button);


        nameTextView.setText(profile.getName());
        usernameTextView.setText("@" + profile.getUsername());
        if (profile.getFollowers().size() == 1) {
            followersButton.setText(profile.getFollowers().size() + " Follower");
        } else {
            followersButton.setText(profile.getFollowers().size() + " Followers");
        }
        followingButton.setText(profile.getFollowing().size() + " Following");

        editProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfile.class);
                i.putExtra("profile", profile);
                startActivity(i);
            }
        });

        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Followers.class);
                i.putExtra("profile", profile);
                startActivity(i);
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Following.class);
                i.putExtra("profile", profile);
                startActivity(i);
            }
        });

        return view;
    }
}
