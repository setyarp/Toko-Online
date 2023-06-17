package com.mypmo.cahbrebes.recycleview;

import static android.text.TextUtils.isEmpty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mypmo.cahbrebes.R;
import com.mypmo.cahbrebes.model.data_barang;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleViewAdapterProduk extends RecyclerView.Adapter<RecycleViewAdapterProduk.ViewHolder> {

    //Deklarasi Variable
    private ArrayList<data_barang> listProduk;
    private Context context;

    public RecycleViewAdapterProduk(ArrayList<data_barang> listProduk, Context context) {
        this.listProduk = listProduk;
        this.context = context;
    }

    @NonNull
    @Override
    public RecycleViewAdapterProduk.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_data_produk, parent, false);
        return new RecycleViewAdapterProduk.ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterProduk.ViewHolder holder, int position) {

        // Mengambil nilai berdasarkan posisi tertentu
        final String namabarang = listProduk.get(position).getNamabarang();
        final String hargabarang = listProduk.get(position).getHargabarang();
        final String gambar = listProduk.get(position).getImagebarang();

        // Menaruh di holder
        holder.NamaProduk.setText(namabarang);
        holder.HargaProduk.setText("Rp " + hargabarang);
        if (isEmpty(gambar)) {
            holder.imageViewProduk.setImageResource(R.drawable.ic_collections);
        } else {
            Picasso.get().load(gambar).into(holder.imageViewProduk);
        }
    }

    @Override
    public int getItemCount() {
        return listProduk.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //inisial Widget
        private TextView NamaProduk, HargaProduk;
        private LinearLayout ListItemProduk;
        private ImageView imageViewProduk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inisialisasi ID
            NamaProduk = itemView.findViewById(R.id.show_namaproduk);
            HargaProduk = itemView.findViewById(R.id.show_hargaproduk);
            imageViewProduk = itemView.findViewById(R.id.show_produk);
            ListItemProduk = itemView.findViewById(R.id.list_item_produk);
        }
    }
}
