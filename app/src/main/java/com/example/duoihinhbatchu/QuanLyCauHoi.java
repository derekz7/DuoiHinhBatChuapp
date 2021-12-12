package com.example.duoihinhbatchu;

import static com.example.duoihinhbatchu.QuanLyActivity.list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.duoihinhbatchu.Adapter.CauHoiAdapter;
import com.example.duoihinhbatchu.Database.CauHoiDB;
import com.example.duoihinhbatchu.Models.CauHoi;

import java.util.List;

public class QuanLyCauHoi extends AppCompatActivity {
    private RecyclerView rcv_CauHoi;
    private ImageButton imbBack, btnSetting, btnAdd;
    private List<CauHoi> cauHoiList;
    private CauHoiAdapter cauHoiAdapter;
    private ImageView imgAnh;
    private CauHoiDB cauHoiDB;
    private Uri imgUri;


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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_themCH();
            }
        });
    }

    private void getData() {
        cauHoiDB = new CauHoiDB();
        cauHoiList = list;
    }

    private void initUI() {
        rcv_CauHoi = findViewById(R.id.rcvListCD);
        btnAdd = findViewById(R.id.igbAdd);
        imbBack = findViewById(R.id.imbBack);
        btnSetting = findViewById(R.id.igbSetting);
        btnAdd = findViewById(R.id.igbAdd);
    }

    public void dialog_themCH() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addcauhoi);
        Button btnSave, btnCancel, btnOpenLib;
        EditText edtDapAn = dialog.findViewById(R.id.editDapAn);
        imgAnh = dialog.findViewById(R.id.imgAnhCH);
        btnSave = dialog.findViewById(R.id.igbSave);
        btnCancel = dialog.findViewById(R.id.igbCancel);
        btnOpenLib = dialog.findViewById(R.id.btnChonAnh);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        windowAttributes.windowAnimations = R.style.DialogAnimation;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String dapAn = edtDapAn.getText().toString().trim();
                if (dapAn.length() != 0 && dapAn.matches(".*[a-zA-Z].*")){
                    if (imgAnh != null){
                        int id = cauHoiList.size();
                        cauHoiDB.upLoadCauHoi(QuanLyCauHoi.this,id, imgUri, dapAn);
                        getData();
                        cauHoiAdapter.setData(cauHoiList);
                        dialog.dismiss();
                    }else {
                        Toast.makeText(QuanLyCauHoi.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    edtDapAn.setError("Vui lòng nhập kí tự A-Z không dấu!");
                }

            }
        });
        btnOpenLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imgUri = data.getData();
            imgAnh.setImageURI(imgUri);
        }

    }
}