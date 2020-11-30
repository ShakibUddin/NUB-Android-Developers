package com.nubandroiddev.nubandroiddevelopers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.nubandroiddev.nubandroiddevelopers.R;
import com.nubandroiddev.nubandroiddevelopers.model.Report;

import java.util.ArrayList;

public class ReportAdapter extends ArrayAdapter<Report> {

    private Context context;
    private int resource;

    public ReportAdapter(@NonNull Context context, int resource, ArrayList<Report> reports) {
        super(context, resource, reports);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MaterialTextView username;
        MaterialTextView dateTime;
        MaterialTextView description;

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        username = (MaterialTextView) convertView.findViewById(R.id.username);
        dateTime = (MaterialTextView) convertView.findViewById(R.id.dateTime);
        description = (MaterialTextView) convertView.findViewById(R.id.description);

        username.setText(getItem(position).getUser());
        dateTime.setText(getItem(position).getDate()+" at "+getItem(position).getTime());
        description.setText(getItem(position).getDescription());

        return convertView;
    }
}
