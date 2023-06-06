package com.mypmo.cahbrebes;

import static android.util.Patterns.EMAIL_ADDRESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextView tvLupaPass, tvLogin;
    EditText etEmailLog, etPasswordLog;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Mengecek jika user udah ada dan email udah verified maka langsung ke halaman main
        if (user != null && user.isEmailVerified()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // Inisialisasi ID
        etEmailLog = findViewById(R.id.emailLog);
        etPasswordLog = findViewById(R.id.passwordLog);
        tvLupaPass = findViewById(R.id.LupaPass);
        tvLogin = findViewById(R.id.tvLogin);
        btnLogin = findViewById(R.id.btnLogin);

//
//        progress = new ProgressDialog(this);
//        progress.setMessage("Loading data ...");
//        progress.setCancelable(false);

// Ketika button login di klik
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
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
                        Toast.makeText(getApplicationContext(), "Login Succes", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    }
                    else if (!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Tidak Dapat Masuk, Silahkan Coba Lagi"
                                , Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}