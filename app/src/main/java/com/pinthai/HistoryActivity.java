package com.pinthai;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.pinthai.adapter.RecyclerViewAdapter;
import com.pinthai.adapter.util.DividerItemDecoration;
import com.pinthai.wrapper.GetDBValue;
import com.pinthai.wrapper.HistoryData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;



public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private String fb_id,fb_email,fb_name,fb_pic_data,fb_pic_url;
    private ArrayList<String> mDataSet;
    private Firebase mRef;
    static Context mContext;
    //private ArrayList<HistoryData> adapterData = new ArrayList<GetDBValue>();
    List<HistoryData> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mContext = this;

        fb_id = getIntent().getStringExtra("fb_id");
        fb_email = getIntent().getStringExtra("fb_email");
        fb_name = getIntent().getStringExtra("fb_name");

        fb_pic_data = getIntent().getStringExtra("fb_pic_data").toString();
        fb_pic_url = getIntent().getStringExtra("fb_pic_url").toString();



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("RecyclerView");
            }
        }
        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Item Decorator:
       // recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.ic_action_book)));
        recyclerView.setItemAnimator(new FadeInLeftAnimator());

        mRef = new Firebase("https://pinthai-84714.firebaseio.com/data/place/pin");
        Long tsLong = System.currentTimeMillis()/1000;
        Long tsLong2 = (System.currentTimeMillis()/1000)-3600;
        String time_four = tsLong2.toString();
        mRef.orderByChild("current_date").startAt(time_four);

        mRef.orderByChild("fb_id")
                .equalTo(fb_id)
                .addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (com.firebase.client.DataSnapshot issue : dataSnapshot.getChildren()) {

                        double latitude = (double) issue.child("latitude").getValue();
                        double longitude = (double) issue.child("longitude").getValue();
                        String pintype = (String) issue.child("pin_type").getValue();
                        String date = (String)issue.child("current_date").getValue();
                        String info = (String) issue.child("info").getValue();

                        final String pin_key = (String) issue.getKey();
                        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                        Long tsLong2 = (System.currentTimeMillis()/1000)-14400;
                        int time_four = tsLong2.intValue();
                        int time_ori = Integer.valueOf(date);


                        if(time_ori > time_four) {
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                Address obj = addresses.get(0);
                                String add = obj.getAddressLine(0);
                                add = add + " " + obj.getLocality();
                                long l = Long.parseLong(date);
                                String date_time = getDate(l);

                                HistoryData history = new HistoryData();
                                history.pin_key = pin_key;
                                history.pin_type = pintype;
                                history.locate = add;
                                history.date_time = date_time;
                                data.add(history);

                                mAdapter = new RecyclerViewAdapter(this, data);
                                recyclerView.setAdapter(mAdapter);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            }
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);
    }

    /**
     * Substitute for our onScrollListener for RecyclerView
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };
    private String getDate(long time) {
        Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));

        return sdf.format(date);
    }
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
