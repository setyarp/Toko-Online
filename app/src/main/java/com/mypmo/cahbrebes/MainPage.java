package com.mypmo.cahbrebes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPage extends AppCompatActivity {

    // Deklarasi Variable
    private Button pembeli, penjual;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Mengecek jika user udah ada dan email udah verified maka langsung ke halaman main
        if (user != null && user.isEmailVerified()) {
            startActivity(new Intent(MainPage.this, MainActivity.class));
            finish();
        }

        // Deklarasi ID
        pembeli = findViewById(R.id.pembeli);
        penjual = findViewById(R.id.penjual);

        pembeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainPage.this, ListDataProduk.class);
                startActivity(a);
            }
        });

        penjual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainPage.this, LoginActivity.class);
                startActivity(a);
            }
        });
    }
}