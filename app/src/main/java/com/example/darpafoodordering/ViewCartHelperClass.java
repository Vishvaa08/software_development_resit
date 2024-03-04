package com.example.darpafoodordering;

public class ViewCartHelperClass {
    String name;
    String quantity;
    String price;
    String image;

    public ViewCartHelperClass(String name, String quantity, String price, String image) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    ViewCartHelperClass(){

    }
}
