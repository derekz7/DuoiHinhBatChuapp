package com.example.duoihinhbatchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class QuanLyActivity extends AppCompatActivity {
    private Button btnQLCauHoi, btnQLPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
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