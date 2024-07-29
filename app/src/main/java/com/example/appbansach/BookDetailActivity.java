package com.example.appbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.Adapter.CartAdapter;
import com.example.appbansach.modle.Book;
import com.example.appbansach.modle.CartItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class BookDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase, mDatabaseCart;
    private Toolbar mToolbar;
    private ImageView btnAddCart;
    private TextView BuyBook;
    private String tenSach;
    private String accountType;
    private Double giaSach;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        btnAddCart = findViewById(R.id.btnAddCart);
        BuyBook = findViewById(R.id.tvBuyBooK);

        Intent intent = getIntent();
        accountType = intent.getStringExtra("role");
        if ("admin".equals(accountType)) {
            btnAddCart.setVisibility(View.GONE);
            BuyBook.setVisibility(View.GONE);
        } else if ("user".equals(accountType)) {
            btnAddCart.setVisibility(View.VISIBLE);
            BuyBook.setVisibility(View.VISIBLE);
        }

        // Khởi tạo Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("books");
        mDatabaseCart = FirebaseDatabase.getInstance().getReference();

        // Thiết lập Toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
        getSupportActionBar().setTitle("Chi tiết sách");

        // Lấy dữ liệu từ Intent và truy vấn chi tiết sách từ Firebase
        String bookId = intent.getStringExtra("book_id");
        String username = intent.getStringExtra("username");
        name = username;

        if (bookId != null) {
            fetchBookDetails(bookId);
        } else {
            Toast.makeText(this, "ID sách không hợp lệ", Toast.LENGTH_SHORT).show();
        }

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCartToFirebase();
            }
        });

        BuyBook = findViewById(R.id.tvBuyBooK);
        BuyBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookDetailActivity.this, OrderNowActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("book_id", bookId);
                intent.putExtra("role", accountType);
                startActivity(intent);
            }
        });
    }

    private void fetchBookDetails(String bookId) {
        mDatabase.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);
                if (book != null) {
                    // Hiển thị thông tin sách lên giao diện
                    displayBookDetails(book);
                    tenSach = book.getTenSach();
                    giaSach = book.getDonGia();
                } else {
                    Toast.makeText(BookDetailActivity.this, "Không tìm thấy thông tin sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookDetailActivity.this, "Lỗi khi truy vấn dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBookDetails(Book book) {
        double donGia = book.getDonGia();
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedDonGia = vnFormat.format(donGia);

        TextView txtMaSach = findViewById(R.id.txtMaSach);
        TextView txtTenSach = findViewById(R.id.txtTenSach);
        TextView txtTacGia = findViewById(R.id.txtTacGia);
        TextView txtTheLoai = findViewById(R.id.txtTheLoai);
        TextView txtDonGia = findViewById(R.id.txtDonGia);
        TextView txtSoLuong = findViewById(R.id.txtSoLuong);
        TextView txtTenNXB = findViewById(R.id.txtTenNXB);

        txtMaSach.setText(book.getMaSach());
        txtTenSach.setText(book.getTenSach());
        txtTacGia.setText(book.getTacGia());
        txtTheLoai.setText(book.getTenTheLoai());
        txtDonGia.setText(formattedDonGia);
        txtSoLuong.setText(String.valueOf(book.getSoLuong()));
        txtTenNXB.setText(book.getTenNXB());
    }

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

    private void addCartToFirebase() {
        if (tenSach == null || giaSach == null || name == null) {
            Toast.makeText(BookDetailActivity.this, "Thông tin không đầy đủ để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        mDatabaseCart.child("carts").orderByChild("name").equalTo(tenSach).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean itemExists = false;
                String existingItemId = null;
                int existingItemQuantity = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    if (cartItem != null && tenSach.equals(cartItem.getName()) && name.equals(cartItem.getTenKH())) {
                        itemExists = true;
                        existingItemId = cartItem.getItemId();
                        existingItemQuantity = cartItem.getQuantity();
                        break;
                    }
                }

                if (itemExists) {
                    int newQuantity = existingItemQuantity + 1;
                    mDatabaseCart.child("carts").child(existingItemId).child("quantity").setValue(newQuantity)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(BookDetailActivity.this, "Cập nhật số lượng sách thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BookDetailActivity.this, CartsActivity.class);
                                intent.putExtra("username", name);
                                intent.putExtra("role",accountType);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(BookDetailActivity.this, "Cập nhật số lượng sách thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // Add a new cart item
                    String maCart = UUID.randomUUID().toString();
                    CartItem cart = new CartItem(maCart, name, tenSach, giaSach, 1);
                    mDatabaseCart.child("carts").child(maCart).setValue(cart)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(BookDetailActivity.this, "Thêm sách vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BookDetailActivity.this, CartsActivity.class);
                                intent.putExtra("username", name);
                                intent.putExtra("role",accountType);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(BookDetailActivity.this, "Thêm sách vào giỏ hàng thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BookDetailActivity.this, "Kiểm tra mã sách thất bại: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
