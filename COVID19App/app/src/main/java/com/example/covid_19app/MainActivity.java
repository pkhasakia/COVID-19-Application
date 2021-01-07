package com.example.covid_19app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.leo.simplearcloader.SimpleArcLoader;

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
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String result = "";

    TextView newCases, newDeaths, active, critical, totalCases, totalDeaths, totalRecovered, affectedCountries;

    HttpURLConnection httpURLConnection = null;

    //Loader from https://github.com/generic-leo/SimpleArcLoader/blob/master/README.md
    SimpleArcLoader simpleArcLoader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newCases = findViewById(R.id.newCases);
        newDeaths = findViewById(R.id.newDeaths);
        active = findViewById(R.id.active);
        critical = findViewById(R.id.critical);
        totalCases = findViewById(R.id.totalCases);
        totalDeaths = findViewById(R.id.totalDeaths);
        totalRecovered = findViewById(R.id.totalRecovered);
        affectedCountries = findViewById(R.id.affectedCountries);

        simpleArcLoader = findViewById(R.id.loader);

        //getData();

        new jsonTask().execute("https://corona.lmao.ninja/v2/all/");


    }



    //TRACK COUNTRIES BUTTON LEADS TO NEXT ACTIVITY
    public void trackCountries(View view) {
        //insert intent that leads to new activity
        Intent newPage = new Intent(this, MapsActivity.class);
        startActivity(newPage);
    }

    public void searchCountries(View view){
        Intent newPage = new Intent(this, SearchActivity.class);
        startActivity(newPage);
    }


    class jsonTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                //String url = "https://corona.lmao.ninja/v2/all/";
                URL myUrl = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) myUrl.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.connect();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder buffer = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();
                }

                //Log.e("Json", stringBuilder.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            //Log.e("RESULT", result);
            finally {
                httpURLConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String jsonString = s;
            try {
                JSONObject jsonObject = new JSONObject(s);
                String casesNew = jsonObject.getString("todayCases");
                String deathsNew = jsonObject.getString("todayDeaths");
                String activeCases = jsonObject.getString("active");
                String criticalCases = jsonObject.getString("critical");
                String sumCases = jsonObject.getString("cases");
                String sumDeaths = jsonObject.getString("deaths");
                String sumRecovered = jsonObject.getString("recovered");
                String countriesAffected = jsonObject.getString("affectedCountries");

                newCases.setText(casesNew);
                newDeaths.setText(deathsNew);
                active.setText(activeCases);
                critical.setText(criticalCases);
                totalCases.setText(sumCases);
                totalDeaths.setText(sumDeaths);
                totalRecovered.setText(sumRecovered);
                affectedCountries.setText(countriesAffected);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
