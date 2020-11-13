  package com.nubandroiddev.nubandroiddevelopers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.nubandroiddev.nubandroiddevelopers.shakib.ShakibUddin;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//TODO  add your portfolio in your section
//      make a package of your name then create an activity their and start coding
//      update ui as you see fit
//      add log messages
//      add app icon

public class MainActivity extends AppCompatActivity {


    private CarouselView carouselView;
    private Button developersPage;
    private Button projectsPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carouselView = findViewById(R.id.carouselView);
        developersPage = (Button) findViewById(R.id.developersPage);
        projectsPage = (Button) findViewById(R.id.projectsPage);

        developersPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDevelopersPage();
            }
        });

        projectsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOurProjectsPage();
            }
        });

        //list of carosoul images
        int[] sampleImages = {R.drawable.one, R.drawable.two, R.drawable.three};

        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };


        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);


    }

    void openDevelopersPage(){
        Intent intent = new Intent(getApplicationContext(),Developers.class);
        startActivity(intent);
    }

    void openOurProjectsPage(){
        Intent intent = new Intent(getApplicationContext(),OurProjects.class);
        startActivity(intent);
    }
}