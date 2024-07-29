package com.example.appbansach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.appbansach.R;
import com.example.appbansach.modle.Book;

import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class CategoryBookAdapter extends ArrayAdapter<Book> {
    private Context context;
//    private List<Categorybook> categorybookList;
private List<Book> categorybookList;

    public CategoryBookAdapter(@NonNull Context context, @NonNull List<Book> categorybookList) {
        super(context, R.layout.item_book, categorybookList);
        this.context = context;
        this.categorybookList= categorybookList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        Book book = categorybookList.get(position);
        TextView ten = convertView.findViewById(R.id.txtBookName);
        TextView tacgia = convertView.findViewById(R.id.txtAuthor);
        TextView dongia = convertView.findViewById(R.id.txtPrice);

        double donGia = book.getDonGia();
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedDonGia = vnFormat.format(donGia);

        ten.setText(book.getTenSach());
        tacgia.setText(book.getTacGia());


        dongia.setText(formattedDonGia);

        return convertView;
    }
}
