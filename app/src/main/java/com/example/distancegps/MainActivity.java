package com.example.distancegps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    //Making variables to gps features
    private LocationManager locationManager;
    private LocationListener locationListener;

    private int buttonTaps = 0;

    //UI elements
    private TextView textViewStart;
    private TextView textViewEnd;
    private TextView textViewResult;
    private TextView textViewAltitude;
    private Button getGPS;
    private TextView textViewAccuracy;

    //changing variable with gps coordinates
    private double longitude;
    private double latitude;
    private double altitude;
    private float accuracy;

    //Making new object for starting values
    private Coordinates startCoordinates = new Coordinates();

    //Making new object for ending values
    private Coordinates endCoordinates = new Coordinates();

    //Permission table what needed to check before use
    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing UI elements
        textViewStart = (TextView) findViewById(R.id.textViewStart);
        textViewEnd = (TextView) findViewById(R.id.textViewEnd);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        textViewAccuracy = (TextView) findViewById(R.id.textViewAccuracy);

        getGPS = (Button) findViewById(R.id.button_getGPS);

        textViewAltitude = (TextView) findViewById(R.id.textViewAltitude);


        //Intent intent = new Intent(this, Coordinates.class);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //location = new Location("");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude(); //in degrees
                latitude = location.getLatitude(); //in degrees
                altitude = location.getAltitude(); //in meters
                accuracy = location.getAccuracy(); //estimated horizontal accuracy of this location, radial, in meters
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 66);
            }
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }


    /**
     * Adding feature when Android back button is pressed it removes gps listener when exiting from
     * the application
     */
    @Override
    public void onBackPressed() {
        //Remove the listener you previously added
        locationManager.removeUpdates(locationListener);
        //normal back button action
        super.onBackPressed();
    }


    //Method when pressing getGPS button
    //1st press gets starting location gps
    //2nd press gets ending point location, stops listening gps and calculates distance between
    //  starting and ending point
    //3rd press resets application
    public void getGPS(View view) {

        if (buttonTaps < 2) {

            if (buttonTaps == 0) {
                textViewStart.setText("Latitude \n" + String.format("%.5f",latitude) + "\n" + "Longitude\n" + String.format("%.5f",longitude));
                //textViewStart.setText(""+location.getLongitude() + location.getLatitude());
                // textViewStart.setText(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).toString());

                textViewAltitude.setText("Start altitude\n" + altitude + " m\n");
                textViewAccuracy.setText("Start accuracy\n" + String.format("%.2f",accuracy) + " m\n");

                //Saving starting point coordinates
                startCoordinates.setLongitude(longitude);
                startCoordinates.setLatitude(latitude);
                startCoordinates.setAltitude(altitude);
                startCoordinates.setAccuracy(accuracy);
            }

            if (buttonTaps == 1) {
                textViewEnd.setText("Latitude \n" + String.format("%.5f",latitude) + "\n" + "Longitude\n" + String.format("%.5f",longitude));
                //textViewEnd.setText(""+location.getLongitude() + location.getLatitude());
                //textViewEnd.setText(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).toString());

                textViewAltitude.setText("Start altitude\n" + String.format("%.2f", startCoordinates.getAltitude()) + " m\n" + "End altitude\n" + String.format("%.2f",altitude) + " m\n" + "Difference\n" + String.format("%.2f",altitude-startCoordinates.getAltitude()) + " m");
                textViewAccuracy.setText("Start accuracy\n" + String.format("%.2f",startCoordinates.getAccuracy()) + " m\n" + "End accuracy\n" + String.format("%.2f",accuracy) + " m");

                //Saving end point coordinates
                endCoordinates.setLongitude(longitude);
                endCoordinates.setLatitude(latitude);
                endCoordinates.setAltitude(altitude);
                endCoordinates.setAccuracy(accuracy);

                //Showing final result distance in meters between coordinates
                textViewResult.setText("Haversine \n" + String.format("%.4f", Coordinates.getDistanceHav(startCoordinates.getLatitude(), startCoordinates.getLongitude(), endCoordinates.getLatitude(), endCoordinates.getLongitude())) + " m"
                        + "\n Pythagoras 2D\n" + String.format("%.4f", Coordinates.getDistancePyth2D(startCoordinates.getLatitude(), startCoordinates.getLongitude(), endCoordinates.getLatitude(), endCoordinates.getLongitude())) + " m"
                        + "\n Pythagoras 3D\n" + String.format("%.4f", Coordinates.getDistancePyth3D(startCoordinates.getLatitude(), startCoordinates.getLongitude(), startCoordinates.getAltitude(), endCoordinates.getLatitude(), endCoordinates.getLongitude(), endCoordinates.getAltitude())) + " m"
                        + "\n Tunnel distance\n" + String.format("%.4f", Coordinates.getTunnelDistance(startCoordinates.getLatitude(), startCoordinates.getLongitude(), endCoordinates.getLatitude(), endCoordinates.getLongitude())) + " m"
                );


                //This is just testing purpose to be sure that application works with some test values and agrees some pages results as well
                //Testing that haversine method works right
                //  http://andrew.hedges.name/experiments/haversine/
                //  http://www.movable-type.co.uk/scripts/latlong.html
                //  https://rechneronline.de/geo-coordinates/#conversion
                //textViewResult.setText(""+Coordinates.getDistanceHav(38.898556, -77.037852, 38.897147, -77.043934)+" m");
                //0.549 km
                //Tampa to Miami example purpose for screenshot
                //textViewResult.setText(""+Coordinates.getDistanceHav(27.950142, -82.457723, 25.763531, -80.193412)+" m");
                //331 km
                getGPS.setText("Reset");

            }

            buttonTaps += 1;

        } else {
            buttonTaps = 0;
            //textViewAltitude.setText(this.getString(R.string.textViewAltitude));
            textViewAltitude.setText(this.getString(R.string.textViewAltitude));
            getGPS.setText(this.getString(R.string.button_getGPS));
            textViewStart.setText(this.getString(R.string.textViewStart));
            textViewEnd.setText(this.getString(R.string.textViewEnd));
            textViewResult.setText(this.getString(R.string.textViewResult));
            textViewAccuracy.setText(this.getString(R.string.textViewAccuracy));

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 66:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
        }
    }


}



