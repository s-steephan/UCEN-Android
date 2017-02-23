package com.durai.ucen.ucen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Steephan Selvaraj on 2/23/2017.
 */

public class CircularAdapter extends ArrayAdapter<Circular> {
    public CircularAdapter(Context context, ArrayList<Circular> resource) {
        super(context, 0, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Circular circular = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ciruclar_row, parent, false);
        }

        TextView t_title= (TextView) convertView.findViewById(R.id.circular_title);
        TextView t_date = (TextView) convertView.findViewById(R.id.circular_date);

        String date = UcenUtils.formatDate(circular.getCreated());

        t_title.setText(circular.getTitle());
        t_date.setText("Published on: "+date);

        return convertView;
    }
}
