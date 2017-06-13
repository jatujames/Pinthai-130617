package com.pinthai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

/**
 * Created by WE on 5/28/2017.
 */
public class LoginFilter extends AppCompatActivity {
    private AccessTokenTracker accessTokenTracker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };
        updateWithToken(AccessToken.getCurrentAccessToken());
    }
    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            Intent i = new Intent(this, MainActivity.class);
            currentAccessToken.getSource();
            startActivity(i);

            finish();
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);

            finish();
        }
    }
}
