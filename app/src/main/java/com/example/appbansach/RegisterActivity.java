package com.example.appbansach;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbansach.modle.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText edUsername, edUserpass, edRepass, edsdt, eddiachi;
    private TextView tvError;
    private Button btnRegister;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        edUsername = findViewById(R.id.edUsername);
        edUserpass = findViewById(R.id.edUserpass);
        edRepass = findViewById(R.id.edRepass);
        edsdt = findViewById(R.id.edSDT);
        eddiachi = findViewById(R.id.edDiaChi);
        tvError = findViewById(R.id.tvError);
        btnRegister = findViewById(R.id.btnRegister);

        // Set click listener for the register button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = edUsername.getText().toString().trim();
        String password = edUserpass.getText().toString().trim();
        String confirmPassword = edRepass.getText().toString().trim();
        String sdt = edsdt.getText().toString().trim();
        String diachi = eddiachi.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(username)) {
            tvError.setText("Vui lòng nhập tên tài khoản");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            tvError.setText("Vui lòng nhập mật khẩu");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            tvError.setText("Vui lòng xác nhận mật khẩu");
            return;
        }

        if (!password.equals(confirmPassword)) {
            tvError.setText("Mật khẩu không khớp");
            return;
        }

        tvError.setText("");

        // Check if the username (account) already exists
        mDatabase.child("accounts").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    showAlertDialog("Tài khoản đã tồn tại", "Vui lòng chọn tên tài khoản khác.");
                } else {
                    // Create new account object
                    Account account = new Account(username, password,"user", sdt, diachi); // Assuming Account class requires username and password

                    // Save account to Firebase Realtime Database
                    mDatabase.child("accounts").child(username).setValue(account)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, "Kiểm tra tài khoản thất bại: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}