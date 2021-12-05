package com.example.medned.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedDbHelper  extends SQLiteOpenHelper {
    public static final String LOG_TAG = MedDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;


    public  MedDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MEDS_TABLE = " CREATE TABLE " + MedContract.MedEntry.TABLE_NAME + " (" + MedContract.MedEntry.COLUMN_MED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ MedContract.MedEntry.COLUMN_MED_NAME + " TEXT NOT NULL, "+ MedContract.MedEntry.COLUMN_MED_GENRE + " TEXT NOT NULL, "+ MedContract.MedEntry.COLUMN_MED_TYPE + " INTEGER NOT NULL DEFAULT 0, "+ MedContract.MedEntry.COLUMN_MED_QUANTITY + " INTEGER NOT NULL DEFAULT 0); ";
        db.execSQL(SQL_CREATE_MEDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
