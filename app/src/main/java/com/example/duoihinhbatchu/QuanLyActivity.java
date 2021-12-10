package com.example.duoihinhbatchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.duoihinhbatchu.Database.CauHoiDB;
import com.example.duoihinhbatchu.Module.CauHoi;

import java.util.List;

public class QuanLyActivity extends AppCompatActivity {
    private Button btnQLCauHoi, btnQLPlayer;
    public static List<CauHoi> list;
    private CauHoiDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        db = new CauHoiDB();
        list = db.getListCauHoi();
        btnQLCauHoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanLyActivity.this,QuanLyCauHoi.class));
            }
        });
    }
    private void init(){
        btnQLCauHoi = findViewById(R.id.btnQlCauHoi);
        btnQLPlayer = findViewById(R.id.btnQlPlayer);
    }
}