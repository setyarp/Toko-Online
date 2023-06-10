package com.mypmo.cahbrebes.recycleview;

import static android.text.TextUtils.isEmpty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mypmo.cahbrebes.ListDataBarang;
import com.mypmo.cahbrebes.R;
import com.mypmo.cahbrebes.model.data_barang;
import com.mypmo.cahbrebes.update_barang;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleViewAdapterBarang extends RecyclerView.Adapter<RecycleViewAdapterBarang.ViewHolder> {

    //Deklarasi Variable
    private ArrayList<data_barang> listBarang;
    private Context context;

    public interface dataListener{
        void onDeleteData(data_barang data, int position);
    }
    RecycleViewAdapterBarang.dataListener listener;

    public RecycleViewAdapterBarang(ArrayList<data_barang> listBarang, Context context) {
        this.listBarang = listBarang;
        this.context = context;
        listener = (ListDataBarang)context;
    }

    @NonNull
    @Override
    public RecycleViewAdapterBarang.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_data_barang, parent, false);
        return new RecycleViewAdapterBarang.ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterBarang.ViewHolder holder, int position) {

        // Mengambil nilai berdasarkan posisi tertentu
        final String namabarang = listBarang.get(position).getNamabarang();
        final String hargabarang = listBarang.get(position).getHargabarang();
        final String expired = listBarang.get(position).getExpired();
        final String stock = listBarang.get(position).getStokbarang();
        final String deskripsi = listBarang.get(position).getDeskripsi();
        final String gambar = listBarang.get(position).getImagebarang();

        // Menaruh di holder
        holder.NamaBarang.setText(": " + namabarang);
        holder.HargaBarang.setText(": " + hargabarang);
        holder.Expired.setText(": " + expired);
        holder.Stock.setText(": " + stock);
        holder.Deskripsi.setText(": " + deskripsi);
        if (isEmpty(gambar)) {
            holder.imageViewBarang.setImageResource(R.drawable.ic_collections);
        } else {
            Picasso.get().load(gambar).into(holder.imageViewBarang);
        }

        // Set listener saat di hold di item
        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] action = {"Ubah", "Hapus"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pilihan) {
                        switch (pilihan){
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("dataNamabarang",listBarang.get(position).getNamabarang());
                                bundle.putString("dataHargabarang",listBarang.get(position).getHargabarang());
                                bundle.putString("dataExpired",listBarang.get(position).getExpired());
                                bundle.putString("dataStockbarang",listBarang.get(position).getStokbarang());
                                bundle.putString("dataDeskripsi",listBarang.get(position).getDeskripsi());
                                bundle.putString("dataImage", listBarang.get(position).getImagebarang());
                                bundle.putString("dataPrimaryKey",listBarang.get(position).getKey());
                                Intent intent = new Intent(view.getContext(), update_barang.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;
                            case 1:
                                listener.onDeleteData(listBarang.get(position),position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return listBarang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //inisial Widget
        private TextView NamaBarang,HargaBarang, Expired, Stock, Deskripsi;
        private LinearLayout ListItem;
        private ImageView imageViewBarang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inisialisasi ID
            NamaBarang = itemView.findViewById(R.id.show_namabarang);
            HargaBarang = itemView.findViewById(R.id.show_hargabarang);
            Expired = itemView.findViewById(R.id.show_expired);
            Stock = itemView.findViewById(R.id.show_stockbarang);
            Deskripsi = itemView.findViewById(R.id.show_deskripsi);
            imageViewBarang = itemView.findViewById(R.id.viewGambarBarang);
            ListItem = itemView.findViewById(R.id.list_item_barang);
        }
    }
}
