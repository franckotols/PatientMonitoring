package com.francesco.patientmonitoring;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RicercaPaziente extends AppCompatActivity {

    SharedPreferences pref;
    private ArrayList<String> diseases = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_paziente);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String server_addr = pref.getString("service_provider", "");
        final String final_addr = server_addr+"/diseases";



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, final_addr,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //JSONObject obj = response.getJSONObject("specializzazioni");
                            JSONArray jArray = response.getJSONArray("diseases");
                            diseases = new ArrayList<>();
                            if (jArray != null) {
                                for (int i=0;i<jArray.length();i++){
                                    diseases.add(jArray.get(i).toString());
                                }
                            }

                            ViewGroup checkboxContainer = (ViewGroup) findViewById(R.id.checkbox_container);



                            for (int i = 0; i < diseases.size(); i++) {
                                CheckBox checkBox = new CheckBox(RicercaPaziente.this);
                                checkBox.setText(diseases.get(i));
                                checkboxContainer.addView(checkBox);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", "ERROR");
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);



    }


}

