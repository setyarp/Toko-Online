package com.mypmo.cahbrebes;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mypmo.cahbrebes.activity.InputTanggal;
import com.mypmo.cahbrebes.model.data_barang;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class update_barang extends AppCompatActivity {

    // Deklarasi Variabel
    private TextView outputTanggal;
    private EditText updateNamaBarang, updateHargaBarang, updateStock, updateDeskripsi;
    private ImageView imageBaru;
    private Button btnUpdateTask, inputTanggal, uploadBaru;
    private ProgressBar progressBar;

    private String cekNamaBarang, cekHargaBarang, cekExpired, cekStock, cekDeskripsi, cekImageBarang;

    // kode gambar
    final int kodeGallery = 2, kodeKamera = 1;
    Uri fotoUrl;

    private DatabaseReference database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_barang);

        // Deklarasi ID
        updateNamaBarang = findViewById(R.id.newNamabarang);
        updateHargaBarang = findViewById(R.id.newHargaBarang);
        inputTanggal = findViewById(R.id.new_input_tanggal);
        outputTanggal = findViewById(R.id.new_output_tanggal);
        updateStock = findViewById(R.id.newStock);
        updateDeskripsi = findViewById(R.id.newDeskripsi);
        imageBaru = findViewById(R.id.newlookimage);
        uploadBaru = findViewById(R.id.newuploadimage);
        progressBar = findViewById(R.id.progres_updatebarang);
        progressBar.setVisibility(View.GONE);

        btnUpdateTask = findViewById(R.id.btnUpdatebarang);

        //        Ketika Tanggal Dipencet
        inputTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TampilTanggal();
            }
        });

        // Tombol upload gambar
        uploadBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUpload = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentUpload, 2);
            }
        });

        database = FirebaseDatabase.getInstance().getReference();
        GetData();

        // Tombol Update
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                StorageReference filePath = mStorage.getReference().child("images/" + UUID.randomUUID().toString());
                filePath.putFile(fotoUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> download = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                cekNamaBarang = updateNamaBarang.getText().toString();
                                cekHargaBarang = updateHargaBarang.getText().toString();
                                cekExpired = outputTanggal.getText().toString();
                                cekStock = updateStock.getText().toString();
                                cekDeskripsi = updateDeskripsi.getText().toString();
                                cekImageBarang = imageBaru.getDisplay().toString();

                                if(isEmpty(cekNamaBarang) || isEmpty(cekHargaBarang) || isEmpty(cekExpired) || isEmpty(cekStock) || isEmpty(cekDeskripsi)){
                                    Toast.makeText(update_barang.this,"Data tidak boleh kosong ",Toast.LENGTH_SHORT).show();
                                }else{

                                    data_barang setBarang = new data_barang();
                                    setBarang.setNamabarang(updateNamaBarang.getText().toString());
                                    setBarang.setHargabarang(updateHargaBarang.getText().toString());
                                    setBarang.setExpired(outputTanggal.getText().toString());
                                    setBarang.setStokbarang(updateStock.getText().toString());
                                    setBarang.setDeskripsi(updateDeskripsi.getText().toString());
                                    setBarang.setImagebarang(task.getResult().toString());

                                    updateBarang(setBarang);
                                }

                            }
                        });
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == kodeGallery && resultCode == RESULT_OK) {
            fotoUrl = data.getData();
            imageBaru.setImageURI(fotoUrl);
        }
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    private void GetData() {

        final String getNamabarang = getIntent().getExtras().getString("dataNamabarang");
        final String getHargabarang = getIntent().getExtras().getString("dataHargabarang");
        final String getExpired = getIntent().getExtras().getString("dataExpired");
        final String getStock = getIntent().getExtras().getString("dataStockbarang");
        final String getDeskripsi = getIntent().getExtras().getString("dataDeskripsi");
        final String getImage = getIntent().getExtras().getString("dataImage");

        Picasso.get().load(getImage).into(imageBaru);

        // Set variable setelah dipilih untuk update
        updateNamaBarang.setText(getNamabarang);
        updateHargaBarang.setText(getHargabarang);
        outputTanggal.setText(getExpired);
        updateStock.setText(getStock);
        updateDeskripsi.setText(getDeskripsi);
    }

    private void updateBarang(data_barang barang) {
        String getKey = getIntent().getExtras().getString("dataPrimaryKey");
        database.child("Admin")
                .child("DataBarang")
                .child(getKey)
                .setValue(barang)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(update_barang.this,"Data produk berhasil di update",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        finish();
                    }
                });


    }

    private void TampilTanggal() {
        InputTanggal inputTanggal = new InputTanggal();
        inputTanggal.show(getSupportFragmentManager(), "data");
        inputTanggal.setOnDateClickListener(new InputTanggal.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String tahun = "" + datePicker.getYear();
                String bulan = "" + (datePicker.getMonth() + 1);
                String hari = "" + datePicker.getDayOfMonth();
                String text = hari + " - " + bulan + " - " + tahun;
                outputTanggal.setText(text);
            }
        });

    }
}