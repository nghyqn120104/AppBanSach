package com.example.appbansach.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbansach.R;
import com.example.appbansach.modle.NhanVien;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> {

    private List<NhanVien> nhanVienList;
    private OnItemClickListener mListener;
    private Context mContext;

    public NhanVienAdapter(Context context, List<NhanVien> nhanVienList) {
        this.mContext = context;
        this.nhanVienList = nhanVienList;
    }

    // Make sure the interface is public
    public interface OnItemClickListener {
        void onItemClick(NhanVien nhanVien);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView txtHoTen;
        public TextView txtMaNV;
        public TextView txtChucVu;
        public ImageView list;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            txtHoTen = itemView.findViewById(R.id.txtHoTen);
            txtMaNV = itemView.findViewById(R.id.txtMaNV);
            txtChucVu = itemView.findViewById(R.id.txtChucVu);
            list = itemView.findViewById(R.id.list);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onItemClick(nhanVienList.get(position));
                }
            });

//            list.setOnClickListener(v -> {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION && mListener != null) {
//                    showOptionsDialog(mContext, nhanVienList.get(position));
//                }
//            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View nhanVienView = inflater.inflate(R.layout.item_nhanvien, parent, false);

        return new ViewHolder(nhanVienView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NhanVien nhanVien = nhanVienList.get(position);

        holder.txtHoTen.setText(nhanVien.getHoTen());
        holder.txtMaNV.setText(nhanVien.getMaNV());
        holder.txtChucVu.setText(nhanVien.getChucVu());
    }

//    private void showOptionsDialog(Context context, NhanVien nhanVien) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Chọn tác vụ");
//        builder.setItems(new CharSequence[]{"Sửa", "Xóa"}, (dialog, which) -> {
//            switch (which) {
//                case 0:
//                    Intent intent = new Intent(context, UpdateNhanVienActivity.class);
//                    intent.putExtra("nhanvien_id", nhanVien.getMaNV());
//                    context.startActivity(intent);
//                    break;
//                case 1:
//                    deleteNhanVien(nhanVien);
//                    break;
//            }
//        });
//        builder.create().show();
//    }

    private void deleteNhanVien(NhanVien nhanVien) {
        int position = nhanVienList.indexOf(nhanVien);
        nhanVienList.remove(position);
        notifyItemRemoved(position);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("nhanvien");
        mDatabase.child(nhanVien.getMaNV()).removeValue();
    }

    @Override
    public int getItemCount() {
        return nhanVienList.size();
    }
}
