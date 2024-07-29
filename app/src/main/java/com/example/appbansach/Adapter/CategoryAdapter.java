package com.example.appbansach.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.appbansach.CategoryActivity;
import com.example.appbansach.MainActivity;
import com.example.appbansach.R;
import com.example.appbansach.modle.Book;
import com.example.appbansach.modle.Category;
import com.example.appbansach.updateBookActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private List<Category> categoryList;

    private ImageView updatecate;
    private ImageView deletecate;

    private CategoryActivity context;
    private DatabaseReference databaseReference;
    public CategoryAdapter(@NonNull CategoryActivity context, @NonNull List<Category> categoryList) {
        super(context, R.layout.listcategory_item, categoryList);
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listcategory_item, parent, false);
        Category category = categoryList.get(position);
        TextView theloai = convertView.findViewById(R.id.textViewName);
        theloai.setText(category.getName());

        updatecate = convertView.findViewById(R.id.imgeupdatecate);
        updatecate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.UpdateCategory(category);
            }
        });

        deletecate = convertView.findViewById(R.id.imgdeletecate);
        deletecate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory(category);
            }
        });

        return convertView;
    }

    private void deleteCategory(Category category) {
        int position = categoryList.indexOf(category);
        categoryList.remove(position);
        notifyDataSetInvalidated();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("categories");
        mDatabase.child(category.getId()).removeValue();
        String nametheloai = category.getName();
        context.deleteBooksInCategory(nametheloai);
    }
}
