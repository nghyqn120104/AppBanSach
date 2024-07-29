package com.example.appbansach;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appbansach.modle.NhanVien;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SinhVienDetailActivity extends AppCompatActivity {

    private EditText etMaSinhVien, etHoTen, etDiaChi, etNgaySinh, etGioiTinh, etEmail, etDiemTB;
    private DatabaseReference mDatabase;
    private String sinhVienId;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference("sinhvien");

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi Tiết Sinh Viên");

        etMaSinhVien = findViewById(R.id.etMaSinhVien);
        etHoTen = findViewById(R.id.etHoTen);
        etDiaChi = findViewById(R.id.etDiaChi);
        etNgaySinh = findViewById(R.id.etNgaySinh);
        etGioiTinh = findViewById(R.id.etGioiTinh);
        etEmail = findViewById(R.id.etEmail);
        etDiemTB = findViewById(R.id.etDiemTB);

        sinhVienId = getIntent().getStringExtra("sinhvien_id");

        fetchSinhVienDetails();
    }

    private void fetchSinhVienDetails() {
        if (sinhVienId != null) {
            mDatabase.child(sinhVienId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NhanVien nhanVien = snapshot.getValue(NhanVien.class);
                    if (nhanVien != null) {
                        etMaSinhVien.setText(nhanVien.getMaNV());
                        etHoTen.setText(nhanVien.getHoTen());
                        etDiaChi.setText(nhanVien.getChucVu());
                        etNgaySinh.setText(String.valueOf(nhanVien.getHesoLuong()));
                        etGioiTinh.setText(String.valueOf(nhanVien.getLuongCoBan()));
                        etEmail.setText(String.valueOf(nhanVien.getPhuCap()));
//                        etDiemTB.setText(String.valueOf(nhanVien.getDiemTB()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SinhVienDetailActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

//    private void updateSinhVienToFirebase() {
//        String maSV = etMaSinhVien.getText().toString().trim();
//        String hoTen = etHoTen.getText().toString().trim();
//        String diaChi = etDiaChi.getText().toString().trim();
//        String ngaySinh = etNgaySinh.getText().toString().trim();
//        String gioiTinh = etGioiTinh.getText().toString().trim();
//        String email = etEmail.getText().toString().trim();
//        String diemTBStr = etDiemTB.getText().toString().trim();
//
//        if (TextUtils.isEmpty(hoTen) || TextUtils.isEmpty(diaChi) || TextUtils.isEmpty(ngaySinh) ||
//                TextUtils.isEmpty(gioiTinh) || TextUtils.isEmpty(email) || TextUtils.isEmpty(diemTBStr)) {
//            Toast.makeText(SinhVienDetailActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        double diemTB;
//        try {
//            diemTB = Double.parseDouble(diemTBStr);
//        } catch (NumberFormatException e) {
//            Toast.makeText(SinhVienDetailActivity.this, "Điểm trung bình không hợp lệ", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        NhanVien sinhVien = new NhanVien(maSV, hoTen, diaChi, ngaySinh, gioiTinh, email, diemTB, "");
//
//        if (sinhVienId != null) {
//            mDatabase.child(sinhVienId).setValue(sinhVien)
//                    .addOnSuccessListener(aVoid -> {
//                        Toast.makeText(SinhVienDetailActivity.this, "Cập nhật sinh viên thành công", Toast.LENGTH_SHORT).show();
//                        finish();
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(SinhVienDetailActivity.this, "Cập nhật sinh viên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    });
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
