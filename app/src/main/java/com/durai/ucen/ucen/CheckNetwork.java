package com.durai.ucen.ucen;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Steephan Selvaraj on 1/31/2017.
 */

public class CheckNetwork {

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
}
