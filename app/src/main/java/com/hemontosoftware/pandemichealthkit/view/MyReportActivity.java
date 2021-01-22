package com.hemontosoftware.pandemichealthkit.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.loginRegister.LoginActivity;
import com.hemontosoftware.pandemichealthkit.uitlity.SessaionManager;

public class MyReportActivity extends AppCompatActivity {

    RadioGroup feverRadioGroup, coughRadioGroup, tiredRadioGroup;
    Button nextPage;
    int counter = 0;
    int faver = 0, cough = 0, tired = 0;
    SessaionManager sessaionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_report);
        Toolbar toolbar = findViewById(R.id.contact_tracing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessaionManager = new SessaionManager(getApplicationContext());

        if (!sessaionManager.get_user_login_status().equals("true") && !sessaionManager.get_user_profile_update_status().equals("true")) {
            Toast.makeText(this, "checking", Toast.LENGTH_SHORT).show();
            checkVisibility();
        }
        feverRadioGroup = findViewById(R.id.fever_radio_group);
        coughRadioGroup = findViewById(R.id.cough_radio_group);
        tiredRadioGroup = findViewById(R.id.tired_radio_group);
        nextPage = findViewById(R.id.next_button);

        feverRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.fever_yes) {
                    faver = 1;
                } else {
                    faver = 0;
                }
            }
        });
        coughRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.cough_yes) {
                    cough = 1;
                } else {
                    cough = 0;
                }
            }
        });
        tiredRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.tired_yes) {
                    tired = 1;
                } else {
                    tired = 0;
                }
            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                if (faver == 1) {
                    counter = counter + 10;
                }
                if (cough == 1) {
                    counter = counter + 10;
                }
                if (tired == 1) {
                    counter = counter + 10;
                }
                Intent intent = new Intent(MyReportActivity.this, MyReportActivity2.class);
                intent.putExtra("score", counter);
                startActivity(intent);
            }
        });


    }

    private void checkVisibility() {
        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("You need to login or update your profile to continue");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}