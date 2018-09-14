package com.example.denishaamrutiya.demoscanbarcode.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ScanningItemPojo extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private String quntity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuntity() {
        return quntity;
    }

    public void setQuntity(String quntity) {
        this.quntity = quntity;
    }


  /*public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }*/
}
