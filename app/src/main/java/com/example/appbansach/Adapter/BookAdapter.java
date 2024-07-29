package com.example.appbansach.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbansach.R;
import com.example.appbansach.modle.Book;
import com.example.appbansach.updateBookActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> bookList;
    private OnItemClickListener mListener;
    private Context mContext;
    private String accountType;

    public BookAdapter(Context context, List<Book> bookList, String accountType) {
        this.mContext = context;
        this.bookList = bookList;
        this.accountType= accountType;
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView txtBookName;
        public TextView txtAuthor;
        public TextView txtPrice;
        public ImageView list;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            txtBookName = itemView.findViewById(R.id.txtBookName);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            list = itemView.findViewById(R.id.list);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onItemClick(bookList.get(position));
                }
            });

            list.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mListener != null) {
                    showOptionsDialog(mContext, bookList.get(position));
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View bookView = inflater.inflate(R.layout.item_book, parent, false);

        return new ViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);
        double donGia = book.getDonGia();

        NumberFormat vnFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        String formattedDonGia = vnFormat.format(donGia);

        holder.txtBookName.setText(book.getTenSach());
        holder.txtAuthor.setText(book.getTacGia());
        holder.txtPrice.setText(formattedDonGia);

        //  Toast.makeText(mContext, "Account Type in Adapter: " + accountType, Toast.LENGTH_SHORT).show();
        // Kiểm tra loại tài khoản và thiết lập visibility cho Button
        if ("admin".equals(accountType)) {
            holder.list.setVisibility(View.VISIBLE);
        } else if("user".equals(accountType)){
            holder.list.setVisibility(View.GONE);
        }

    }

    private void showOptionsDialog(Context context, Book book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn tác vụ");
        builder.setItems(new CharSequence[]{"Sửa", "Xóa"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    Intent intent = new Intent(context, updateBookActivity.class);
                    intent.putExtra("book_id", book.getMaSach());
                    context.startActivity(intent);
                    break;
                case 1:
                    deleteBook(book);
                    break;
            }
        });
        builder.create().show();
    }

    private void deleteBook(Book book) {
        int position = bookList.indexOf(book);
        bookList.remove(position);
        notifyItemRemoved(position);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("books");
        mDatabase.child(book.getMaSach()).removeValue();
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}