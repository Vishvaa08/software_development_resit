package com.example.darpafoodordering;

public class ViewUserMenuHelperClass {

    String name;
    String price;
    String image;

    public ViewUserMenuHelperClass(String name, String price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    ViewUserMenuHelperClass(){

    }
}
