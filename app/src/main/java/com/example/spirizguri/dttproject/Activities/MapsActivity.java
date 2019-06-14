package com.example.spirizguri.dttproject.Activities;

import android.Manifest;
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
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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






    String addressString = "No address found";

    private RelativeLayout callPopUp;
    private Button buttonCallNow;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        callPopUp = findViewById(R.id.call_popop);
        buttonCallNow = findViewById(R.id.callbtn);
        //Returning to the Main Activity

        btnBack= (Button)findViewById(R.id.btnback);
        btnBack.setOnClickListener(new View.OnClickListener() {
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
                        //Toast.makeText(MapsActivity.this, currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
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

            //Adding marker to the current location,using geocoder to get your address
            mMap = googleMap;
            ImageView locObtaining;

            float zoomLevel = (float) 18.0;


            if (currentLocation!=null) {
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                double lat = currentLocation.getLatitude();
                double lng = currentLocation.getLongitude();

                Geocoder gc = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> adresses = gc.getFromLocation(lat, lng, 1);
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb1= new StringBuilder();
                    if (adresses.size() > 0) {
                        Address address = adresses.get(0);

                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                            sb.append(address.getAddressLine(i)).append(" ");
                        sb.append(adresses.get(0).getAddressLine(0) + ", " + adresses.get(0).getCountryName());



                    }
                    addressString = sb.toString();


                    }
                 catch (IOException e) {}



                 MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                    .title(addressString)


                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                                    .visible(true);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));


                            InfoWindowCustom infowindow = new InfoWindowCustom(MapsActivity.this);
                            googleMap.setInfoWindowAdapter(infowindow);
                            googleMap.addMarker(markerOptions).showInfoWindow();
                        }



            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            locObtaining= findViewById(R.id.location_obtaining);
            locObtaining.setVisibility(View.GONE);


        }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(MapsActivity.this, "Location permission missing", Toast.LENGTH_SHORT).show();
                }
                break;
                //Ask to allow the app to make phone calls

            case 2: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:0123456789"));
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    public void btnCallNowClick(View view) {
       callPopUp.setVisibility(View.VISIBLE);
        buttonCallNow.setVisibility(View.GONE);
    }

    public void btnPopupCloseClick(View view) {
        callPopUp.setVisibility(View.GONE);
        buttonCallNow.setVisibility(View.VISIBLE);
    }
    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }



    public void DialClick(View view){

            //After granting the permission,clicking again on the bel nu button will allow us to make a call.

        if(isPermissionGranted()){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0123456789"));
            startActivity(intent);
        }
    }







}

