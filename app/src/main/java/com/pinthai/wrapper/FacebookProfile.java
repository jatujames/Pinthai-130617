package com.pinthai.wrapper;

/**
 * Created by WE on 4/5/2017.
 */

public class FacebookProfile {
    public String fb_id;
    public String fb_email;
    public String fb_name;
    public String fb_pic_data;
    public String fb_pic_url;

    public FacebookProfile(String fb_id,String fb_email, String fb_name, String fb_pic_data, String fb_pic_url ) {
        this.fb_id = fb_id;
        this.fb_email = fb_email;
        this.fb_name = fb_name;
        this.fb_pic_data = fb_pic_data;
        this.fb_pic_url = fb_pic_url;
    }
}
