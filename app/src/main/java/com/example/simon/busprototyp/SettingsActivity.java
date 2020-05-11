package com.example.simon.busprototyp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class SettingsActivity extends AppCompatActivity {

    private String outmsg;
    private String inmsg;
    private String[] numbers;
    private String[] linienNumbers;
    private String[] fullmsg;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> linienAdapter;
    private String[] linienArraySpinner;
    private String[] arraySpinner;
    private Spinner fahrzeugSpinner;
    private Spinner linienSpinner;
   // private EditText halteText;
    private Button speicherButton;
    private Button btn1;
    private int busid;
    private int linienid;
    private int j;
    private boolean busb;
    private boolean linieb;
    //private int planpos;

    private RelativeLayout layout;
    private Resources r;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        layout = (RelativeLayout)findViewById(R.id.activity_settings);
        r = getResources();
        busb = false;
        linieb = false;

        //fahrzeugSpinner = (Spinner) findViewById(R.id.fahrzeugSpinner);
        //linienSpinner = (Spinner) findViewById(R.id.linienSpinner);
        speicherButton = (Button) findViewById(R.id.speicherButton);
        //halteText = (EditText) findViewById(R.id.editText);

        speicherButton.setText("Weiter");
        speicherButton.setVisibility(View.GONE);

        arraySpinner = new String[]{
                "Please select", "Test1", "Test2", "Test3"
        };

        SendingTask sendinit = new SendingTask();
        outmsg = "init";
        sendinit.execute(outmsg);

        /*fahrzeugSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //busid = position + 1;
                busid = Integer.parseInt(numbers[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                busid = 1;
            }
        });

        linienSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //busid = position + 1;
                linienid = Integer.parseInt(linienNumbers[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                linienid = 1;
            }
        }); */



        speicherButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //speicherButton.setText("Bitte warten");
                //planpos = Integer.parseInt(halteText.getText().toString());
                if(linieb && busb) {
                    Intent gotoSelectPosActivity = new Intent(getApplicationContext(), SelectPosActivity.class);
                    gotoSelectPosActivity.putExtra("busid", busid);
                    gotoSelectPosActivity.putExtra("linienid", linienid);
                    //gotoMainActivity.putExtra("planpos", planpos);
                    startActivity(gotoSelectPosActivity);
                    System.out.println("IDs: " + busid + ", " + linienid);
                }
            }
        });


    }

    private class SendingTask extends AsyncTask<String, Integer, Integer> {




        @Override
        protected Integer doInBackground(String... params) {



            try {
                //setting up connection to database
                DatagramSocket socket = new DatagramSocket(8765);
                socket.setSoTimeout(3000);

                byte outblock[] = params[0].getBytes();
                InetAddress address = InetAddress.getByName("www.klingel-reisen-rbi.de");

                //sending
                DatagramPacket outpacket = new DatagramPacket(outblock, outblock.length, address, 8765);

                while (true) {

                    try {
                        socket.send(outpacket);

                        //receiving
                        byte block[] = new byte[256];
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

            return null;
        }


        @Override
        protected void onPostExecute(Integer result) {

            fullmsg = inmsg.split("/");
            arraySpinner = fullmsg[0].split(", ");
            numbers = fullmsg[1].split(", ");
            linienArraySpinner = fullmsg[2].split(", ");
            linienNumbers = fullmsg[3].split(", ");
            //adapter = new ArrayAdapter<String>(SettingsActivity.this, R.layout.big_spinner_item, arraySpinner);
            //linienAdapter = new ArrayAdapter<String>(SettingsActivity.this, R.layout.big_spinner_item, linienArraySpinner);

            //fahrzeugSpinner.setAdapter(adapter);
            //linienSpinner.setAdapter(linienAdapter);
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, r.getDisplayMetrics());
            int count = 0;
            int textSize = 25;
            int margin = 20;

            for(j = 0; j < arraySpinner.length; j++) {
                final int suc = j;
                btn = new Button(getApplicationContext());
                btn.setText(arraySpinner[j]);
                btn.setWidth((int) px);
                btn.setBackgroundColor(0xffcccccc);
                btn.setTextSize(textSize);
                btn.setId(j+1);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,margin,10,0);
                params.addRule(RelativeLayout.LEFT_OF, findViewById(R.id.dummy).getId());
                if(j == 0) {
                    params.addRule(RelativeLayout.BELOW, findViewById(R.id.textView6).getId());
                } else {
                    params.addRule(RelativeLayout.BELOW, j);
                }
                layout.addView(btn, params);

                btn1 = ((Button) findViewById(suc+1));
                btn1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        busb = true;
                        busid = Integer.parseInt(numbers[suc]);
                        for(int i = 0; i < arraySpinner.length; i++){
                            btn1 = ((Button) findViewById(i+1));
                            btn1.setBackgroundColor(0xffcccccc);
                        }
                        btn1 = ((Button) findViewById(suc+1));
                        btn1.setBackgroundColor(0xfffccccc);
                        // to do: highlight button
                        if(linieb) speicherButton.setVisibility(View.VISIBLE);
                    }
                });


                count = j;
                //prevId = btn.getId();
            }

            for(j = 0; j < linienArraySpinner.length; j++) {
                final int suc = j;
                final int c = count;
                btn = new Button(getApplicationContext());
                btn.setText(linienArraySpinner[j]);
                //btn.setWidth((int) px);
                btn.setBackgroundColor(0xffcccccc);
                btn.setId(count+j+2);
                btn.setTextSize(textSize);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,margin,0,0);

                if(j == 0) {
                    params.addRule(RelativeLayout.BELOW, findViewById(R.id.textView7).getId());

                } else {
                    params.addRule(RelativeLayout.BELOW, count+j+1);

                }
                params.addRule(RelativeLayout.ALIGN_LEFT, findViewById(R.id.dummy).getId());
                layout.addView(btn, params);
                btn1 = ((Button) findViewById(c+suc+2));
                btn1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        linieb = true;
                        linienid = Integer.parseInt(linienNumbers[suc]);
                        for(int i = 0; i < linienArraySpinner.length; i++){
                            btn1 = ((Button) findViewById(c+i+2));
                            btn1.setBackgroundColor(0xffcccccc);
                        }
                        btn1 = ((Button) findViewById(c+suc+2));
                        btn1.setBackgroundColor(0xfffccccc);
                        // to do: highlight button
                        if(busb) speicherButton.setVisibility(View.VISIBLE);
                    }
                });
                //prevId = btn.getId();
            }




        }


    }
}
