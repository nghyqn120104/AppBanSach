package com.example.appbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.modle.Book;
import com.example.appbansach.modle.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

public class InformationUserActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ImageView outuser;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_user);
        // Khởi tạo Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("accounts");

        // Lấy dữ liệu từ Intent và truy vấn chi tiết sách từ Firebase
        Intent intent = getIntent();
        String accountType = intent.getStringExtra("role");
        String username = intent.getStringExtra("username");
        if (intent != null) {
            fetchUserDetails(username);
        }


        outuser = findViewById(R.id.imgoutuser);
        outuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String username = intent.getStringExtra("username");
                Intent intent = new Intent(InformationUserActivity.this, MainActivityUser.class);
                intent.putExtra("username",username);
                intent.putExtra("role", accountType);
                startActivity(intent);
            }
        });
    }

    private void fetchUserDetails(String username) {
        mDatabase.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    // Hiển thị thông tin sách lên giao diện
                    displayUserDetails(user);
                } else {
                    Toast.makeText(InformationUserActivity.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(InformationUserActivity.this, "Lỗi khi truy vấn dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserDetails(User user) {

        TextView txtkhachhang = findViewById(R.id.txtkhachhang);
        TextView txtsdt = findViewById(R.id.txtsdt);
        TextView txtdiachi = findViewById(R.id.txtdiachi);


        txtkhachhang.setText(user.getUsername());
        txtsdt.setText(user.getsdt());
        txtdiachi.setText(user.getDiachi());
    }
}