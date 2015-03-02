package shopping.with.friends.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import shopping.with.friends.R;

/**
 * Created by Ryan Brooks on 2/26/15.
 */
public class EditProfile extends Fragment {

    private Toolbar toolbar;
    private Button saveButton;

    public EditProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        saveButton = (Button) view.findViewById(R.id.fep_save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
