package com.francesco.patientmonitoring.fragmentParametri;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.pojo.SpinnerParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fra on 17/10/2016.
 */
public class ParametriGraficiFragment extends Fragment {

    TextView tvNome;
    TextView tvCity;
    TextView tvBirthdate;
    Spinner paramSelectorSpinner;
    Button searchButton;
    SpinnerParams p;
    ArrayList<SpinnerParams> params;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        if (container == null){
            return null;
        }

        View rootview = inflater.inflate(R.layout.parametri_grafici, container, false);

        /*
         * Riempimento delle textView relative alle info del paziente
         */
        Intent i = getActivity().getIntent();
        final String nome = i.getStringExtra("nome");
        final String city = i.getStringExtra("città");
        final String birthdate = i.getStringExtra("data_di_nascita");
        final String id_pat = i.getStringExtra("id");
        tvNome = (TextView)rootview.findViewById(R.id.tv_nomePaziente);
        tvCity = (TextView)rootview.findViewById(R.id.tv_cittàPaziente);
        tvBirthdate = (TextView)rootview.findViewById(R.id.tv_birthPaziente);
        tvNome.setText(nome);
        tvCity.setText(city);
        tvBirthdate.setText(birthdate);

        /*
         * Spinner Configuration
         */
        params = setData();
        paramSelectorSpinner  = (Spinner)rootview.findViewById(R.id.spinner_params_graph);
        ArrayAdapter<SpinnerParams> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,params);
        paramSelectorSpinner.setAdapter(adapter);
        paramSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                p = (SpinnerParams)parent.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*
         * DatePickerInitialization
         */
        final DatePicker startDatePicker = (DatePicker)rootview.findViewById(R.id.graph_day_start);
        final DatePicker endDatePicker = (DatePicker)rootview.findViewById(R.id.graph_day_end);

        /*
         * Button onClick
         */
        searchButton = (Button)rootview.findViewById(R.id.search_graphs);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day_start = startDatePicker.getDayOfMonth();
                int month_start = startDatePicker.getMonth() + 1;
                int year_start = startDatePicker.getYear();
                final String date_start = String.valueOf(day_start)+"-"+String.valueOf(month_start)+"-"+String.valueOf(year_start);
                int day_end = endDatePicker.getDayOfMonth();
                int month_end = endDatePicker.getMonth() + 1;
                int year_end = endDatePicker.getYear();
                final String date_end = String.valueOf(day_end)+"-"+String.valueOf(month_end)+"-"+String.valueOf(year_end);
                final String param_id = p.getId();
                //Toast.makeText(getActivity(),"ID: "+p.getId()+",  Name : "+p.getName()+"\n"+date_start+"******"+date_end,Toast.LENGTH_LONG).show();
                sendParams(id_pat,date_start,date_end,param_id);
            }
        });

        return rootview;
    }

    private ArrayList<SpinnerParams> setData(){
        ArrayList<SpinnerParams> params = new ArrayList<>();
        String[] ids = getResources().getStringArray(R.array.params_ids);
        String[] names = getResources().getStringArray(R.array.params_names);
        for (int i=0;i<ids.length;i++){
            params.add(new SpinnerParams(ids[i],names[i]));
        }
        return params;
    }

    private void sendParams(final String id_pat, final String start_date, final String end_date, final String param_id) {

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = pref.getString("service_provider", "");
        final String final_addr = url+"/test";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "params putted", Toast.LENGTH_LONG).show();
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
                            String err_stringa = new String(err_.data);
                            String err_msg = "";
                            int err_stringa_A = err_stringa.indexOf("<p>");
                            err_stringa_A = err_stringa_A + ("<p>").length();
                            int err_stringa_B = err_stringa.indexOf("</p>");
                            if (err_stringa_A > 0 && err_stringa_B > err_stringa_A && err_stringa_B <= err_stringa.length()) {
                                err_msg = err_stringa.substring(err_stringa_A, err_stringa_B);
                            }
                            if (err_msg.equals("wrong_params")) {
                                Boolean cb = pref.getBoolean("show_dialogs", false);
                                if (cb.equals(true)){
                                    AlertDialog.Builder wrongParamsAlert = new AlertDialog.Builder(getActivity());
                                    wrongParamsAlert.setTitle("Attenzione!")
                                            .setMessage("Not succesfull Request")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .create();
                                    wrongParamsAlert.show();
                                }
                                else{
                                    Toast.makeText(getActivity(), getString(R.string.toast_user_password_wrong), Toast.LENGTH_LONG).show();
                                }
                            }
                            if (err_msg.equals("no_server")) {
                                Boolean cb = pref.getBoolean("show_dialogs", false);
                                if (cb.equals(true)) {
                                    AlertDialog.Builder noServerAlert = new AlertDialog.Builder(getActivity());
                                    noServerAlert.setTitle("Attenzione!")
                                            .setMessage("server is down")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .create();
                                    noServerAlert.show();
                                }
                                else{
                                    Toast.makeText(getActivity(), getString(R.string.toast_server_wrong), Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                        else{
                            Boolean cb = pref.getBoolean("show_dialogs", false);
                            if (cb.equals(true)) {
                                AlertDialog.Builder noServerAlert = new AlertDialog.Builder(getActivity());
                                noServerAlert.setTitle("Attenzione!")
                                        .setMessage(getString(R.string.toast_server_wrong))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create();
                                noServerAlert.show();
                            }
                            else{
                                Toast.makeText(getActivity(), getString(R.string.toast_server_wrong), Toast.LENGTH_LONG).show();
                            }
                        }

                        error.printStackTrace();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_pat", id_pat);
                params.put("start_date", start_date);
                params.put("end_date",end_date);
                params.put("param_id", param_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
