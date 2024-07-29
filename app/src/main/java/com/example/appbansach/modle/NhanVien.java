package com.example.appbansach.modle;

public class NhanVien {
    private String maNV;
    private String hoTen;
    private String chucVu;
    private double hesoLuong;
    private double luongCoBan;
    private double phuCap;

    public NhanVien() {
    }

    public NhanVien(String maNV, String hoTen, String chucVu, double hesoLuong, double luongCoBan, double phuCap) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.chucVu = chucVu;
        this.hesoLuong = hesoLuong;
        this.luongCoBan = luongCoBan;
        this.phuCap = phuCap;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public double getHesoLuong() {
        return hesoLuong;
    }

    public void setHesoLuong(double hesoLuong) {
        this.hesoLuong = hesoLuong;
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(int luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public double getPhuCap() {
        return phuCap;
    }

    public void setPhuCap(int phuCap) {
        this.phuCap = phuCap;
    }
}
