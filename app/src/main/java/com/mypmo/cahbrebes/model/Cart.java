package com.mypmo.cahbrebes.model;



public class Cart {
    private String key,pname,price,quantity;

    public Cart() {
    }

    public Cart(String key, String pname, String price, String quantity) {
        this.key = key;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
