package com.example.self_billing;

public class Item_Details_Class {
    String Barcode_ID, Name;
    int cost;

    public Item_Details_Class() {
    }

    public Item_Details_Class(String BarcodeID, String Name, int Cost) {
        this.Barcode_ID = BarcodeID;
        this.Name = Name;
        this.cost = Cost;
    }

    public String getBarcodeID() {
        return Barcode_ID;
    }

    public void setBarcodeID(String BarcodeID) {
        Barcode_ID = BarcodeID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
