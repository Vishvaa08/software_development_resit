package com.example.darpafoodordering;

public class ViewOrdersHelperClass {

    String name;
    String price;
    String quantity;
    String userID;
    String status;

    public ViewOrdersHelperClass(String name, String price, String quantity, String userID, String status) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.userID = userID;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    ViewOrdersHelperClass(){

    }

}
