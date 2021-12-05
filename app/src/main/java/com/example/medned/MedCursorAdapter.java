package com.example.medned;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.medned.data.MedContract;

public class  MedCursorAdapter extends CursorAdapter {

    public MedCursorAdapter(Context context,Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView)view.findViewById(R.id.medName);
        TextView aboutTextView =(TextView)view.findViewById(R.id.medAbout);
        TextView quantityTextView = (TextView)view.findViewById(R.id.medQuantity);

        String MedName = cursor.getString(cursor.getColumnIndexOrThrow(MedContract.MedEntry.COLUMN_MED_NAME));
        String MedAbout = cursor.getString(cursor.getColumnIndexOrThrow(MedContract.MedEntry.COLUMN_MED_GENRE));
        int MedQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(MedContract.MedEntry.COLUMN_MED_QUANTITY));

        nameTextView.setText(MedName);
        //aboutTextView.setText(MedAbout);
        quantityTextView.setText(String.valueOf(MedQuantity));
        if (TextUtils.isEmpty(MedAbout)){
            MedAbout = context.getString(R.string.unknown);
        }
        aboutTextView.setText(MedAbout);


    }
}
