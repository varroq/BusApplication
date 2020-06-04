package com.example.simon.busprototyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.simon.busprototyp.service.BusApiCall;
import com.example.simon.busprototyp.service.BusStopResponse;
import com.example.simon.busprototyp.service.TimeTableResponse;

import java.util.ArrayList;
import java.util.List;

public class SelectPosActivity extends AppCompatActivity {

    int busid;
    int linienid;
    int planpos;

    private Button speicherButton;
    private Spinner posSpinner;
    private ArrayAdapter<String> adapter;
    private TimeTableResponse timeTableForMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pos);

        timeTableForMain = null;

        speicherButton = (Button) findViewById(R.id.button);
        posSpinner = (Spinner) findViewById(R.id.spinner);

        Intent i = getIntent();
        busid = i.getIntExtra("busid", 1);
        linienid = i.getIntExtra("linienid", 1);

        planpos = 0;


        speicherButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                speicherButton.setText("Bitte warten");

                Intent gotoMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                gotoMainActivity.putExtra("busid", busid);
                gotoMainActivity.putExtra("linie", timeTableForMain);
                gotoMainActivity.putExtra("planpos", planpos);
                System.out.println("IDs: " + busid + ", " + timeTableForMain.getTableId());
                startActivity(gotoMainActivity);

                finish();
            }
        });

        posSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("PositionSelected", "PlanPos " + position);
                planpos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                busid = 1;
            }
        });

        BusApiCall apiConnection = new BusApiCall("http://192.168.178.24:8080/rbi-api/time?id=" + linienid , this.getApplicationContext());

        apiConnection.getTimeTable(this);
    }

    public void applyTimeTable(TimeTableResponse timeTable) {

        timeTableForMain = timeTable;
        List<BusStopResponse> stops = timeTable.getStops();
        List<String> adapterList = new ArrayList<>();

        for(BusStopResponse stop : stops) {
            adapterList.add(stop.getBusStop().getName() + " um " + stop.getAbfahrt());
        }

        adapter = new ArrayAdapter<String>(SelectPosActivity.this, R.layout.big_spinner_item, adapterList);
        posSpinner.setAdapter(adapter);




    }

}

