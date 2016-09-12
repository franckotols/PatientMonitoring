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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    SharedPreferences pref;
    //public final String REGISTER_URL = "http://192.168.173.1:9090";
    public static final String KEY_USERNAME = "Username";
    public static final String KEY_PASSWORD = "Password";

    private EditText etUsername;
    private EditText etPassword;
    private TextView logResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        logResponse = (TextView) findViewById(R.id.log_response);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);

        //to avoid errors
        if (bLogin != null){
            bLogin.setOnClickListener(this);
        }

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(LoginActivity.this, Settings.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendLogin() {

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String url = pref.getString("service_provider", "");

        final String username_string = etUsername.getText().toString();
        final String password_string = etPassword.getText().toString();
        final String final_addr = url+"/authentication";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, final_addr,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        logResponse.setText("");
                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();

                        Intent ii = new Intent (LoginActivity.this, HomeActivity.class);
                        //ii.putExtra("response", response);
                        startActivity(ii);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println("Something went wrong!");
                        NetworkResponse err_ = error.networkResponse;
                        String display_err_user_msg="\n\n\nError in sending request.";
                        if(err_ != null && err_.data != null) {
                            int err_status_code = err_.statusCode;
                            String err_status_code_str = ("" + err_status_code);
                            String err_stringa = new String(err_.data);
                            String err_msg = "";
                            int err_stringa_A = err_stringa.indexOf("<p>");
                            err_stringa_A = err_stringa_A + ("<p>").length();
                            int err_stringa_B = err_stringa.indexOf("</p>");
                            if (err_stringa_A > 0 && err_stringa_B > err_stringa_A && err_stringa_B <= err_stringa.length()) {
                                err_msg = err_stringa.substring(err_stringa_A, err_stringa_B);
                                System.out.println(err_msg);
                            }
                            display_err_user_msg = (display_err_user_msg + "\n\nSTATUS CODE: " + err_status_code_str + "\nError Message:\n" + err_msg);
                        }
                        else{
                            display_err_user_msg = (display_err_user_msg+"\n\nPossible causes:\nServer Address is incorrect;\nServer is down.");
                        }

                        error.printStackTrace();

                        logResponse.setText(display_err_user_msg);
                        logResponse.setTextColor(Color.RED);


                        }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME, username_string);
                params.put(KEY_PASSWORD, password_string);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);





    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.bSignIn:
                sendLogin();
                break;
            /*case R.id.post_button:
                sendData();
                break;*/
        }

    }
}
