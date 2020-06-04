package com.example.simon.busprototyp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.simon.busprototyp.service.BusApiCall;
import com.example.simon.busprototyp.service.BusLine;
import com.example.simon.busprototyp.service.BusSettings;
import com.example.simon.busprototyp.service.BusVehicle;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Button speicherButton;
    private Button btn1;
    private int busid;
    private int linienid;
    private int j;
    private boolean busb;
    private boolean linieb;

    private RelativeLayout layout;
    private Resources r;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        layout = (RelativeLayout) findViewById(R.id.activity_settings);
        r = getResources();
        busb = false;
        linieb = false;

        speicherButton = (Button) findViewById(R.id.speicherButton);

        speicherButton.setText("Weiter");
        speicherButton.setVisibility(View.GONE);


        BusApiCall apiConnection = new BusApiCall("http://192.168.178.24:8080/rbi-api/plates", this.getApplicationContext());

        apiConnection.getSettings(this);



        speicherButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (linieb && busb) {
                    Intent gotoSelectPosActivity = new Intent(getApplicationContext(), SelectPosActivity.class);
                    gotoSelectPosActivity.putExtra("busid", busid);
                    gotoSelectPosActivity.putExtra("linienid", linienid);

                    startActivity(gotoSelectPosActivity);
                    System.out.println("IDs: " + busid + ", " + linienid);
                }
            }
        });
    }

        public void applySettings (BusSettings settings) {

            final List<BusVehicle> vehicleList = settings.getVehicles();
            final List<BusLine> lineList = settings.getTimetables();

            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, r.getDisplayMetrics());
            int count = 0;
            int textSize = 25;
            int margin = 20;

            for (j = 0; j < vehicleList.size(); j++) {
                final int suc = j;
                btn = new Button(getApplicationContext());
                btn.setText(vehicleList.get(j).getKennzeichen());
                btn.setWidth((int) px);
                btn.setBackgroundColor(0xffcccccc);
                btn.setTextSize(textSize);
                btn.setId(j + 1);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, margin, 10, 0);
                params.addRule(RelativeLayout.LEFT_OF, findViewById(R.id.dummy).getId());
                if (j == 0) {
                    params.addRule(RelativeLayout.BELOW, findViewById(R.id.textView6).getId());
                } else {
                    params.addRule(RelativeLayout.BELOW, j);
                }
                layout.addView(btn, params);

                btn1 = ((Button) findViewById(suc + 1));
                btn1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        busb = true;
                        busid = vehicleList.get(suc).getNbr();
                        for (int i = 0; i < vehicleList.size(); i++) {
                            btn1 = ((Button) findViewById(i + 1));
                            btn1.setBackgroundColor(0xffcccccc);
                        }
                        btn1 = ((Button) findViewById(suc + 1));
                        btn1.setBackgroundColor(0xfffccccc);
                        if (linieb) speicherButton.setVisibility(View.VISIBLE);
                    }
                });


                count = j;
                //prevId = btn.getId();
            }

            for (j = 0; j < lineList.size(); j++) {
                final int suc = j;
                final int c = count;
                btn = new Button(getApplicationContext());
                btn.setText(lineList.get(j).getLinie());
                //btn.setWidth((int) px);
                btn.setBackgroundColor(0xffcccccc);
                btn.setId(count + j + 2);
                btn.setTextSize(textSize);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, margin, 0, 0);

                if (j == 0) {
                    params.addRule(RelativeLayout.BELOW, findViewById(R.id.textView7).getId());

                } else {
                    params.addRule(RelativeLayout.BELOW, count + j + 1);

                }
                params.addRule(RelativeLayout.ALIGN_LEFT, findViewById(R.id.dummy).getId());
                layout.addView(btn, params);
                btn1 = ((Button) findViewById(c + suc + 2));
                btn1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        linieb = true;
                        linienid = lineList.get(suc).getLinie_id();
                        for (int i = 0; i < lineList.size(); i++) {
                            btn1 = ((Button) findViewById(c + i + 2));
                            btn1.setBackgroundColor(0xffcccccc);
                        }
                        btn1 = ((Button) findViewById(c + suc + 2));
                        btn1.setBackgroundColor(0xfffccccc);
                        if (busb) speicherButton.setVisibility(View.VISIBLE);
                    }
                });
            }

        }




}
