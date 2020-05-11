package com.example.simon.busprototyp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
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

    private int zugestiegene;
    private int ausgestiegene;
    private int fahrgaeste;
    private int busid;
    private int linienid;
    private int f;
    private int nextStop;
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



    private Haltestelle[] haltestellen;

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
        linienid = i.getIntExtra("linienid", 1);
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
        initreq = true;

        haltestellen = new Haltestelle[50];
        plan = new String[50];
        nextStop = stopCounter;
        nextStopString = "";

        distance = 1000000;

        SendingTask sendinit = new SendingTask();
        outmsg = "WS " + linienid;
        sendinit.execute(outmsg);

        try{ Thread.sleep(5000); }catch(InterruptedException e){}

        nextStop = Integer.parseInt(plan[stopCounter]);

        for (int j = 0; j < 50; j++){
            if(haltestellen[j].getNbr() == nextStop){
                nextStopString = haltestellen[j].getName()+ zeiten(stopCounter);
                nextStopView.setText(nextStopString);
                nextLon = haltestellen[j].getLon();
                nextLat = haltestellen[j].getLat();
                break;
            }
        }

        /*
        nextStop = Integer.parseInt(plan[stopCounter]);
        System.out.println("Counter: " + stopCounter + " nextStop: " + nextStop);
        nextStopString = haltestellen[nextStop].getName();
        nextStopView.setText(nextStopString);
        nextLon = haltestellen[nextStop].getLon();
        nextLat = haltestellen[nextStop].getLat();
        */



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
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
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
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // set to normal color
                    ausstiegsbutton.setBackgroundColor(0xffff8833);
                }

                return true;
            }
        });

        /*zustiegsbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                zugestiegene += 1;
                fahrgaeste = zugestiegene - ausgestiegene;
                textView2.setText(fahrgaeste + "");
            }
        });*/

        leerbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ausgestiegene = zugestiegene;
                fahrgaeste = 0;
                textView2.setText(fahrgaeste + "");
            }
        });

        /*ausstiegsbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (fahrgaeste != 0) {
                    ausgestiegene += 1;
                    fahrgaeste = zugestiegene - ausgestiegene;
                    textView2.setText(fahrgaeste + "");
                }
            }
        });*/

        staubutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(stau && !ausfall){
                    status = "Keine Vorkommnisse";
                    stau = false;
                    staubutton.setBackgroundColor(0xffcccccc);
                } else if(!ausfall){
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
                if(ausfall && !stau){
                    status = "Keine Vorkommnisse";
                    ausfall = false;
                    ausfallbutton.setBackgroundColor(0xffcccccc);
                }else if(ausfall & stau){
                    status = "Steht im Stau";
                    ausfall = false;
                    ausfallbutton.setBackgroundColor(0xffcccccc);

                } else {
                    status = "Ausfall!";
                    ausfall = true;
                    ausfallbutton.setBackgroundColor(0xffffcccc);
                    staubutton.setBackgroundColor(0xffcccccc);
                }
                //to do: highlight button
            }
        });

        /*nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementStop();
            }
        });*/

        settingsbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orientationListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if(true){
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        };




        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                distance = (Math.acos(Math.sin(nextLat)*Math.sin(location.getLatitude())+Math.cos(nextLat)*Math.cos(location.getLatitude()*Math.cos(location.getLongitude()-nextLon))))/360 * 40000;
                System.out.println("Distance: "+distance);
                if (distance < 0.100) {
                    incrementStop();
                }

                SendingTask sending = new SendingTask();
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                fahrgaeste = zugestiegene - ausgestiegene;
                textView.setText("Last message sent: " + currentDateTimeString);
                outmsg = currentDateTimeString + ", " + location.getLongitude() + ", " + location.getLatitude() + ", " + busid + ", " + fahrgaeste + ", " + location.getSpeed() + ", " + status + ", " + linienid + ", " + (stopCounter+1) + ", " + distance;

                sending.execute(outmsg);
                //inmsg = sending.inmsg;

                //new SendingTask().execute(outmsg);


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
        //updateLocation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.WAKE_LOCK}, 10);
                return;
            }
            updateLocation();
        } else {
            updateLocation();
        }
    }
    /*
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("fahrgaeste", fahrgaeste);
    }*/

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

        super.onPause();
    }

    @Override
    protected void onResume() {
        if(locationManager != null){
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
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        //textView4.setText("Update" + f);
        f++;

    }

    private void setTextView4(String text) {
        textView4.setText(text);
    }

    private void incrementStop(){
        stopCounter++;
        if (stopCounter < plan.length) {

            nextStop = Integer.parseInt(plan[stopCounter]);


            for (int i = 0; i < 50; i++) {
                if (haltestellen[i].getNbr() == nextStop) {
                    nextStopString = haltestellen[i].getName() + zeiten(stopCounter);
                    nextStopView.setText(nextStopString);
                    nextLon = haltestellen[i].getLon();
                    nextLat = haltestellen[i].getLat();
                    break;
                }
            }
        }
    }

    private String zeiten(int t){
        if(ansplit[t].equals("00:00:00") && absplit[t].equals("00:00:00")) return "\nAn: -- Ab: --";
        else if(ansplit[t].equals("00:00:00")) return "\nAn: -- Ab: "+ absplit[t];
        else if(absplit[t].equals("00:00:00")) return "\nAn: "+ absplit[t] + " Ab: --";
        else return "\nAn: "+ absplit[t] + " Ab: " + absplit[t];
    }


    private class SendingTask extends AsyncTask<String, Integer, Integer> {




        @Override
        protected Integer doInBackground(String... params) {



                try {
                    //setting up connection to database
                    DatagramSocket socket = new DatagramSocket(8765);
                    socket.setSoTimeout(5000);

                    byte outblock[] = params[0].getBytes();
                    InetAddress address = InetAddress.getByName("www.klingel-reisen-rbi.de");

                    //sending
                    DatagramPacket outpacket = new DatagramPacket(outblock, outblock.length, address, 8765);

                    while (true) {

                        try {
                            socket.send(outpacket);

                            //receiving
                            byte block[] = new byte[4096];
                            DatagramPacket inpacket = new DatagramPacket(block, block.length);
                            socket.receive(inpacket);

                            int inlength = inpacket.getLength();
                            byte inblock[] = inpacket.getData();
                            inmsg = new String(inblock, 0, inlength);
                            System.out.println(inmsg);


                            socket.close();
                            break;
                        }  catch (SocketTimeoutException e){
                            System.out.println("Timeout! Sending again!");
                        }
                    }

                } catch (SocketException e) {

                }  catch (UnknownHostException e) {

                } catch (IOException e) {

                }
            System.out.println("PostExecute");
            String[] fullmsg = inmsg.split("/");
            if(initreq){
                System.out.println("Full ");
                plan = fullmsg[0].split(", ");
                ansplit = fullmsg[2].split(", ");
                absplit = fullmsg[3].split(", ");
                String[] hsplit = fullmsg[4].split("; ");
                String[] hnsplit = fullmsg[5].split(", ");
                String[] lonsplit = fullmsg[6].split(", ");
                String[] latsplit = fullmsg[7].split(", ");
                System.out.println(hsplit.length);
                for (int i=0; i<hsplit.length; i++){
                    haltestellen[i] = new Haltestelle(Integer.parseInt(hnsplit[i]), hsplit[i], Double.parseDouble(lonsplit[i]), Double.parseDouble(latsplit[i]));
                }

            }//else setTextView4(inmsg);
            return null;
        }


        @Override
        protected void onPostExecute(Integer result) {
            /*System.out.println("PostExecute");
            String[] fullmsg = inmsg.split("/");
            if(initreq){
                System.out.println("Full ");
                plan = fullmsg[0].split(", ");
                String[] hsplit = fullmsg[2].split(", ");
                String[] hnsplit = fullmsg[3].split(", ");
                String[] lonsplit = fullmsg[4].split(", ");
                String[] latsplit = fullmsg[5].split(", ");
                for (int i=0; i<hsplit.length; i++){
                    haltestellen[i] = new Haltestelle(Integer.parseInt(hnsplit[i]), hsplit[i], Double.parseDouble(lonsplit[i]), Double.parseDouble(latsplit[i]));
                }
                initreq = false;
            }else*/ if(!initreq){
                setTextView4(inmsg);

            } else initreq = false;

        }


    }
    private class Haltestelle {
        private int nbr;
        private String name;
        private double lon;
        private double lat;


        public Haltestelle(int nbr, String name, double lon, double lat){
            this.nbr = nbr;
            this.name = name;
            this.lon = lon;
            this.lat = lat;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNbr() {
            return nbr;
        }

        public void setNbr(int nbr) {
            this.nbr = nbr;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }
}