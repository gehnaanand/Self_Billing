package com.example.self_billing;

import java.io.Serializable;

public class Ordered_Item_Class implements Serializable {

    private String Name;
    private int Cost, Quantity;

    public Ordered_Item_Class(String name, int cost) {
        Name = name;
        Cost = cost;
        Quantity = 1;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int cost) {
        Cost = cost;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void addQuantity()
    {
        this.Quantity+=1;
    }

    public void removeQuantity(){
        if(this.Quantity > 1){
            this.Quantity-= 1;
        }
    }

}
