package com.example.appbansach.modle;

public class Order {
    private String maOrder;
    private String tenKH;
    private String SDT;
    private String DiaChi;
    private String tenSach;
    private String tacGia;
    private int soLuong;
    private double Price;
    private String NgayDate;

    public Order() {
    }

    public Order(String maOrder, String tenKH, String SDT, String diaChi, String tenSach, String tacGia, int soLuong, double price, String ngayDate) {
        this.maOrder = maOrder;
        this.tenKH = tenKH;
        this.SDT = SDT;
        DiaChi = diaChi;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.soLuong = soLuong;
        Price = price;
        NgayDate = ngayDate;
    }

    public String getMaOrder() {
        return maOrder;
    }

    public void setMaOrder(String maOrder) {
        this.maOrder = maOrder;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getNgayDate() {
        return NgayDate;
    }

    public void setNgayDate(String ngayDate) {
        NgayDate = ngayDate;
    }
}
