package com.example.appbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbansach.modle.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.StartupTime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateCategoryActivity extends AppCompatActivity {

    private EditText editTextCategoryName;
    private EditText editTextCategoryID;
    private Button buttonUpdateCategory;
    private DatabaseReference databaseCategories;
    private String categoryId;
    private String categoryName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        editTextCategoryID = findViewById(R.id.editUpdateCategoryID);
        editTextCategoryName = findViewById(R.id.editUpdateCategoryName);
        buttonUpdateCategory = findViewById(R.id.buttonUpdateCategory);

        editTextCategoryID.setEnabled(false);
        // Lấy thông tin thể loại cần sửa từ Intent
        // categoryId = getIntent().getStringExtra("id");
        categoryId = getIntent().getStringExtra("id");
        categoryName = getIntent().getStringExtra("name");

        editTextCategoryID.setText(categoryId);
        editTextCategoryName.setText(categoryName);
        // Khởi tạo Firebase
        databaseCategories = FirebaseDatabase.getInstance().getReference("categories");

        // Xử lý sự kiện khi người dùng nhấn nút "Update"
        buttonUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCategory();
            }
        });
    }

    private void updateCategory() {
        final String newCategoryName = editTextCategoryName.getText().toString().trim();

        if (newCategoryName.isEmpty()) {
            Toast.makeText(this, "Hãy nhập tên thể loại cần  sửa!!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu tên thể loại mới giống với tên ban đầu, không cần kiểm tra trùng lặp
//        if (newCategoryName.equals(categoryName)) {
//           // updateCategoryInDatabase(newCategoryName);
//            return;
//        }
        // Kiểm tra xem tên thể loại đã tồn tại hay chưa
        databaseCategories.orderByChild("name").equalTo(newCategoryName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Tên thể loại đã tồn tại
                            Toast.makeText(UpdateCategoryActivity.this, "Tên thể loại: '" + newCategoryName + "' đã tồn tại!!", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference categoryRef = databaseCategories.child(categoryId);
                            categoryRef.child("name").setValue(newCategoryName).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UpdateCategoryActivity.this, "Sửa thành công!!", Toast.LENGTH_SHORT).show();
                                    finish(); // Đóng Activity sau khi cập nhật thành công
                                } else {
                                    Toast.makeText(UpdateCategoryActivity.this, "Sửa thất bại!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi xảy ra ở firebase ví dụ như không có mạng
                        Toast.makeText(UpdateCategoryActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
