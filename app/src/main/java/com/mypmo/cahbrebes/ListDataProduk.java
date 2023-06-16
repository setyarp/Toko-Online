package com.mypmo.cahbrebes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mypmo.cahbrebes.model.data_barang;
import com.mypmo.cahbrebes.recycleview.RecycleViewAdapterBarang;
import com.mypmo.cahbrebes.recycleview.RecycleViewAdapterProduk;

import java.util.ArrayList;

public class ListDataProduk extends AppCompatActivity {

    // Deklarasi variable
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference getReference; // ini yg jadi dipake
    private ArrayList<data_barang> dataProduk;

    private EditText searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_produk);

        recyclerView = findViewById(R.id.datalistproduk);

        MyRecyclerView();
        GetData("");

        // Untuk membaca inputan text pada search
        searchView = findViewById(R.id.etSearchProduk);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null){
                    GetData(s.toString());
                }
                else {
                    GetData("");
                }

            }
        });
    }

    private void MyRecyclerView() {
        //        Menggunakan Layout dan Membuat List Secara vertical
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void GetData(String cari) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        getReference = database.getReference();

        if (cari.isEmpty() == true) {

            MyRecyclerView();
            getReference.child("Admin").child("DataBarang")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataProduk = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                data_barang barang = snapshot.getValue(data_barang.class);
                                barang.setKey(snapshot.getKey());
                                dataProduk.add(barang);
                            }

                            // Agar data yang baru diposisi paling atas
                            adapter = new RecycleViewAdapterProduk(dataProduk, ListDataProduk.this);
                            recyclerView.setAdapter(adapter);

                            int size = dataProduk.size();
//                            if(size>0){
//                                gambar.setVisibility(View.GONE);
//                            }else{
//                                gambar.setVisibility(View.VISIBLE);
//                            }
//                            progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Task failed to load", Toast.LENGTH_SHORT).show();
                            Log.e("MainActivity",databaseError.getDetails()+" "+databaseError.getMessage());

                        }
                    });
        }else{
            getReference.child("Admin").child("DataBarang").orderByChild("namabarang").startAt(cari).endAt(cari+"\uf8ff")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            dataProduk = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                data_barang barang = snapshot.getValue(data_barang.class);
                                barang.setKey(snapshot.getKey());
                                dataProduk.add(barang);

                            }
                            adapter = new RecycleViewAdapterProduk(dataProduk, ListDataProduk.this);
                            recyclerView.setAdapter(adapter);

                            int size = dataProduk.size();
//                            if(size>0){
//                                gambar.setVisibility(View.GONE);
//                            }else{
//                                gambar.setVisibility(View.VISIBLE);
//                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Task failed to load", Toast.LENGTH_SHORT).show();
                            Log.e("MainActivity",databaseError.getDetails()+" "+databaseError.getMessage());

                        }
                    });
        }
    }
}