package com.nubandroiddev.nubandroiddevelopers.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nubandroiddev.nubandroiddevelopers.R;
import com.nubandroiddev.nubandroiddevelopers.adapters.LinkAdapter;
import com.nubandroiddev.nubandroiddevelopers.model.Link;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LinkFragment extends Fragment {
    private ListView linkViewList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextInputEditText searchInputBox;
    private LinkAdapter linkAdapter;
    private static final String TAG = "LinkFragment";

    private View root;
    private TextInputLayout titleLayout;
    private TextInputLayout linkLayout;
    private TextInputEditText title;
    private TextInputEditText link;
    private Button post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_link, container, false);

        setHasOptionsMenu(true);

        linkViewList = (ListView) root.findViewById(R.id.linkViewList);
        searchInputBox = (TextInputEditText) root.findViewById(R.id.searchInputBox);

        fetchLinks();

        searchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                linkAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.link_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.post) {
            openBottomSheet(root);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void fetchLinks() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Links");
        progressDialog.show();
        ArrayList<Link> links = new ArrayList<>();
        db.collection("links").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Link link = document.toObject(Link.class);
                            links.add(link);
                        }
                        //reversing links
                        Collections.reverse(links);
                        //initializing custom array adapter
                        linkAdapter = new LinkAdapter(getContext(), R.layout.rich_link_view, links);
                        //setting adapter to list view
                        linkViewList.setAdapter(linkAdapter);
                        progressDialog.dismiss();
                        if (links.size() == 0) {
                            Toast.makeText(getContext(), "No links yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void openBottomSheet(View root){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.SheetDialog);

        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout,(LinearLayout)root.findViewById(R.id.bottomSheetContainer));

        titleLayout = (TextInputLayout) bottomSheetView.findViewById(R.id.titleLayout);
        linkLayout = (TextInputLayout) bottomSheetView.findViewById(R.id.linkLayout);
        title = (TextInputEditText) bottomSheetView.findViewById(R.id.titleid);
        link = (TextInputEditText) bottomSheetView.findViewById(R.id.linkid);
        post = (Button) bottomSheetView.findViewById(R.id.postLink);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateLink(extractLink()) && validateTitle(extractTitle())){
                    postLink(extractTitle(),extractLink());
                    bottomSheetDialog.dismiss();
                }
                else{
                    if(validateTitle(extractTitle())){
                        titleLayout.setError(null);
                    }
                    else{
                        titleLayout.setError("Enter a title");
                    }
                    if(validateLink(extractLink())){
                        linkLayout.setError(null);
                    }
                    else{
                        linkLayout.setError("Enter a link");
                    }
                }
            }
        });
    }
    private String extractLink() {
        return link.getText().toString().trim();
    }

    private String extractTitle() {
        return title.getText().toString().trim();
    }

    private boolean validateTitle(String title) {
        return title.length() != 0;
    }

    private boolean validateLink(String link) {
        return link.length() != 0;
    }

    void postLink(String title, String link) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("link", link);
        db.collection("links").document()
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(getContext(), "Link posted successfully.", Toast.LENGTH_SHORT).show();
                        fetchLinks();
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
}