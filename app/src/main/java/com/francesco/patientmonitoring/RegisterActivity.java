package com.francesco.patientmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "New_User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void riepilogo(View view) throws JSONException {
        Intent nuovo_intento = new Intent(this, VisualizzaRiepilogo.class);

        EditText firstnameText = (EditText) findViewById(R.id.user_first_name);
        String firstnameString = firstnameText.getText().toString();
        EditText lastnameText = (EditText) findViewById(R.id.user_last_name);
        String lastnameString = lastnameText.getText().toString();
        EditText usernameText = (EditText) findViewById(R.id.user_username);
        String usernameString = usernameText.getText().toString();
        EditText passwordText = (EditText) findViewById(R.id.user_password);
        String passwordString = passwordText.getText().toString();
        Spinner sexSpinner = (Spinner)findViewById(R.id.user_sex);
        String sexString = sexSpinner.getSelectedItem().toString();
        DatePicker birthdateDatePicker = (DatePicker)findViewById(R.id.user_birthdate);
        int day = birthdateDatePicker.getDayOfMonth();
        int month = birthdateDatePicker.getMonth() + 1;
        int year = birthdateDatePicker.getYear();

        EditText tax_code_Text = (EditText) findViewById(R.id.user_tax_code);
        String tax_code_String = tax_code_Text.getText().toString();
        EditText email_Text = (EditText) findViewById(R.id.user_email);
        String email_String = email_Text.getText().toString();
        EditText phone_Text = (EditText) findViewById(R.id.user_phone);
        String phone_String = phone_Text.getText().toString();


        JSONObject user_dataJSON = new JSONObject();

        user_dataJSON.put("first_name",firstnameString);
        user_dataJSON.put("last_name",lastnameString);
        user_dataJSON.put("user",usernameString);
        user_dataJSON.put("password",passwordString);
        user_dataJSON.put("sex",sexString);
        user_dataJSON.put("day",day);
        user_dataJSON.put("month",month);
        user_dataJSON.put("year",year);

        user_dataJSON.put("tax_code",tax_code_String);
        user_dataJSON.put("email",email_String);
        user_dataJSON.put("phone",phone_String);

        String user_dataSTRING = user_dataJSON.toString();
        nuovo_intento.putExtra(EXTRA_MESSAGE, user_dataSTRING);
        startActivity(nuovo_intento);
    }



}
