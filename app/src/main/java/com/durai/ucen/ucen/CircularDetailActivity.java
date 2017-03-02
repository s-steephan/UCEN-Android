package com.durai.ucen.ucen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CircularDetailActivity extends AppCompatActivity {
    String circular_id;
    WebView webView;
    public static String ROOT_URL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_detail);
        ROOT_URL = UcenUtils.getCoreUrl();
        webView = (WebView) findViewById(R.id.newweb);
        circular_id = getIntent().getStringExtra("circular_id");

        getCircularDetail(circular_id);
    }

    private void getCircularDetail(String circular_id){
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Circular","Please wait...",false,false);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        String token;
                        token = UcenUtils.getToken(CircularDetailActivity.this);
                        request.addHeader("Authorization", token);
                    }
                })
                .build();

        UcenAPI api = adapter.create(UcenAPI.class);

        api.getCircularDetail(circular_id, new Callback<CircularDetail>(){

            @Override
            public void success(CircularDetail circularDetail, Response response) {
                StringBuilder s = new StringBuilder(10000);
                String date = UcenUtils.formatDate(circularDetail.getCreated());
                s = s.append("<h3>"+circularDetail.getTitle()+"</h3>");
                s = s.append("<h5>Published on: "+date+"<h5>");
                s = s.append(circularDetail.getContentHtml());
                webView.loadData(s.toString(), "text/html", "UTF-8");
                //textView.setText(Html.fromHtml(circularDetail.getContentHtml()));
                loading.dismiss();
            }

            @Override
                    public void failure(RetrofitError cause) {
                        String errorDescription;
                        if (cause.getResponse() == null) {
                            errorDescription = "No Internet Connection";
                        }
                        else {
                            UcenUtils.clearAllDetails(CircularDetailActivity.this);
                            errorDescription = "Please login again";
                            startActivity(new Intent(CircularDetailActivity.this, LoginActivity.class));
                        }
                        Toast.makeText(CircularDetailActivity.this, errorDescription, Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }
        );
    }
}
