package com.example.appbansach;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appbansach.modle.NhanVien;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNhanVienActivity extends AppCompatActivity {

    private EditText etMaSinhVien, etHoTen, etChucVu, etHeSoLuong, etLuongCoBan, etPhuCap;
    private Button btnAddNhanVien;
    private DatabaseReference mDatabase;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sinhvien);

        mDatabase = FirebaseDatabase.getInstance().getReference("nhanvien");

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm Nhân Viên");

        etMaSinhVien = findViewById(R.id.etMaNhanVien);
        etHoTen = findViewById(R.id.etHoTen);
        etChucVu = findViewById(R.id.etChucVu);
        etHeSoLuong = findViewById(R.id.etHeSoLuong);
        etLuongCoBan = findViewById(R.id.etLuongCoBan);
        etPhuCap = findViewById(R.id.etPhuCap);
        btnAddNhanVien = findViewById(R.id.btnAddNhanVien);

        btnAddNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSinhVienToFirebase();
            }
        });
    }

    private void addSinhVienToFirebase() {
        String maSV = etMaSinhVien.getText().toString().trim();
        String hoTen = etHoTen.getText().toString().trim();
        String chucVu = etChucVu.getText().toString().trim();
        String heSoLuong = etHeSoLuong.getText().toString().trim();
        String luongCoBan = etLuongCoBan.getText().toString().trim();
        String phuCap = etPhuCap.getText().toString().trim();
//        String diemTBStr = etDiemTB.getText().toString().trim();

        if (TextUtils.isEmpty(maSV) || TextUtils.isEmpty(hoTen) || TextUtils.isEmpty(chucVu) ||
                TextUtils.isEmpty(heSoLuong) || TextUtils.isEmpty(luongCoBan) || TextUtils.isEmpty(phuCap)) {
            Toast.makeText(AddNhanVienActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double HSL;
        double Lcb;
        double pc;
        try {
            HSL = Double.parseDouble(heSoLuong);
            Lcb= Double.parseDouble(luongCoBan);
            pc = Double.parseDouble(phuCap);
        } catch (NumberFormatException e) {
            Toast.makeText(AddNhanVienActivity.this, "không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        NhanVien nhanVien = new NhanVien(maSV, hoTen, chucVu, HSL, Lcb, pc);

        mDatabase.child(maSV).setValue(nhanVien)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddNhanVienActivity.this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result", "success");
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddNhanVienActivity.this, "Thêm sinh viên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
