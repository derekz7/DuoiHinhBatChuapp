package com.example.duoihinhbatchu.Admin;

import static com.example.duoihinhbatchu.Admin.QuanLyActivity.list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.duoihinhbatchu.Adapter.CauDoAdapter;
import com.example.duoihinhbatchu.Database.CauDoDB;
import com.example.duoihinhbatchu.Database.UserDB;
import com.example.duoihinhbatchu.DialogSetting;
import com.example.duoihinhbatchu.Models.CauDo;
import com.example.duoihinhbatchu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QuanLyCauHoi extends AppCompatActivity {
    private RecyclerView rcv_CauHoi;
    private ImageButton imbBack, btnSetting, btnAdd;
    private List<CauDo> cauDoList;
    private CauDoAdapter cauDoAdapter;
    private ImageView imgAnh;
    private CauDoDB cauDoDB;
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
        cauDoAdapter = new CauDoAdapter(this, cauDoList);
        rcv_CauHoi.setAdapter(cauDoAdapter);
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
        cauDoAdapter.setOnItemClickListener(new CauDoAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                dialog_update(cauDoList.get(pos),pos);
            }
        });
    }

    private void getData() {
        cauDoDB = new CauDoDB();
        cauDoList = list;
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
                if (dapAn.length() != 0){
                    if (imgAnh != null && imgUri != null){
                        int id = cauDoList.size()+1;
                        cauDoDB.upLoadCauHoi(QuanLyCauHoi.this,id, imgUri, dapAn);
                        getData();
                        cauDoAdapter.setData(cauDoList);
                        dialog.dismiss();
                    }else {
                        Toast.makeText(QuanLyCauHoi.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    edtDapAn.setError("Vui lòng nhập đáp án");
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

    public void dialog_update(CauDo cauDo, int pos) {
        Dialog dialogUpdate = new Dialog(this);
        dialogUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUpdate.setContentView(R.layout.dialog_updatecaudo);
        Button btnSave, btnDel , btnOpenLib;
        TextView tvDapAn = dialogUpdate.findViewById(R.id.tvDapan);
        EditText edtDapAn = dialogUpdate.findViewById(R.id.edtDapAn);
        imgAnh = dialogUpdate.findViewById(R.id.imgAnhCD);
        btnSave = dialogUpdate.findViewById(R.id.igbSaveCD);
        btnDel = dialogUpdate.findViewById(R.id.igbDel);
        btnOpenLib = dialogUpdate.findViewById(R.id.btnChonAnhCD);
        Window window = dialogUpdate.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        windowAttributes.windowAnimations = R.style.DialogAnimation;
        window.setAttributes(windowAttributes);
        dialogUpdate.setCancelable(true);
        edtDapAn.setText(cauDo.getDapan());
        tvDapAn.setText(cauDo.getDapan());
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyCauHoi.this);
                builder.setTitle("Xoá câu đố " + cauDo.getDapan() + "!");
                builder.setMessage("Việc xoá dữ liệu không thể khôi phục lại!");
                builder.setIcon(R.drawable.cancel);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialogUpdate.dismiss();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cauDoDB.deleteCauDo(cauDo,QuanLyCauHoi.this);
                        cauDoList.remove(cauDo);
                        cauDoAdapter.setData(cauDoList);
                        dialog.dismiss();
                        dialogUpdate.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                String dapAn = edtDapAn.getText().toString().trim();
                if (dapAn.length() != 0){
                    if (imgAnh != null && imgUri != null){
                        cauDoDB.updateCauDo(QuanLyCauHoi.this,cauDo,imgUri,dapAn);
                        cauDoList.set(pos,cauDo);
                        cauDoAdapter.setData(cauDoList);
                        dialogUpdate.dismiss();
                    }else {
                        Toast.makeText(QuanLyCauHoi.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    edtDapAn.setError("Vui lòng nhập đáp án");
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
        dialogUpdate.show();

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