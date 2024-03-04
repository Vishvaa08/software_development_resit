package com.example.darpafoodordering;

public class AdminViewUserDataClass {
    private String name;
    private String age;
    private String userID;
    private String phoneNum;
    private String userCredit;
    private String email;
    private String key;
    private String role;
    private String status;
    private String user_picture;

    public AdminViewUserDataClass() {

    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getName() { return name; }

    public String getAge() {
        return age;
    }

    public String getUserID() {
        return userID;
    }
    public String getPhoneNum(){return phoneNum;}
    public void setPhoneNum(String phoneNum){this.phoneNum = phoneNum;}
    public String getUserCredit(){return userCredit;}
    public void setUserCredit(String userCredit){this.userCredit = userCredit;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}
    public String getStatus(){return status;}
    public void setStatus(String status){this.status = status;}
    public String getUser_picture() {
        return user_picture;
    }
    public String getRole() {
        return role;
    }


    public AdminViewUserDataClass(String name, String age, String userID, String phoneNum, String userCredit, String email, String role, String status, String user_picture) {
        this.name = name;
        this.age = age;
        this.userID = userID;
        this.phoneNum = phoneNum;
        this.userCredit = userCredit;
        this.email = email;
        this.role = role;
        this.status = status;
        this.user_picture = user_picture;
    }


}