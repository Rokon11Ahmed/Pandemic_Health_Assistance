package com.hemontosoftware.pandemichealthkit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.loginRegister.LoginActivity;
import com.hemontosoftware.pandemichealthkit.model.User;
import com.hemontosoftware.pandemichealthkit.uitlity.SessaionManager;

public class ProfileActivity extends AppCompatActivity {

    EditText name, phoneNumber, age, address;
    SessaionManager sessaionManager;
    Spinner spinner;
    ImageView bloodGroupImageView;
    TextView bloodGroup;
    Button saveButton;
    ProgressBar progressBar;

    String userName, userAge, userBloodGroup, userAddress;
    int selectedItemPosition;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.contact_tracing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessaionManager = new SessaionManager(getApplicationContext());

        firestore = FirebaseFirestore.getInstance();
        sessaionManager = new SessaionManager(getApplicationContext());
        name = findViewById(R.id.user_name);
        phoneNumber = findViewById(R.id.phone_number);
        age = findViewById(R.id.user_age);
        address = findViewById(R.id.user_address);
        spinner = findViewById(R.id.blood_group_spinner);
        bloodGroup = findViewById(R.id.blood_group_textview);
        bloodGroupImageView = findViewById(R.id.blood_group_image);
        saveButton = findViewById(R.id.save_button);
        progressBar = findViewById(R.id.progress_bar);
        phoneNumber.setText(sessaionManager.get_user_number());

        if (sessaionManager.get_user_profile_update_status().equals("true")) {
            getUserData();
        }
        final String bloodGroups[] = {"Select blood group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.profile_spinner_single_row, R.id.spinner_text, bloodGroups);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemPosition = position;
                bloodGroup.setText(bloodGroups[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });
        bloodGroupImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = name.getText().toString().trim();
                userAge = age.getText().toString().trim();
                if (selectedItemPosition == 0) {
                    Toast.makeText(ProfileActivity.this, "Select blood group", Toast.LENGTH_SHORT).show();
                    return;
                }
                userBloodGroup = bloodGroups[selectedItemPosition];
                userAddress = address.getText().toString().trim();

                if (userName.isEmpty() || userAge.isEmpty() || userAddress.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Information correctly", Toast.LENGTH_SHORT).show();
                } else {
                    insertData();
                }
            }
        });
    }

    private void getUserData() {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference myRef = database.getReference().child("users").child(sessaionManager.get_user_number());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                name.setText(sessaionManager.get_user_name());
                age.setText(sessaionManager.get_user_age());
                bloodGroup.setText(sessaionManager.get_user_blood_group());
                address.setText(sessaionManager.get_user_address());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void insertData() {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference myRef = database.getReference("users").child(sessaionManager.get_user_number());
        User user = new User(userName, sessaionManager.get_user_number(), userAge, userBloodGroup, userAddress);
        myRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                sessaionManager.set_user_name(userName);
                sessaionManager.set_user_age(userAge);
                sessaionManager.set_user_blood_group(userBloodGroup);
                sessaionManager.set_user_address(userAddress);

                Toast.makeText(ProfileActivity.this, "Data Update Successfully", Toast.LENGTH_SHORT).show();
                sessaionManager.set_user_profile_update_status("true");
                saveBloodDonorData();
                getUserData();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBloodDonorData() {
        progressBar.setVisibility(View.VISIBLE);
        CollectionReference reference = firestore.collection("bloodDonor");
        User user = new User(userName, sessaionManager.get_user_number(), userAge, userBloodGroup, userAddress);
        reference.document(sessaionManager.get_user_number()).set(user)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}