package com.durai.ucen.ucen;

import android.app.Activity;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Steephan Selvaraj on 1/25/2017.
 */

public class UcenUtils {
    public static final String PREFS_NAME = "Login_Token";
    public static String getToken(Activity activity){
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String token = settings.getString("token", "");
        return "JWT "+token;
    }
}
