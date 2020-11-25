  package com.nubandroiddev.nubandroiddevelopers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

//TODO  BUG:    Error icon is overlapping with toggle password icon

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.nubandroiddev.nubandroiddevelopers";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout)findViewById(R.id.fragmentContainer);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);

        //The getSharedPreferences() method (from the activity Context) opens the file at the given filename (sharedPrefFile) with the mode MODE_PRIVATE.
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        //I added this if statement to keep the selected fragment when rotating the device
        //and to initialise the first fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                    new HomeFragment()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                        selectedFragment).commit();

                return true;
            }
        });
    }
}