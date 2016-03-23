package com.example.shuweizhao.wokba;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class MainUIActicity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback, LocationListener {
    private FragmentManager fragmentManager;

    private GoogleMap mgoogleMap;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 0;

    private com.google.android.gms.maps.MapFragment mapFragment;
    private OrderFragment orderFragment;
    private MessageFragment shakeFragment;
    private ProfileFragment profileFragment;

    private RelativeLayout mapLayout;
    private RelativeLayout orderLayout;
    private RelativeLayout shakeLayout;
    private RelativeLayout profileLayout;

    private ImageView mapImage;
    private ImageView orderImage;
    private ImageView shakeImage;
    private ImageView profileImage;

    private TextView mapText;
    private TextView orderText;
    private TextView shakeText;
    private TextView profileText;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.main_ui_layout);
        initView();
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        fragmentManager = getFragmentManager();
        setTabSelection(0);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
    }

    private void initView() {

        mapLayout = (RelativeLayout) findViewById(R.id.tab_map_layout);
        orderLayout = (RelativeLayout) findViewById(R.id.tab_order_layout);
        shakeLayout = (RelativeLayout) findViewById(R.id.tab_shake_layout);
        profileLayout = (RelativeLayout) findViewById(R.id.tab_profile_layout);

        mapImage = (ImageView) findViewById(R.id.tab_map_image);
        orderImage = (ImageView) findViewById(R.id.tab_order_image);
        shakeImage = (ImageView) findViewById(R.id.tab_shake_image);
        profileImage = (ImageView) findViewById(R.id.tab_profile_image);

        mapText = (TextView) findViewById(R.id.tab_map_text);
        orderText = (TextView) findViewById(R.id.tab_order_text);
        shakeText = (TextView) findViewById(R.id.tab_shake_text);
        profileText = (TextView) findViewById(R.id.tab_profile_text);

        mapLayout.setOnClickListener(this);
        orderLayout.setOnClickListener(this);
        shakeLayout.setOnClickListener(this);
        profileLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tab_map_layout:
                setTabSelection(0);
                break;
            case R.id.tab_order_layout:
                setTabSelection(1);
                break;
            case R.id.tab_shake_layout:
                setTabSelection(2);
                break;
            case R.id.tab_profile_layout:
                setTabSelection(3);
                break;
        }
    }

    private void setTabSelection(int id) {
        clearSelection();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (id) {
            case 0:
                mapLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mapText.setTextColor(Color.WHITE);
                if (mapFragment == null) {
                    mapFragment = MapFragment.newInstance();
                    fragmentTransaction.add(R.id.content, mapFragment);
                } else {
                    fragmentTransaction.show(mapFragment);
                }
                mapFragment.getMapAsync(this);
                break;
            case 1:
                orderLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                orderText.setTextColor(Color.WHITE);
                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                    fragmentTransaction.add(R.id.content, orderFragment);
                } else {
                    fragmentTransaction.show(orderFragment);
                }
                break;
            case 2:
                shakeLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                shakeText.setTextColor(Color.WHITE);
                if (shakeFragment == null) {
                    shakeFragment = new MessageFragment();
                    fragmentTransaction.add(R.id.content, shakeFragment);
                } else {
                    fragmentTransaction.show(shakeFragment);
                }
                break;
            case 3:
                profileLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                profileText.setTextColor(Color.WHITE);
                if (profileFragment == null) {
                    profileFragment = new ProfileFragment();
                    fragmentTransaction.add(R.id.content, profileFragment);
                } else {
                    fragmentTransaction.show(profileFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void clearSelection() {
        mapLayout.setBackgroundColor(Color.WHITE);
        mapText.setTextColor(Color.BLACK);
        orderLayout.setBackgroundColor(Color.WHITE);
        orderText.setTextColor(Color.BLACK);
        shakeLayout.setBackgroundColor(Color.WHITE);
        shakeText.setTextColor(Color.BLACK);
        profileLayout.setBackgroundColor(Color.WHITE);
        profileText.setTextColor(Color.BLACK);
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        if (orderFragment != null) {
            transaction.hide(orderFragment);
        }
        if (shakeFragment != null) {
            transaction.hide(shakeFragment);
        }
        if (profileFragment != null) {
            transaction.hide(profileFragment);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            return;
        }
        mgoogleMap.setMyLocationEnabled(true);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Snackbar.make(findViewById(R.id.content), R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}
                                    , REQUEST_LOCATION);
                        }
                    });
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}
                                                ,REQUEST_LOCATION);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }


}
