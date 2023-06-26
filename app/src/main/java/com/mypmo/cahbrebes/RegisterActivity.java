package com.mypmo.cahbrebes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText nama, emailReg, passwordReg, phone;
    Button registerReg;
    TextView tvLogin;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    //    Iklan
    private AdView mAdView;
    DatabaseReference getReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Iklan banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adViewRegister);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        emailReg = findViewById(R.id.emailRegis);
        nama = findViewById(R.id.fullname);
        passwordReg = findViewById(R.id.passwordRegis);
        phone = findViewById(R.id.phone);
        registerReg = findViewById(R.id.btnReg);
        tvLogin = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progres_register);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        registerReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                createUser();
//                progressBar.setVisibility(View.VISIBLE);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    private void createUser() {
        String email = emailReg.getText().toString();
        String password = passwordReg.getText().toString();
        String fullname = nama.getText().toString();
        String phonenum = phone.getText().toString();

        if (TextUtils.isEmpty(email)){
            emailReg.setError("Email Cannot be Empty");
            emailReg.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            passwordReg.setError("Password Cannot be Empty");
            passwordReg.requestFocus();
        }
        else  {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            getReference = database.getReference();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    getReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!(snapshot.child("Users").child(phonenum).exists())){
                                HashMap<String, Object> userdataMap = new HashMap<>();
                                userdataMap.put("phone", phonenum);
                                userdataMap.put("password", password);
                                userdataMap.put("name", fullname);
                                getReference.child("Users").child(phonenum).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            sendVerificationEmail();
                                            Toast.makeText(RegisterActivity.this, "User Berhasil Registrasi", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        }
                                        else if (!task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Nomor " + phonenum + " sudah ada.", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(RegisterActivity.this, "User Gagal Registrasi", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                    });
                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
        }
    }

    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Email verifikasi berhasil dikirim!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(RegisterActivity.this, "Email verifikasi tidak dapat dikirim!", Toast.LENGTH_SHORT).show();
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0,  0);
                            startActivity(getIntent());
                        }
                    }
                });
    }
}