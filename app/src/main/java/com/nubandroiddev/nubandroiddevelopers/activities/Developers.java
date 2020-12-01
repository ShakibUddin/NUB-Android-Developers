package com.nubandroiddev.nubandroiddevelopers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nubandroiddev.nubandroiddevelopers.R;
import com.nubandroiddev.nubandroiddevelopers.adapters.ProfileAdapter;
import com.nubandroiddev.nubandroiddevelopers.model.Profile;
import com.nubandroiddev.nubandroiddevelopers.activities.Tariqul.TariqIslam;
import com.nubandroiddev.nubandroiddevelopers.activities.nahid.NahidHasan;
import com.nubandroiddev.nubandroiddevelopers.activities.shakib.ShakibUddin;
import com.nubandroiddev.nubandroiddevelopers.activities.shohan.SohanIslam;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Developers extends AppCompatActivity {

    private ListView developers;
    //I used TreeMap as it automatically sorts elements
    private TreeMap<String,Integer> data;
    private ArrayList<Profile> profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        //remove these 2 lines to show the use of logcat
        data = new TreeMap<>();
        profiles = new ArrayList<>();

        //adding name and image source to treemap
        data.put("Evan",R.drawable.evan);
        data.put("Mannan",R.drawable.mannan);
        data.put("Raihan",R.drawable.raihan);
        data.put("Saddam",R.drawable.saddam);
        data.put("Sakib",R.drawable.sakib);
        data.put("Shohan",R.drawable.shohan);
        data.put("Tareq",R.drawable.tareq);
        data.put("Nahid",R.drawable.nahid);



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
                    case "Nahid":
                        openNahidHasan();
                        break;
                    //make your case with your name to call your method
                    case "Shohan":
                        openSohanIslam();
                        break;
                    case "Tareq":
                        openTariqulIslam();
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

    private void openNahidHasan() {
        Intent intent = new Intent(getApplicationContext(), NahidHasan.class);
        startActivity(intent);
    }
  
    private void openSohanIslam(){
        Intent intent = new Intent(getApplicationContext(), SohanIslam.class);
        startActivity(intent);
    }
    private void openTariqulIslam(){
        Intent intent = new Intent(getApplicationContext(), TariqIslam.class);
        startActivity(intent);
    }
}