package com.example.darpafoodordering;

public class viewStudentsHelperClass {

    String name;
    String phoneNum;
    String email;
    String userCredit;
    String status;
    String userID;
    String user_picture;

    public viewStudentsHelperClass(String name, String phoneNum, String email, String userCredit, String status, String userID, String user_picture) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.userCredit = userCredit;
        this.status = status;
        this.userID = userID;
        this.user_picture = user_picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(String userCredit) {
        this.userCredit = userCredit;
    }

    public String getStatus(){return status;}

    public void setStatus(String status){this.status = status;}

    public String getUserID(){return userID;}
    public void setUserID(String userID){this.userID = userID;}

    public String getUser_picture(){return user_picture;}
    public void setUser_picture(String user_picture){this.user_picture = user_picture;}

    viewStudentsHelperClass(){

    }

}
