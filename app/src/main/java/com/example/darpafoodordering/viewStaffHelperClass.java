package com.example.darpafoodordering;

public class viewStaffHelperClass {

    String phoneNum;
    String name;
    String email;
    String status;
    String user_picture;

    public viewStaffHelperClass(String name, String phoneNum, String email, String status, String user_picture) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.status = status;
        this.user_picture = user_picture;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getUser_picture(){return user_picture;}
    public void setUser_picture(String user_picture){this.user_picture = user_picture;}

    viewStaffHelperClass(){

    }

}
