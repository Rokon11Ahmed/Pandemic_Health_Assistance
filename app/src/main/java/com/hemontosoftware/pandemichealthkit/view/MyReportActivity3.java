package com.hemontosoftware.pandemichealthkit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.model.CoronaUserModel;
import com.hemontosoftware.pandemichealthkit.model.User;
import com.hemontosoftware.pandemichealthkit.uitlity.SessaionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyReportActivity3 extends AppCompatActivity {

    RadioGroup achesRadioGroup, testRadioGroup, headacheRadioGroup, rashRadioGroup;
    Button save;
    int counter = 0, previousNumber;
    int aches = 0, test = 0, headache = 0, rash = 0;
    FirebaseFirestore firestore;
    SessaionManager sessaionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_report3);
        Toolbar toolbar = findViewById(R.id.contact_tracing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        previousNumber = getIntent().getIntExtra("score2", 0);
        firestore = FirebaseFirestore.getInstance();
        sessaionManager = new SessaionManager(getApplicationContext());

        achesRadioGroup = findViewById(R.id.aches_radio_group);
        testRadioGroup = findViewById(R.id.test_radio_group);
        headacheRadioGroup = findViewById(R.id.headache_radio_group);
        rashRadioGroup = findViewById(R.id.rash_radio_group);
        save = findViewById(R.id.save_button);

        achesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.aches_yes){
                    aches = 1;
                }else {
                    aches = 0;
                }
            }
        });

        testRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.test_yes){
                    test = 1;
                }else {
                    test = 0;
                }
            }
        });
        headacheRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.headach_yes){
                    headache = 1;
                }else {
                    headache = 0;
                }
            }
        });
        rashRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rash_yes){
                    rash = 1;
                }else {
                    rash = 0;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                if (aches == 1){
                    counter = counter+5;
                }if (test == 1){
                    counter = counter+5;
                }if (headache == 1){
                    counter = counter+5;
                }if (rash == 1){
                    counter = counter+5;
                }
                counter = counter+previousNumber;
                if (counter>50){
                    coronaPositive();
                }else {
                    coronaNegative();
                }

                sessaionManager.setNextReportTime(String.valueOf(System.currentTimeMillis()+86400000));
            }
        });
    }

    private void coronaNegative() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Corona Negative");
        alertDialog.setMessage("From survey we can say, currently you are corona Negative\n\nThank you for take survey\nStay home stay safe");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void coronaPositive() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Corona Positive");
        alertDialog.setMessage("From survey we can say, currently you are corona Positive. We suggest you take a COVID19 test to make sure it.\n\nThank you for take survey\nStay home stay safe");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        saveCoronaData();
                        finish();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void saveCoronaData() {
        CollectionReference reference = firestore.collection("coronaSuspected");
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String todayString = formatter.format(todayDate);

        CoronaUserModel user = new CoronaUserModel(sessaionManager.get_user_name(), sessaionManager.get_user_number(), sessaionManager.get_user_age(), sessaionManager.get_user_blood_group(), sessaionManager.get_user_address(), todayString);
        reference.document(sessaionManager.get_user_number()).set(user)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}