package com.pinthai;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pinthai.fragment.AppUtils;
import com.pinthai.wrapper.DBDetails;
import com.pinthai.wrapper.FacebookProfile;
import com.pinthai.wrapper.GetDBValue;
import com.pinthai.wrapper.PostDBFirebase;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    static Context mContext;
    private LatLng mCenterLatLong;

    private AddressResultReceiver mResultReceiver;
    RatingBar mBar;
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;

    TextView mLocationText;
    Button rateing_button;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    Toolbar mToolbar;
    View mapView;
    FrameLayout card_layout;
    LinearLayout card_layout_hide;
    TextView user_name, user_email, pin_texttitle, pin_text1, pin_text2, vote_rating_text, vote_amount_text;
    ImageView user_picture;
    JSONObject response, profile_pic_data, profile_pic_url;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;

    String tx_user_name,tx_user_email,tx_user_id;
    JSONObject tx_user_pic_url,tx_user_pic_data;
    DatabaseReference mProfileRef = FirebaseDatabase.getInstance().getReference("Profile");

    private Firebase mRef,mRef2;
    double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mBar = (RatingBar) findViewById(R.id.ratingBar);
        rateing_button = (Button) findViewById(R.id.rating_button);

        mLocationText = (TextView) findViewById(R.id.Locality);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        card_layout = (FrameLayout) findViewById(R.id.card_pin);
        pin_texttitle = (TextView) findViewById(R.id.card_pin_texttitle);
        pin_text1 = (TextView) findViewById(R.id.card_pin_text1);
        pin_text2 = (TextView) findViewById(R.id.card_pin_text2);
        vote_rating_text = (TextView) findViewById(R.id.vote_rating_text);
        vote_amount_text = (TextView) findViewById(R.id.vote_amount_text);
        card_layout_hide = (LinearLayout) findViewById(R.id.card_pin_hide);

        card_layout_hide.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                card_layout.setVisibility(View.GONE);
                materialDesignFAM.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideOutDown)
                        .duration(400)
                        .repeat(0)
                        .playOn(findViewById(R.id.card_pin));
            }
        });

        mapFragment.getMapAsync(this);
        mResultReceiver = new AddressResultReceiver(new Handler());

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");
        setNavigationHeader();
        setUserProfile(jsondata);

        onSetPin();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        //final FloatingActionButton programFab1 = new FloatingActionButton(this);
        materialDesignFAM.setClosedOnTouchOutside(true);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapActivity.class);
                i.putExtra("fb_email", tx_user_email );
                i.putExtra("fb_name", tx_user_name );
                i.putExtra("fb_id", tx_user_id );
                i.putExtra("fb_pic_data", tx_user_pic_data.toString());
                i.putExtra("fb_pic_url", tx_user_pic_url.toString());
                startActivity(i);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registerForContextMenu(floatingActionButton2);
                v.showContextMenu();
            }
        });

        mLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }
        });

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!AppUtils.isLocationEnabled(mContext)) {
                // notify user
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }


    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("กรุณาเลือกประเภท");
        menu.add(0, v.getId(), 0, "ด่านตรวจ");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "มีอุบัติเหตุ");
        menu.add(0, v.getId(), 0, "ขอความช่วยเหลือ");
    }
    private void setFirebaseDB(String fb_id,double pin_latitude,double pin_longitude,String pin_type,float vote_rating,int vote_amount){

        Long tsLong = System.currentTimeMillis()/1000;
        String server_timestamp = tsLong.toString();

        String dbLocation = "data/place";

        DBDetails details = new DBDetails(fb_id ,server_timestamp, pin_type,pin_latitude,pin_longitude,vote_rating,vote_amount);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(dbLocation);
        ref.child("pin").push().setValue(details);
        GeoFire geoFire = new GeoFire(ref);



    }
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="ด่านตรวจ"){
            Toast.makeText(getApplicationContext(),"เพิ่มหมุด 'ด่านตรวจ' แล้ว",Toast.LENGTH_LONG).show();
            setFirebaseDB(tx_user_id,lat,lng,"checkpoint",(float) 0.001,0);
        }
        else if(item.getTitle()=="มีอุบัติเหตุ"){
            Toast.makeText(getApplicationContext(),"เพิ่มหมุด 'มีอุบัติเหตุ' แล้ว",Toast.LENGTH_LONG).show();
            setFirebaseDB(tx_user_id,lat,lng,"accident",(float) 0.001,0);
        }
        else if(item.getTitle()=="ขอความช่วยเหลือ"){
            Toast.makeText(getApplicationContext(),"เพิ่มหมุด 'ขอความช่วยเหลือ' แล้ว",Toast.LENGTH_LONG).show();
            setFirebaseDB(tx_user_id,lat,lng,"help", (float) 0.001,0);
        }else{
            return false;
        }
        return true;
    }

    public void onSetPin() {

        mRef = new Firebase("https://pinthai-84714.firebaseio.com/data/place/pin");
        mRef.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (com.firebase.client.DataSnapshot issue : dataSnapshot.getChildren()) {
                      //  System.out.println("IS NANA2 "+issue.child("fb_id"));
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    private String getDate(long time) {
        Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss ,EEE dd MMM yy "); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        return sdf.format(date);
    }


    @Override


    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;

        mRef = new Firebase("https://pinthai-84714.firebaseio.com/data/place/pin");
        Long tsLong = System.currentTimeMillis()/1000;
        Long tsLong2 = (System.currentTimeMillis()/1000)-14400;
        String server_timestamp = tsLong.toString();
        String time_four = tsLong2.toString();
        mRef.orderByChild("current_date").startAt(tsLong2);
        mRef.orderByChild("current_date").startAt(time_four).addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (com.firebase.client.DataSnapshot issue : dataSnapshot.getChildren()) {

                        String fb_id = (String) issue.child("fb_id").getValue();
                        String date = (String) issue.child("current_date").getValue();
                        long l = Long.parseLong(date);
                        String date_conv = getDate(l);
                        double latitude = (double) issue.child("latitude").getValue();
                        double longitude = (double) issue.child("longitude").getValue();
                        String pintype = (String) issue.child("pin_type").getValue();
                        String pin_key = (String) issue.getKey();
                        long vote_amount = (long) issue.child("vote_amount").getValue();

                        double vote_rating = (double) issue.child("vote_rating").getValue();
                      //  double vote_rating = Double.parseDouble((String) dataSnapshot.child("vote_rating").getValue());

                        Geocoder geocoder = new Geocoder(MainActivity.mContext, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            Address obj = addresses.get(0);
                            //String add = "HELLO";//
                            String add = obj.getAddressLine(0);
                           // add = add + "\n" + obj.getLocality();
                           // add = add + "\n" + obj.getSubThoroughfare();
                            if(pintype.equals("checkpoint")){
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latitude, longitude))
                                        .title(add+" ")
                                        .snippet(pintype+" ")
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_police)));
                                marker.setTag(new GetDBValue(pin_key,fb_id,date_conv,pintype,latitude,longitude,vote_rating,vote_amount));

                            }
                            else if(pintype.equals("accident")){
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latitude, longitude))
                                        .title(add+" ")
                                        .snippet(pintype+" ")
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_accident)));
                                marker.setTag(new GetDBValue(pin_key,fb_id,date_conv,pintype,latitude,longitude,vote_rating,vote_amount));

                            }
                            else if(pintype.equals("help")){
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(latitude, longitude))
                                        .title(add+" ")
                                        .snippet(pintype+" ")
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pin_help)));
                                marker.setTag(new GetDBValue(pin_key,fb_id,date_conv,pintype,latitude,longitude,vote_rating,vote_amount));

                            }
                           // Log.v("IGA", "Address" + add);
                            // Toast.makeText(this, "Address=>" + add,
                            // Toast.LENGTH_SHORT).show();

                            // TennisAppActivity.showDialog(add);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                           // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 19.0f)); //zoom out

                card_layout.setVisibility(View.VISIBLE);
                materialDesignFAM.setVisibility(View.GONE);
                YoYo.with(Techniques.SlideInUp)
                        .duration(400)
                        .repeat(0)
                        .playOn(findViewById(R.id.card_pin));
               // Toast.makeText(mContext, marker.getTitle(), Toast.LENGTH_SHORT).show();

                pin_texttitle.setText(marker.getSnippet());
                pin_text1.setText(marker.getTitle());
                final GetDBValue data = (GetDBValue) marker.getTag();
                pin_text2.setText("เพิ่มเมื่อ "+data.current_date.toString());
                Button sentdata = (Button) findViewById(R.id.rating_button);
                mRef2 = new Firebase("https://pinthai-84714.firebaseio.com/data/place/pin/"+data.pin_key);
                mRef2.addListenerForSingleValueEvent(new  com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null) {
                            vote_amount_text.setText(Long.toString((long) dataSnapshot.child("vote_amount").getValue()));
                            double new_rating2 = (double) dataSnapshot.child("vote_rating").getValue();

                            double l = Double.parseDouble(String.valueOf(dataSnapshot.child("vote_rating").getValue()));
                            vote_rating_text.setText(String.format("%.1f", l));
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                sentdata.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRef2 = new Firebase("https://pinthai-84714.firebaseio.com/data/place/pin/"+data.pin_key);

                        mRef2.addListenerForSingleValueEvent(new  com.firebase.client.ValueEventListener() {
                            @Override
                            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                                float star = mBar.getRating();
                                long v_amount = (long) dataSnapshot.child("vote_amount").getValue();
                                double v_rate = (double) dataSnapshot.child("vote_rating").getValue();
                                long v_amount_call = v_amount;
                                if(v_amount == 0) v_amount_call+=1;
                                double new_rating = (double) ((v_rate*v_amount_call)+star)/(v_amount+1);
                                double a = (v_rate*v_amount_call)+star;
                                double b = v_amount+1;
                                double new_rating2 = a/b;

                                String dbLocation = "data/place/pin/"+data.pin_key;
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(dbLocation);
                                ref.child("vote_rating").setValue(new_rating2);
                                ref.child("vote_amount").setValue(v_amount+1);

                                vote_amount_text.setText(Long.toString((Long) dataSnapshot.child("vote_amount").getValue()+1));
                                vote_rating_text.setText(String.format("%.1f", new_rating2));
                              //  System.out.println("IS "+a+"/"+b+"TO "+v_rate+"*"+v_amount+"+"+star );
                                Toast.makeText(mContext, "ส่งผลโหวตแล้ว", Toast.LENGTH_SHORT).show();

                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                });

                return true;
            }
        });



        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;


               // mMap.clear();

                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    startIntentService(mLocation);
                //    mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            View locationButton2 = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("5"));

            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams)
                    locationButton2.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 1370);

            layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams2.setMargins(30, 200, 0, 0);

        }


//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }



    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            changeMap(mLastLocation);
            Log.d(TAG, "ON connected");
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                changeMap(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }
    private void changeMap(Location location) {

        Log.d(TAG, "Reaching map" + mMap);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;

            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(16.0f).build();
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

//            mLocationMarkerText.setText("Lat : " + location.getLatitude() + "," + "Long : " + location.getLongitude());
            startIntentService(location);

        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    public void setNavigationHeader(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        user_name = (TextView) header.findViewById(R.id.username);
        user_picture = (ImageView) header.findViewById(R.id.profile_pic);
        user_email = (TextView) header.findViewById(R.id.email);
    }
    public  void  setUserProfile(String jsondata){
        try {
            response = new JSONObject(jsondata);
            user_email.setText(response.get("email").toString());
            user_name.setText(response.get("name").toString());
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            Picasso.with(this).load(profile_pic_url.getString("url")).into(user_picture);
            //set text
            tx_user_name = (response.get("name").toString());
            tx_user_email = (response.get("email").toString());
            tx_user_id = (response.get("id").toString());
            tx_user_pic_data = new JSONObject(response.get("picture").toString());
            tx_user_pic_url = new JSONObject(profile_pic_data.getString("data"));

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("data");
            FacebookProfile fb_profile = new FacebookProfile(tx_user_id, tx_user_email, tx_user_name,tx_user_pic_data.toString(),tx_user_pic_url.toString());
            ref.child("users").setValue(fb_profile);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            //Toast.makeText(mContext, tx_user_name, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, HistoryActivity.class);
            i.putExtra("fb_email", tx_user_email );
            i.putExtra("fb_name", tx_user_name );
            i.putExtra("fb_id", tx_user_id );
            i.putExtra("fb_pic_data", tx_user_pic_data.toString());
            i.putExtra("fb_pic_url", tx_user_pic_url.toString());
            startActivity(i);
        }
        else if (id == R.id.nav_troubleshooting) {
            Intent i = new Intent(MainActivity.this, TroubleshootingActivity.class);
            startActivity(i);

        }
        else if (id == R.id.nav_helpcenter) {
            Intent i = new Intent(MainActivity.this, CallcenterActivity.class);
            startActivity(i);

        }
        else if (id == R.id.nav_info) {
            Toast.makeText(mContext, lat+" | "+lng, Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();
            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));
            }
        }
    }
    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput() {
        //  mLocationAddressTextView.setText(mAddressOutput);
        try {
            if (mAreaOutput != null){}
                // mLocationText.setText(mAreaOutput+ "");
               // mLocationMarkerText.setText(mAddressOutput);
            //mLocationText.setText(mAreaOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("TH").build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter).build(this);

            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);

        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(mContext, data);

                // TODO call location based filter


                LatLng latLong;


                latLong = place.getLatLng();

                //mLocationText.setText(place.getName() + "");

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(19f).tilt(70).build();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }

        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(mContext, data);
        } else if (resultCode == RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }
    }


}