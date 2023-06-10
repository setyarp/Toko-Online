package com.mypmo.cahbrebes.model;

public class data_barang {

    private String namabarang;
    private String hargabarang;
    private String expired;
    private String stokbarang;
    private String deskripsi;
    private String imagebarang;
    private String key;

    public String getNamabarang() {
        return namabarang;
    }

    public void setNamabarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public String getHargabarang() {
        return hargabarang;
    }

    public void setHargabarang(String hargabarang) {
        this.hargabarang = hargabarang;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getStokbarang() {
        return stokbarang;
    }

    public void setStokbarang(String stokbarang) {
        this.stokbarang = stokbarang;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImagebarang() {
        return imagebarang;
    }

    public void setImagebarang(String imagebarang) {
        this.imagebarang = imagebarang;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public data_barang() {
    }

    public data_barang(String namabarang, String hargabarang, String expired, String stokbarang, String deskripsi, String imagebarang) {
        this.namabarang = namabarang;
        this.hargabarang = hargabarang;
        this.expired = expired;
        this.stokbarang = stokbarang;
        this.deskripsi = deskripsi;
        this.imagebarang = imagebarang;
    }
}
