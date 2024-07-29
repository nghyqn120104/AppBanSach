package com.example.appbansach;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivityAdmin extends AppCompatActivity {

    private FrameLayout category;
    private FrameLayout book;
    private FrameLayout logOut;

    private FrameLayout userList;
    private FrameLayout InvoiceAdmin;
    private FrameLayout statistics;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        //Nhận dữ liệu trạng thái của tài khoản là user/admin
        Intent intent = getIntent();
        String accountType = intent.getStringExtra("role");
        //Toast.makeText(this, "check" + accountType, Toast.LENGTH_SHORT).show();

        category = findViewById(R.id.Category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityAdmin.this, CategoryActivity.class);
                intent.putExtra("role", accountType);
                startActivity(intent);
            }
        });

        book = findViewById(R.id.Book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityAdmin.this, ListBookActivity.class);
                intent.putExtra("role", accountType);
                startActivity(intent);
            }
        });

        logOut = findViewById(R.id.Logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityAdmin.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        userList = findViewById(R.id.user_list);
        userList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityAdmin.this, UserListActivity.class);
                intent.putExtra("role", accountType);
                startActivity(intent);
                finish();
            }
        });
        InvoiceAdmin = findViewById(R.id.InvoiceAmin);
        InvoiceAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityAdmin.this, InvoiceUserActivity.class);
                intent.putExtra("role", accountType);
                startActivity(intent);
            }
        });

        statistics = findViewById(R.id.Statistics);
        statistics.setOnClickListener(v -> {
            Intent intent16 = new Intent(MainActivityAdmin.this, StatisticsActivity.class);
            intent16.putExtra("role", accountType);
            startActivity(intent16);
        });
    }

}