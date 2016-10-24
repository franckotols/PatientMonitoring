package com.francesco.patientmonitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fra on 29/09/2016.
 */
public class AlertAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public AlertAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(Alerts object) {
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
        AlertHolder alertHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.alert_list_layout,parent,false);
            alertHolder = new AlertHolder();
            alertHolder.tx_pat_name = (TextView) row.findViewById(R.id.tv_pat_name);
            alertHolder.tx_date = (TextView) row.findViewById(R.id.tv_date);
            alertHolder.tx_type = (TextView) row.findViewById(R.id.tv_type);
            alertHolder.tx_message = (TextView) row.findViewById(R.id.tv_message);
            alertHolder.cbRead = (CheckBox) row.findViewById(R.id.cb_read);
            alertHolder.cbRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        Toast.makeText(getContext(), "checked", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "not checked", Toast.LENGTH_LONG).show();
                    }
                }
            });

            row.setTag(alertHolder);
        }
        else{
            alertHolder = (AlertHolder)row.getTag();
        }

        Alerts alerts =(Alerts) this.getItem(position);
        alertHolder.tx_pat_name.setText(alerts.getPatient_name());
        alertHolder.tx_date.setText(alerts.getDate());
        alertHolder.tx_type.setText(alerts.getType());
        alertHolder.tx_message.setText(alerts.getMessage());
        String status = alerts.getRead_status();
        if (status.equals("true")) {
            alertHolder.cbRead.setChecked(true);
        }

        return row;
    }

    static class AlertHolder{

        TextView tx_pat_name,tx_date,tx_type,tx_message;
        CheckBox cbRead;

    }
}
