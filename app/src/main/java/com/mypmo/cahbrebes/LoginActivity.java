package com.mypmo.cahbrebes;

import static android.content.ContentValues.TAG;
import static android.util.Patterns.EMAIL_ADDRESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
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
import com.mypmo.cahbrebes.Prevalent.Prevalent;
import com.mypmo.cahbrebes.model.Users;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextView tvLogin, AdminLink, NotAdminLink;
    EditText etEmailLog, etPasswordLog,etPhoneLog;
    ProgressBar progressBar;
    private String parentDbName = "Users";
    DatabaseReference getReference;
    FirebaseAuth mAuth;
    FirebaseUser user;

//    Iklan
    private AdView mAdView;
    private RewardedAd mRewardedAd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Mengecek jika user udah ada dan email udah verified maka langsung ke halaman main
        if (user != null && user.isEmailVerified()) {
            startActivity(new Intent(LoginActivity.this, ListDataProduk.class));
            finish();
        }
        // Iklan banner
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adViewLogin);
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

        // Iklan Reward
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();



        // Inisialisasi ID
        etEmailLog = findViewById(R.id.emailLog);
        etPasswordLog = findViewById(R.id.passwordLog);
        etPhoneLog = findViewById(R.id.phoneLogin);
        tvLogin = findViewById(R.id.tvLogin);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progres_login);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);
        progressBar.setVisibility(View.GONE);
//
//        progress = new ProgressDialog(this);
//        progress.setMessage("Loading data ...");
//        progress.setCancelable(false);

// Ketika button login di klik
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                login();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                btnLogin.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                btnLogin.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });

        // ketika button register di klik
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    // proses login
    private void login() {
        String email = etEmailLog.getText().toString();
        String password = etPasswordLog.getText().toString();
        String phone = etPhoneLog.getText().toString();

        if (email.isEmpty()) {
            etEmailLog.setError("Email tidak boleh kosong.");
            etEmailLog.requestFocus();
        } else if (!EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailLog.setError("Email tidak valid.");
            etEmailLog.requestFocus();
        } else if (password.isEmpty()) {
            etPasswordLog.setError("Password tidak boleh kosong.");
            etPasswordLog.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
//                        checkIfEmailVerified();
                        AllowAccessToAccount(phone, password);
                    }
                    else if (!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Tidak Dapat Masuk, Silahkan Coba Lagi"
                                , Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()){

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "Hallo admin...", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Login Berhasil...", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                if (mRewardedAd != null) {
                                    Activity activityContext = LoginActivity.this;
                                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                        @Override
                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                            // Handle the reward.
                                            startActivity( new Intent(LoginActivity.this, ListDataProduk.class));
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "Iklan reward tidak tersedia saat ini.");
                                }
//                                Intent intent = new Intent(LoginActivity.this, ListDataProduk.class);
                                Prevalent.currentOnlineUser = usersData;
//                                startActivity(intent);
                            }

                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this,"Password is incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Akun dengan nomor " + phone + " tidak ada.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    // verifikasi email
    private void checkIfEmailVerified()
    {
        user = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        getReference = database.getReference();

        if (user.isEmailVerified())
        {
            Toast.makeText(LoginActivity.this, "Berhasil login", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            if (mRewardedAd != null) {
                Activity activityContext = LoginActivity.this;
                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        startActivity( new Intent(LoginActivity.this, MainActivity.class));
                    }
                });
            } else {
                Log.d(TAG, "Iklan reward tidak tersedia saat ini.");
            }
        }
        else
        {
            showDialog();
        }
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Email kamu belum diverifikasi!");

        alertDialogBuilder
                .setMessage("Kirim ulang email verifikasi?")
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        sendVerificationEmail();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void sendVerificationEmail() {

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email verifikasi berhasil dikirim!", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email verifikasi tidak dapat dikirim!", Toast.LENGTH_SHORT).show();
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }
}