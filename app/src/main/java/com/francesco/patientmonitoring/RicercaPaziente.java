package com.francesco.patientmonitoring;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RicercaPaziente extends AppCompatActivity implements View.OnClickListener {

    /**
     * Volley Needs
     **/
    SharedPreferences pref;
    public static final String KEY_NAME = "patient_name";
    public static final String KEY_DISEASE = "diseases";

    /**
     * Layout
     */
    private Button bSearch;
    private EditText etName;


    /**
     * per inserire i checkBox relativi alle patologie
     **/
    private CheckBox checkBoxDisease;
    private ArrayList<String> diseases = new ArrayList<>();
    private ArrayList<CheckBox> checkboxes = new ArrayList<>();
    private ArrayList<String> checked_disease = new ArrayList<>();

    /**
     * Per rilevare la risposta del server e creare gli elementi per la ListView
     */
    private JSONObject jsonServerResp;
    PazienteAdapter pazienteAdapter;
    ArrayAdapter<String> adapter;
    ArrayList<String> items;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_paziente);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String server_addr = pref.getString("service_provider", "");
        final String final_addr = server_addr + "/diseases";
        bSearch = (Button) findViewById(R.id.search_button);
        bSearch.setOnClickListener(this);
        etName = (EditText) findViewById(R.id.patient_name);


        /**
         * parte REST per riempire la sezione dei checkBox relativi alle varie malattie (GET)
         */
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, final_addr,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jArray = response.getJSONArray("diseases");
                            diseases = new ArrayList<>();
                            if (jArray != null) {
                                for (int i = 0; i < jArray.length(); i++) {
                                    diseases.add(jArray.get(i).toString());
                                }
                            }
                            ViewGroup checkboxContainer = (ViewGroup) findViewById(R.id.checkbox_container);
                            for (int i = 0; i < diseases.size(); i++) {
                                checkBoxDisease = new CheckBox(RicercaPaziente.this);
                                checkBoxDisease.setText(diseases.get(i));
                                checkboxContainer.addView(checkBoxDisease);
                                checkboxes.add(checkBoxDisease);
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

    private void sendParams() {

        //get server url
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");

        //populating listView
        listView=(ListView)findViewById(R.id.listv);
        pazienteAdapter = new PazienteAdapter(this,R.layout.patient_list_layout);
        listView.setAdapter(pazienteAdapter);

        /**
         * Prima di far vedere la listView la funzione va a vedere quali sono i checkboxes
         * selezionati per mandare le patologie al server come params. In base a questi il
         * server darà una certa risposta piuttosto che un'altra
         */
        final String patname_string = etName.getText().toString();
        for (CheckBox item : checkboxes) {
            if (item.isChecked()) {
                String check_text = item.getText().toString();
                checked_disease.add(check_text);
            }
        }

        final String final_addr = url + "/searchPatient";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //trasforma la stringa in oggetto json
                            jsonServerResp = new JSONObject(response);
                            JSONArray jsonArray = jsonServerResp.getJSONArray("server_response");
                            String username, name, birthdate;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                JSONObject properties = jsonObj.getJSONObject("properties");
                                username = jsonObj.getString("userName");
                                name = jsonObj.getString("name");
                                //properties
                                birthdate = properties.getString("birthdate");

                                Pazienti paziente = new Pazienti(username,name,birthdate);
                                pazienteAdapter.add(paziente);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pazienteAdapter.notifyDataSetChanged();
                        //Toast.makeText(RicercaPaziente.this, response, Toast.LENGTH_LONG).show();
                        //la riga successiva serve perchè altrimenti ad ogni richiesta accumulerebbe
                        //le patologie selezionate
                        checked_disease.clear();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse err_ = error.networkResponse;
                        //String display_err_user_msg="\n\n\nError in sending request.";
                        if(err_ != null && err_.data != null) {
                            //int err_status_code = err_.statusCode;
                            //String err_status_code_str = ("" + err_status_code);
                            /**
                             * elaborazione del file html della risposta del server
                             * per estrapolare il return del web service
                             */
                            String err_stringa = new String(err_.data);
                            String err_msg = "";
                            int err_stringa_A = err_stringa.indexOf("<p>");
                            err_stringa_A = err_stringa_A + ("<p>").length();
                            int err_stringa_B = err_stringa.indexOf("</p>");
                            if (err_stringa_A > 0 && err_stringa_B > err_stringa_A && err_stringa_B <= err_stringa.length()) {
                                err_msg = err_stringa.substring(err_stringa_A, err_stringa_B);
                            }
                            if (err_msg.equals("no_selected")) {
                                Toast.makeText(RicercaPaziente.this, getString(R.string.toast_no_patient), Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(RicercaPaziente.this, error.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_NAME, patname_string);
                params.put(KEY_DISEASE, String.valueOf(checked_disease));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.search_button:
                sendParams();
                break;
        }
    }


}





