package com.example.covid_19app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopUpWindowActivity extends AppCompatActivity {

    TextView newCases, newDeaths, active, critical, totalCases, totalDeaths, totalRecovered, countryName;
    String cases, deathss, actives, criticals, totalCasess, totalDeathss, totalRecovereds, cName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window);

        newCases = (TextView) findViewById(R.id.newCasesCountry);
        newDeaths = (TextView) findViewById(R.id.newDeathsCountry);
        active = (TextView) findViewById(R.id.activeCountry);
        critical = (TextView) findViewById(R.id.criticalCountry);
        totalCases = (TextView) findViewById(R.id.totalCasesCountry);
        totalDeaths = (TextView) findViewById(R.id.totalDeathsCountry);
        totalRecovered = (TextView) findViewById(R.id.totalRecoveredCountry);
        countryName = (TextView) findViewById(R.id.countryName);

        Intent intent = getIntent();
        cases = intent.getStringExtra("cases");
        deathss = intent.getStringExtra("deaths");
        actives = intent.getStringExtra("active");
        criticals = intent.getStringExtra("critical");
        totalCasess = intent.getStringExtra("totalCases");
        totalDeathss = intent.getStringExtra("totalDeaths");
        totalRecovereds = intent.getStringExtra("totalRecovered");
        cName = intent.getStringExtra("country");
        cName = cName+(" Stats");

        newCases.setText(cases);
        newDeaths.setText(deathss);
        active.setText(actives);
        critical.setText(criticals);
        totalCases.setText(totalCasess);
        totalDeaths.setText(totalDeathss);
        totalRecovered.setText(totalRecovereds);
        countryName.setText(cName);
    }
}