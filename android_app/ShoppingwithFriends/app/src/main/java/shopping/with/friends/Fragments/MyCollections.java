package shopping.with.friends.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shopping.with.friends.R;

/**
 * Created by Ryan Brooks on 2/4/15.
 */
public class MyCollections extends Fragment {

    public MyCollections() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_collections, container, false);
    }
}
