package com.durai.ucen.ucen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class SemesterDetailActivity extends AppCompatActivity {
    ListView listView;
    private static String ROOT_URL = null;
    String semester_id;
    SubjectAdapter subjectAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_detail);

        semester_id = getIntent().getStringExtra("semester_id");
        listView = (ListView) findViewById(R.id.subjects_list);
        ArrayList<Subject> arrayOfSubjects = new ArrayList<Subject>();
        subjectAdapter = new SubjectAdapter(this, arrayOfSubjects);
        ROOT_URL = UcenUtils.getCoreUrl();
        getSemesterDetail(semester_id);

        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                subjectAdapter.clear();
                getSemesterDetail(semester_id);
                swipeView.setRefreshing(false);
            }
        });
    }

    private void getSemesterDetail(String semester_id) {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Semester Details","Please wait...",false,false);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        String token;
                        token = UcenUtils.getToken(SemesterDetailActivity.this);
                        request.addHeader("Authorization", token);
                    }
                })
                .build();

        UcenAPI api = adapter.create(UcenAPI.class);

        api.getSemesterDetail(semester_id, new Callback<Semester>(){
            @Override
            public void success(Semester semester, Response response) {
                TextView semester_title = (TextView) findViewById(R.id.semester_title_text);
                TextView updated_date = (TextView) findViewById(R.id.semester_updated_date);
                String date = UcenUtils.formatDate(semester.getModified());
                semester_title.setText(semester.getSemester());
                updated_date.setText("Updated on: "+date);
                updated_date.setVisibility(View.VISIBLE);
                subjectAdapter.addAll(semester.getSubjects());
                listView.setAdapter(subjectAdapter);
                loading.dismiss();
            }

            @Override
                    public void failure(RetrofitError cause) {
                        String errorDescription;
                        if (cause.getResponse() == null) {
                            errorDescription = "No Internet Connection";
                        }
                        else {
                            UcenUtils.clearAllDetails(SemesterDetailActivity.this);
                            errorDescription = "Please login again";
                            startActivity(new Intent(SemesterDetailActivity.this, LoginActivity.class));
                        }
                        Toast.makeText(SemesterDetailActivity.this, errorDescription, Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }
        );
    }
}
