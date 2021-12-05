package com.example.medned;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Networkutiles {
    private static final String STATIC_WEATHER_URL ="https://api.covidtracking.com/v1/us/daily.json";


    public  static URL buildUrl(){
        Uri buildUri = Uri.parse(STATIC_WEATHER_URL).buildUpon().build();
        URL url= null;
        try {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            if (urlConnection.getResponseCode() == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                return line;
            } }finally{
                urlConnection.disconnect();
            }


        return jsonResponse;
    }


    public static List<CoronaData> getSimpleWeatherStringFromJson( String forecastJsonStr) throws JSONException {

        if (TextUtils.isEmpty(forecastJsonStr)){
            return null;
        }

        List<CoronaData> CovidQuakes= new ArrayList<>();

       // String[] parsedWeatherData = null;
        JSONArray jsonArray = new JSONArray(forecastJsonStr);
       // parsedWeatherData = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentDate = jsonArray.getJSONObject(i);
            String date = currentDate.getString("date");
            String positiveCase = currentDate.getString("positive");
            String negativeCase = currentDate.getString("negative");
            String deathCase = currentDate.getString("death");
           // parsedWeatherData[i] = date + "-" + positiveCase + "-" + negativeCase + "-" + deathCase;
                    CoronaData earthquake = new CoronaData(positiveCase,negativeCase,date,deathCase);
            CovidQuakes.add(earthquake);
        }
        return  CovidQuakes;


    }
    public static List<CoronaData> fetchEarthQuakeData(String[] requestUrl) throws JSONException {
        URL url = buildUrl();
        String jsonResponse = null;
        try {
            jsonResponse = Networkutiles.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
       List<CoronaData> earthquakes = Networkutiles.getSimpleWeatherStringFromJson(jsonResponse);
        return earthquakes;

    }

}
