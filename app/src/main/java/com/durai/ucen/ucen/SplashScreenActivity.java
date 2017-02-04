package com.durai.ucen.ucen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {

    private static final int SPLASH_TIME_OUT = 3000;
    public static final String PREFS_NAME = "Login_Token";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences settings = SplashScreenActivity.this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        token = settings.getString("token", "");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i;
                if (token!="") {
                    i = new Intent(SplashScreenActivity.this, MainActivity.class);
                }
                else {
                    i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
