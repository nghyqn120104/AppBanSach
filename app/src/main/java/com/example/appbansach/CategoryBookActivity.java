package com.example.appbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appbansach.Adapter.CategoryAdapter;
import com.example.appbansach.Adapter.CategoryBookAdapter;
import com.example.appbansach.modle.Book;
import com.example.appbansach.modle.Category;
//import com.example.appbansach.modle.Categorybook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryBookActivity extends AppCompatActivity {


    private ListView listViewBooks;
    private DatabaseReference databaseBooks;
    private List<Book> bookList;
    private String categoryId;

    private ImageView outcategorybook;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_book);

        Intent intent = getIntent();
        String accountType = intent.getStringExtra("role");
        databaseBooks = FirebaseDatabase.getInstance().getReference("books");

        listViewBooks = findViewById(R.id.listViewCategoriesBook);
        bookList = new ArrayList<>();

        // Lấy categoryId từ Intent
        categoryId = getIntent().getStringExtra("tenTheLoai");

        outcategorybook = findViewById(R.id.imgoutcategorybook);
        outcategorybook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryBookActivity.this, CategoryActivity.class);
                intent.putExtra("role", accountType);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseBooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Book book = postSnapshot.getValue(Book.class);
                    if (book.getTenTheLoai().equals(categoryId)) {
                        bookList.add(book);
                    }
                }

                CategoryBookAdapter adapter = new CategoryBookAdapter(CategoryBookActivity.this, bookList);
                listViewBooks.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CategoryBookActivity.this, "Dữ liệu không có quyền truy cập", Toast.LENGTH_SHORT).show();
            }
        });
    }

}