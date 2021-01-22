package com.hemontosoftware.pandemichealthkit.view;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.uitlity.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


public class HospitalMapsActivity extends AppCompatActivity {

    double currentLat = 0.0d;
    double currentLong = 0.0d;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap map;
    SupportMapFragment supportMapFragment;

    FloatingActionButton fab;


    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        private ParserTask() {
        }

        /* access modifiers changed from: protected */
        public List<HashMap<String, String>> doInBackground(String... strings) {
            try {
                return new JsonParser().parseResult(new JSONObject(strings[0]));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<HashMap<String, String>> hashMaps) {
            HospitalMapsActivity.this.map.clear();
            for (int i = 0; i < hashMaps.size(); i++) {
                HashMap<String, String> hashMapList = (HashMap) hashMaps.get(i);
                String name = (String) hashMapList.get("name");
                LatLng latLng = new LatLng(Double.parseDouble((String) hashMapList.get("lat")), Double.parseDouble((String) hashMapList.get("lng")));
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);
                HospitalMapsActivity.this.map.addMarker(options);
            }
        }
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {
        private PlaceTask() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... strings) {
            try {
                return HospitalMapsActivity.this.downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String s) {
            new ParserTask().execute(new String[]{s});
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_maps);
        Toolbar toolbar = findViewById(R.id.maps_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        String str = "android.permission.ACCESS_FINE_LOCATION";
        fab = findViewById(R.id.floating_action_button);

        if (ActivityCompat.checkSelfPermission(this, str) == 0) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{str}, 44);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                sb.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
                sb.append(HospitalMapsActivity.this.currentLat);
                sb.append(",");
                sb.append(HospitalMapsActivity.this.currentLong);
                sb.append("&radius=5000&type=");
                sb.append("shopping");
                sb.append("&sensor=true&key=");
                sb.append(HospitalMapsActivity.this.getResources().getString(R.string.google_map_key));
                String url = sb.toString();
                new PlaceTask().execute(new String[]{url});
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            public void onSuccess(Location location) {
                if (location != null) {
                    HospitalMapsActivity.this.currentLat = location.getLatitude();
                    HospitalMapsActivity.this.currentLong = location.getLongitude();
                    HospitalMapsActivity.this.supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        public void onMapReady(GoogleMap googleMap) {
                            HospitalMapsActivity.this.map = googleMap;
                            HospitalMapsActivity.this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(HospitalMapsActivity.this.currentLat, HospitalMapsActivity.this.currentLong), 14.0f));
                        }
                    });
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 44 && grantResults.length > 0 && grantResults[0] == 0) {
            getCurrentLocation();
        }
    }

    /* access modifiers changed from: private */
    public String downloadUrl(String string) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(string).openConnection();
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String str = BuildConfig.FLAVOR;
        while (true) {
            String readLine = reader.readLine();
            String line = readLine;
            if (readLine != null) {
                builder.append(line);
            } else {
                String data = builder.toString();
                reader.close();
                return data;
            }
        }
    }


}