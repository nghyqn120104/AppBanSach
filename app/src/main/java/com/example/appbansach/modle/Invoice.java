package com.example.appbansach.modle;

import java.util.Date;
import java.util.List;

public class Invoice {
    private String customerName;
    private List<CartItem> cartItems;
    private String timeIssued;
    private double totalAmount;

    public Invoice(String customerName, List<CartItem> cartItems, String timeIssued, double totalAmount) {
        this.customerName = customerName;
        this.cartItems = cartItems;
        this.timeIssued = timeIssued;
        this.totalAmount = totalAmount;
    }

    public Invoice() {
    }

    // Getters and setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getTimeIssued() {
        return timeIssued;
    }

    public void setTimeIssued(String timeIssued) {
        this.timeIssued = timeIssued;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
