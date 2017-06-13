package com.pinthai;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.pinthai.adapter.ContactsAdapter;
import com.pinthai.wrapper.Contacts;

import java.util.ArrayList;
import java.util.List;


public class CallcenterActivity extends AppCompatActivity {
    private List<Contacts> contactsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callcenter);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ContactsAdapter(contactsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prepareMovieData();
    }
    private void prepareMovieData() {
        Contacts contacts = new Contacts("วชิระพยาบาล","1554");
        contactsList.add(contacts);

        contacts = new Contacts("หน่วยงานรัฐ","1669");
        contactsList.add(contacts);

        contacts = new Contacts("โรงพยาบาลตำรวจ","1691");
        contactsList.add(contacts);

        contacts = new Contacts("เหตุด่วนเหตุร้าย","191");
        contactsList.add(contacts);

        contacts = new Contacts("ดับเพลิง","199");
        contactsList.add(contacts);

        contacts = new Contacts("ไฟฟ้าขัดข้อง","1130");
        contactsList.add(contacts);

        contacts = new Contacts("กรมทางหลวงชนบท","1146");
        contactsList.add(contacts);

        contacts = new Contacts("ตำรวจท่องเที่ยว","1155");
        contactsList.add(contacts);

        contacts = new Contacts("แจ้งรถหาย","1192");
        contactsList.add(contacts);

        contacts = new Contacts("ตำรวจทางหลวง","1193");
        contactsList.add(contacts);

        contacts = new Contacts("กองปราบปราม","1195");
        contactsList.add(contacts);

        contacts = new Contacts("ข้อมูลการจราจร","1197");
        contactsList.add(contacts);

        contacts = new Contacts("เหตุด่วนทางน้ำ","1199");
        contactsList.add(contacts);

        contacts = new Contacts("สายตรงทางด่วน","1543");
        contactsList.add(contacts);

        contacts = new Contacts("จส.100","1137");
        contactsList.add(contacts);

        contacts = new Contacts("สถานีวิทยุจราจรเพื่อสังคม","1255");
        contactsList.add(contacts);

        contacts = new Contacts("สวพ.91 1644-1677","1644");
        contactsList.add(contacts);


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
