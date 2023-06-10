package com.mypmo.cahbrebes;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.UUID;

public class tambah_barang extends AppCompatActivity {

    // Deklarasi Variable
    private EditText inputNamaBarang, inputHargaBarang, inputStock, inputDeskripsi;
    private ImageView Image;
    private Button btnSaveTask, inputTanggal, btnGambar;
    private TextView outputTanggal;

    private String getNamaBarang, getHargaBarang, getExpired, getStock, getDeskripsi, getImageBarang;

    final int kodeGallery = 2, kodeKamera = 1;
    Uri fotoUrl;

    DatabaseReference getReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        // Deklarasi ID
        inputNamaBarang = findViewById(R.id.addNamabarang);
        inputHargaBarang = findViewById(R.id.addHargaBarang);
        inputTanggal = findViewById(R.id.input_tanggal);
        outputTanggal = findViewById(R.id.output_tanggal);
        inputStock = findViewById(R.id.addStock);
        inputDeskripsi = findViewById(R.id.addDeskripsi);
        Image = findViewById(R.id.lookimage);
        btnGambar = findViewById(R.id.uploadimage);

        btnSaveTask = findViewById(R.id.btnSavebarang);

        //        Ketika Tanggal Dipencet
        inputTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TampilTanggal();
            }
        });

        // Upload Gambar
        btnGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUpload = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentUpload, 2);
            }
        });

        // Tombol Save
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        getReference = database.getReference();
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                StorageReference filePath = mStorage.getReference().child("images/" + UUID.randomUUID().toString());
                filePath.putFile(fotoUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> download = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                getNamaBarang = inputNamaBarang.getText().toString();
                                getHargaBarang = inputHargaBarang.getText().toString();
                                getExpired = outputTanggal.getText().toString();
                                getStock = inputStock.getText().toString();
                                getDeskripsi = inputDeskripsi.getText().toString();
                                getImageBarang = task.getResult().toString();

                                checkBarang();

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
            Image.setImageURI(fotoUrl);
        }
    }

    private void checkBarang() {
        if(isEmpty(getNamaBarang) || isEmpty(getHargaBarang) || isEmpty(getStock) || isEmpty(getDeskripsi)){
            Toast.makeText(tambah_barang.this,"Data harus diisi",Toast.LENGTH_SHORT).show();
        } else{
            getReference.child("Admin").child("DataBarang").push()
                    .setValue(new data_barang(
                            getNamaBarang, getHargaBarang, getExpired, getStock, getDeskripsi, getImageBarang))
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(tambah_barang.this,"Berhasil menambah data produk", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(tambah_barang.this, ListDataBarang.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // mengatur agar ketika mengklik tombol kembali tidak looping pada activity sebelumnya
                            startActivity(intent);
                        }
                    });
        }
    }

    // Tanggal Picker
    private void TampilTanggal() {
        InputTanggal inputTanggal = new InputTanggal();
        inputTanggal.show(tambah_barang.this.getSupportFragmentManager(), "data");
        inputTanggal.setOnDateClickListener(new InputTanggal.onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String tahun = ""+datePicker.getYear();
                String bulan = ""+(datePicker.getMonth()+1);
                String hari = ""+datePicker.getDayOfMonth();
                String text = hari+" - "+bulan+" - "+tahun;
                outputTanggal.setText(text);
            }
        });

    }
}