package com.pinthai;

import android.content.Intent;

/**
 * Created by WE on 5/29/2017.
 */

public class RSSPullService {
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        // Do work here, based on the contents of dataString

    }
}
