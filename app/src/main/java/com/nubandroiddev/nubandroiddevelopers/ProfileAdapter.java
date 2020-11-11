package com.nubandroiddev.nubandroiddevelopers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProfileAdapter extends ArrayAdapter<Profile> {

    private Context context;
    private int resource;

    public ProfileAdapter(@NonNull Context context, int resource, ArrayList<Profile> profiles) {
        super(context, resource, profiles);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Integer imageSource = getItem(position).getImageSource();
        String name = getItem(position).getName();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        TextView nameView = (TextView) convertView.findViewById(R.id.name);

        imageView.setImageResource(imageSource);
        nameView.setText(name);

        return convertView;
    }
}
