package com.pinthai;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;

/**
 * Created by WE on 4/22/2017.
 */

public class FireApp extends Application {
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
