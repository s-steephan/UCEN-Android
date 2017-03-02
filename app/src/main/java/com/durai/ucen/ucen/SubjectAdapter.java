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

public class SubjectAdapter extends ArrayAdapter<Subject> {
    public SubjectAdapter(Context context, ArrayList<Subject> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Subject subject = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.subject_row, parent, false);
        }

        TextView t_title = (TextView) convertView.findViewById(R.id.subject_title_text);
        TextView t_code = (TextView) convertView.findViewById(R.id.subject_code_text);
        TextView t_mark = (TextView) convertView.findViewById(R.id.subject_score_text);
        TextView attempts_count = (TextView) convertView.findViewById(R.id.subject_attempts_text);
        TextView t_status = (TextView) convertView.findViewById(R.id.subject_status_text);
        TextView t_date = (TextView) convertView.findViewById(R.id.subject_updated_date);

        String date = UcenUtils.formatDate(subject.getModified());
        t_title.setText(subject.getName());
        t_code.setText("Subject Code: "+subject.getCode());
        attempts_count.setText("Number of Attempts: "+subject.getAttemptCount());
        t_mark.setText("Score: "+subject.getScore());
        t_status.setText("Status: "+subject.getStatus());
        t_date.setText("Updated on: "+date);
        return convertView;
    }
}
