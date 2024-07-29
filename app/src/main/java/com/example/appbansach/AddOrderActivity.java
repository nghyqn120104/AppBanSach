package com.example.appbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.Adapter.CartAdapter;
import com.example.appbansach.modle.Account;
import com.example.appbansach.modle.Book;
import com.example.appbansach.modle.CartItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddOrderActivity extends AppCompatActivity {

    private Button buttonDecrease, buttonIncrease, btnThanhToanOrder;
    private EditText editQuantity;
    private String tenSach, tacgia;
    private ArrayList<Book> books;
    private TextView ngaylap, tongtien;
    private ArrayList<CartItem> cartItems;
    private ListView listViewOrder;
    private CartAdapter cartAdapter;
    private DatabaseReference mDatabase, uDatabase, databaseReference;
    private String role;
    private ImageView imgoutaddorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        role = intent.getStringExtra("role");
        String bookId = intent.getStringExtra("book_id");

        // Khởi tạo Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("books");
        uDatabase = FirebaseDatabase.getInstance().getReference("accounts");
        databaseReference = FirebaseDatabase.getInstance().getReference("carts");

        listViewOrder = findViewById(R.id.listViewOrder);
        TextView totalPriceTextViewOrder = findViewById(R.id.txtTongtienOrder);

        Toast.makeText(this, "" + bookId, Toast.LENGTH_SHORT).show();

        ngaylap = findViewById(R.id.txtNgayLapOrder);

        // Lấy ngày hiện tại và định dạng nó
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(Calendar.getInstance().getTime());
        // Hiển thị ngày lên TextView
        ngaylap.setText(currentDate);

        cartItems = new ArrayList<>();
        getCartsFromDatabase(username);

        cartAdapter = new CartAdapter(this, cartItems, totalPriceTextViewOrder, true);
        listViewOrder.setAdapter(cartAdapter);
        fetchUserDetails(username);
        btnThanhToanOrder = findViewById(R.id.btnThanhToanOrder);

        btnThanhToanOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddOrderActivity.this, InvoiceActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("role",role);
                startActivity(intent);
            }
        });

        imgoutaddorder = findViewById(R.id.imgoutaddoder);
        imgoutaddorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AddOrderActivity.this, CartsActivity.class);
                intent1.putExtra("username", username);
                intent1.putExtra("role", role);
                startActivity(intent1);
            }
        });
    }

    private void fetchUserDetails(String username) {
        uDatabase.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);
                if (account != null) {
                    // Hiển thị thông tin người dùng lên giao diện
                    displayUserDetails(account);
                } else {
                    Toast.makeText(AddOrderActivity.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddOrderActivity.this, "Lỗi khi truy vấn dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserDetails(Account account) {
        TextView txtKhachHang = findViewById(R.id.txtKhachHangOrder);
        TextView txtSDT = findViewById(R.id.txtSDTOrder);
        TextView txtDiaChi = findViewById(R.id.txtDiaChiOrder);

        txtKhachHang.setText(account.getUsername());
        txtSDT.setText(account.getsdt());
        txtDiaChi.setText(account.getDiachi());
    }

    private void getCartsFromDatabase(String name) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        CartItem cartItem = snapshot.getValue(CartItem.class);
                        String tenKH = snapshot.child("tenKH").getValue(String.class);
                        if (cartItem != null && name.equals(tenKH)) {
                            cartItems.add(cartItem);
                        }
                    } catch (Exception e) {
                        Log.e("AddOrderActivity", "Lỗi khi lấy thông tin giỏ hàng: " + e.getMessage());
                    }
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddOrderActivity.this, "Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
