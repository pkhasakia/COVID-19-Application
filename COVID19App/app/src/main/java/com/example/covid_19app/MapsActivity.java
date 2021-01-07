package com.example.covid_19app;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Marker m;
    Double lat, lon;
    String country;
    String cases, deaths, active, critical, totalCases, totalDeaths, totalRecovered;
    Button GetInfo;
    private GoogleMap mMap;

    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        GetInfo = (Button) findViewById(R.id.BtnGetInfo);
        GetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, PopUpWindowActivity.class);
                i.putExtra("cases",cases);
                i.putExtra("deaths",deaths);
                i.putExtra("active",active);
                i.putExtra("critical",critical);
                i.putExtra("totalCases",totalCases);
                i.putExtra("totalDeaths",totalDeaths);
                i.putExtra("totalRecovered",totalRecovered);
                i.putExtra("country", country);
                startActivity(i);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (m == null) {
                    m = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                } else {
                    m.setPosition(latLng);
                }
                lat = (latLng.latitude);
                lon = (latLng.longitude);



                getAddress(lat, lon);

                jsonParse();

                //to animate map and zoom
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            }


        });
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            country = addresses.get(0).getCountryName();

        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        return country;
    }

    public void jsonParse() {
        String url = "https://corona.lmao.ninja/v2/countries/"+country;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            cases = jsonObject.getString("todayCases");
                            deaths = jsonObject.getString("todayDeaths");
                            active = jsonObject.getString("active");
                            critical = jsonObject.getString("critical");
                            totalCases = jsonObject.getString("cases");
                            totalDeaths = jsonObject.getString("deaths");
                            totalRecovered = jsonObject.getString("recovered");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, "Data not recieved", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);

    }
}
