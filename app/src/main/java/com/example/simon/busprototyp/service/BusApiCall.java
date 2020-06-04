package com.example.simon.busprototyp.service;


import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.simon.busprototyp.MainActivity;
import com.example.simon.busprototyp.SelectPosActivity;
import com.example.simon.busprototyp.SettingsActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class BusApiCall {
    private String url;
    private Context ctx;

    public BusApiCall(String url, Context ctx) {
        this.url = url;
        this.ctx = ctx;
    }


    public void getSettings(final SettingsActivity activity){

        // Instantiate the cache
        Cache cache = new DiskBasedCache(ctx.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        final RequestQueue requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        // Formulate the request and handle the response.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Response", response.toString());
                Gson gson=new Gson();
                BusSettings receivedSettings = gson.fromJson(response.toString(), BusSettings.class);
                Log.i("JSON", receivedSettings.toString());
                activity.applySettings(receivedSettings);
                requestQueue.stop();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Error", error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        requestQueue.add(request);
        Log.i("APICallSettings","Started Request " + url);

    }

    public void sendData(BusDataSet dataSet, final MainActivity activity) throws JSONException {
        // Create JsonObject to send form dataSet
        Gson gson = new Gson();
        JSONObject dataJson = new JSONObject(gson.toJson(dataSet));


        // Instantiate the cache
        Cache cache = new DiskBasedCache(ctx.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        final RequestQueue requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        // Formulate the request and handle the response.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, dataJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Response", response.toString());
                Gson gson=new Gson();
                DataResponse receivedResponse = gson.fromJson(response.toString(), DataResponse.class);
                Log.i("JSON", receivedResponse.toString());
                activity.updateMessages(receivedResponse);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Error", error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        requestQueue.add(request);
        Log.i("APICallData","Started Request" + url);

    }

    public void getTimeTable(final SelectPosActivity activity){
        // Instantiate the cache
        Cache cache = new DiskBasedCache(ctx.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        final RequestQueue requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        // Formulate the request and handle the response.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Response", response.toString());
                Gson gson=new Gson();
                TimeTableResponse receivedTimeTable = gson.fromJson(response.toString(), TimeTableResponse.class);
                Log.i("JSON", receivedTimeTable.toString());
                activity.applyTimeTable(receivedTimeTable);
                requestQueue.stop();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Error", error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        requestQueue.add(request);
        Log.i("APICallTablePos","Started Request" + url);
    }
}
