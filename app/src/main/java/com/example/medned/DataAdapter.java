package com.example.medned;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataAdapter  extends ArrayAdapter<CoronaData> {


    public DataAdapter(Context context, ArrayList<CoronaData> objects) {
        super(context,0, objects);
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {


        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.corona, parent, false);
        }
        CoronaData currentEvent = (CoronaData) getItem(position);




        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        dateView.setText( "Date " +currentEvent.getTimeInMilliseconds());

        TextView NegativeView = (TextView)listItemView.findViewById(R.id.negativeData);
        NegativeView.setText(currentEvent.getNegativeCase());

        TextView PositiveView = (TextView)listItemView.findViewById(R.id.positiveData);
        PositiveView.setText(currentEvent.getPositiveCaseName());

        TextView DeathView = (TextView)listItemView.findViewById(R.id.deathCount);
        DeathView.setText("Death  "+currentEvent.getDeathCase());













        return listItemView;
    }


    }


