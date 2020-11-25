package com.nubandroiddev.nubandroiddevelopers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedHashMap;
import java.util.concurrent.Executor;

public class SignUpFragment extends Fragment {
    private static final String TAG = "SignUp";

    private TextInputLayout usernameLayout;
    private TextInputLayout emailAddressLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private TextInputLayout pinLayout;

    private TextInputEditText username;
    private TextInputEditText emailAddress;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private TextInputEditText pin;
    private TextView signin;

    private Button createAccount;

    private LinkedHashMap<String, String> signUpData = new LinkedHashMap<>();

    private FirebaseAuth mAuth;

    boolean pinCodeValidated = false;
    String errorMessage = "Enter field";
    String passwordErrorMessage = "Enter at least 6 characters";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth = FirebaseAuth.getInstance();

        getPinFromFireStoreDatabase(db);

        usernameLayout = (TextInputLayout) root.findViewById(R.id.usernameLayout);
        emailAddressLayout = (TextInputLayout) root.findViewById(R.id.emailLayout);
        passwordLayout = (TextInputLayout) root.findViewById(R.id.passwordLayout);
        confirmPasswordLayout = (TextInputLayout) root.findViewById(R.id.confirmPasswordLayout);
        pinLayout = (TextInputLayout) root.findViewById(R.id.pinLayout);

        username = (TextInputEditText) root.findViewById(R.id.usernameid);
        emailAddress = (TextInputEditText) root.findViewById(R.id.emailid);
        password = (TextInputEditText) root.findViewById(R.id.passwordid);
        confirmPassword = (TextInputEditText) root.findViewById(R.id.confirmPasswordid);
        pin = (TextInputEditText) root.findViewById(R.id.pinid);

        createAccount = (Button) root.findViewById(R.id.createAccountid);

        signin = (TextView) root.findViewById(R.id.signinid);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignIn();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateUsername(extractUsername())) {
                    if (!signUpData.containsKey(extractUsername()))
                        usernameLayout.setError(null);
                    signUpData.put("username", extractUsername());
                } else {
                    usernameLayout.setError(errorMessage);
                }

                if (validateEmailAddress(extractEmailAddress())) {
                    if (!signUpData.containsKey(extractEmailAddress()))
                        signUpData.put("email", extractEmailAddress());
                    emailAddressLayout.setError(null);
                } else {
                    emailAddressLayout.setError(errorMessage);
                }

                if (validatePin(extractPin(),PublicVariables.mPincode)) {
                    pinCodeValidated = true;
                    pinLayout.setError(null);
                } else {
                    pinCodeValidated = false;
                    pinLayout.setError("Pin code is not correct");
                }

                if (validatePassword(extractPassword())) {
                    if (!signUpData.containsKey(extractPassword()))
                        signUpData.put("password", extractPassword());
                    passwordLayout.setError(null);
                } else {
                    passwordLayout.setError(passwordErrorMessage);
                }

                if (!validateConfirmPassword(extractPassword(), extractConfirmPassword())) {
                    confirmPasswordLayout.setError("Passwords don't match");
                }
                else{
                    if (signUpData.size() == 3 && pinCodeValidated) {
                        pinCodeValidated = false;
                        addNewUser(extractEmailAddress(),extractPassword());
                    }
                }
                for(String data:signUpData.values()){
                    Log.d(TAG,"data: "+data);
                }
            }
        });

        return root;
    }
    void openSignIn() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new SignInFragment()).commit();
    }

    String extractUsername() {
        return username.getText().toString();
    }

    String extractEmailAddress() {
        return emailAddress.getText().toString().trim();
    }

    String extractPassword() {
        return password.getText().toString();
    }

    String extractConfirmPassword() {
        return confirmPassword.getText().toString();
    }


    String extractPin() {
        return pin.getText().toString();
    }

    boolean validateUsername(String username) {
        return username.length() != 0;
    }

    boolean validateEmailAddress(String email) {
        return email.length() != 0;
    }

    boolean validatePassword(String password) {
        return password.length() >= 6;
    }

    boolean validateConfirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    boolean validatePin(String pin, String pinInDatabase) {
        return pin.equals(pinInDatabase);
    }

    void addNewUser(String email,String password){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Creating Account");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            PublicVariables.currentUser = user;
                            sendVerificationEmail();
                            createProfile(PublicVariables.currentUser);
                            openSignIn();
                            progressDialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "auth exception: ", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    void getPinFromFireStoreDatabase(FirebaseFirestore db) {
        db.collection("pin")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                PublicVariables.mPincode = document.getString("code");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    void createProfile(FirebaseUser firebaseUser){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(extractUsername())
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }
    void sendVerificationEmail(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }
}