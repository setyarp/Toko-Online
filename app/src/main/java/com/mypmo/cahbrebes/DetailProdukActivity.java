package com.mypmo.cahbrebes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailProdukActivity extends AppCompatActivity {

    ImageView detailGambar;
    TextView detailNama, detailHarga, detailExpired, detailDeskripsi;
    Button btnPesan;
    private String productID="", state = "Normal", quantity="1";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        productID = getIntent().getStringExtra("pid");

        detailGambar = findViewById(R.id.detail_gambar);
        detailNama = findViewById(R.id.detail_nama);
        detailHarga = findViewById(R.id.detail_harga);
        detailExpired = findViewById(R.id.detail_expired);
        detailDeskripsi = findViewById(R.id.detail_deskripsi);
        btnPesan = findViewById(R.id.btnPesan);

        detailNama.setText(getIntent().getExtras().getString("namaproduk"));
        detailHarga.setText("Rp. " + getIntent().getExtras().getString("hargaproduk"));
        detailExpired.setText("- Baik digunakan sebelum : " + getIntent().getExtras().getString("expiredproduk"));
        detailDeskripsi.setText("- Deskripsi : " + getIntent().getExtras().getString("deskripsiproduk"));
        Picasso.get().load(getIntent().getExtras().getString("gambarproduk")).into(detailGambar);

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(DetailProdukActivity.this,"Anda dapat membeli banyak produk setelah pesanan dikonfirmasi",Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });
    }

    private void addingToCartList() {
        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd. yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",detailNama.getText().toString());
        cartMap.put("price",detailHarga.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",quantity);
//        cartMap.put("discount","");

        cartListRef.child("User view").child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    cartListRef.child("Admin")
                            .child("Products").child(productID)
                            .updateChildren(cartMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(DetailProdukActivity.this,"Ditambahkan ke keranjang",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(DetailProdukActivity.this,ListDataProduk.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });



    }
}