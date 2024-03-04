package com.example.darpafoodordering;

public class viewMenuHelperClass {

    String image;
    String name;
    String price;
    String category;
    String foodtype;
    String status;

    public viewMenuHelperClass(String image, String name, String price, String category, String foodtype, String status) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.category = category;
        this.foodtype = foodtype;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getfoodtype() {
        return foodtype;
    }

    public void setfoodtype(String foodtype) {
        this.foodtype = foodtype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    viewMenuHelperClass(){

    }

}
