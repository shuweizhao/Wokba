package com.example.shuweizhao.wokba;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class MainUIActicity extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback, LocationListener {
    private final String[] names = {"uid", "customer", "customer_4", "customer_b", "points"
            , "phone", "nickname"};
    private final Gson gson = new Gson();
    private FragmentManager fragmentManager;

    private GoogleMap mgoogleMap;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 0;
    private HashMap<String, String> UserData;

    private com.google.android.gms.maps.MapFragment mapFragment;
    private OrderFragment orderFragment;
    private MessageFragment shakeFragment;
    private ProfileFragment profileFragment;
    private StoreListFragment storeListFragment;

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

    private FloatingActionButton fab;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.main_ui_layout);
        initView();
        saveUserData(getIntent());
        fragmentManager = getFragmentManager();
        setTabSelection(0);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
    }

    private void saveUserData(Intent intent) {
        String[] params = intent.getStringExtra(Intent.EXTRA_TEXT).split("/n");
        new User(params[0], params[1], params[2], params[3], params[4], params[5],
                params[6]);

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

        fab = (FloatingActionButton) findViewById(R.id.store_list_button);

        mapLayout.setOnClickListener(this);
        orderLayout.setOnClickListener(this);
        shakeLayout.setOnClickListener(this);
        profileLayout.setOnClickListener(this);
        fab.setOnClickListener(this);
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
            case R.id.store_list_button:
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                hideFragment(fragmentTransaction);
                fragmentTransaction.hide(mapFragment);
                if (storeListFragment == null) {
                    storeListFragment = new StoreListFragment();
                    fragmentTransaction.add(R.id.content, storeListFragment);
                }
                else {
                    fragmentTransaction.show(storeListFragment);
                }
                fragmentTransaction.commit();
                fab.setVisibility(View.GONE);
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
                fab.setVisibility(View.VISIBLE);
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
                fab.setVisibility(View.GONE);
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
                fab.setVisibility(View.GONE);
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
                fab.setVisibility(View.GONE);
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
        if (storeListFragment != null) {
            transaction.hide(storeListFragment);
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

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        //set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();
        System.out.println(latitude + " " + longitude);
        FetchStoreDataTask fetchSDataTask = new FetchStoreDataTask(latitude, longitude);
        fetchSDataTask.execute();
        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
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

    public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            View rootView = getLayoutInflater().inflate(R.layout.map_info_window_layout, null);
            String title = marker.getTitle();
            String snippet = marker.getSnippet();
            String[] info = snippet.split("#");

            TextView windowTitle = (TextView) rootView.findViewById(R.id.info_window_title);
            TextView windowRanking = (TextView) rootView.findViewById(R.id.store_points);
            TextView windowThumb = (TextView) rootView.findViewById(R.id.store_thumb_ups);
            TextView windowDistance = (TextView) rootView.findViewById(R.id.store_distance);
            TextView windowDescription = (TextView) rootView.findViewById(R.id.info_window_description);

            windowTitle.setText(title);
            windowRanking.setText(info[9]);
            windowThumb.setText(info[6]);
            windowDistance.setText(info[8]);
            windowDescription.setText(info[3]);

            return rootView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    public class FetchStoreDataTask extends AsyncTask<Void, Void, String> {
        private double mLong;
        private double mLat;
        private LatLng mLatLng;

        FetchStoreDataTask(double longitude, double latitude) {
            mLong = longitude;
            mLat = latitude;
            mLatLng = new LatLng(mLat, mLong);
        }
        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                response = post("https://wokba.com/api/mappin.php", ""+mLong,
                        ""+mLat);
            }
            catch (IOException e) {
                System.out.println("unexpected code");
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JsonElement jElement = new JsonParser().parse(s);
            Store[] stores = gson.fromJson(jElement,Store[].class);
            for (int i = 0; i < stores.length; i++) {
                mgoogleMap.addMarker(new MarkerOptions()
                        .position(stores[i].getLatLng())
                        .title(stores[i].getTitle())
                        .snippet(stores[i].toString()));
            }
            mgoogleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
            mgoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(marker.getSnippet());
                    Intent intent = new Intent(getApplication(),StoreDetail.class);
                    intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                    startActivity(intent);
                }
            });
        }


        private String post(String url, String longitude, String latitude) throws IOException {
            StringBuilder sb = new StringBuilder();
            sb.append(longitude).append(latitude);
            String token = Encryption.encryptData(sb.toString());
            RequestBody body = new FormBody.Builder()
                    .add("lon", longitude+"")
                    .add("lat", latitude+"")
                    .add("loadmore", "-1")
                    .add("token", token)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = MyHttpClient.getClient().newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            final String res = response.body().string();
            System.out.println(res);
            response.body().close();
            return res;
        }
    }
}
