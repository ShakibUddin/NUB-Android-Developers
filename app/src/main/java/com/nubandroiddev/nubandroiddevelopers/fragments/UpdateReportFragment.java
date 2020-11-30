package com.nubandroiddev.nubandroiddevelopers.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nubandroiddev.nubandroiddevelopers.R;

import java.util.Calendar;
import java.util.HashMap;

public class UpdateReportFragment extends Fragment {

    private static final String TAG = "ReportFragment";
    private TextInputLayout descriptionLayout;
    private TextInputEditText description;
    private Button submit;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_update_report, container, false);

        mAuth = FirebaseAuth.getInstance();

        descriptionLayout = (TextInputLayout) root.findViewById(R.id.descrioptionLayout);
        description = (TextInputEditText) root.findViewById(R.id.description);
        submit = (Button) root.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateDescription(extractDescription())) {
                    updateReport();
                    Toast.makeText(getContext(), "Report updated successfully.", Toast.LENGTH_LONG).show();
                } else {
                    descriptionLayout.setError("Description can not be empty");
                }
            }
        });

        return root;
    }

    String extractDescription() {
        return description.getText().toString();
    }

    boolean validateDescription(String description) {
        return description.length() != 0;
    }

    String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        if (hour > 12) {
            hour -= 12;
            return hour + ":" + min + "PM";
        }
        return hour + ":" + min + "AM";
    }

    String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return month + "/" + day + "/" + year;
    }

    HashMap<String, String> getReport() {
        HashMap<String, String> report = new HashMap<>();
        report.put("user", getCurrentUser());
        report.put("description", extractDescription());
        report.put("date", getCurrentDate());
        report.put("time", getCurrentTime());
        return report;
    }

    String getCurrentUser() {
        return mAuth.getCurrentUser().getDisplayName();
    }

    void updateReport() {
        db.collection("reports").document(getCurrentUser())
                .set(getReport())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        goBackToReports();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    void goBackToReports() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer,
                new ReportFragment());
        transaction.disallowAddToBackStack();
        transaction.commit();
    }
}