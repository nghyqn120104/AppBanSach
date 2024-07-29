// StatisticsActivity.java
package com.example.appbansach;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbansach.Adapter.InvoiceAdapters;
import com.example.appbansach.modle.CartItem;
import com.example.appbansach.modle.Invoice;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    private Spinner monthSpinner;
    private ListView invoiceListView;
    private TextView totalRevenueTextView;
    private List<Invoice> invoiceList;
    private InvoiceAdapters invoiceAdapter;
    private DatabaseReference databaseReference;
    private ImageView imgoutstaistic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        Intent intent = getIntent();
        String role = intent.getStringExtra("role");
        monthSpinner = findViewById(R.id.monthSpinner);
        invoiceListView = findViewById(R.id.invoiceListView);
        totalRevenueTextView = findViewById(R.id.totalRevenueTextView);

        invoiceList = new ArrayList<>();
        invoiceAdapter = new InvoiceAdapters(this, invoiceList);
        invoiceListView.setAdapter(invoiceAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("invoices");

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = String.format("%02d", position + 1);
                fetchInvoicesForMonth(selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        imgoutstaistic = findViewById(R.id.imgoutstatistics);
        imgoutstaistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticsActivity.this, MainActivityAdmin.class);
                intent.putExtra("role", role);
                startActivity(intent);
            }
        });
    }

    private void fetchInvoicesForMonth(String month) {
        databaseReference.orderByChild("timeIssued").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                invoiceList.clear();
                double totalRevenue = 0.0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Invoice invoice = snapshot.getValue(Invoice.class);
                    if (invoice != null && invoice.getTimeIssued().substring(5, 7).equals(month)) {
                        // Fetch chi tiết các CartItem cho mỗi hóa đơn
                        List<CartItem> cartItems = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : snapshot.child("cartItems").getChildren()) {
                            CartItem cartItem = itemSnapshot.getValue(CartItem.class);
                            if (cartItem != null) {
                                cartItems.add(cartItem);
                            }
                        }
                        invoice.setCartItems(cartItems);
                        invoiceList.add(invoice);
                        totalRevenue += invoice.getTotalAmount();
                    }
                }
                invoiceAdapter.notifyDataSetChanged();
                totalRevenueTextView.setText("Total Revenue: " + totalRevenue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StatisticsActivity.this, "Lỗi khi truy xuất dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

