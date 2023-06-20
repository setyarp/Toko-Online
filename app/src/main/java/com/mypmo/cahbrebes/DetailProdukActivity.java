package com.mypmo.cahbrebes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailProdukActivity extends AppCompatActivity {

    ImageView detailGambar;
    TextView detailNama, detailHarga, detailExpired, detailDeskripsi;
    Button btnPesan;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        detailGambar = findViewById(R.id.detail_gambar);
        detailNama = findViewById(R.id.detail_nama);
        detailHarga = findViewById(R.id.detail_harga);
        detailExpired = findViewById(R.id.detail_expired);
        detailDeskripsi = findViewById(R.id.detail_deskripsi);
        btnPesan = findViewById(R.id.btnPesan);
    }
}