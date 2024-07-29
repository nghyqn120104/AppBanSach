package com.example.appbansach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appbansach.R;
import com.example.appbansach.modle.CartItem;
import com.example.appbansach.modle.Invoice;

import java.util.List;

public class InvoiceAdapters extends BaseAdapter {
    private Context context;
    private List<Invoice> invoices;
    private LayoutInflater inflater;

    public InvoiceAdapters(Context context, List<Invoice> invoices) {
        this.context = context;
        this.invoices = invoices;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return invoices.size();
    }

    @Override
    public Object getItem(int position) {
        return invoices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_invoice, parent, false);
        }

        Invoice invoice = invoices.get(position);

        TextView customerNameTextView = convertView.findViewById(R.id.invoiceCustomerName);
        TextView timeIssuedTextView = convertView.findViewById(R.id.invoiceTimeIssued);
        TextView totalAmountTextView = convertView.findViewById(R.id.invoiceTotalAmount);
        TextView cartItemsDetailsTextView = convertView.findViewById(R.id.cartItemsDetails);

        customerNameTextView.setText("Khách hàng: " + invoice.getCustomerName());
        timeIssuedTextView.setText("Ngày đặt: " + invoice.getTimeIssued());
        totalAmountTextView.setText("Tổng tiền: " + invoice.getTotalAmount());

        // Display CartItem details
        StringBuilder cartItemsDetails = new StringBuilder();
        for (CartItem item : invoice.getCartItems()) {
            cartItemsDetails.append("Tên sách: ").append(item.getName())
                    .append(", Giá: ").append(item.getPrice())
                    .append(", Số lượng: ").append(item.getQuantity())
                    .append("\n");
        }
        cartItemsDetailsTextView.setText(cartItemsDetails.toString());

        return convertView;
    }
}
