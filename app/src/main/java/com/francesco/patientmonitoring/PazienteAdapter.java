package com.francesco.patientmonitoring;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fra on 20/09/2016.
 */
public class PazienteAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public PazienteAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Pazienti object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        PatientHolder patientHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.patient_list_layout,parent,false);
            patientHolder = new PatientHolder();
            patientHolder.tx_username = (TextView) row.findViewById(R.id.tx_username);
            patientHolder.tx_name = (TextView) row.findViewById(R.id.tx_name);
            patientHolder.listItemBtn = (Button) row.findViewById(R.id.list_item_button);
            patientHolder.listItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Bottone in posizione "+position, Toast.LENGTH_SHORT).show();
                    /**
                     * per passare all'activity specifica del paziente
                     */
                    Intent intent= new Intent(getContext(), HomePaziente.class);
                    getContext().startActivity(intent);

                }
            });
            row.setTag(patientHolder);
        }
        else{
            patientHolder = (PatientHolder)row.getTag();
        }

        Pazienti pazienti =(Pazienti)this.getItem(position);
        patientHolder.tx_username.setText(pazienti.getUsername());
        patientHolder.tx_name.setText(pazienti.getName());

        return row;
    }

    static class PatientHolder{

        TextView tx_username, tx_name;
        Button listItemBtn;

    }
}
