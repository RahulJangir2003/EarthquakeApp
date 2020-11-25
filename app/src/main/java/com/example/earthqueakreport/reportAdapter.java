package com.example.earthqueakreport;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class reportAdapter extends BaseAdapter {
    private ArrayList<report> reports;
    private Context context;
   private TextView mag;
   private TextView place;
   private TextView date;
    public reportAdapter(ArrayList<report> reports, Context context) {
        this.reports = reports;
        this.context = context;
    }

    @Override
    public int getCount() {

        if(reports==null)return 0;
        else
        return reports.size();
    }

    @Override
    public Object getItem(int i) {
        return reports.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list,viewGroup,false);
        mag = view.findViewById(R.id.mag);
       place = view.findViewById(R.id.place);
       date = view.findViewById(R.id.date);
        String magV = new Double(reports.get(i).getMag()).toString();
        int magni = (int)reports.get(i).getMag();
       mag.setText(magV);
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();
        int magnitudeColor = getMagnitudeColor(reports.get(i).getMag());
        magnitudeCircle.setColor(magnitudeColor);
       place.setText(reports.get(i).getPlace());
       date.setText(reports.get(i).getDate());
        return view;
    }
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(context, magnitudeColorResourceId);
    }
}
