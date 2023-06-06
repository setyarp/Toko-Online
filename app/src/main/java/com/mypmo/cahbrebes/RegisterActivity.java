package com.mypmo.cahbrebes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText nama, emailReg, passwordReg;
    Button registerReg;
    TextView tvLogin;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        emailReg = findViewById(R.id.emailRegis);
        nama = findViewById(R.id.fullname);
        passwordReg = findViewById(R.id.passwordRegis);
        registerReg = findViewById(R.id.btnReg);
        tvLogin = findViewById(R.id.tvRegister);


        mAuth = FirebaseAuth.getInstance();

        registerReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        if (TextUtils.isEmpty(email)){
            emailReg.setError("Email Cannot be Empty");
            emailReg.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            passwordReg.setError("Password Cannot be Empty");
            passwordReg.requestFocus();
        }
        else  {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User Berhasil Registrasi", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                    else if (!task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User Gagal Registrasi", Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}