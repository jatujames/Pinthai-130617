package com.pinthai.wrapper;

/**
 * Created by WE on 4/7/2017.
 */

public class PostDBFirebase {
    public String current_date;
    public String fb_id;
    public String latitude;
    public String longitude;
    public String pin_type;

    public PostDBFirebase(String current_date, String fb_id, String latitude, String longitude, String pin_type ) {
        this.current_date = current_date;
        this.fb_id = fb_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pin_type = pin_type;

    }
}
