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
import com.hemontosoftware.pandemichealthkit.adapter.MedicalShopRecyclerViewAdapter;
import com.hemontosoftware.pandemichealthkit.model.BloodDonorModel;
import com.hemontosoftware.pandemichealthkit.model.MedicineShopModel;

import java.util.ArrayList;
import java.util.List;

public class MedicineShopActivity extends AppCompatActivity {
    ProgressBar progressBar;
    RecyclerView medicineShopRecyclerView;
    SearchView searchView;

    ArrayList<MedicineShopModel> medicineShopList = new ArrayList<>();
    MedicalShopRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_shop);
        Toolbar toolbar = findViewById(R.id.medicine_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = findViewById(R.id.medicine_shop_searchView);
        progressBar = findViewById(R.id.progress_bar);
        medicineShopRecyclerView = findViewById(R.id.medicine_shop_recyclerView);

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
        FirebaseFirestore.getInstance().collection("medicineShop")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList){
                            Log.d("FireBase Data", "onSuccess: "+snapshot.get("name").toString());
                            medicineShopList.add(new MedicineShopModel(snapshot.get("name").toString(), snapshot.get("address").toString(),snapshot.get("phone").toString(),snapshot.get("website").toString()));
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
        adapter = new MedicalShopRecyclerViewAdapter(MedicineShopActivity.this, medicineShopList);
        medicineShopRecyclerView.setAdapter(adapter);
        medicineShopRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}