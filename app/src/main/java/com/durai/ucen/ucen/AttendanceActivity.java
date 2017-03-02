package com.durai.ucen.ucen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AttendanceActivity extends AppCompatActivity {
    public static String ROOT_URL = null;
    ListView listView;
    TextView t_empty;
    AttendanceAdapter attendanceAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        ROOT_URL = UcenUtils.getCoreUrl();
        listView = (ListView) findViewById(R.id.attendance_list);
        ArrayList<Attendance> arrayOfAttendance = new ArrayList<Attendance>();

        t_empty = (TextView) findViewById(R.id.empty_text);
        attendanceAdapter = new AttendanceAdapter(this, arrayOfAttendance);

        getAttendance();
    }

    private void getAttendance(){
        final ProgressDialog loading = ProgressDialog.show(this,"Updating Attendance","Please wait...",false,false);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        String token;
                        token = UcenUtils.getToken(AttendanceActivity.this);
                        request.addHeader("Authorization", token);
                    }
                })
                .build();
        UcenAPI api = adapter.create(UcenAPI.class);

        api.getAttendance(new Callback<List<Attendance>>() {
            @Override
            public void success(List<Attendance> attendances, Response response) {
                for(int i=0; i<attendances.size(); i++){
                    Attendance attendance = new Attendance();
                    attendance.setSemester(attendances.get(i).getSemester());
                    attendance.setPercentage(attendances.get(i).getPercentage());
                    //circular.setCreated(circulars.get(i).getCreated());
                    attendanceAdapter.add(attendance);
                }

                t_empty.setVisibility(View.INVISIBLE);
                if(attendances.size()==0){
                    t_empty.setVisibility(View.VISIBLE);
                }

                listView.setAdapter(attendanceAdapter);
                loading.dismiss();
            }

            @Override
            public void failure(RetrofitError cause) {
                String errorDescription;
                if (cause.getResponse() == null) {
                    errorDescription = "No Internet Connection";
                }
                else {
                    UcenUtils.clearAllDetails(AttendanceActivity.this);
                    errorDescription = "Please login again";
                    startActivity(new Intent(AttendanceActivity.this, LoginActivity.class));
                }
                Toast.makeText(AttendanceActivity.this, errorDescription, Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        });
    }

}
