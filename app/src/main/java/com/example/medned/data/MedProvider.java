package com.example.medned.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.medned.R;



public class MedProvider extends ContentProvider {

    public static final String LOG_TAG = MedProvider.class.getSimpleName();

    private MedDbHelper medDbHelper;

    private static final int MED = 401;
    private static final int MED_ID= 402;

    private static final UriMatcher urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {

            urimatcher.addURI(MedContract.CONTENT_AUTHORITY,MedContract.PATH_MEDs,MED);
            urimatcher.addURI(MedContract.CONTENT_AUTHORITY,MedContract.PATH_MEDs + "/#",MED_ID);
    }



    @Override
    public boolean onCreate() {
        medDbHelper =  new MedDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection, String[] selectionArgs,  String sortOrder) {
        SQLiteDatabase database = medDbHelper.getReadableDatabase();
        Cursor cursor;
        int match =urimatcher.match(uri);
        switch (match){
            case MED:
                cursor = database.query(MedContract.MedEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MED_ID:
                selection= MedContract.MedEntry.COLUMN_MED_ID +"=?";
                selectionArgs =new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MedContract.MedEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("cant query unknown URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }



    @Override
    public String getType( Uri uri) {
        final int match = urimatcher.match(uri);
        switch (match){
            case MED:
                return MedContract.MedEntry.CONTENT_LIST_TYPE;
            case MED_ID:
                return MedContract.MedEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("unknown uri"+ uri+"with match"+ match);
        }


    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = urimatcher.match(uri);
        switch (match){
            case MED:
                return insertMed(uri,values);
            default:
                throw new IllegalArgumentException("insertion is not supported"+ uri);
        }

    }
    private Uri insertMed(Uri uri, ContentValues values){

        String name =  values.getAsString(MedContract.MedEntry.COLUMN_MED_NAME);
        if (TextUtils.isEmpty(name)){
            return null;
           // throw new IllegalArgumentException("medicine requires a name");
        }

        Integer type = values.getAsInteger(MedContract.MedEntry.COLUMN_MED_TYPE);
        if (type== null || !MedContract.MedEntry.isValidType(type)){
            throw new IllegalArgumentException("select medicine type");
        }

        Integer quantity = values.getAsInteger(MedContract.MedEntry.COLUMN_MED_QUANTITY);
        if (quantity != null && quantity < 0){
            throw new IllegalArgumentException(" enter a valid quantity");
        }

        SQLiteDatabase database = medDbHelper.getWritableDatabase();

       long id =  database.insert(MedContract.MedEntry.TABLE_NAME,null,values);
       if (id==-1){
           Log.e(LOG_TAG,"failed to insert row" + uri);
           return null;
       }
       getContext().getContentResolver().notifyChange(uri,null);
       return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = medDbHelper.getWritableDatabase();
        int rowsDelete;

        final int match = urimatcher.match(uri);
        switch (match){
            case MED:
                 rowsDelete = database.delete(MedContract.MedEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case MED_ID:
                selection= MedContract.MedEntry.COLUMN_MED_ID +"=?";
                selectionArgs =new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDelete = database.delete(MedContract.MedEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("problem in deleting"+ uri);
        }
        if (rowsDelete!=0){
            getContext().getContentResolver().notifyChange(uri,null);

        }
        return rowsDelete;


    }


    @Override
    public int update(Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        final  int match = urimatcher.match(uri);
        switch (match){
            case MED:
                return updateMed(uri,values,selection,selectionArgs);
            case MED_ID:
                selection= MedContract.MedEntry.COLUMN_MED_ID +"=?";
                selectionArgs =new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateMed(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("update failed"+ uri);

        }

    }
    private int updateMed(Uri uri ,ContentValues values, String selection,String[] selectionArgs){


        if (values.containsKey(MedContract.MedEntry.COLUMN_MED_NAME)){
            String name = values.getAsString(MedContract.MedEntry.COLUMN_MED_NAME);
            if (TextUtils.isEmpty(name)){
                throw new IllegalArgumentException("medicine requires name");
            }
        }

        if (values.containsKey(MedContract.MedEntry.COLUMN_MED_TYPE)){
            Integer type = values.getAsInteger(MedContract.MedEntry.COLUMN_MED_TYPE);
            if (type== null || !MedContract.MedEntry.isValidType(type)){
                throw new IllegalArgumentException("select medicine type");
            }
        }
        if (values.containsKey(MedContract.MedEntry.COLUMN_MED_QUANTITY)){
            Integer quantity = values.getAsInteger(MedContract.MedEntry.COLUMN_MED_QUANTITY);
            if (quantity != null && quantity<0){
                throw new IllegalArgumentException(" enter a valid quantity");
            }
        }
        if (values.size() == 0){
            return 0;
        }



        SQLiteDatabase database= medDbHelper.getWritableDatabase();
        int rowsUpdate = database.update(MedContract.MedEntry.TABLE_NAME,values,selection,selectionArgs);
        if (rowsUpdate!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdate;

    }


}
