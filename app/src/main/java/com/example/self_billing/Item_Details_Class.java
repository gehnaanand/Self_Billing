package com.example.self_billing;

public class Item_Details_Class {
    String B_ID, Name;
    int cost;

    public Item_Details_Class() {
    }

    public Item_Details_Class(String b_ID, String name, int cost) {
        B_ID = b_ID;
        Name = name;
        this.cost = cost;
    }

    public String getB_ID() {
        return B_ID;
    }

    public void setB_ID(String b_ID) {
        B_ID = b_ID;
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
