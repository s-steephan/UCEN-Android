package com.durai.ucen.ucen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String ROOT_URL = null;
    public static final String PREFS_NAME_1 = "User_Details", PREFS_NAME_2 = "Login_Token";
    ListView listView;
    TextView t_empty;
    CircularAdapter circularAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ROOT_URL = UcenUtils.getCoreUrl();
        t_empty = (TextView) findViewById(R.id.empty_text);
        listView = (ListView) findViewById(R.id.circular_list);
        ArrayList<Circular> arrayOfCirculars = new ArrayList<Circular>();
        circularAdapter = new CircularAdapter(this, arrayOfCirculars);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getUserDetails();
        getCirculars();

        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeView.setProgressViewOffset(false, 0,160);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                           @Override
                                           public void onRefresh() {
                                               swipeView.setRefreshing(true);
                                               circularAdapter.clear();
                                               getCirculars();
                                               swipeView.setRefreshing(false);
                                           }
                                       });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                Circular model = (Circular) parent.getItemAtPosition(position);
                //Toast.makeText(MainActivity.this, ""+model.getId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, CircularDetailActivity.class);
                intent.putExtra("circular_id", model.getId().toString());
                startActivity(intent);
            }
        });
    }

    private void getUserDetails() {
        final ProgressDialog progressdialog = new ProgressDialog(MainActivity.this);
        progressdialog.setMessage("Please wait..");
        progressdialog.show();
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ROOT_URL)
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            String token;
                            token = UcenUtils.getToken(MainActivity.this);
                            request.addHeader("Authorization", token);
                        }
                    })
                    .build();

        UcenAPI api = adapter.create(UcenAPI.class);

        api.getUserDetails(new Callback<UserDetails>() {
            @Override
            public void success(UserDetails userDetails, Response response) {
                TextView t1 = (TextView) findViewById(R.id.t_displayname);
                TextView t2 = (TextView) findViewById(R.id.t_email);
                String displayname = null, email ;
                SharedPreferences settings = getSharedPreferences(PREFS_NAME_1, MODE_PRIVATE);
                    if (!userDetails.getBasic_details().getFirst_name().isEmpty() && !userDetails.getBasic_details().getLast_name().isEmpty()) {
                        displayname = userDetails.getBasic_details().getFirst_name() + " " + userDetails.getBasic_details().getLast_name();
                    } else if (userDetails.getBasic_details().getFirst_name().isEmpty() && userDetails.getBasic_details().getLast_name().isEmpty()) {
                        displayname = userDetails.getBasic_details().getUsername();
                    } else if (userDetails.getBasic_details().getFirst_name().isEmpty()) {
                        displayname = userDetails.getBasic_details().getLast_name();
                    } else if (userDetails.getBasic_details().getLast_name().isEmpty()) {
                        displayname = userDetails.getBasic_details().getFirst_name();
                    }

                    email = userDetails.getBasic_details().getEmail();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("display_name", displayname);
                    editor.putString("email", email);
                    editor.apply();
                    t1.setText(displayname);
                    t2.setText(email);
                    progressdialog.dismiss();
            }

            @Override
            public void failure(RetrofitError cause) {
                String errorDescription;
                SharedPreferences user_details = MainActivity.this.getSharedPreferences(PREFS_NAME_1, MODE_PRIVATE);
                if (cause.getResponse() == null) {
                    TextView t1 = (TextView) findViewById(R.id.t_displayname);
                    TextView t2 = (TextView) findViewById(R.id.t_email);
                    String displayname, email ;
                    displayname = user_details.getString("display_name", "");
                    email = user_details.getString("email", "");
                    t1.setText(displayname);
                    t2.setText(email);
                    errorDescription = "No Internet Connection";
                } else {
                    UcenUtils.clearAllDetails(MainActivity.this);
                    errorDescription = "Please login again";
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                Toast.makeText(MainActivity.this, errorDescription, Toast.LENGTH_LONG).show();
                progressdialog.dismiss();
            }
        });
    }

    private void getCirculars(){
        final ProgressDialog loading = ProgressDialog.show(this,"Updating Cirulars","Please wait...",false,false);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        String token;
                        token = UcenUtils.getToken(MainActivity.this);
                        request.addHeader("Authorization", token);
                    }
                })
                .build();


        UcenAPI api = adapter.create(UcenAPI.class);

        api.getCirculars(new Callback<List<Circular>>() {
            @Override
            public void success(List<Circular> circulars, Response response) {
                circularAdapter.addAll(circulars);
                t_empty.setVisibility(View.INVISIBLE);
                if(circulars.size()==0){
                    t_empty.setVisibility(View.VISIBLE);
                }
                listView.setAdapter(circularAdapter);
                loading.dismiss();
            }

            @Override
            public void failure(RetrofitError cause) {
                String errorDescription;
                if (cause.getResponse() == null) {
                    errorDescription = "No Internet Connection";
                }
                else {
                    UcenUtils.clearAllDetails(MainActivity.this);
                    errorDescription = "Please login again";
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                Toast.makeText(MainActivity.this, errorDescription, Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            ActivityCompat.finishAffinity(MainActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close) {
            final ProgressDialog progressdialog = new ProgressDialog(MainActivity.this);
            progressdialog.setMessage("Please wait..");
            progressdialog.show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            progressdialog.dismiss();
                            Toast.makeText(MainActivity.this, "Bye", Toast.LENGTH_SHORT).show();
                            ActivityCompat.finishAffinity(MainActivity.this);
                        }
                    }, 2000);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_attendance) {
            startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
        }
        else if (id == R.id.nav_result) {
            startActivity(new Intent(MainActivity.this, SemestersListActivity.class));
        }
        else if (id == R.id.nav_map) {
            String lat="8.167338", lng="77.413882", mTitle="University College of Engineering, Nagercoil";
            String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + mTitle + ")";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(geoUri));
            intent.setClassName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }
        else if (id == R.id.nav_send) {
            String mailId = "selvaduraimurugan@gmail.com";
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                    Uri.fromParts("mailto", mailId, null));
            //emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback - Reg");
            startActivity(Intent.createChooser(emailIntent, "Email via"));
        }
        else if (id == R.id.nav_logout) {
            final ProgressDialog progressdialog = new ProgressDialog(MainActivity.this);
            progressdialog.setMessage("Logging out..");
            progressdialog.show();
            UcenUtils.clearAllDetails(MainActivity.this);
            Toast.makeText(MainActivity.this, "Bye", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            progressdialog.dismiss();
        }
        else if (id==R.id.nav_analytics){
            Intent intent = new Intent(MainActivity.this, SemestersListActivity.class);
            intent.putExtra("is_analytics", "true");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
