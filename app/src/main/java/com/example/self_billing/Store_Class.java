package com.example.self_billing;

public class Store_Class {
    String Store_Name, Phone, Store_Email;

    public Store_Class(String store_Name, String phone, String store_Email) {
        Store_Name = store_Name;
        Phone = phone;
        Store_Email = store_Email;
    }

    public Store_Class() {
    }

    public String getStore_Name() {
        return Store_Name;
    }

    public void setStore_Name(String store_Name) {
        Store_Name = store_Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getStore_Email() {
        return Store_Email;
    }

    public void setStore_Email(String store_Email) {
        Store_Email = store_Email;
    }
}
