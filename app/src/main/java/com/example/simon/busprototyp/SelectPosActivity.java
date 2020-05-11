package com.example.simon.busprototyp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class SelectPosActivity extends AppCompatActivity {

    String[] plan;
    String[] spinnerPlan;
    String[] hsplit;
    String[] hnsplit;
    private String[] absplit;
    String inmsg;
    String outmsg;
    int busid;
    int linienid;
    int planpos;
    Haltestelle[] haltestellen;


    private Button speicherButton;
    private Spinner posSpinner;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pos);

        speicherButton = (Button) findViewById(R.id.button);
        posSpinner = (Spinner) findViewById(R.id.spinner);

        Intent i = getIntent();
        busid = i.getIntExtra("busid", 1);
        linienid = i.getIntExtra("linienid", 1);

        planpos = 0;
        haltestellen = new Haltestelle[50];
        plan = new String[]{
                "Please select", "Test1", "Test2", "Test3"
        };

        speicherButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                speicherButton.setText("Bitte warten");

                Intent gotoMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                gotoMainActivity.putExtra("busid", busid);
                gotoMainActivity.putExtra("linienid", linienid);
                gotoMainActivity.putExtra("planpos", planpos);
                startActivity(gotoMainActivity);
                System.out.println("IDs: " + busid + ", " + linienid);
                finish();
            }
        });

        posSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                planpos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                busid = 1;
            }
        });

        SendingTask sendinit = new SendingTask();
        outmsg = "WS " + linienid;
        sendinit.execute(outmsg);
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

            System.out.println("Full ");
            plan = fullmsg[0].split(", ");
            String[] ansplit = fullmsg[2].split(", ");
            absplit = fullmsg[3].split(", ");
            hsplit = fullmsg[4].split("; ");
            hnsplit = fullmsg[5].split(", ");
            String[] lonsplit = fullmsg[6].split(", ");
            String[] latsplit = fullmsg[7].split(", ");
            System.out.println(hsplit.length);
            for (int i=0; i<hsplit.length; i++){
                haltestellen[i] = new SelectPosActivity.Haltestelle(Integer.parseInt(hnsplit[i]), hsplit[i], Double.parseDouble(lonsplit[i]), Double.parseDouble(latsplit[i]));
                System.out.println("H: " + i);
            }


            return null;
        }


        @Override
        protected void onPostExecute(Integer result) {
            spinnerPlan = new String[plan.length];
            int k = 0;
            for(int j = 0; j<plan.length; j++){
                for(int l = 0; l<hsplit.length; l++){
                    if (plan[j].equals(hnsplit[l])){
                        k = l;
                        System.out.println(k);
                    }
                    System.out.println(hnsplit[l]);
                }
                spinnerPlan[j] = haltestellen[k].getName() + " um " + absplit[j];
                System.out.println("Plan: " +plan[j] + spinnerPlan[j]);
            }
            adapter = new ArrayAdapter<String>(SelectPosActivity.this, R.layout.big_spinner_item, spinnerPlan);
            posSpinner.setAdapter(adapter);

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

