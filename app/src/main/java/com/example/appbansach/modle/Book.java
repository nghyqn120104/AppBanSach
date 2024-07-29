package com.example.appbansach.modle;

public class Book {
    private String maSach;
    private String tenSach;
    private String tenTheLoai;
    private String tacGia;
    private double donGia;
    private int soLuong;
    private String tenNXB;
    private String imageUrl;

    public Book() {
    }

    public Book(String maSach, String tenSach, String tenTheLoai, String tacGia, double donGia, int soLuong, String tenNXB, String imageUrl) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tenTheLoai = tenTheLoai;
        this.tacGia = tacGia;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.tenNXB = tenNXB;
        this.imageUrl = imageUrl;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTenNXB() {
        return tenNXB;
    }

    public void setTenNXB(String tenNXB) {
        this.tenNXB = tenNXB;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
