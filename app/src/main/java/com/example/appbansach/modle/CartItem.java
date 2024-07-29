package com.example.appbansach.modle;

public class CartItem {
    private String ItemId;
    private String tenKH;
    private String Name;
    private double Price;
    private int Quantity;

    public CartItem(String itemId, String tenKH, String name, double price, int quantity) {
        ItemId = itemId;
        this.tenKH = tenKH;
        Name = name;
        Price = price;
        Quantity = quantity;
    }

    public CartItem() {
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
