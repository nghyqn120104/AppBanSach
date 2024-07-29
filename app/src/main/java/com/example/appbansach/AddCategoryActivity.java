package com.example.appbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbansach.modle.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText editTextCategoryName;
    private EditText editTextCategoryID;
    private Button buttonAddCategory;
    private DatabaseReference databaseCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        Intent intent = getIntent();
        String accountType = intent.getStringExtra("role");
        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        editTextCategoryID = findViewById(R.id.editTextCategoryID);
        buttonAddCategory = findViewById(R.id.buttonAddCategory);

        databaseCategories = FirebaseDatabase.getInstance().getReference("categories");

        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategory();
            }
        });
    }
    private void addCategory() {
        String accountType = getIntent().getStringExtra("role");
        final String categoryIdStr = editTextCategoryID.getText().toString().trim();
        final String categoryName = editTextCategoryName.getText().toString().trim();

        if (categoryIdStr.isEmpty() || categoryName.isEmpty()) {
            Toast.makeText(this, "Hãy nhập ID and tên thể loại!!", Toast.LENGTH_SHORT).show();
            return;
        }



        // Kiểm tra xem mã thể loại đã tồn tại hay chưa
        databaseCategories.child(categoryIdStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Thể loại đã tồn tại
                    Toast.makeText(AddCategoryActivity.this, "Mã thể loại: '" + categoryIdStr + "' đã tồn tại!!", Toast.LENGTH_SHORT).show();
                } else {
                    // Kiểm tra xem tên thể loại đã tồn tại hay chưa
                    databaseCategories.orderByChild("name").equalTo(categoryName)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Tên thể loại đã tồn tại
                                        Toast.makeText(AddCategoryActivity.this, "Tên thể loại: '" + categoryName + "' đã tồn tại!!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Thêm thể loại mới vào Firebase Realtime Database
                                        Category category = new Category(categoryIdStr, categoryName);
                                        databaseCategories.child(categoryIdStr).setValue(category, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    // Thêm thành công
                                                    Toast.makeText(AddCategoryActivity.this, "Category added successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(AddCategoryActivity.this, CategoryActivity.class);
                                                    intent.putExtra("role", accountType);
                                                    startActivity(intent);
                                                } else {
                                                    // Xảy ra lỗi
                                                    Toast.makeText(AddCategoryActivity.this, "Failed to add category: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Xử lý khi có lỗi xảy ra ở firebase ví dụ như không có mạng
                                    Toast.makeText(AddCategoryActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(AddCategoryActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}