package com.example.appbansach;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbansach.Adapter.NhanVienAdapter;
import com.example.appbansach.modle.NhanVien;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListNhanVienActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NhanVienAdapter adapter;
    private List<NhanVien> nhanVienList;
    private List<NhanVien> filteredNhanVienList;
    private TextView addNhanVien;
    private SearchView searchView;
    private DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nhan_vien);

        mDatabase = FirebaseDatabase.getInstance().getReference("nhanvien");

        recyclerView = findViewById(R.id.recyclerView);
        addNhanVien = findViewById(R.id.addNhanVien);
        searchView = findViewById(R.id.search_view);

        nhanVienList = new ArrayList<>();
        filteredNhanVienList = new ArrayList<>();

        fetchNhanViensFromFirebase();

        addNhanVien.setOnClickListener(v -> {
            Intent intent1 = new Intent(ListNhanVienActivity.this, AddNhanVienActivity.class);
            startActivity(intent1);
        });

        adapter = new NhanVienAdapter(this, filteredNhanVienList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNhanViens(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNhanViens(newText);
                return false;
            }
        });
    }

    private void fetchNhanViensFromFirebase() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nhanVienList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NhanVien nhanVien = snapshot.getValue(NhanVien.class);
                    if (nhanVien != null) {
                        nhanVienList.add(nhanVien);
                    }
                }
                filteredNhanVienList.clear();
                filteredNhanVienList.addAll(nhanVienList);
                adapter.notifyDataSetChanged();
                if (nhanVienList.isEmpty()) {
                    Toast.makeText(ListNhanVienActivity.this, "Danh sách nhân viên rỗng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListNhanVienActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterNhanViens(String query) {
        filteredNhanVienList.clear();
        if (!TextUtils.isEmpty(query)) {
            for (NhanVien nhanVien : nhanVienList) {
                if (nhanVien.getChucVu().toLowerCase().contains(query.toLowerCase())) {
                    filteredNhanVienList.add(nhanVien);
                }
            }
        } else {
            filteredNhanVienList.addAll(nhanVienList);
        }
        adapter.notifyDataSetChanged();
    }
}
