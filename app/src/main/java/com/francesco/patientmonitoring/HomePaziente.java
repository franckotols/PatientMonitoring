package com.francesco.patientmonitoring;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Date;

public class HomePaziente extends AppCompatActivity {

    ImageButton diarioImageButton;
    ImageButton parametriImageButton;
    ImageButton analisiImageButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_paziente);

        diarioImageButton = (ImageButton)findViewById(R.id.image_diario);
        parametriImageButton = (ImageButton)findViewById(R.id.image_parametri);
        analisiImageButton = (ImageButton)findViewById(R.id.image_blood_analysis);



        Intent i = getIntent();
        final String nome = i.getStringExtra("nome");
        final String city = i.getStringExtra("città");
        final String birthdate = i.getStringExtra("data_di_nascita");
        final String pat_id = i.getStringExtra("id");

        parametriImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(HomePaziente.this,ParametriActivity.class);
                ii.putExtra("nome",nome);
                ii.putExtra("città",city);
                ii.putExtra("data_di_nascita",birthdate);
                ii.putExtra("id",pat_id);
                startActivity(ii);
            }
        });

        diarioImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(HomePaziente.this,DiarioMedico.class);
                ii.putExtra("nome",nome);
                ii.putExtra("città",city);
                ii.putExtra("data_di_nascita",birthdate);
                ii.putExtra("id",pat_id);
                startActivity(ii);
            }
        });

        analisiImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(HomePaziente.this,EsamiSangue.class);
                ii.putExtra("nome",nome);
                ii.putExtra("città",city);
                ii.putExtra("data_di_nascita",birthdate);
                ii.putExtra("id",pat_id);
                startActivity(ii);
            }
        });


    }
}
