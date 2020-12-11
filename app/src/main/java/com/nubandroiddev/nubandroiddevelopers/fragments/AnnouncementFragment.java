package com.nubandroiddev.nubandroiddevelopers.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nubandroiddev.nubandroiddevelopers.R;
import com.nubandroiddev.nubandroiddevelopers.utils.PublicVariables;

public class AnnouncementFragment extends Fragment {
    private TextView announcements;
    FirebaseFirestore db;
    private static final String TAG = "AnnouncementFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_announcements, container, false);

        announcements =root.findViewById(R.id.announcements);

        getAnnouncements(db);
        return root;
    }

    void getAnnouncements(FirebaseFirestore db) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Announcements");
        progressDialog.show();
        PublicVariables.announcements.clear();
        db.collection("announcements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                PublicVariables.announcements.add(document.getString("Meeting"));
                            }
                            for (String announcement : PublicVariables.announcements) {
                                announcements.append(announcement + "\n");
                            }
                            progressDialog.dismiss();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
