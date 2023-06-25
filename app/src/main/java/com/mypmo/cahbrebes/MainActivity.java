package com.mypmo.cahbrebes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Deklarasi Variable
    private CardView DataBarang, TambahBarang, DataPesanan;
    private TextView logout, emailLogin;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        // Deklarasi ID
        DataBarang = (CardView) findViewById(R.id.cv1);
        DataPesanan = (CardView) findViewById(R.id.cv2);
        TambahBarang = (CardView) findViewById(R.id.cv3);
        logout = findViewById(R.id.logout);
        emailLogin = findViewById(R.id.name);

        //        Mendapatkan Data Email yang sudah Login
        if (mAuth.getCurrentUser() != null) {
            emailLogin.setText(mAuth.getCurrentUser().getEmail());

        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, MainPage.class));
                finish();
            }
        });

        DataBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainActivity.this, ListDataBarang.class);
                startActivity(a);
            }
        });

        TambahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainActivity.this, tambah_barang.class);
                startActivity(a);
            }
        });

        DataPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(MainActivity.this, AdminNewOrdersActivity.class);
                startActivity(a);
            }
        });
    }

    // Keluar Aplikasi
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }
}