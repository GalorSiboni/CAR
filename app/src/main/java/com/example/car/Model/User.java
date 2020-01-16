package com.example.car.Model;

public class User {
    private String username;
    private String password;
    private String fName;
    private String sName;
    private String mail;

    public User(){
    }
    public User(String username,String password,String fName,String sName,String mail){
        this.username = username;
        this.password = password;
        this.fName = fName;
        this.sName = sName;
        this.mail = mail;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
}
