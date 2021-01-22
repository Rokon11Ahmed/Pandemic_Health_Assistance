package com.hemontosoftware.pandemichealthkit.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.loginRegister.LoginActivity;
import com.hemontosoftware.pandemichealthkit.uitlity.SessaionManager;

public class SplashScreenActivity extends AppCompatActivity {

    SessaionManager sessaionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sessaionManager = new SessaionManager(this);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (sessaionManager.get_user_login_status().equals("true")){
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }

    }
}