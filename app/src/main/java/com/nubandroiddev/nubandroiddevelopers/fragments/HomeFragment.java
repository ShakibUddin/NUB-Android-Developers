package com.nubandroiddev.nubandroiddevelopers.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nubandroiddev.nubandroiddevelopers.Developers;
import com.nubandroiddev.nubandroiddevelopers.OurProjects;
import com.nubandroiddev.nubandroiddevelopers.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeFragment extends Fragment {

    private CarouselView carouselView;
    private Button developersPage;
    private Button projectsPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        carouselView = root.findViewById(R.id.carouselView);
        developersPage = (Button) root.findViewById(R.id.developersPage);
        projectsPage = (Button) root.findViewById(R.id.projectsPage);


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

        return root;
    }
    void openDevelopersPage(){
        Intent intent = new Intent(getContext(), Developers.class);
        startActivity(intent);
    }

    void openOurProjectsPage(){
        Intent intent = new Intent(getContext(), OurProjects.class);
        startActivity(intent);
    }
}