package com.example.medned;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.LoaderManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import android.content.CursorLoader;
import android.content.Loader;


import com.example.medned.data.MedContract;
import com.example.medned.data.MedDbHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;
    private Uri mCurrentUri;
    private static final int EXISTING_MED_LOADER = 0;



    private int mGender = MedContract.MedEntry.TYPE_UNKNOWN;
    private  boolean mMedsHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mMedsHasChanged = true;
            return false;
        }
    };
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
      mCurrentUri=intent.getData();
        if (mCurrentUri==null){
            setTitle("Add New");
            invalidateOptionsMenu();
        }else{
            setTitle(getString(R.string.edit_pet));
            getLoaderManager().initLoader(EXISTING_MED_LOADER,null,this);

        }

        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText)  findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText)findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner)findViewById(R.id.spinner_gender);

        mNameEditText.setOnTouchListener(mTouchListener);
        mBreedEditText.setOnTouchListener(mTouchListener);
        mWeightEditText.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);
        setupSpinner();




    }


    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.med_available))) {
                        mGender = MedContract.MedEntry.TYPE_AVAILABLE; // Male
                    } else if (selection.equals(getString(R.string.med_unavailable))) {
                        mGender = MedContract.MedEntry.TYPE_UNAVAILABLE; // Female
                    } else {
                        mGender = MedContract.MedEntry.TYPE_UNKNOWN; // Unknown
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender= MedContract.MedEntry.TYPE_UNKNOWN;
            }


        });
    }
    private void saveMeds(){
       String nameString=  mNameEditText.getText().toString().trim();
       String genreString= mBreedEditText.getText().toString().trim();
       String quantityString = mWeightEditText.getText().toString().trim();


       if (mCurrentUri== null && TextUtils.isEmpty(nameString) || TextUtils.isEmpty(quantityString) ||mGender == MedContract.MedEntry.TYPE_UNKNOWN){
           return;
       }

        //MedDbHelper medDbHelper = new MedDbHelper(this);
       // SQLiteDatabase db = medDbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(MedContract.MedEntry.COLUMN_MED_NAME, nameString);
        values.put(MedContract.MedEntry.COLUMN_MED_GENRE,genreString);
        values.put(MedContract.MedEntry.COLUMN_MED_TYPE,mGender);
        int quantity= 0;
        if (!TextUtils.isEmpty(quantityString)){
            quantity= Integer.parseInt(quantityString);
        }
        values.put(MedContract.MedEntry.COLUMN_MED_QUANTITY,quantity);
        if (mCurrentUri==null) {

          Uri newUri =
                    getContentResolver().insert(MedContract.MedEntry.CONTENT_URI, values);
            // long newRowId = db.insert(MedContract.MedEntry.TABLE_NAME,null,values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.facing_error), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.successfully_done), Toast.LENGTH_SHORT).show();
            }

        }
        else {
            int rowUpdate =
                    getContentResolver().update(mCurrentUri,values,null,null);
           if (rowUpdate==0){
                Toast.makeText(this, getString(R.string.update_declined), Toast.LENGTH_SHORT).show();
            }
        else {
                Toast.makeText(this, getString(R.string.update_successful), Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        if (mCurrentUri==null){
            MenuItem menuItem= menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save:
                saveMeds();
                finish();

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConformationDialog();


                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!mMedsHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConformationDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMed();

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (dialog!= null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }



    @Override
    public void onBackPressed() {
        if (!mMedsHasChanged){
        super.onBackPressed();
        return;
        }
        DialogInterface.OnClickListener discardButtonClickListener= new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();
            }
        };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
      if (mCurrentUri== null){
            return null;
        }

        String[] projection = {MedContract.MedEntry.COLUMN_MED_ID, MedContract.MedEntry.COLUMN_MED_NAME, MedContract.MedEntry.COLUMN_MED_GENRE, MedContract.MedEntry.COLUMN_MED_TYPE,MedContract.MedEntry.COLUMN_MED_QUANTITY};
        return new CursorLoader(this,mCurrentUri,projection,null,null,null);

    }

    @Override
    public void onLoadFinished( Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() <1) {
            return;
        }
        if (cursor.moveToFirst()){
            int nameColumnIndex = cursor.getColumnIndex(MedContract.MedEntry.COLUMN_MED_NAME);
            int genreColumnIndex = cursor.getColumnIndex(MedContract.MedEntry.COLUMN_MED_GENRE);
             int typeColumnIndex = cursor.getColumnIndex(MedContract.MedEntry.COLUMN_MED_TYPE);
             int quantityColumnIndex = cursor.getColumnIndex(MedContract.MedEntry.COLUMN_MED_QUANTITY);



            String currentName= cursor.getString(nameColumnIndex);
            String currentGenre = cursor.getString(genreColumnIndex);
            int currentType = cursor.getInt(typeColumnIndex);
             int currentQuantity = cursor.getInt(quantityColumnIndex);

             mNameEditText.setText(currentName);
             mBreedEditText.setText(currentGenre);
             mWeightEditText.setText(Integer.toString(currentQuantity));
             mGenderSpinner.setSelection(currentType);

        }

    }




    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    mNameEditText.setText("");
    mBreedEditText.setText("");
    mGenderSpinner.setSelection(0);
    mWeightEditText.setText("");
    }
    private  void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard,discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog!= null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }

    private void deleteMed(){
        if (mCurrentUri!=null){
            getContentResolver().delete(mCurrentUri,null,null);
            Toast.makeText(this, getString(R.string.editor_delete_successful), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, getString(R.string.editor_delete_failed), Toast.LENGTH_SHORT).show();
            }

        finish();
    }
}