package com.pinthai.adapter;

/**
 * Created by WE on 5/14/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pinthai.R;
import com.pinthai.wrapper.GetDBValue;
import com.pinthai.wrapper.HistoryData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerSwipeAdapter<RecyclerViewAdapter.SimpleViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<HistoryData> data= Collections.emptyList();
    HistoryData current;
    private Firebase mRef;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView textViewPinType;
        TextView textViewLocate;
        TextView textViewDate;
        ImageView imageViewPin;
        Button buttonDelete;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            textViewPinType = (TextView) itemView.findViewById(R.id.text_data_pintype);
            textViewLocate = (TextView) itemView.findViewById(R.id.text_data_where);
            textViewDate = (TextView) itemView.findViewById(R.id.text_data_time);
            imageViewPin = (ImageView) itemView.findViewById(R.id.img_pin);
            buttonDelete = (Button) itemView.findViewById(R.id.delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + textViewLocate.getText().toString());
                    Toast.makeText(view.getContext(), "onItemSelected: " + textViewLocate.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Context mContext;
    private List<GetDBValue> mDataset;

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public RecyclerViewAdapter(ValueEventListener context/*ValueEventListener context*/, List<HistoryData> objects) {
       // this.mDataset = objects;
        this.data=objects;

    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final HistoryData current = data.get(position);
        viewHolder.textViewLocate.setText(current.locate);
        viewHolder.textViewPinType.setText(current.pin_type);
        viewHolder.textViewDate.setText(current.date_time);
        if(current.pin_type.equals("สิ่งกีดขวางควรหลีกเลี่ยง")){
            viewHolder.imageViewPin.setImageResource(R.mipmap.ic_pin_danger);
        }
        else if(current.pin_type.equals("มีอุบัติเหตุ")){
            viewHolder.imageViewPin.setImageResource(R.mipmap.ic_pin_accident);
        }
        else if(current.pin_type.equals("ขอความช่วยเหลือ")){
            viewHolder.imageViewPin.setImageResource(R.mipmap.ic_pin_help);
        }

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
      /*  viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });*/
        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, data.size());
                mItemManger.closeAllItems();

                String pin_key = current.pin_key;
                mRef = new Firebase("https://pinthai-84714.firebaseio.com/data/place/pin/"+pin_key);
                mRef.removeValue();
               /* String dbLocation = "data/place/pin/" + pin_type;
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(dbLocation);
                ref.child("pin_type");*/

                String alert = current.pin_type+" "+current.locate+" "+current.date_time;
                Toast.makeText(view.getContext(), "ลบ " + alert + "เรียบร้อยแล้ว!", Toast.LENGTH_SHORT).show();

            }
        });
     //   viewHolder.textViewPos.setText((position + 1) + ". ");
        //viewHolder.textViewData.setText(item.fb_id);
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}