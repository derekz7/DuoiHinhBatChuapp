package com.example.duoihinhbatchu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.duoihinhbatchu.Adapter.UserAdapter;
import com.example.duoihinhbatchu.Database.CauHoiDB;
import com.example.duoihinhbatchu.Database.UserDB;
import com.example.duoihinhbatchu.Module.CauHoi;
import com.example.duoihinhbatchu.Module.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LinearLayout layout_play;
    private TextView tvStart;
    private ImageView imgLogo;
    private View layout;
    private Button btnPlay, btnChart;
    public static MediaPlayer mpBackground;
    public static Animation animation, animBtn, slide_up;
    private List<User> listUser, listPlayer;
    private UserDB userDB;
    private CauHoiDB cauHoiDB;
    public static List<CauHoi> listQuestions;
    private User user;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        getData();
        imgLogo.startAnimation(animation);
        tvStart.startAnimation(animBtn);
        startMusicBackground(this);
        onClick();


    }

    private void onClick() {
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.playClick(v.getContext());
                PlaySound.animClick(v);
                imgLogo.startAnimation(slide_up);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                        Animation slide_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
                        tvStart.setVisibility(View.GONE);
                        layout_play.setVisibility(View.VISIBLE);
                        btnPlay.startAnimation(slide_right);
                        btnChart.startAnimation(slide_left);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                buttonScaleAnim();
                            }
                        }, 500);

                    }
                }, 600);

            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.playClick(v.getContext());
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.playClick(v.getContext());
                PlaySound.animClick(v);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonScaleAnim();
                        if (user.getName().equals("null")){
                            dialogCreateUser(Gravity.CENTER);
                        }else {
                            User log = userDB.getUser(user.getName(),listUser);
                            Intent intent = new Intent(MainActivity.this, ChoiGameActivity.class);
                            intent.putExtra("user", log);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 600);
            }
        });
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.playClick(v.getContext());
                PlaySound.animClick(v);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog_Leaderboard();
                    }
                },500);
            }
        });

    }

    private void getData() {
        cauHoiDB = new CauHoiDB();
        listQuestions = cauHoiDB.getListCauHoi();
        userDB = new UserDB();
        listUser = userDB.getDataUser();
    }

    private void buttonScaleAnim(){
        btnPlay.startAnimation(animBtn);
        btnChart.startAnimation(animBtn);
    }

    private List<User> getListPlayer(List<User> list){
        List<User> listPlayer = new ArrayList<>();
        for (User user : list){
            if (user.getIsAdmin() == 0){
                listPlayer.add(user);
            }
        }
        return listPlayer;
    }

    private void init() {
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
        animBtn = AnimationUtils.loadAnimation(this, R.anim.anim_btn);
        slide_up = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        layout = findViewById(R.id.mainLayout);
        imgLogo = findViewById(R.id.imgLogo);
        tvStart = findViewById(R.id.tvStart);
        layout_play = findViewById(R.id.layout_play);
        btnChart = findViewById(R.id.btnLeaderBoard);
        btnPlay = findViewById(R.id.btnPlayGame);
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        user = new User(sharedPreferences.getString("username","null"));
    }

    private void sapXep(){
        Collections.sort(listPlayer, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return Integer.compare(o2.getScore(), o1.getScore());
            }
        });
    }


    private static void startMusicBackground(Context context) {
        mpBackground = MediaPlayer.create(context, R.raw.background);
        mpBackground.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpBackground.start();
                mpBackground.setLooping(true);
                mpBackground.setVolume(0.5f,0.5f);
            }
        });
        mpBackground.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mpBackground.release();
            }
        });
    }

//    public static Animation getClickAnim(Context context) {
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_click);
//        return animation;
//    }

    @Override
    protected void onPause() {
        mpBackground.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mpBackground.start();
        super.onResume();
    }

    public void dialogCreateUser(int gravity) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_inputname);
        Button btnOk;
        EditText edtName;
        btnOk = dialog.findViewById(R.id.btnOK);
        edtName = dialog.findViewById(R.id.edtUsername);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.windowAnimations = R.style.DialogAnimation;
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.BOTTOM == gravity || Gravity.CENTER == gravity || Gravity.TOP == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                PlaySound.playClick(v.getContext());
                PlaySound.animClick(v);
                if (userDB.checkAdmin(listUser,name)) {
                    startActivity(new Intent(MainActivity.this, QuanLyActivity.class));
                    finish();
                } else if (name.length() == 0 || !userDB.checkUsername(listUser, name) || name.length() > 15){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setIcon(R.drawable.cancel);
                    builder.setTitle("Tên người dùng đã tồn tại");
                    builder.setMessage("Vui lòng chọn tên khác < 15 kí tự !");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }else {
                    user = new User(name);
                    userDB.createUser(user, getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username",user.getName());
                    editor.putString("diem","0");
                    editor.apply();
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }

    public void dialog_Leaderboard() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_leaderboard);
        RecyclerView rcvUser;
        rcvUser = dialog.findViewById(R.id.rcv_leaderboard);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.windowAnimations = R.style.DialogAnimation;
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        listPlayer = getListPlayer(listUser);
        sapXep();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcvUser.setLayoutManager(layoutManager);
        UserAdapter userAdapter = new UserAdapter(listPlayer,this);
        rcvUser.setAdapter(userAdapter);
        dialog.show();
    }

}