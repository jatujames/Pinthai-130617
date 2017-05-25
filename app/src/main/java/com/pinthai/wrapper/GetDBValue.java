package com.pinthai.wrapper;

/**
 * Created by WE on 4/5/2017.
 */

public class GetDBValue {
    public String pin_key;
    public String fb_id;
    public String current_date;
    public String pin_type;
    public double latitude;
    public double longitude;
    public double vote_rating;
    public double vote_amount;
    public GetDBValue(String pin_key,String fb_id,String current_date, String pin_type, double latitude,double longitude,double vote_rating,double vote_amount ){
        this.pin_key = pin_key;
        this.fb_id = fb_id;
        this.current_date = current_date;
        this.pin_type = pin_type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vote_rating = vote_rating;
        this.vote_amount = vote_amount;

    }
}
