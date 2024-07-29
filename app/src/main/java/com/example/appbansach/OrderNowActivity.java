package com.example.appbansach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbansach.modle.Book;
import com.example.appbansach.modle.CartItem;
import com.example.appbansach.modle.Invoice;
import com.example.appbansach.modle.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class OrderNowActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, bDatabase, databaseReference;
    private TextView ngaylap, tvquantity, tvTotalPrice, txtgia, txttensach;
    private Button Decrease, Increase, ThanhToan;
    private int quantity = 1;
    private double bookPrice = 0.0;
    private String tenSach;
    private Double giaSach;
    private String name;
    private String role;
    private ImageView imgoutordernow;
    private String cartItemId; // ID của mục sách trong giỏ hàng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);

        // Initialize Firebase references
        mDatabase = FirebaseDatabase.getInstance().getReference("accounts");
        bDatabase = FirebaseDatabase.getInstance().getReference("books");
        databaseReference = FirebaseDatabase.getInstance().getReference("carts");

        // Get intent data
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String bookid = intent.getStringExtra("book_id");
        String role1 = intent.getStringExtra("role");
        role = role1;
        // Initialize views
        ngaylap = findViewById(R.id.txtNgayLapOrdernow);
        tvquantity = findViewById(R.id.book_quantitynow);
        Decrease = findViewById(R.id.button_decreasenow);
        Increase = findViewById(R.id.button_increasenow);
        tvTotalPrice = findViewById(R.id.txtTongtienOrdernow);
        txtgia = findViewById(R.id.book_pricenow);
        txttensach = findViewById(R.id.book_namenow);
        ThanhToan = findViewById(R.id.btnThanhToanOrdernow);

        // Set username
        name = username;

        // Set current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(Calendar.getInstance().getTime());
        ngaylap.setText(currentDate);

        // Decrease quantity button click listener
        Decrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvquantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        // Increase quantity button click listener
        Increase.setOnClickListener(v -> {
            quantity++;
            tvquantity.setText(String.valueOf(quantity));
            updateTotalPrice();
        });

        // Thanh Toan button click listener
        ThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCartToFirebase(); // Add book to cart and proceed to payment
            }
        });

        // Fetch user information from Firebase
        fetchUser(username);

        // Fetch book information from Firebase
        fetchBook(bookid);

        imgoutordernow = findViewById(R.id.imgoutaddodernow);
        imgoutordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderNowActivity.this, BookDetailActivity.class);
                intent.putExtra("role", role);
                startActivity(intent);
            }
        });
    }

    // Fetch user information from Firebase
    private void fetchUser(String username) {
        mDatabase.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    displayUser(user);
                } else {
                    Toast.makeText(OrderNowActivity.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderNowActivity.this, "Lỗi khi truy vấn dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display user information in views
    private void displayUser(User user) {
        TextView txtkhachhang = findViewById(R.id.txtKhachHangOrdernow);
        TextView txtsdt = findViewById(R.id.txtSDTOrdernow);
        TextView txtdiachi = findViewById(R.id.txtDiaChiOrdernow);

        txtkhachhang.setText(user.getUsername());
        txtsdt.setText(user.getsdt());
        txtdiachi.setText(user.getDiachi());
    }

    // Fetch book information from Firebase
    private void fetchBook(String bookid) {
        bDatabase.child(bookid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);
                if (book != null) {
                    displayBook(book);
                    tenSach = book.getTenSach();
                    giaSach = book.getDonGia();
                } else {
                    Toast.makeText(OrderNowActivity.this, "Không tìm thấy thông tin sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderNowActivity.this, "Lỗi khi truy vấn dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display book information in views
    private void displayBook(Book book) {
        double donGia = book.getDonGia();
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedDonGia = vnFormat.format(donGia);

        txttensach.setText(book.getTenSach());
        txtgia.setText(formattedDonGia);

        // Update book price for total calculation
        bookPrice = donGia;
        updateTotalPrice();
    }

    // Update total price based on quantity
    private void updateTotalPrice() {
        double totalPrice = quantity * bookPrice;
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalPrice = vnFormat.format(totalPrice);
        tvTotalPrice.setText(formattedTotalPrice);
    }

    // Add book to cart and proceed to create invoice
    private void addCartToFirebase() {
        if (tenSach == null || giaSach == null || name == null) {
            Toast.makeText(OrderNowActivity.this, "Thông tin không đầy đủ để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra và thêm hoặc cập nhật giỏ hàng
        databaseReference.orderByChild("tenKH").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean itemExists = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    if (cartItem != null && tenSach.equals(cartItem.getName()) && name.equals(cartItem.getTenKH())) {
                        itemExists = true;
                        cartItemId = snapshot.getKey(); // Lấy cartItemId của mục sách trong giỏ hàng
                        break;
                    }
                }

                if (itemExists) {
                    int newQuantity = quantity;
                    databaseReference.child(cartItemId).child("quantity").setValue(newQuantity)
                            .addOnSuccessListener(aVoid -> {
                                // Toast.makeText(OrderNowActivity.this, "Cập nhật số lượng sách thành công", Toast.LENGTH_SHORT).show();
                                createInvoiceAndDisplay(name); // Tạo hóa đơn và hiển thị
                                deleteBookFromCart(cartItemId); // Xóa sách khỏi giỏ hàng
                            })
                            .addOnFailureListener(e -> {
                                //Toast.makeText(OrderNowActivity.this, "Cập nhật số lượng sách thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // Thêm mới một mục sách vào giỏ hàng
                    String maCart = UUID.randomUUID().toString();
                    CartItem cart = new CartItem(maCart, name, tenSach, giaSach, quantity);
                    databaseReference.child(maCart).setValue(cart)
                            .addOnSuccessListener(aVoid -> {
                                // Toast.makeText(OrderNowActivity.this, "Thêm sách vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                createInvoiceAndDisplay(name); // Tạo hóa đơn và hiển thị
                                deleteBookFromCart(maCart); // Xóa sách khỏi giỏ hàng
                            })
                            .addOnFailureListener(e -> {
                                // Toast.makeText(OrderNowActivity.this, "Thêm sách vào giỏ hàng thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderNowActivity.this, "Lỗi khi truy vấn dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xóa sách khỏi giỏ hàng
    private void deleteBookFromCart(String cartItemId) {
        databaseReference.child(cartItemId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Toast.makeText(OrderNowActivity.this, "Đã xóa sách khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Toast.makeText(OrderNowActivity.this, "Xóa sách khỏi giỏ hàng thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Tạo hóa đơn và hiển thị
    private void createInvoiceAndDisplay(String customerName) {
        // Tạo hóa đơn mới chỉ có một cuốn sách này
        CartItem cartItem = new CartItem(cartItemId, customerName, tenSach, giaSach, quantity);
        Invoice invoice = new Invoice(customerName, Collections.singletonList(cartItem),
                new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()),
                quantity * bookPrice);

        // Lưu hóa đơn vào Firebase
        saveInvoiceToFirebase(invoice, customerName);

        // Chuyển sang màn hình danh sách sách
        Intent intent = new Intent(OrderNowActivity.this, ListBookActivity.class);
        intent.putExtra("username", customerName);
        intent.putExtra("role", role);
        startActivity(intent);
    }

    // Lưu hóa đơn vào Firebase
    private void saveInvoiceToFirebase(Invoice invoice, String customerName) {
        DatabaseReference invoicesRef = FirebaseDatabase.getInstance().getReference("invoices");

        // Tạo một key mới sử dụng phương thức push()
        String invoiceId = invoicesRef.push().getKey();
        if (invoiceId != null) {
            invoicesRef.child(invoiceId).setValue(invoice)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(OrderNowActivity.this, "Hóa đơn đã được lưu thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OrderNowActivity.this, "Lưu hóa đơn thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(OrderNowActivity.this, "Lấy mã hóa đơn thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
