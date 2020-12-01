package com.nubandroiddev.nubandroiddevelopers.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textview.MaterialTextView;
import com.nubandroiddev.nubandroiddevelopers.R;
import com.nubandroiddev.nubandroiddevelopers.model.Link;
import com.nubandroiddev.nubandroiddevelopers.model.Report;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

import static android.content.ContentValues.TAG;

public class LinkAdapter extends ArrayAdapter<Link> {

    private Context context;
    private int resource;
    private MetaData data;
    private static final String TAG = "LinkAdapter";


    public LinkAdapter(@NonNull Context context, int resource, ArrayList<Link> links) {
        super(context, resource, links);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //RichLinkView richLinkView;
        LinearLayout linkLayout;
        TextView title;
        TextView description;
        TextView website;
        ImageView imageView;

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        //richLinkView = (RichLinkView) convertView.findViewById(R.id.richLinkView);
        linkLayout = (LinearLayout) convertView.findViewById(R.id.linkLayout);
        title = (TextView) convertView.findViewById(R.id.title);
        description = (TextView) convertView.findViewById(R.id.description);
        website = (TextView) convertView.findViewById(R.id.website);
        imageView = (ImageView) convertView.findViewById(R.id.image);

        RichPreview richPreview = new RichPreview(new ResponseListener() {
            @Override
            public void onData(MetaData metaData) {
                data = metaData;

                //richLinkView.setLinkFromMeta(data);

                Log.d(TAG,"title: "+data.getTitle());
                Log.d(TAG,"description: "+data.getDescription());
                Log.d(TAG,"website: "+data.getSitename());
                Log.d(TAG,"imageView: "+data.getImageurl());
                title.setText(data.getTitle());
                description.setText(data.getDescription());
                website.setText(data.getSitename());
                Picasso.get().load(data.getImageurl()).into(imageView);
            }

            @Override
            public void onError(Exception e) {
                //handle error
            }
        });

        richPreview.getPreview(getItem(position).getLink());

        linkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(Uri.parse(getItem(position).getLink()));
            }
        });

        return convertView;
    }

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
