package com.hemontosoftware.pandemichealthkit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.adapter.BloodDonorRecyclerViewAdapter;
import com.hemontosoftware.pandemichealthkit.model.BloodDonorModel;


import java.util.ArrayList;
import java.util.List;

public class BloodDonorActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView bloodDonorRecyclerView;
    SearchView searchView;

    ArrayList<BloodDonorModel> bloodDonorList = new ArrayList<>();
    BloodDonorRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donor);
        Toolbar toolbar = findViewById(R.id.blood_donor_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = findViewById(R.id.blood_donor_searchView);
        progressBar = findViewById(R.id.progress_bar);
        bloodDonorRecyclerView = findViewById(R.id.blood_donor_recyclerView);

        loadData();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void loadData() {

        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("bloodDonor")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList){
                            Log.d("FireBase Data", "onSuccess: "+snapshot.get("name").toString());
                            bloodDonorList.add(new BloodDonorModel(snapshot.get("name").toString(), snapshot.get("age").toString(),snapshot.get("address").toString(),snapshot.get("bloodGroup").toString(),snapshot.get("phone").toString()));
                        }
                        progressBar.setVisibility(View.GONE);
                        setAdapter();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("faild", "onFailure: "+e.toString() );
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setAdapter() {
        adapter = new BloodDonorRecyclerViewAdapter(BloodDonorActivity.this, bloodDonorList);
        bloodDonorRecyclerView.setAdapter(adapter);
        bloodDonorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}