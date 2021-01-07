package com.example.covid_19app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {
    Button search, show;
    EditText countryInput;

    private String country;
    private String newCases;
    private String newDeaths;
    private String active;
    private String critical;
    private String totalCases;
    private String totalDeaths;
    private String totalRecovered;
    MySQLiteHelper mySQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mySQLiteHelper = new MySQLiteHelper(this, null, null, 4);


        search = (Button) findViewById(R.id.btnSearch2);
        show = (Button) findViewById(R.id.btnShow);
        countryInput = (EditText) findViewById(R.id.txtSearch);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                country = countryInput.getText().toString();
                jsonParse(country);

            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                populateTable(country, newCases, newDeaths, active, critical, totalCases, totalDeaths, totalRecovered);
                Intent i = new Intent(SearchActivity.this, PopUpWindowActivity.class);
                i.putExtra("cases",newCases);
                i.putExtra("deaths",newDeaths);
                i.putExtra("active",active);
                i.putExtra("critical",critical);
                i.putExtra("totalCases",totalCases);
                i.putExtra("totalDeaths",totalDeaths);
                i.putExtra("totalRecovered",totalRecovered);
                i.putExtra("country", country);
                startActivity(i);
            }
        });
    }

    public void jsonParse(String country) {
        String url = "https://corona.lmao.ninja/v2/countries/"+country;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            newCases = jsonObject.getString("todayCases");
                            newDeaths = jsonObject.getString("todayDeaths");
                            active = jsonObject.getString("active");
                            critical = jsonObject.getString("critical");
                            totalCases = jsonObject.getString("cases");
                            totalDeaths = jsonObject.getString("deaths");
                            totalRecovered = jsonObject.getString("recovered");

                            Toast.makeText(SearchActivity.this, "Data Retrieved", Toast.LENGTH_LONG).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Data not recieved", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);

    }
    public void populateTable(String country, String newCases, String newDeaths, String active, String critical,
                              String totalCases, String totalDeaths, String totalRecovered){
        boolean status =  mySQLiteHelper.addRecord(country, newCases, newDeaths, active, critical, totalCases,
                                                                totalDeaths, totalRecovered);

    }
}