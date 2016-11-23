package com.algonquincollege.lalo0417.dooropenottawa;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

/**
 * Created by CalebLalonde on 2016-11-22.
 */

public class DetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView bNameTv;
    private TextView bAddressTv;
    private TextView bDescriptionTv;
    private TextView bHoursTv;
    private GoogleMap mMap;
    private Geocoder mGeocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        mGeocoder = new Geocoder(this, Locale.getDefault());

        bNameTv = (TextView) findViewById(R.id.tvTitle);
        bAddressTv = (TextView) findViewById(R.id.tvAddress);
        bDescriptionTv = (TextView) findViewById(R.id.tvDescription);
        bHoursTv = (TextView) findViewById(R.id.tvHours);

        // Get the bundle of extras that was sent to this activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String bNameFromMainActivity = bundle.getString("bName");
            String bAddressFromMainActivity = bundle.getString("bAddress");
            String bDescriptionFromMainActivity = bundle.getString("bDescription");
            String bHoursFromMainActivity = bundle.getString("bHours");

            bNameTv.setText(bNameFromMainActivity);
            bAddressTv.setText(bAddressFromMainActivity);
            bDescriptionTv.setText(bDescriptionFromMainActivity);
            bHoursTv.setText(bHoursFromMainActivity);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String pinAddress = bundle.getString("bAddress");
            pin(pinAddress);
        }
    }

    private void pin(String locationName) {
        try {
            Address address = mGeocoder.getFromLocationName(locationName, 1).get(0);
            LatLng ll = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(ll).title(locationName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15.0f));
            Toast.makeText(this, "Pinned: " + locationName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Not found: " + locationName, Toast.LENGTH_SHORT).show();
        }
    }
}
