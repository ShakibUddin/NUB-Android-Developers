package com.nubandroiddev.nubandroiddevelopers.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nubandroiddev.nubandroiddevelopers.PublicVariables;
import com.nubandroiddev.nubandroiddevelopers.R;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private static final String TAG = "ProfileFragment";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private TextView username;
    private TextView announcements;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        username = (TextView)root.findViewById(R.id.username);
        announcements = (TextView)root.findViewById(R.id.announcements);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logOut();
            return true;
        }
        return false;
    }

    void updateUI(FirebaseUser firebaseUser){
        if(firebaseUser == null){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                    new SignInFragment()).commit();
        }
        else{
            //get current user data
            // Name, email address, and profile photo Url
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();

            // Check if user's email is verified
            boolean emailVerified = firebaseUser.isEmailVerified();
            String uid = firebaseUser.getUid();

            if(emailVerified){
                username.setText(name);

                getAnnouncements(db);

            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Verify your email address")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logOut();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                logOut();
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
        }
    }
    void logOut(){
        FirebaseAuth.getInstance().signOut();
        PublicVariables.currentUser = null;
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new SignInFragment()).commit();
    }

    void getAnnouncements(FirebaseFirestore db){
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
                                for(String announcement : PublicVariables.announcements){
                                    announcements.append(announcement+"\n");
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}