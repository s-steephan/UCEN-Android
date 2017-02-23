package com.durai.ucen.ucen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Steephan Selvaraj on 1/25/2017.
 */

public class UcenUtils {
    public static final String PREFS_NAME_1 = "User_Details", PREFS_NAME_2 = "Login_Token";
    public static String getToken(Activity activity){
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME_2, MODE_PRIVATE);
        String token = settings.getString("token", "");
        return "JWT "+token;
    }

    public static void clearAllDetails(Activity activity){
        SharedPreferences user_details = activity.getSharedPreferences(PREFS_NAME_1, MODE_PRIVATE);
        SharedPreferences login_credentials = activity.getSharedPreferences(PREFS_NAME_2, MODE_PRIVATE);
        user_details.edit().clear().apply();
        login_credentials.edit().clear().apply();
    }

    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public static String formatDate(String inputString) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ssssss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            if(inputString != null && !inputString.isEmpty()) {
                date = simpleDateFormat.parse(inputString);
                DateFormat dateformat = DateFormat.getDateInstance();
                return dateformat.format(date);
            }
        }
        catch (ParseException e) {
        }
        return null;
    }


}
