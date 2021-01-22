package com.hemontosoftware.pandemichealthkit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.adapter.BloodDonorRecyclerViewAdapter;
import com.hemontosoftware.pandemichealthkit.adapter.ContactTracingRecyclerViewAdapter;
import com.hemontosoftware.pandemichealthkit.loginRegister.LoginActivity;
import com.hemontosoftware.pandemichealthkit.model.BloodDonorModel;
import com.hemontosoftware.pandemichealthkit.model.ContactTracingModel;
import com.hemontosoftware.pandemichealthkit.uitlity.SessaionManager;

import java.util.ArrayList;
import java.util.List;

public class ContactTracingActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView contactTracingRecyclerView;
    SearchView searchView;
    ArrayList<ContactTracingModel> contactTracingList = new ArrayList<>();
    ContactTracingRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_tracing);
        Toolbar toolbar = findViewById(R.id.contact_tracing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = findViewById(R.id.contact_tracing_searchView);
        progressBar = findViewById(R.id.progress_bar);
        contactTracingRecyclerView = findViewById(R.id.contact_tracing_recyclerView);
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
        FirebaseFirestore.getInstance().collection("coronaSuspected")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList){
                            Log.d("FireBase Data", "onSuccess: "+snapshot.get("name").toString());
                            contactTracingList.add(new ContactTracingModel(snapshot.get("name").toString(), snapshot.get("age").toString(),snapshot.get("address").toString(),snapshot.get("time").toString()));
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
        adapter = new ContactTracingRecyclerViewAdapter(ContactTracingActivity.this, contactTracingList);
        contactTracingRecyclerView.setAdapter(adapter);
        contactTracingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}