package com.example.duoihinhbatchu;

import static com.example.duoihinhbatchu.MainActivity.listQuestions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.duoihinhbatchu.Adapter.CauHoiAdapter;
import com.example.duoihinhbatchu.Database.CauHoiDB;
import com.example.duoihinhbatchu.Module.CauHoi;

import java.util.List;

public class QuanLyCauHoi extends AppCompatActivity {
    private RecyclerView rcv_CauHoi;
    private ImageButton imbBack, btnSetting, btnAdd;
    private List<CauHoi> cauHoiList;
    private CauHoiAdapter cauHoiAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_cau_hoi);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initUI();
        getData();
        GridLayoutManager layoutManager =new GridLayoutManager(this,2);
        rcv_CauHoi.setLayoutManager(layoutManager);
        cauHoiAdapter = new CauHoiAdapter(this,cauHoiList);
        rcv_CauHoi.setAdapter(cauHoiAdapter);
        onClick();
    }

    private void onClick() {
        imbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSetting dialogSetting =new DialogSetting(QuanLyCauHoi.this);
                dialogSetting.show(Gravity.CENTER);
            }
        });
    }

    private void getData() {
        cauHoiList = listQuestions;
    }

    private void initUI() {
        rcv_CauHoi = findViewById(R.id.rcvListCD);
        btnAdd = findViewById(R.id.igbAdd);
        imbBack = findViewById(R.id.imbBack);
        btnSetting = findViewById(R.id.igbSetting);
    }
}