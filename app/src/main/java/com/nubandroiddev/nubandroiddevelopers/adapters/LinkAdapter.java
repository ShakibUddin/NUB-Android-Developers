package com.nubandroiddev.nubandroiddevelopers.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.nubandroiddev.nubandroiddevelopers.R;
import com.nubandroiddev.nubandroiddevelopers.model.Link;

import java.util.ArrayList;
import java.util.LinkedList;

public class LinkAdapter extends ArrayAdapter<Link> {

    private Context context;
    private int resource;
    private LinkedList<Link> listForFiltering;
    private static final String TAG = "LinkAdapter";


    public LinkAdapter(@NonNull Context context, int resource, ArrayList<Link> links) {
        super(context, resource, links);
        this.context = context;
        this.resource = resource;
        listForFiltering = new LinkedList<>(links);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Create an instance of the TextCrawler to parse your url into a preview.
        LinearLayout linkLayout;
        MaterialTextView title;
        MaterialTextView link;

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        linkLayout = (LinearLayout) convertView.findViewById(R.id.linkLayout);
        title = (MaterialTextView) convertView.findViewById(R.id.title);
        link = (MaterialTextView) convertView.findViewById(R.id.link);


        title.setText(getItem(position).getTitle());
        link.setText(getItem(position).getLink());

        linkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(Uri.parse(getItem(position).getLink()));
            }
        });

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return linkFilter;
    }

    private Filter linkFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            LinkedList<Link> suggestions = new LinkedList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(listForFiltering);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Link link : listForFiltering) {
                    if (link.getTitle().toLowerCase().contains(filterPattern) || link.getLink().toLowerCase().contains(filterPattern)) {
                        suggestions.add(link);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((LinkedList) results.values);
            notifyDataSetChanged();
        }
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Link) resultValue).getLink();
        }
    };

    void openLink(Uri uri){
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // Find an activity to hand the intent and start that activity.
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
            Toast.makeText(getContext(),"Can not open link",Toast.LENGTH_SHORT).show();

        }
    }
}
