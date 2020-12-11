package com.nubandroiddev.nubandroiddevelopers.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nubandroiddev.nubandroiddevelopers.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private static final String TAG = "ProfileFragment";
    private CircleImageView cameraIcon, circleImageView;
    Uri imageUri;
    private static final int PIC_IMAGE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private TextView username;
    private Button announcements;
    private Button report;
    private Button chat;
    private Button link;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        username = (TextView) root.findViewById(R.id.username);
        announcements = (Button) root.findViewById(R.id.announcements);
        report = (Button) root.findViewById(R.id.report);
        chat = (Button) root.findViewById(R.id.chat);
        link = (Button) root.findViewById(R.id.link);

        circleImageView=(CircleImageView)root.findViewById(R.id.profile_image);
        cameraIcon=(CircleImageView)root.findViewById(R.id.camera);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReportFragment();
            }
        });

        announcements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AnnouncementFragment()).commit();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ChatFragment()).commit();
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLinksFragment();
            }
        });

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

    //open chat fragmnet and announcemnets like this methid
    //keep this line [ transaction.addToBackStack("ProfileFragment") ] same
    //pass your fragment name in replace method
    void openReportFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer,
                new ReportFragment());
        transaction.addToBackStack("ProfileFragment");
        transaction.commit();
    }

    void openLinksFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer,
                new LinkFragment());
        transaction.addToBackStack("ProfileFragment");
        transaction.commit();
    }

    void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser == null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                    new SignInFragment()).commit();
        } else {
            //get current user data
            // Name, email address, and profile photo Url
            String name = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();

            // Check if user's email is verified
            boolean emailVerified = firebaseUser.isEmailVerified();
            String uid = firebaseUser.getUid();

            if (emailVerified) {
                username.setText(name);
                //create on click listener for open dialog box
                cameraIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });
//                getAnnouncements(db);

            } else {
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

    void logOut() {
        FirebaseAuth.getInstance().signOut();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new SignInFragment()).commit();
    }


    //create dialog box
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    openCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    openGallery();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }


    //open camera and capture picture
    private void openCamera() {
        Intent camera =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAMERA_REQUEST_CODE);
    }

    //open gallery to select picture
    private void openGallery() {
        Intent gallery=new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery,PIC_IMAGE);
    }
    //save the picture in circleimageview
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap= (Bitmap) data.getExtras().get("data");
                circleImageView.setImageBitmap(bitmap);
            }
        }else if (requestCode==PIC_IMAGE&&resultCode==Activity.RESULT_OK){
            imageUri=data.getData();
            circleImageView.setImageURI(imageUri);
        }
    }
//    void getAnnouncements(FirebaseFirestore db){
//        ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Loading Announcements");
//        progressDialog.show();
//        PublicVariables.announcements.clear();
//        db.collection("announcements")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                PublicVariables.announcements.add(document.getString("Meeting"));
//                            }
//                            for(String announcement : PublicVariables.announcements){
//                                announcements.append(announcement+"\n");
//                            }
//                            progressDialog.dismiss();
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//    }
}