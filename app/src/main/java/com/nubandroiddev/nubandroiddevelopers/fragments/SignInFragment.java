package com.nubandroiddev.nubandroiddevelopers.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nubandroiddev.nubandroiddevelopers.PublicVariables;
import com.nubandroiddev.nubandroiddevelopers.R;

public class SignInFragment extends Fragment {
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;

    private TextInputEditText email;
    private TextInputEditText password;
    private Button login;

    private TextView signup;

    private FirebaseAuth mAuth;

    private static final String TAG = "SignIn";

    String errorMessage = "Invalid input";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_signin, container, false);

        emailLayout = (TextInputLayout) root.findViewById(R.id.emailLayout);
        passwordLayout = (TextInputLayout) root.findViewById(R.id.passwordLayout);

        email = (TextInputEditText) root.findViewById(R.id.emailid);
        password = (TextInputEditText) root.findViewById(R.id.passwordid);

        login = (Button) root.findViewById(R.id.loginid);
        signup = (TextView) root.findViewById(R.id.signupid);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUp();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateEmail(extractEmail())){
                    emailLayout.setError(null);
                }
                else{
                    emailLayout.setError(errorMessage);
                }

                if(validatePassword(extractPassword())){
                    passwordLayout.setError(null);
                }
                else{
                    passwordLayout.setError(errorMessage);
                }

                if(validateEmail(extractEmail()) && validatePassword(extractPassword())){
                    signInUser(extractEmail(),extractPassword());
                }
            }
        });

        return root;
    }

    void openProfileFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new ProfileFragment()).commit();
    }

    void openSignUp() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new SignUpFragment()).commit();

    }

    String extractEmail() {
        return email.getText().toString();
    }

    String extractPassword() {
        return password.getText().toString();
    }

    boolean validateEmail(String username) {
        if (username.length() == 0 || username.length() > 30) return false;
        return true;
    }

    boolean validatePassword(String password) {
        if (password.length() == 0 || password.length() > 20) return false;
        return true;
    }

    void signInUser(String email,String password){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Signing in");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            progressDialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    void updateUI(FirebaseUser firebaseUser){
        if(firebaseUser!=null){
            PublicVariables.currentUser = firebaseUser;
            Toast.makeText(getActivity(), "Welcome",
                    Toast.LENGTH_SHORT).show();
            openProfileFragment();
        }
        else{
            Toast.makeText(getActivity(), "Invalid username/password.",
                    Toast.LENGTH_SHORT).show();
        }

    }
}