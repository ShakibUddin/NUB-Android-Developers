package com.nubandroiddev.nubandroiddevelopers.shakib;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nubandroiddev.nubandroiddevelopers.R;

public class Skills extends Fragment {
    public Skills() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sakib_skills, container, false);

        ListView mySkillsListView = (ListView) rootView.findViewById(R.id.mySkillsList);

        ArrayAdapter<CharSequence> skillAdapter = ArrayAdapter.createFromResource(rootView.getContext(),R.array.sakib_skills, android.R.layout.simple_dropdown_item_1line);
        mySkillsListView.setAdapter(skillAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }
}