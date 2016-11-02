package com.francesco.patientmonitoring;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Notifiche extends AppCompatActivity {

    /**
     * Volley Needs
     **/
    SharedPreferences pref;

    /**
     * Per rilevare la risposta del server e creare gli elementi per la ListView
     */
    private JSONObject jsonServerResp;
    AlertAdapter alertAdapter;
    ListView listView;

    /**
     * Layout
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiche);


        //get server url
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");
        final String final_addr = url + "/notifications";

        //populating listView
        listView=(ListView)findViewById(R.id.listv_alerts);
        alertAdapter = new AlertAdapter(this,R.layout.alert_list_layout);
        listView.setAdapter(alertAdapter);

        /**
         * parte REST per riempire la sezione dei checkBox relativi alle varie malattie (GET)
         * In questo caso la richiesta deve essere sincrona
         */

        JsonObjectRequest jobjRequest = new JsonObjectRequest(Request.Method.GET, final_addr,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray jsonArray = response.getJSONArray("results");
                            String patient_name,date,type,message,status,id;

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                patient_name = jsonObj.getString("assetName");
                                date = jsonObj.getString("eventDate");
                                type = jsonObj.getString("type");
                                message = jsonObj.getString("message");
                                status = jsonObj.getString("status");
                                id = jsonObj.getString("id");

                                Alerts alert = new Alerts(patient_name,date,type,message,status,id);
                                alertAdapter.add(alert);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        alertAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", "ERROR");
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jobjRequest);

    }



}


