package com.durai.ucen.ucen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SemestersListActivity extends AppCompatActivity {
    public static String ROOT_URL = null;
    ListView listView;
    TextView t_empty;
    SemestersListAdapter semestersListAdapter;
    String is_analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesters_list);
        ROOT_URL=UcenUtils.getCoreUrl();
        is_analytics = getIntent().getStringExtra("is_analytics");
        t_empty = (TextView) findViewById(R.id.empty_text);
        listView = (ListView) findViewById(R.id.semesters_list);
        ArrayList<SemestersList> arrayOfSemesters = new ArrayList<SemestersList>();
        semestersListAdapter = new SemestersListAdapter(this, arrayOfSemesters);
        getSemesterList();

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position,
                                    long id) {
                SemestersList model = (SemestersList) parent.getItemAtPosition(position);
                Intent intent;
                if (is_analytics != null) {
                    intent = new Intent(SemestersListActivity.this, SemesterAnalyticsActivity.class);
                }
                else {
                    intent = new Intent(SemestersListActivity.this, SemesterDetailActivity.class);
                }
                intent.putExtra("semester_id", model.getId().toString());
                startActivity(intent);
            }
        });
    }

    private void getSemesterList() {
        final ProgressDialog loading = ProgressDialog.show(this,"Updating Semesters List","Please wait...",false,false);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        String token;
                        token = UcenUtils.getToken(SemestersListActivity.this);
                        request.addHeader("Authorization", token);
                    }
                })
                .build();


        UcenAPI api = adapter.create(UcenAPI.class);

        api.getSemestersList(new Callback<List<SemestersList>>() {

            @Override
            public void success(List<SemestersList> semestersLists, Response response) {
                semestersListAdapter.addAll(semestersLists);
                if(semestersLists.size()==0){
                    t_empty.setVisibility(View.VISIBLE);
                }
                listView.setAdapter(semestersListAdapter);
                loading.dismiss();
            }

            @Override
            public void failure(RetrofitError cause) {
                String errorDescription;
                if (cause.getResponse() == null) {
                    errorDescription = "No Internet Connection";
                }
                else {
                    UcenUtils.clearAllDetails(SemestersListActivity.this);
                    errorDescription = "Please login again";
                    startActivity(new Intent(SemestersListActivity.this, LoginActivity.class));
                }
                Toast.makeText(SemestersListActivity.this, errorDescription, Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        });
    }
}
