//package com.example.appbansach;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.appbansach.modle.NhanVien;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class UpdateSinhVienActivity extends AppCompatActivity {
//
//    private EditText etMaSV, etHoTen, etDiaChi, etNgaySinh, etGioiTinh, etEmail, etDiemTB;
//    private TextView btnUpdateSinhVien;
//    private DatabaseReference mDatabase;
//    private Toolbar mToolbar;
//    private String sinhVienId;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_update_sinh_vien);
//
//        // Retrieve sinhvien_id from Intent
//        Intent intent = getIntent();
//        if (intent != null) {
//            sinhVienId = intent.getStringExtra("sinhvien_id");
//        }
//
//        // Initialize Firebase
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        // Set up Toolbar
//        mToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
//        getSupportActionBar().setTitle("Cập nhật sinh viên"); // Set title
//
//        // Connect EditText and TextView
//        etMaSV = findViewById(R.id.etMaSVUpdate);
//        etHoTen = findViewById(R.id.etHoTenUpdate);
//        etDiaChi = findViewById(R.id.etDiaChiUpdate);
//        etNgaySinh = findViewById(R.id.etNgaySinhUpdate);
//        etGioiTinh = findViewById(R.id.etGioiTinhUpdate);
//        etEmail = findViewById(R.id.etEmailUpdate);
//        etDiemTB = findViewById(R.id.etDiemTBUpdate);
//        btnUpdateSinhVien = findViewById(R.id.btnUpdateSinhVien);
//        etMaSV.setEnabled(false);
//        etMaSV.setFocusable(false);
//
//        // Fetch existing sinhvien details based on sinhvien_id and populate EditText fields
//        fetchSinhVienDetails();
//
//        // Set click listener for update button
//        btnUpdateSinhVien.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateSinhVienToFirebase();
//            }
//        });
//    }
//
//    // Hiển thị thông tin sinh viên trong update qua sinhvien_id
//    private void fetchSinhVienDetails() {
//        if (sinhVienId != null) {
//            mDatabase.child("sinhvien").child(sinhVienId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        NhanVien sinhVien = snapshot.getValue(NhanVien.class);
//                        if (sinhVien != null) {
//                            etMaSV.setText(sinhVien.getMaSV());
//                            etHoTen.setText(sinhVien.getHoTen());
//                            etDiaChi.setText(sinhVien.getDiaChi());
//                            etNgaySinh.setText(sinhVien.getNgaySinh());
//                            etGioiTinh.setText(sinhVien.getGioiTinh());
//                            etEmail.setText(sinhVien.getEmail());
//                            etDiemTB.setText(String.valueOf(sinhVien.getDiemTB()));
//                        }
//                    } else {
//                        Toast.makeText(UpdateSinhVienActivity.this, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(UpdateSinhVienActivity.this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    // Cập nhật sinh viên
//    private void updateSinhVienToFirebase() {
//        String maSV = etMaSV.getText().toString().trim();
//        String hoTen = etHoTen.getText().toString().trim();
//        String diaChi = etDiaChi.getText().toString().trim();
//        String ngaySinh = etNgaySinh.getText().toString().trim();
//        String gioiTinh = etGioiTinh.getText().toString().trim();
//        String email = etEmail.getText().toString().trim();
//        String diemTBStr = etDiemTB.getText().toString().trim();
//
//        if (maSV.isEmpty() || hoTen.isEmpty() || diaChi.isEmpty() || ngaySinh.isEmpty() ||
//                gioiTinh.isEmpty() || email.isEmpty() || diemTBStr.isEmpty()) {
//            Toast.makeText(UpdateSinhVienActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        double diemTB;
//        try {
//            diemTB = Double.parseDouble(diemTBStr);
//        } catch (NumberFormatException e) {
//            Toast.makeText(UpdateSinhVienActivity.this, "Điểm TB phải là số", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Tạo đối tượng SinhVien mới
//        NhanVien sinhVien = new NhanVien(maSV, hoTen, diaChi, ngaySinh, gioiTinh, email, diemTB, "");
//
//        mDatabase.child("sinhvien").child(sinhVienId).setValue(sinhVien)
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(UpdateSinhVienActivity.this, "Cập nhật sinh viên thành công", Toast.LENGTH_SHORT).show();
//                    finish();
//                    etMaSV.setText("");
//                    etHoTen.setText("");
//                    etDiaChi.setText("");
//                    etNgaySinh.setText("");
//                    etGioiTinh.setText("");
//                    etEmail.setText("");
//                    etDiemTB.setText("");
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(UpdateSinhVienActivity.this, "Cập nhật sinh viên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
//
//    // Handle back button on ActionBar
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//}
