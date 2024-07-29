package com.example.appbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.modle.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class
updateBookActivity extends AppCompatActivity {

    private EditText etMaSach, etTenSach, etTenTheLoai, etTacGia, etDonGia, etSoLuong, etTenNXB;
    private TextView btnUpdateBook , tvTenTheLoaiUpdate;
    private DatabaseReference mDatabase;
    private Toolbar mlToolbar;
    private String bookId;
    private addBookActivity addBookActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        // Retrieve book_id from Intent
        Intent intent = getIntent();
        if (intent != null) {
            bookId = intent.getStringExtra("book_id");
        }

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set up Toolbar
        mlToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        getSupportActionBar().setTitle("Cập nhật sách"); // Set title

        // Connect EditText and TextView
        etMaSach = findViewById(R.id.etMaSachUpdate);
        etTenSach = findViewById(R.id.etTenSachUpdate);
        etTenTheLoai = findViewById(R.id.etTenTheLoaiUpdate);
        etTacGia = findViewById(R.id.etTacGiaUpdate);
        etDonGia = findViewById(R.id.etDonGiaUpdate);
        etSoLuong = findViewById(R.id.etSoLuongUpdate);
        etTenNXB = findViewById(R.id.etTenNXBUpdate);
        tvTenTheLoaiUpdate = findViewById(R.id.tvTenTheLoaiUpdate);
        btnUpdateBook = findViewById(R.id.btnUpdateBook);
        etMaSach.setEnabled(false);
        etMaSach.setFocusable(false);
        // Fetch existing book details based on book_id and populate EditText fields
        fetchBookDetails();

        // Set click listener for update button
        btnUpdateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBookToFirebase();
            }
        });
//        tvTenTheLoaiUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addBookActivity.showGenreDialog(etTenTheLoai);
//            }
//        });
    }

    // Hiển thị thông tin sách trong update qua book_id
    private void fetchBookDetails() {
        if (bookId != null) {
            mDatabase.child("books").child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Book book = snapshot.getValue(Book.class);
                        if (book != null) {
                            etMaSach.setText(book.getMaSach());
                            etTenSach.setText(book.getTenSach());
                            etTenTheLoai.setText(book.getTenTheLoai());
                            etTacGia.setText(book.getTacGia());
                            etDonGia.setText(String.valueOf(book.getDonGia()));
                            etSoLuong.setText(String.valueOf(book.getSoLuong()));
                            etTenNXB.setText(book.getTenNXB());
                        }
                    } else {
                        Toast.makeText(updateBookActivity.this, "Không tìm thấy sách", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(updateBookActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Cập nhật sách
    private void updateBookToFirebase() {
        String maSach = etMaSach.getText().toString().trim();
        String tenSach = etTenSach.getText().toString().trim();
        String tenTheLoai = etTenTheLoai.getText().toString().trim();
        String tacGia = etTacGia.getText().toString().trim();
        String donGiaStr = etDonGia.getText().toString().trim();
        String soLuongStr = etSoLuong.getText().toString().trim();
        String tenNXB = etTenNXB.getText().toString().trim();

        if (maSach.isEmpty() || tenSach.isEmpty() || tenTheLoai.isEmpty() || tacGia.isEmpty() ||
                donGiaStr.isEmpty() || soLuongStr.isEmpty() || tenNXB.isEmpty()) {
            Toast.makeText(updateBookActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double donGia;
        int soLuong;
        try {
            donGia = Double.parseDouble(donGiaStr);
            soLuong = Integer.parseInt(soLuongStr);
        } catch (NumberFormatException e) {
            Toast.makeText(updateBookActivity.this, "Đơn giá và số lượng phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Book mới
        Book book = new Book(maSach, tenSach, tenTheLoai, tacGia, donGia, soLuong, tenNXB, "");

        mDatabase.child("books").child(bookId).setValue(book)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(updateBookActivity.this, "Cập nhật sách thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    etMaSach.setText("");
                    etTenSach.setText("");
                    etTenTheLoai.setText("");
                    etTacGia.setText("");
                    etDonGia.setText("");
                    etSoLuong.setText("");
                    etTenNXB.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(updateBookActivity.this, "Cập nhật sách thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Handle back button on ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}