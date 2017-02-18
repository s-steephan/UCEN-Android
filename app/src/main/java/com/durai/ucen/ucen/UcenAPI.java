package com.durai.ucen.ucen;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Steephan Selvaraj on 1/24/2017.
 */

public interface UcenAPI {
        
        @FormUrlEncoded
        @POST("/api/v1/auth-token/")
        public void getUser(@Field ("username") String username, @Field ("password") String password, Callback<Token> response);

        @GET("/api/v1/me/")
        public void getUserDetails(Callback<UserDetails> response);

};