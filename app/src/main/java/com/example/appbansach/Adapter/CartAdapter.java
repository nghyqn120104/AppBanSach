package com.example.appbansach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appbansach.R;
import com.example.appbansach.modle.CartItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.text.NumberFormat;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CartItem> cartItems;
    private TextView totalPriceTextView;
    private DatabaseReference databaseReference;
    private boolean isAddOrderActivity;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems, TextView totalPriceTextView, boolean isAddOrderActivity) {
        this.context = context;
        this.cartItems = cartItems;
        this.totalPriceTextView = totalPriceTextView;
        this.isAddOrderActivity = isAddOrderActivity;
        databaseReference = FirebaseDatabase.getInstance().getReference("carts");
        updateTotalPrice();
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        updateTotalPrice();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        ImageView bookImage = convertView.findViewById(R.id.book_image);
        TextView bookName = convertView.findViewById(R.id.book_name);
        TextView bookPrice = convertView.findViewById(R.id.book_price);
        TextView bookQuantity = convertView.findViewById(R.id.book_quantity);
        Button buttonDecrease = convertView.findViewById(R.id.button_decrease);
        Button buttonIncrease = convertView.findViewById(R.id.button_increase);
        TextView buttonDelete = convertView.findViewById(R.id.button_delete);

        CartItem cartItem = cartItems.get(position);

        // Load image using Picasso or other image loading library
        double donGia = cartItem.getPrice();
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        String formattedDonGia = vnFormat.format(donGia);

        bookName.setText(cartItem.getName());
        bookPrice.setText(formattedDonGia);
        bookQuantity.setText(String.valueOf(cartItem.getQuantity()));

        buttonDecrease.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                updateCartItem(cartItem);
                notifyDataSetChanged();
                updateTotalPrice();
            }
        });

        buttonIncrease.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            updateCartItem(cartItem);
            notifyDataSetChanged();
            updateTotalPrice();
        });

        buttonDelete.setOnClickListener(v -> {
            removeCartItem(cartItem);
            cartItems.remove(position);
            notifyDataSetChanged();
            updateTotalPrice();
        });

        if (isAddOrderActivity) {
            buttonDelete.setVisibility(View.GONE);
        } else {
            buttonDelete.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();
        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalPrice = vnFormat.format(totalPrice);
        totalPriceTextView.setText(formattedTotalPrice);
    }

    private void updateCartItem(CartItem cartItem) {
        databaseReference.child(cartItem.getItemId()).setValue(cartItem);
    }

    private void removeCartItem(CartItem cartItem) {
        databaseReference.child(cartItem.getItemId()).removeValue();
    }
}
