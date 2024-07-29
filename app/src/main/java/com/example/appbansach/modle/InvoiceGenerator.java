package com.example.appbansach.modle;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.util.List;

public class InvoiceGenerator {
    public Invoice createInvoice(String customerName, List<CartItem> cartItems) {
        String currentTime = getCurrentTime();
        double totalAmount = calculateTotalAmount(cartItems);
        return new Invoice(customerName, cartItems, currentTime, totalAmount);
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    private double calculateTotalAmount(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}
