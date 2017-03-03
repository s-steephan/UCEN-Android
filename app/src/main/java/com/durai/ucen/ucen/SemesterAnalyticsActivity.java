package com.durai.ucen.ucen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SemesterAnalyticsActivity extends AppCompatActivity {
    ListView listView;
    private static String ROOT_URL = null;
    String semester_id;
    SubjectAdapter subjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_analytics);

        semester_id = getIntent().getStringExtra("semester_id");
        listView = (ListView) findViewById(R.id.subjects_list);
        ArrayList<Subject> arrayOfSubjects = new ArrayList<Subject>();
        subjectAdapter = new SubjectAdapter(this, arrayOfSubjects);
        ROOT_URL = UcenUtils.getCoreUrl();
        getSemesterDetail(semester_id);
    }

    private void getSemesterDetail(String semester_id) {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Semester Details","Please wait...",false,false);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        String token;
                        token = UcenUtils.getToken(SemesterAnalyticsActivity.this);
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
                        BarChart barChart = (BarChart) findViewById(R.id.barchart);
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<String>();
                        BarDataSet bardataset = new BarDataSet(entries, "Cells");
                        for(int i=0; i<semester.getSubjects().size(); i++){
                            entries.add(new BarEntry(Float.parseFloat(semester.getSubjects().get(i).getScore()), i));
                            labels.add(semester.getSubjects().get(i).getName());
                        }
                        BarData data = new BarData(labels, bardataset);
                        barChart.setData(data);
                        barChart.setDescription("Semester Analytics");
                        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                        barChart.animateY(2000);
                        loading.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError cause) {
                        String errorDescription;
                        if (cause.getResponse() == null) {
                            errorDescription = "No Internet Connection";
                        }
                        else {
                            UcenUtils.clearAllDetails(SemesterAnalyticsActivity.this);
                            errorDescription = "Please login again";
                            startActivity(new Intent(SemesterAnalyticsActivity.this, LoginActivity.class));
                        }
                        Toast.makeText(SemesterAnalyticsActivity.this, errorDescription, Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }
        );
    }
}

