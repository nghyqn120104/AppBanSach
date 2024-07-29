package com.example.appbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtUsername, edtNewPassword;
    private Button btnChangePassword;
    private DatabaseReference databaseReference;

    private ImageView outpass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        String accountType = intent.getStringExtra("role");
        String username = intent.getStringExtra("username");
        edtUsername = findViewById(R.id.edtUsername);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        outpass = findViewById(R.id.imgoutpass);
        outpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, MainActivityUser.class);
                intent.putExtra("role", accountType);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        edtUsername.setText(username); // Hiển thị username trong EditText
        edtUsername.setEnabled(false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("accounts");

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword(username);
            }
        });
    }

    private void changePassword(final String username) {
        final String newPassword = edtNewPassword.getText().toString().trim();

        if (newPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập password mới", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật password mới vào Firebase Database
        databaseReference.child(username).child("password").setValue(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
