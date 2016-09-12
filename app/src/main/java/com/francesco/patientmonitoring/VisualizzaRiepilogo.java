package com.francesco.patientmonitoring;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VisualizzaRiepilogo extends AppCompatActivity {

    SharedPreferences pref;
    private  TextView tvFirstname;
    private TextView tvLastname;
    private TextView tvEmailaddress;
    private TextView tvPassword;
    private TextView tvSex;
    private TextView tvSpec;
    private TextView tvBirthdate;
    private TextView tvPhonenumber;
    private Button bRegistra;
    private TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_riepilogo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intento = getIntent();
        final String user_data_STRING = intento.getStringExtra(RegisterActivity.EXTRA_MESSAGE);
        tvFirstname = (TextView) findViewById(R.id.riep_first);
        tvLastname = (TextView) findViewById(R.id.riep_last);
        tvEmailaddress = (TextView) findViewById(R.id.riep_mail);
        tvPassword = (TextView) findViewById(R.id.riep_pass);
        tvSex = (TextView) findViewById(R.id.riep_sex);
        tvSpec = (TextView) findViewById(R.id.riep_spec);
        tvBirthdate = (TextView) findViewById(R.id.riep_birth);
        tvPhonenumber = (TextView) findViewById(R.id.riep_phone);
        bRegistra = (Button) findViewById(R.id.registra);
        tvResponse = (TextView) findViewById(R.id.riep_response);
        Boolean formFilled = true;
        Boolean checkPassword = false;



        try {
            JSONObject user_data_OBJ = new JSONObject(user_data_STRING);

            String first_name = user_data_OBJ.getString("first_name");
            String first_name_full = ("First Name: " + first_name);
            tvFirstname.setText(first_name_full);
            if (first_name.equals(""))
                formFilled = false;

            String last_name = user_data_OBJ.getString("last_name");
            String last_name_full = ("Last Name: " + last_name);
            tvLastname.setText(last_name_full);
            if (last_name.equals(""))
                formFilled = false;

            String email = user_data_OBJ.getString("email");
            String email_full = ("e-mail: " + email);
            tvEmailaddress.setText(email_full);
            if (email.equals(""))
                formFilled = false;


            String password = user_data_OBJ.getString("password");
            String password_full = ("Password: " + password);
            tvPassword.setText(password_full);
            if (password.equals(""))
                formFilled = false;

            String check_Password = user_data_OBJ.getString("checkpassword");
            if (check_Password.equals(""))
                formFilled = false;

            if (check_Password.equals(password)){
                checkPassword = true;
            }

            String sex = user_data_OBJ.getString("sex");
            String sex_full = ("Sex: " + sex);
            tvSex.setText(sex_full);
            if (sex.equals(""))
                formFilled = false;

            String spec = user_data_OBJ.getString("specializzazione");
            String spec_full = ("Specializzazione: " + spec);
            tvSpec.setText(spec_full);
            if (spec.equals(""))
                formFilled = false;

            int d = user_data_OBJ.getInt("day");
            int m = user_data_OBJ.getInt("month");
            int y = user_data_OBJ.getInt("year");
            String birthdate = (d + "/" + m + "/" + y);
            String birthdate_full = ("Birthdate: " + birthdate);
            tvBirthdate.setText(birthdate_full);
            if (birthdate.equals(""))
                formFilled = false;


            String phone = user_data_OBJ.getString("phone");
            String phone_full = ("Phone Number: " + phone);
            tvPhonenumber.setText(phone_full);
            if (phone.equals(""))
                formFilled = false;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (formFilled && checkPassword) {

            pref = PreferenceManager.getDefaultSharedPreferences(this);
            final String server_addr = pref.getString("service_provider", "");
            final String finalServer_addr = server_addr + "/registration";

            bRegistra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


// Request a string response
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, finalServer_addr,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                    tvResponse.setText("Request successfully sent!\n\nServer answer:\n"+response);
                                    tvResponse.setTextColor(Color.BLACK);
                                    bRegistra.setEnabled(false);


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            // Error handling
                            System.out.println("Something went wrong!");
                            NetworkResponse err_ = error.networkResponse;
                            String display_err_user_msg="\n\n\nError in sending request.";
                            if(err_ != null && err_.data != null){
                                int err_status_code= err_.statusCode;
                                String err_status_code_str = (""+err_status_code);
                                String err_stringa = new String (err_.data);
                                String err_msg="";
                                int err_stringa_A=err_stringa.indexOf("<p>");
                                err_stringa_A=err_stringa_A+("<p>").length();
                                int err_stringa_B=err_stringa.indexOf("</p>");
                                if (err_stringa_A>0 && err_stringa_B>err_stringa_A && err_stringa_B<=err_stringa.length()){
                                    err_msg=err_stringa.substring(err_stringa_A,err_stringa_B);
                                    System.out.println(err_msg);
                                }
                                display_err_user_msg = (display_err_user_msg+"\n\nSTATUS CODE: "+err_status_code_str+"\nError Message:\n"+err_msg);
                            }
                            else{
                                display_err_user_msg = (display_err_user_msg+"\n\nPossible causes:\nServer Address is incorrect;\nServer is down.");
                            }

                            error.printStackTrace();


                            tvResponse.setText(display_err_user_msg);
                            tvResponse.setTextColor(Color.RED);
                            bRegistra.setEnabled(false);

                        }
                    }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("new_user_data",user_data_STRING);
                            return params;
                        }

                    };

// Add the request to the queue
                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
                }
            });
        }
        else if (!checkPassword && formFilled){
            tvResponse.setText("Le password inserite sono diverse.");
            tvResponse.setTextColor(Color.RED);
            bRegistra.setEnabled(false);
        }

        else {
            tvResponse.setText("Information missing!\nPlease, go back and fill all the required fields.");
            tvResponse.setTextColor(Color.RED);
            bRegistra.setEnabled(false);
        }

    }



}


