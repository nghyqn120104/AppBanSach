package com.example.appbansach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appbansach.R;
import com.example.appbansach.modle.Book;
import com.example.appbansach.modle.CartItem;
import com.example.appbansach.modle.Category;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Book> books;
    private TextView totalPriceTextView;
    private DatabaseReference databaseReference;

    public OrderAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
    }
    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        Book book = books.get(position);
        TextView tensach = convertView.findViewById(R.id.book_name);
        TextView gia = convertView.findViewById(R.id.book_price);
        double donGia = book.getDonGia();
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        String formattedDonGia = vnFormat.format(donGia);
        tensach.setText(book.getTenSach());
        gia.setText(formattedDonGia);
        return convertView;
    }
}
