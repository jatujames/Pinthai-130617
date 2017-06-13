package com.pinthai.wrapper;

/**
 * Created by WE on 4/5/2017.
 */

public class DBDetails {
    public String fb_id;
    public String current_date;
    public String pin_type;
    public double latitude;
    public double longitude;
    public float vote_rating;
    public long vote_amount;
    public String info;


    public DBDetails(String fb_id,String current_date, String pin_type, double latitude,double longitude,float vote_rating,int vote_amount,String info ){
        this.fb_id = fb_id;
        this.current_date = current_date;
        this.pin_type = pin_type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vote_rating = vote_rating;
        this.vote_amount = vote_amount;
        this.info = info;

    }
}
