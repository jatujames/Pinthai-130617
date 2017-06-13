package com.pinthai.adapter;

/**
 * Created by WE on 5/28/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pinthai.R;
import com.pinthai.wrapper.Contacts;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    private static final int REQUEST_PHONE_CALL = 1;
    public ImageButton imageButton1 ;

    private List<Contacts> contactsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView call_name, number;


        public MyViewHolder(View view) {
            super(view);
            call_name = (TextView) view.findViewById(R.id.call_name);
            imageButton1 = (ImageButton) view.findViewById(R.id.call_butt);
        }
    }


    public ContactsAdapter(List<Contacts> contactsList) {
        this.contactsList = contactsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Contacts contacts = contactsList.get(position);
        holder.call_name.setText(contacts.call_name);


        imageButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contacts.number));
                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                }else{
                    v.getContext().startActivity(intent);
                }

            }
        });
       // holder.number.setText(contacts.number);
    }


    @Override
    public int getItemCount() {
        return contactsList.size();
    }
}