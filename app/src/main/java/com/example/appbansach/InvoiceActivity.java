package com.example.appbansach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbansach.modle.CartItem;
import com.example.appbansach.modle.Invoice;
import com.example.appbansach.modle.InvoiceGenerator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<CartItem> cartItems;
    private String username;
    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        accountType = intent.getStringExtra("role");

        databaseReference = FirebaseDatabase.getInstance().getReference("carts");

        cartItems = new ArrayList<>();

        if (username != null) {
            fetchCartItemsFromFirebase(username);
        } else {
            Toast.makeText(this, "Không cung cấp tên khách hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCartItemsFromFirebase(String customerName) {
        databaseReference.orderByChild("tenKH").equalTo(customerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        CartItem cartItem = snapshot.getValue(CartItem.class);
                        String tenKH = snapshot.child("tenKH").getValue(String.class);
                        if (cartItem != null && customerName.equals(tenKH)) {
                            cartItems.add(cartItem);
                        }
                    } catch (Exception e) {
                        Log.e("InvoiceActivity", "Lỗi khi lấy thông tin giỏ hàng: " + e.getMessage());
                    }
                }

                if (cartItems.isEmpty()) {
                    Toast.makeText(InvoiceActivity.this, "Không tìm thấy sản phẩm cho khách hàng " + customerName, Toast.LENGTH_SHORT).show();
                } else {
                    createInvoiceAndDisplay(customerName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(InvoiceActivity.this, "Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createInvoiceAndDisplay(String customerName) {
        InvoiceGenerator invoiceGenerator = new InvoiceGenerator();
        Invoice invoice = invoiceGenerator.createInvoice(customerName, cartItems);

        saveInvoiceToFirebase(invoice, customerName);
        deleteCartItems(customerName);

        Intent intent = new Intent(InvoiceActivity.this, ListBookActivity.class);
        intent.putExtra("username", customerName);
        intent.putExtra("role", accountType);
        startActivity(intent);
    }

    private void saveInvoiceToFirebase(Invoice invoice, String name) {
        DatabaseReference invoicesRef = FirebaseDatabase.getInstance().getReference("invoices");

        String invoiceId = invoicesRef.push().getKey();
        if (invoiceId != null) {
            invoicesRef.child(invoiceId).setValue(invoice).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(InvoiceActivity.this, "Hóa đơn đã được lưu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InvoiceActivity.this, "Lưu hóa đơn thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteCartItems(String customerName) {
        databaseReference.orderByChild("tenKH").equalTo(customerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("InvoiceActivity", "Item deleted successfully");
                        } else {
                            Log.e("InvoiceActivity", "Failed to delete item: " + task.getException().getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(InvoiceActivity.this, "Error occurred while deleting items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
