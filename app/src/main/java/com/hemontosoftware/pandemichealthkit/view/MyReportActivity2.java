package com.hemontosoftware.pandemichealthkit.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hemontosoftware.pandemichealthkit.R;

public class MyReportActivity2 extends AppCompatActivity {

    RadioGroup breathRadioGroup, painRadioGroup, speechRadioGroup;
    Button nextButton, previousButton;
    int counter = 0, previousNumber;
    int breath = 0, pain = 0, speech = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_report2);
        Toolbar toolbar = findViewById(R.id.contact_tracing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        previousNumber = getIntent().getIntExtra("score", 0);

        breathRadioGroup = findViewById(R.id.breath_radio_group);
        painRadioGroup = findViewById(R.id.pain_radio_group);
        speechRadioGroup = findViewById(R.id.speech_radio_group);

        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);

        breathRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.breath_yes) {
                    breath = 1;
                } else {
                    breath = 0;
                }
            }
        });
        painRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pain_yes){
                    pain = 1;
                }else {
                    pain = 0;
                }
            }
        });
        speechRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.speech_yes){
                    speech = 1;
                }else {
                    speech = 0;
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyReportActivity2.this, MyReportActivity.class));
                finish();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                if (breath == 1){
                    counter = counter+16;
                }if (pain == 1){
                    counter = counter+16;
                }if (speech == 1){
                    counter = counter+16;
                }
                counter = counter+previousNumber;
                Intent intent = new Intent(MyReportActivity2.this, MyReportActivity3.class);
                intent.putExtra("score2", counter);
                startActivity(intent);
            }
        });
    }
}