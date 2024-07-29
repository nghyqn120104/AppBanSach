package com.example.appbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.appbansach.Adapter.CategoryAdapter;
import com.example.appbansach.modle.Book;
import com.example.appbansach.modle.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ListView listViewCategories;
    private DatabaseReference databaseCategories;
    private DatabaseReference databaseBooks;
    private List<Category> categoryList;

    private List<Category> filteredCategorylist;
    private ImageView outcategory;
    private LinearLayout liaddcategory;

    private EditText SearchTextCategory;
    private ImageView SearchCategory;
    private CategoryAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        String acountType = intent.getStringExtra("role");
        databaseCategories = FirebaseDatabase.getInstance().getReference("categories");
        databaseBooks = FirebaseDatabase.getInstance().getReference("books");
//                saveCategories();

        LoadCategory();


        listViewCategories = findViewById(R.id.listViewCategories);
        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(this, categoryList);
        listViewCategories.setAdapter(adapter);

        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = categoryList.get(position);
                Intent intent = new Intent(CategoryActivity.this, CategoryBookActivity.class);
                intent.putExtra("categoryId", category.getId());
                intent.putExtra("tenTheLoai", category.getName());
                intent.putExtra("role", acountType);
                startActivity(intent);;
            }
        });

        outcategory = findViewById(R.id.imgoutcategory);
        outcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, MainActivityAdmin.class);
                intent.putExtra("role", acountType);
                startActivity(intent);
            }
        });

        liaddcategory = findViewById(R.id.liaddcategory);
        liaddcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
                intent.putExtra("role", acountType);
                startActivity(intent);
            }
        });

        SearchTextCategory = findViewById(R.id.txtTenTimCate);
        SearchCategory = findViewById(R.id.imgTimKiemCate);
        SearchCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCate();
            }
        });
    }

    public void deleteBooksInCategory(String books) {
        // Xóa các sách có trong thể loại từ Firebase
        DatabaseReference book = FirebaseDatabase.getInstance().getReference("books");
        Query query = book.orderByChild("tenTheLoai").equalTo(books);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
                // Hiển thị thông báo sau khi xóa sách thành công
                Toast.makeText(CategoryActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xảy ra lỗi khi xóa sách trong thể loại
                Toast.makeText(CategoryActivity.this, "Xóa thất bại: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UpdateCategory(Category category) {
        Intent intent = new Intent(CategoryActivity.this, UpdateCategoryActivity.class);
        intent.putExtra("id", category.getId());
        intent.putExtra("name", category.getName());
        startActivity(intent);
    }
    private void SearchCate() {
        String searchQuery = SearchTextCategory.getText().toString().trim();
        if (TextUtils.isEmpty(searchQuery)) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Category> filteredList = new ArrayList<>();
        for (Category category : categoryList) {
            //Tìm kiếm dữ liệu tên linh động không phải mặc định
            if (category.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                filteredList.add(category);
            }
        }
        adapter = new CategoryAdapter(this, filteredList);
        listViewCategories.setAdapter(adapter);
    }
    // Thêm dữ liệu vào firebase
//    private void saveCategories() {
//        List<Category> categories = new ArrayList<>();
//        categries.add(new Category("1", "Truyen Tranh"));
////        categoroies.add(new Category("2", "Van Hoc"));
//
//        for (int i = 0; i < categories.size(); i++) {
//            Category category = categories.get(i);
//            databaseCategories.child(category.getId()).setValue(category);
//        }
//    }

    //Hiển thị dữ liệu thể loại từ firebase
    private void LoadCategory()
    {
        databaseCategories.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Category category = postSnapshot.getValue(Category.class);
                    categoryList.add(category);
                }

                CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, categoryList);
                listViewCategories.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryActivity.this, "Dữ liệu không có quyền truy cập", Toast.LENGTH_SHORT).show();
            }
        });
    }
}