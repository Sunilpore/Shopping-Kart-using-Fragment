package com.example.sunil.cartadd.Model;

/**
 * Created by Sunil on 11/9/2017.
 */

public class UserModel{

    public int id;
    public String fullname;
    public String uname,pass;

    public UserModel() {
    }

    public UserModel(String fullname, String uname, String pass) {
        this.fullname = fullname;
        this.uname = uname;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
