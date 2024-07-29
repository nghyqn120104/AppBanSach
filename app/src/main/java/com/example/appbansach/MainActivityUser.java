package com.example.appbansach;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbansach.modle.Book;
import com.google.firebase.database.DatabaseReference;

public class MainActivityUser extends AppCompatActivity {

    private FrameLayout logOut;
    private FrameLayout chamngePassword;
    private FrameLayout InformationUser;
    private TextView user;
    private FrameLayout BookUsers;
    private FrameLayout InvoiceUser;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        //Nhận dữ liệu trạng thái của tài khoản là user/admin
        Intent intent = getIntent();
        String accountType = intent.getStringExtra("role");
        //     Toast.makeText(this, "check" + accountType, Toast.LENGTH_SHORT).show();

        logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityUser.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        chamngePassword = findViewById(R.id.btnChangePassword);
        chamngePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getIntent().getStringExtra("username");
                Intent intent = new Intent(MainActivityUser.this, ChangePasswordActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("role", accountType);
                startActivity(intent);
            }
        });

        user = findViewById(R.id.user);
        String username = getIntent().getStringExtra("username");
        //String accountType = getIntent().getStringExtra("role");
        user.setText(username);

        BookUsers = findViewById(R.id.bookuser);
        BookUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityUser.this, ListBookActivity.class);
                intent.putExtra("role", accountType);
                intent.putExtra("username", username);// Truyền loại tài khoản (admin/user)
                startActivity(intent);
            }
        });

        InformationUser = findViewById(R.id.Informationuser);
        InformationUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityUser.this, InformationUserActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("role", accountType);
                startActivity(intent);
            }
        });

        InvoiceUser = findViewById(R.id.Invoiceuser);
        InvoiceUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityUser.this, InvoiceUserActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("role", accountType);
                startActivity(intent);
            }
        });
    }
}