package com.example.appbansach.modle;

public class Account {
    private String username;
    private String password;
    private String role;

    private String sdt;
    private String Diachi;


    public Account() {
    }

    public Account(String username, String password, String role, String sdt, String Diachi) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.sdt = sdt;
        this.Diachi = Diachi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getsdt() {
        return sdt;
    }

    public void setsdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String Diachi) {
        this.Diachi = Diachi;
    }
}