package com.nubandroiddev.nubandroiddevelopers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private TextView username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        username = (TextView)root.findViewById(R.id.username);

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
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Verify your email address")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
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
}