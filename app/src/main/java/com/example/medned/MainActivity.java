package com.example.medned;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private TextView mDataText;
    private static  final String STATIC_WEATHER_URL = ("https://api.covidtracking.com/dav1/us/daily.json");
    private DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ListView CoronaListView = (ListView) findViewById(R.id.main);
        mAdapter = new DataAdapter(MainActivity.this, new ArrayList<CoronaData>());
        CoronaListView.setAdapter(mAdapter);
        new DataTask().execute(STATIC_WEATHER_URL);

    }

    public class DataTask extends AsyncTask<String,Void,List<CoronaData>> {

        @Override
        protected List<CoronaData> doInBackground(String... strings) {
            if (strings.length<1 ||strings[0] == null){
                return null;
            }
            List<CoronaData> result = null;
            try {
                result = Networkutiles.fetchEarthQuakeData(strings);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;


        }

        @Override
        protected void onPostExecute(List<CoronaData> data) {
            mAdapter.clear();
           if (data!= null && !data.isEmpty()){
               mAdapter.addAll(data);
               }
           }
        }

        }





