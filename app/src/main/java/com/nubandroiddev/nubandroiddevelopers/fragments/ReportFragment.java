package com.nubandroiddev.nubandroiddevelopers.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nubandroiddev.nubandroiddevelopers.R;
import com.nubandroiddev.nubandroiddevelopers.model.Report;
import com.nubandroiddev.nubandroiddevelopers.adapters.ReportAdapter;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ReportFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView reportListView;


    @Override
    public void onResume() {
        super.onResume();
        fetchReports();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_report, container, false);

        reportListView = (ListView) root.findViewById(R.id.reports);


        fetchReports();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.report_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logout:
                logOut();
                return true;
            case R.id.update_report:
                openUpdateReportFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void logOut(){
        FirebaseAuth.getInstance().signOut();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new SignInFragment()).commit();
    }

    void openUpdateReportFragment(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer,
                new UpdateReportFragment());
        transaction.addToBackStack("ReportFragment");
        transaction.commit();
    }

    void fetchReports(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Reports");
        progressDialog.show();
        ArrayList<Report> reports= new ArrayList<>();
        db.collection("reports").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() ){
                    if(task.getResult() != null){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Report report = document.toObject(Report.class);
                            reports.add(report);
                        }
                        //initializing custom array adapter
                        ReportAdapter reportAdapter = new ReportAdapter(getContext(),R.layout.report_card_style,reports);
                        //setting adapter to list view
                        reportListView.setAdapter(reportAdapter);
                        progressDialog.dismiss();
                        if(reports.size() == 0){
                            Toast.makeText(getContext(),"No reports yet",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getContext(),"Network error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}