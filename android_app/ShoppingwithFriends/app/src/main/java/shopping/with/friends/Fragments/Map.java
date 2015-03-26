package shopping.with.friends.Fragments;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import shopping.with.friends.R;

/**
 * Created by Ryan Brooks on 3/11/15.
 */
public class Map extends Fragment implements LocationListener{

    private GoogleMap map;
    private SupportMapFragment supportMapFragment;
    private LocationManager locationManager;
    private String provider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.fm_map);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.fm_map, supportMapFragment).commit();
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        provider = locationManager.getBestProvider(criteria, true);
    }

    @Override
    public void onResume() {
        super.onResume();

        locationManager.requestLocationUpdates(provider, 1000, 1, this);
        if (map == null) {
            map = supportMapFragment.getMap();
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        // Enabling MyLocation Layer of Google Map
        map.setMyLocationEnabled(true);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://" + getString(R.string.server_address))
                .build();

        ApiInterface apiInterface = restAdapter.create(ApiInterface.class);
        apiInterface.getAllLocations(new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.d("Location Response", jsonObject.toString());
                try {
                    JSONObject mainObject = new JSONObject(jsonObject.toString());
                    if (mainObject.getBoolean("status")) {
                        JSONArray locationArray = mainObject.getJSONArray("locations");
                        for (int i = 0; i < locationArray.length(); i++) {
                            JSONObject obj = locationArray.getJSONObject(i);
                            double latitude = Double.parseDouble(obj.getString("lat"));
                            double longitude = Double.parseDouble(obj.getString("long"));
                            LatLng loc = new LatLng(latitude, longitude);
                            map.addMarker(new MarkerOptions().position(loc));
                        }


                    } else {
                        Toast.makeText(getActivity(), "Error getting data!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
