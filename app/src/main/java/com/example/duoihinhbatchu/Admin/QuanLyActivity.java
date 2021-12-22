package com.example.duoihinhbatchu.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.duoihinhbatchu.Database.CauDoDB;
import com.example.duoihinhbatchu.Database.UserDB;
import com.example.duoihinhbatchu.Models.CauDo;
import com.example.duoihinhbatchu.Models.User;
import com.example.duoihinhbatchu.R;

import java.util.List;

public class QuanLyActivity extends AppCompatActivity {
    private Button btnQLCauHoi, btnQLPlayer;
    public static List<CauDo> list;
    public static List<User> userList;
    private UserDB userDB;
    private CauDoDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        db = new CauDoDB();
        userDB = new UserDB();
        list = db.getListCauHoi();
        userList = userDB.getDataUser();
        btnQLCauHoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanLyActivity.this, QuanLyCauHoi.class));
            }
        });
        btnQLPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuanLyActivity.this, QuanLyUserActivity.class));
            }
        });
    }
    private void init(){
        btnQLCauHoi = findViewById(R.id.btnQlCauHoi);
        btnQLPlayer = findViewById(R.id.btnQlPlayer);
    }
}