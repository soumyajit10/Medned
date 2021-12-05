package com.example.medned;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.example.medned.data.MedContract;
import com.example.medned.data.MedDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private MedDbHelper medDbHelper;

    private static final int MED_LOADER = 0;
    MedCursorAdapter mMedCursorAdapter;
    private  TextView view ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




       view = (TextView) findViewById(R.id.covid);
       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ActionCall();
           }
       });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView)findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);


        medDbHelper = new MedDbHelper(this);

        mMedCursorAdapter = new MedCursorAdapter(this,null);
        listView.setAdapter(mMedCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
                Uri currentMed = ContentUris.withAppendedId(MedContract.MedEntry.CONTENT_URI,id);
                intent.setData(currentMed);
                startActivity(intent);
            }
        });



        getLoaderManager().initLoader(MED_LOADER,null,this);
    }
    private void ActionCall(){
        String number = "100";
        Intent u = new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",number,null));
        startActivity(u);
    }




  //  public  void  displayDatabaseInfo(){

    //  SQLiteDatabase db = medDbHelper.getReadableDatabase();

     //  String[] projection= {
       //        MedContract.MedEntry.COLUMN_MED_ID,
         //      MedContract.MedEntry.COLUMN_MED_NAME,
           //    MedContract.MedEntry.COLUMN_MED_GENRE,
             //  MedContract.MedEntry.COLUMN_MED_TYPE,
              // MedContract.MedEntry.COLUMN_MED_QUANTITY };

      // Cursor cursor =db.query(MedContract.MedEntry.TABLE_NAME,
        //      projection,null,null,null,null,null);

      // Cursor cursor = getContentResolver().query(MedContract.MedEntry.CONTENT_URI,projection,null,null,null);

       // TextView displayView = (TextView) findViewById(R.id.text_view_med);
        // try {

          //  displayView.setText("the medicine table contains " + cursor.getCount() +" meds.\n\n");
           // displayView.append(MedContract.MedEntry.COLUMN_MED_ID + " - "+ MedContract.MedEntry.COLUMN_MED_NAME+" - "+ MedContract.MedEntry.COLUMN_MED_GENRE+" - "+ MedContract.MedEntry.COLUMN_MED_TYPE+" - "+ MedContract.MedEntry.COLUMN_MED_QUANTITY+"\n");
           // int idColumnIndex = cursor.getColumnIndex(MedContract.MedEntry.COLUMN_MED_ID);
          //  int nameColumnIndex = cursor.getColumnIndex(MedContract.MedEntry.COLUMN_MED_NAME);
           // int genreColumnIndex = cursor.getColumnIndex(MedContract.MedEntry.COLUMN_MED_GENRE);
           // int typeColumnIndex = cursor.getColumnIndex(MedContract.MedEntry.COLUMN_MED_TYPE);
           // int quantityColumnIndex = cursor.getColumnIndex(MedContract.MedEntry.COLUMN_MED_QUANTITY);

        // while (cursor.moveToNext()){
             //   int currentId = cursor.getInt(idColumnIndex);
               // String currentName= cursor.getString(nameColumnIndex);
                //String currentGenre = cursor.getString(genreColumnIndex);
                //int currentType = cursor.getInt(typeColumnIndex);
               // int currentQuantity = cursor.getInt(quantityColumnIndex);

              //  displayView.append(("\n"+ currentId + " - "+currentName + " - " + currentGenre + " - " + currentType + " - " + currentQuantity ));
           // }
        //}finally {
          //  cursor.close();
        //}
      //  ListView listView =(ListView)findViewById(R.id.list);
        //MedCursorAdapter adapter = new MedCursorAdapter(this,cursor);
        //listView.setAdapter(adapter);

   // } this was for direct database calling




    private void  insertMeds(){
        //SQLiteDatabase db = medDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MedContract.MedEntry.COLUMN_MED_NAME, "pan 40");
        values.put(MedContract.MedEntry.COLUMN_MED_GENRE, "gastro");
        values.put(MedContract.MedEntry.COLUMN_MED_TYPE, MedContract.MedEntry.TYPE_AVAILABLE);
        values.put(MedContract.MedEntry.COLUMN_MED_QUANTITY, 8);


        Uri newUri =getContentResolver().insert(MedContract.MedEntry.CONTENT_URI,values);



       // long newRowId = db.insert(MedContract.MedEntry.TABLE_NAME,null,values);
        if (newUri !=null){
            Toast.makeText(this,"saved",Toast.LENGTH_SHORT).show();
        }
    }
    private void  deleteAllMeds(){
        int rowsDeleted= getContentResolver().delete(MedContract.MedEntry.CONTENT_URI,null,null);
        Log.e("CatalogActivity", "rows deleted"+ rowsDeleted);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertMeds();


                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllMeds();
                return true;
            case R.id.coronaDta:
                Intent webSiteIntent = new Intent(CatalogActivity.this,MainActivity.class);
                startActivity(webSiteIntent);
                return true;
            case R.id.orderMeds:
                String address = "Lenin Sarani,Serampore,Dakshin Rajyadharpur,West bengal 712201";
                Intent location = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+ address));
                if (location.resolveActivity(getPackageManager())!= null){
                    Toast.makeText(this,"opening maps",Toast.LENGTH_SHORT).show();
                    startActivity(location);
                }else {
                    Toast.makeText(CatalogActivity.this,"failed",Toast.LENGTH_SHORT).show();
                }
            case  R.id.share:
                Intent shareIntent = ShareCompat.IntentBuilder.from(this).setType("text/italic").setText("see ur medicine list").getIntent();
                startActivity(shareIntent);

            }


        return super.onOptionsItemSelected(item);

    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {MedContract.MedEntry.COLUMN_MED_ID, MedContract.MedEntry.COLUMN_MED_NAME, MedContract.MedEntry.COLUMN_MED_GENRE, MedContract.MedEntry.COLUMN_MED_QUANTITY};

        return new CursorLoader(this,MedContract.MedEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished( Loader<Cursor> loader, Cursor cursor) {
        mMedCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset( Loader<Cursor> loader) {
        mMedCursorAdapter.swapCursor(null);

    }
}







