package com.example.duoihinhbatchu.Admin;

import static com.example.duoihinhbatchu.Admin.QuanLyActivity.userList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.duoihinhbatchu.Adapter.CauDoAdapter;
import com.example.duoihinhbatchu.Adapter.UserAdapter;
import com.example.duoihinhbatchu.Database.UserDB;
import com.example.duoihinhbatchu.DialogSetting;
import com.example.duoihinhbatchu.Models.User;
import com.example.duoihinhbatchu.R;

import java.util.List;


public class QuanLyUserActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUser;
    private UserAdapter userAdapter;
    private List<User> list;
    private ImageButton igbBack, igbSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_player);
        initUI();
        list = userList;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewUser.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(list,this);
        recyclerViewUser.setAdapter(userAdapter);
        onClick();
    }
    private void onClick() {
        igbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        igbSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSetting dialogSetting =new DialogSetting(QuanLyUserActivity.this);
                dialogSetting.show(Gravity.CENTER);
            }
        });
        userAdapter.setOnItemLongClickListener(new UserAdapter.onItemLongClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                User user = list.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyUserActivity.this);
                builder.setTitle("Xoá người chơi " + user.getName() + "!");
                builder.setMessage("Việc xoá người chơi không thể khôi phục lại!");
                builder.setIcon(R.drawable.cancel);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserDB userDB = new UserDB();
                        userDB.deleteUser(user,QuanLyUserActivity.this);
                        list.remove(user);
                        userAdapter.setData(list);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }
    private void initUI() {
        igbBack = findViewById(R.id.imbBackQLU);
        igbSetting = findViewById(R.id.igbSettingQLU);
        recyclerViewUser = findViewById(R.id.rcvListUser);
    }
}