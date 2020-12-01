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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nubandroiddev.nubandroiddevelopers.R;
import com.nubandroiddev.nubandroiddevelopers.adapters.LinkAdapter;
import com.nubandroiddev.nubandroiddevelopers.adapters.ReportAdapter;
import com.nubandroiddev.nubandroiddevelopers.model.Link;
import com.nubandroiddev.nubandroiddevelopers.model.Report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class LinkFragment extends Fragment {
    private ListView linkViewList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextInputLayout linkInputLayout;
    private TextInputEditText linkInputBox;
    private Button post;
    private static final String TAG = "LinkFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_link, container, false);

        linkViewList = (ListView) root.findViewById(R.id.linkViewList);
        linkInputLayout = (TextInputLayout) root.findViewById(R.id.linkInputLayout);
        linkInputBox = (TextInputEditText) root.findViewById(R.id.linkInputBox);
        post = (Button) root.findViewById(R.id.post);

        fetchLinks();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateLink()){
                    postLink(extractLink());
                    fetchLinks();
                    linkInputLayout.setError(null );
                }
                else{
                    linkInputLayout.setError("Enter an url");
                }
            }
        });

        return root;
    }

    void fetchLinks(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Links");
        progressDialog.show();
        ArrayList<Link> links= new ArrayList<>();
        db.collection("links").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() ){
                    if(task.getResult() != null){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Link link = document.toObject(Link.class);
                            links.add(link);
                        }
                        //reversing links
                        Collections.reverse(links);
                        //initializing custom array adapter
                        LinkAdapter linkAdapter = new LinkAdapter(getContext(),R.layout.rich_link_view,links);
                        //setting adapter to list view
                        linkViewList.setAdapter(linkAdapter);
                        progressDialog.dismiss();
                        if(links.size() == 0){
                            Toast.makeText(getContext(),"No links yet",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getContext(),"Network error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void postLink(String link){
        HashMap<String,Object> data = new HashMap<>();
        data.put("link",link);
        db.collection("links").document()
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(getContext(), "Link posted successfully.", Toast.LENGTH_SHORT).show();
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

    String extractLink(){
        return linkInputBox.getText().toString().trim();
    }

    boolean validateLink(){
        return extractLink().length() != 0;
    }
}