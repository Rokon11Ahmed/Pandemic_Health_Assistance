package com.hemontosoftware.pandemichealthkit.loginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.uitlity.SessaionManager;
import com.hemontosoftware.pandemichealthkit.view.MainActivity;
import com.hemontosoftware.pandemichealthkit.view.ProfileActivity;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    LinearLayout enterNumberLayout, enterOTPLayout;
    EditText enterNumberEditText, enterOtpEditText;
    Button nextButton, loginButton;
    ProgressBar progressBar;
    String number;
    FirebaseAuth firebaseAuth;
    String codeSend;
    SessaionManager sessaionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        sessaionManager = new SessaionManager(getApplicationContext());
        enterNumberLayout = findViewById(R.id.enter_number_layout);
        enterOTPLayout = findViewById(R.id.login_code_layout);
        enterNumberEditText = findViewById(R.id.number_edittext);
        enterOtpEditText = findViewById(R.id.OTP_edittext);
        nextButton = findViewById(R.id.next_button);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = enterNumberEditText.getText().toString().trim();
                if (!number.isEmpty()){
                    enterNumberLayout.setVisibility(View.GONE);
                    enterOTPLayout.setVisibility(View.VISIBLE);
                    sendVerificationCode();
                }else {
                    enterNumberEditText.setError("Enter your number");
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode();
            }
        });
    }

    private void verifySignInCode() {
        String userCode = enterOtpEditText.getText().toString().trim();
        if (!userCode.isEmpty()){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSend, userCode);
            signInWithPhoneAuthCredential(credential);
        }else {
            enterOtpEditText.setError("Enter Code");
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            sessaionManager.set_user_login_status("true");
                            sessaionManager.set_user_number("+88"+number);
                            if (sessaionManager.get_user_profile_update_status().equals("true")){
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else {
                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                                finish();
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(LoginActivity.this, "verification code entered was invalid", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88"+number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity.this, ""+e, Toast.LENGTH_SHORT).show();
            Log.e("VerificationFailed", "onVerificationFailed: "+e );
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSend = s;
        }
    };
}