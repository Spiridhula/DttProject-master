package com.example.spirizguri.dttproject.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.spirizguri.dttproject.R;
import com.example.spirizguri.dttproject.Utils.InfoWindowCustom;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;


public class MapsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener
        ,OnMapReadyCallback {


    private GoogleMap mMap;

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 101;

    private Location myLocation;
    private double latitude = 0.0d, longitude = 0.0d;
    private Activity activity;
    public final static int TAG_PERMISSION_CODE = 1;
    Button btnback;
    private ReentrantLock addressObtainedLock;
    String addressString = "No address found";
    String routeString="Noroute found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        addressObtainedLock = new ReentrantLock();
        //Returning to the Main Activity

        Button btnback= (Button)findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
               startActivity( new Intent(MapsActivity.this, MainActivity.class));


            }
        });
         //Ask for Permision

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        fetchLastLocation();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


        private void fetchLastLocation () {

            @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                        Toast.makeText(MapsActivity.this, currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        supportMapFragment.getMapAsync(MapsActivity.this);
                    } else {
                        Toast.makeText(MapsActivity.this, "No Location recorded", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
        public void onMapReady (GoogleMap googleMap){
            mMap = googleMap;
            String latLongString;


            if (currentLocation!=null) {
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                double lat = currentLocation.getLatitude();
                double lng = currentLocation.getLongitude();
                latLongString = "Lat:" + lat + "\nLong:" + lng;
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> adresses = gc.getFromLocation(lat, lng, 1);
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb1= new StringBuilder();
                    if (adresses.size() > 0) {
                        Address address = adresses.get(0);

                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                            sb.append(address.getAddressLine(i)).append(" ");
                        sb.append(adresses.get(0).getFeatureName() + ", " + adresses.get(0).getLocality() +", " + adresses.get(0).getAdminArea() + ", " + adresses.get(0).getCountryName());

                        //sb.append(address.getCountryName());
                        //sb1.append(address.getLocality());

                    }
                    addressString = sb.toString();
                    //routeString=sb1.toString();

                    }
                 catch (IOException e) {}



                 MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                    .title(addressString)
                                     //.snippet(routeString)

                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                                    .visible(true);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


                            InfoWindowCustom infowindow = new InfoWindowCustom(MapsActivity.this);
                            googleMap.setInfoWindowAdapter(infowindow);
                            googleMap.addMarker(markerOptions).showInfoWindow();
                        }

                        // Add a marker in Sydney and move the camera
           // LatLng sydney = new LatLng(-34, 151);
           // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
           // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Toast.makeText(MapsActivity.this,"Location permission missing",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}

