package com.example.medned.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MedContract {
    public static final String CONTENT_AUTHORITY = "com.example.medned";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MEDs= "meds";

    private MedContract(){}

    public  static final class MedEntry implements BaseColumns{
        public static final String TABLE_NAME="meds";
        public static final String COLUMN_MED_ID= BaseColumns._ID;
        public  static final String COLUMN_MED_NAME = "name";
        public static  final String COLUMN_MED_GENRE = "genre";
        public static final  String COLUMN_MED_TYPE = "type";
        public static  final String COLUMN_MED_QUANTITY = "quantity";

        public static  final int TYPE_AVAILABLE = 1;
        public static  final int TYPE_UNKNOWN = 0;
        public static  final int TYPE_UNAVAILABLE = 2;

       public  static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_MEDs);

       public static  boolean isValidType( int type){
           if (type== TYPE_AVAILABLE || type== TYPE_UNAVAILABLE|| type==TYPE_UNKNOWN){
               return true;
           }
           return false;
       }
       public  static final String CONTENT_LIST_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+ CONTENT_AUTHORITY+"/"+PATH_MEDs;
       public static final String CONTENT_ITEM_TYPE= ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+ CONTENT_AUTHORITY+"/"+PATH_MEDs;

    }
}
