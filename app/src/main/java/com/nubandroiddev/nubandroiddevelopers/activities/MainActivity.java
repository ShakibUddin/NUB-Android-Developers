  package com.nubandroiddev.nubandroiddevelopers.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nubandroiddev.nubandroiddevelopers.R;
import com.nubandroiddev.nubandroiddevelopers.fragments.HomeFragment;
import com.nubandroiddev.nubandroiddevelopers.fragments.ProfileFragment;

//TODO  add your portfolio in your section
//      make a package of your name then create an activity their and start coding
//      update ui as you see fit
//      add log messages
//      add app icon

//TODO  BUG:    Error icon is overlapping with toggle password icon



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableOfflinePersistence();

        frameLayout = (FrameLayout)findViewById(R.id.fragmentContainer);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);


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
    void enableOfflinePersistence(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }
}