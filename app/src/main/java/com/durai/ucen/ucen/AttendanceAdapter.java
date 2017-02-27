package com.durai.ucen.ucen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;

/**
 * Created by Steephan Selvaraj on 2/23/2017.
 */

public class AttendanceAdapter extends ArrayAdapter<Attendance> {
    public AttendanceAdapter(Context context, ArrayList<Attendance> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Attendance attendance = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.attendance_row, parent, false);
        }

        TextView t_semester= (TextView) convertView.findViewById(R.id.semester_text);
        TextView t_percentage = (TextView) convertView.findViewById(R.id.percentage_text);
        PieView pieView = (PieView) convertView.findViewById(R.id.pieView);
        pieView.setPercentage(new Integer(attendance.getPercentage()));
        //pieView.setPercentageBackgroundColor(getResources().getColor(R.color.colorPrimary));
        pieView.setInnerText(attendance.getPercentage()+"%");

        PieView animatedPie = (PieView) convertView.findViewById(R.id.pieView);

        PieAngleAnimation animation = new PieAngleAnimation(animatedPie);
        animation.setDuration(1000); //This is the duration of the animation in millis
        animatedPie.startAnimation(animation);

        //String date = UcenUtils.formatDate(circular.getCreated());

        t_semester.setText(attendance.getSemester());
        t_percentage.setText(attendance.getPercentage()+"%");

        return convertView;
    }
}
