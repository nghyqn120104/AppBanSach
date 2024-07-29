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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.appbansach.Adapter.BookAdapter;
import com.example.appbansach.modle.Account;
import com.example.appbansach.modle.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText edUsername, edUserpass;
    private Button btnLogin;
    private BookAdapter adapter;
    private String accountType;
    private List<Book> bookList;
    private FirebaseAuth auth;
    private LinearLayout Register;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUsername = findViewById(R.id.edUsername);
        edUserpass = findViewById(R.id.edUserpass);
        btnLogin = findViewById(R.id.btnLogin);

        Register = findViewById(R.id.dangKi);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("accounts");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUsername.getText().toString().trim();
                String userpass = edUserpass.getText().toString().trim();

                if (!username.isEmpty()) {
                    fetchAccountDetails(username, userpass);
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchAccountDetails(final String username, final String userpass) {
        mDatabase.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);
                String role = dataSnapshot.child("role").getValue(String.class);
                if (account != null && account.getPassword().equals(userpass)) {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                    // Determine user role and redirect accordingly
                    if ("admin".equals(account.getRole())) {
                        Intent intent = new Intent(LoginActivity.this, MainActivityAdmin.class);
                        intent.putExtra("role", role);
                        startActivity(intent);
                    }  else {
                        Intent intent = new Intent(LoginActivity.this, MainActivityUser.class);
                        intent.putExtra("username", username);
                        intent.putExtra("role", role);
                        startActivity(intent);
                    }
                } else {
                    // Invalid credentials or account does not exist
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Error fetching account details", Toast.LENGTH_SHORT).show();
            }
        });

        // Check if the username exists in the database
        mDatabase.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(LoginActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                    // Handle logic to close the app or take appropriate action
                    // For example, you can finish the activity to prevent further navigation
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Error checking account existence", Toast.LENGTH_SHORT).show();
            }
        });
    }
}