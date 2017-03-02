package com.durai.ucen.ucen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Steephan Selvaraj on 2/23/2017.
 */

public class SemestersListAdapter extends ArrayAdapter<SemestersList> {
    public SemestersListAdapter(Context context, ArrayList<SemestersList> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SemestersList semestersList = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.semester_row, parent, false);
        }

        TextView t_title= (TextView) convertView.findViewById(R.id.semester_title_text);
        TextView t_created_date = (TextView) convertView.findViewById(R.id.semester_created_text);
        TextView t_modified_date = (TextView) convertView.findViewById(R.id.semester_lastmodified_text);

        String created_date = UcenUtils.formatDate(semestersList.getCreated());
        String modified_date = UcenUtils.formatDate(semestersList.getModified());
        t_title.setText(semestersList.getSemester());
        t_created_date.setText("Created on: "+created_date);
        t_modified_date.setText("Last Updated on: "+modified_date);
        return convertView;
    }
}
