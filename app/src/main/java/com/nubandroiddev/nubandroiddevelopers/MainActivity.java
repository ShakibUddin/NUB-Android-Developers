package com.nubandroiddev.nubandroiddevelopers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nubandroiddev.nubandroiddevelopers.shakib.ShakibUddin;

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

    private ListView developers;
    //I used TreeMap as it automatically sorts elements
    private TreeMap<String,Integer> data;
    private ArrayList<Profile> profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //remove these 2 lines to show the use of logcat
        data = new TreeMap<>();
        profiles = new ArrayList<>();

        //adding name and image source to hashmap
        data.put("Evan",R.drawable.evan);
        data.put("Mannan",R.drawable.mannan);
        data.put("Raihan",R.drawable.raihan);
        data.put("Saddam",R.drawable.saddam);
        data.put("Sakib",R.drawable.sakib);
        data.put("Shohan",R.drawable.shohan);
        data.put("Tareq",R.drawable.tareq);

        //filling profiles list with profile object
        for(Map.Entry<String,Integer> entry: data.entrySet()){
            //initializing profile objects from map
            Profile profile = new Profile(entry.getKey(),entry.getValue());
            profiles.add(profile);
        }

        developers = (ListView) findViewById(R.id.developers);

        //initializing custom array adapter
        ProfileAdapter profileAdapter = new ProfileAdapter(getApplicationContext(),R.layout.profile,profiles);
        //setting adapter to list view
        developers.setAdapter(profileAdapter);

        developers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting clicked item object
                Profile clickedProfile = (Profile) adapterView.getItemAtPosition(i);
                //extracting name from that object
                String profileName = clickedProfile.getName();
                //calling openPage with the name
                switch (profileName){
                    case "Sakib":
                        openShakibUddin();
                        break;
                    //make your case with your name to call your method
                    default:
                        break;
                }
            }
        });
    }
    private void openShakibUddin(){
        Intent intent = new Intent(getApplicationContext(), ShakibUddin.class);
        startActivity(intent);
    }
    //add your methods to get your activity
}