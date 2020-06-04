package com.example.simon.busprototyp;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.simon.busprototyp.service.BusApiCall;
import com.example.simon.busprototyp.service.BusDataSet;
import com.example.simon.busprototyp.service.BusStop;
import com.example.simon.busprototyp.service.BusStopResponse;
import com.example.simon.busprototyp.service.DataResponse;
import com.example.simon.busprototyp.service.TimeTableResponse;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    //Buttons
    private Button zustiegsbutton;
    private Button ausstiegsbutton;
    private Button staubutton;
    private Button ausfallbutton;
    private Button settingsbutton;
    //private Button nextButton;
    private Button leerbutton;

    //TextViews
    private TextView textView;
    private TextView textView2;
    private TextView nextStopView;
    private TextView textView4;

    //Location
    private LocationManager locationManager;
    private LocationListener locationListener;
    private OrientationEventListener orientationListener;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private int zugestiegene;
    private int ausgestiegene;
    private int fahrgaeste;
    private int busid;
    private int linienid;
    private int f;
    private int stopCounter;

    private double distance;
    private double nextLon;
    private double nextLat;

    private String outmsg;
    private String inmsg;
    private String status;
    private String nextStopString;
    private String[] plan;
    private String[] ansplit;
    private String[] absplit;

    private TimeTableResponse timeTable;
    private BusStop nextStop;
    private BusApiCall apiConnection;

    private boolean stau;
    private boolean ausfall;
    private boolean initreq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        System.out.println("Main activated");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    sendDataSet(location);
                }
            }
        };

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this, 0x1);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

        apiConnection = new BusApiCall("http://192.168.178.24:8080/rbi-api", this.getApplicationContext());

        zustiegsbutton = (Button) findViewById(R.id.zustiegsbutton);
        ausstiegsbutton = (Button) findViewById(R.id.ausstiegsbutton);
        staubutton = (Button) findViewById(R.id.staubutton);
        ausfallbutton = (Button) findViewById(R.id.ausfallbutton);
        settingsbutton = (Button) findViewById(R.id.settingsbutton);
        //nextButton = (Button) findViewById(R.id.nextbutton);
        leerbutton = (Button) findViewById(R.id.leerbutton);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        //textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        nextStopView = (TextView) findViewById(R.id.nextStop);


        Intent i = getIntent();
        busid = i.getIntExtra("busid", 1);
        timeTable = (TimeTableResponse) i.getSerializableExtra("linie");
        stopCounter = i.getIntExtra("planpos", 0);

        zugestiegene = 0;
        ausgestiegene = 0;
        fahrgaeste = 0;

        /*if(savedInstanceState != null){
            fahrgaeste = savedInstanceState.getInt("fahrgaeste");
        }*/

        f = 0;
        status = "keine Vorkommnisse";
        stau = false;
        ausfall = false;

        distance = 1000000;

        nextStop = timeTable.getStops().get(stopCounter).getBusStop();
        nextStopString = nextStop.getName() + zeiten(stopCounter);
        nextStopView.setText(nextStopString);
        nextLon = nextStop.getLaengengrad();
        nextLat = nextStop.getBreitengrad();


        staubutton.setBackgroundColor(0xffcccccc);
        ausfallbutton.setBackgroundColor(0xffcccccc);
        zustiegsbutton.setBackgroundColor(0xffaaff00);
        ausstiegsbutton.setBackgroundColor(0xffff8833);

        zustiegsbutton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    zustiegsbutton.setBackgroundColor(0xffddff00);
                    zugestiegene += 1;
                    fahrgaeste = zugestiegene - ausgestiegene;
                    textView2.setText(fahrgaeste + "");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // set to normal color
                    zustiegsbutton.setBackgroundColor(0xffaaff00);
                }

                return true;
            }
        });

        ausstiegsbutton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // change color
                    ausstiegsbutton.setBackgroundColor(0xffffaa33);
                    if (fahrgaeste != 0) {
                        ausgestiegene += 1;
                        fahrgaeste = zugestiegene - ausgestiegene;
                        textView2.setText(fahrgaeste + "");
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // set to normal color
                    ausstiegsbutton.setBackgroundColor(0xffff8833);
                }

                return true;
            }
        });


        leerbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ausgestiegene = zugestiegene;
                fahrgaeste = 0;
                textView2.setText(fahrgaeste + "");
            }
        });


        staubutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (stau && !ausfall) {
                    status = "Keine Vorkommnisse";
                    stau = false;
                    staubutton.setBackgroundColor(0xffcccccc);
                } else if (!ausfall) {
                    status = "Steht im Stau";
                    stau = true;
                    staubutton.setBackgroundColor(0xffffcccc);
                }
                // to do: highlight button
            }
        });

        ausfallbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ausfall && !stau) {
                    status = "Keine Vorkommnisse";
                    ausfall = false;
                    ausfallbutton.setBackgroundColor(0xffcccccc);
                } else if (ausfall & stau) {
                    status = "Steht im Stau";
                    ausfall = false;
                    ausfallbutton.setBackgroundColor(0xffcccccc);

                } else {
                    status = "Ausfall!";
                    ausfall = true;
                    ausfallbutton.setBackgroundColor(0xffffcccc);
                    staubutton.setBackgroundColor(0xffcccccc);
                }

            }
        });


        settingsbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orientationListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (true) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        };


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                sendDataSet(location);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.WAKE_LOCK}, 10);
                return;
            }
            Log.i("updateLocation","if");
            updateLocation();
        } else {
            Log.i("updateLocation","else");
            updateLocation();
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            sendDataSet(location);
                        } else{
                            Log.i("updateLocation", "location is null");
                        }
                    }
                });



    }

    @Override
    protected void onDestroy() {
        locationManager.removeUpdates(locationListener);
        locationManager = null;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        locationManager.removeUpdates(locationListener);
        Log.i("onPause","paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (locationManager != null) {
            Log.i("onResume","resumed");
            updateLocation();
        }

        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    updateLocation();
                return;
        }
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("updateLocation", "Permissions missing");
            return;
        }
        //locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
        Log.i("updateLocation","updates requested");
        f++;

    }

    private void sendDataSet(Location location){
        Log.i("prepareData","Current lon: " + location.getLongitude() + " Current lat: " + location.getLatitude());
        Log.i("prepareData","Next lon: " + nextLon + " Next lat: " + nextLat);
        //distance = (Math.acos(Math.sin(nextLat) * Math.sin(location.getLatitude()) + Math.cos(nextLat) * Math.cos(location.getLatitude() * Math.cos(location.getLongitude() - nextLon)))) / 360 * 40000;
        Location nextLocation = new Location(location);
        nextLocation.setLatitude(nextLat);
        nextLocation.setLongitude(nextLon);
        distance = location.distanceTo(nextLocation)/1000;
        System.out.println("Distance: " + distance);
        if (distance < 0.100) {
            incrementStop();
        }

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        fahrgaeste = zugestiegene - ausgestiegene;
        BusDataSet dataSet = new BusDataSet(currentDateTimeString, location.getLongitude(), location.getLatitude(), busid, fahrgaeste, location.getSpeed(), status, timeTable.getTableId(), stopCounter + 1, distance);
        try {
            sendDataTask(dataSet);
            textView.setText("Last message sent: " + currentDateTimeString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setTextView4(String text) {
        textView4.setText(text);
    }

    private void incrementStop(){
        stopCounter++;
        if (stopCounter < timeTable.getStops().size()) {

            nextStop = timeTable.getStops().get(stopCounter).getBusStop();
            nextStopString = nextStop.getName() + zeiten(stopCounter);
            nextStopView.setText(nextStopString);
            nextLon = nextStop.getLaengengrad();
            nextLat = nextStop.getBreitengrad();
        }
    }

    private void sendDataTask(BusDataSet dataSet) throws JSONException {
        apiConnection.sendData(dataSet,this);

    }

    public void updateMessages(DataResponse response){
        String messages = "Public: " + response.getPublicText() + "\nPrivate: "+ response.getPrivateText();
        setTextView4(messages);
    }

    private String zeiten(int t){
        BusStopResponse busStop = timeTable.getStops().get(t);
        if(busStop.getAnkunft().equals("00:00:00") && busStop.getAbfahrt().equals("00:00:00")) return "\nAn: -- Ab: --";
        else if(busStop.getAnkunft().equals("00:00:00")) return "\nAn: -- Ab: "+ busStop.getAbfahrt();
        else if(busStop.getAbfahrt().equals("00:00:00")) return "\nAn: "+ busStop.getAbfahrt() + " Ab: --";
        else return "\nAn: "+ busStop.getAbfahrt() + " Ab: " + busStop.getAbfahrt();
    }



}