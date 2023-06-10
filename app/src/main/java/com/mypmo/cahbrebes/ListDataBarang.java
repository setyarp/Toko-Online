package com.mypmo.cahbrebes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mypmo.cahbrebes.model.data_barang;
import com.mypmo.cahbrebes.recycleview.RecycleViewAdapterBarang;

import java.util.ArrayList;

public class ListDataBarang extends AppCompatActivity implements RecycleViewAdapterBarang.dataListener {

    // Deklarasi variable
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference getReference; // ini yg jadi dipake
    private ArrayList<data_barang> dataBarang;
    private FloatingActionButton floatingButton;

    //DEKLARASI SEARCH
    private EditText searchView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_barang);

        recyclerView = findViewById(R.id.datalistbarang);
        floatingButton = findViewById(R.id.floatingButton);
        MyRecyclerView();
        GetData("");

        // Untuk membaca inputan text pada search
        searchView = findViewById(R.id.etSearch);
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

        // on click floatingbutton
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(ListDataBarang.this, tambah_barang.class);
                startActivity(a);
            }
        });
    }

    private void MyRecyclerView() {
        //        Menggunakan Layout dan Membuat List Secara vertical
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
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
                            dataBarang = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                data_barang barang = snapshot.getValue(data_barang.class);
                                barang.setKey(snapshot.getKey());
                                dataBarang.add(barang);
                            }

                            // Agar data yang baru diposisi paling atas
                            adapter = new RecycleViewAdapterBarang(dataBarang, ListDataBarang.this);
                            recyclerView.setAdapter(adapter);

                            int size = dataBarang.size();
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
        }else{
            getReference.child("Admin").child("DataBarang").orderByChild("namabarang").startAt(cari).endAt(cari+"\uf8ff")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            dataBarang = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                data_barang barang = snapshot.getValue(data_barang.class);
                                barang.setKey(snapshot.getKey());
                                dataBarang.add(barang);

                            }
                            adapter = new RecycleViewAdapterBarang(dataBarang, ListDataBarang.this);
                            recyclerView.setAdapter(adapter);

                            int size = dataBarang.size();
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

    @Override
    public void onDeleteData(data_barang data, int position) {
        if (getReference != null) {
            getReference.child("Admin")
                    .child("DataBarang")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ListDataBarang.this, "Data Produk Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}