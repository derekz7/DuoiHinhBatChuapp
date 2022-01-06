package com.example.duoihinhbatchu;

import static com.example.duoihinhbatchu.MainActivity.listQuestions;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class WonActivity extends AppCompatActivity {

    private Button btnShare;
    private ImageButton igbBack, igbExit;
    private CircularProgressBar circularProgressBar;
    private TextView txtCorrectCount, txtScore;
    private int correctCount = 0, score;
    private LottieAnimationView anim_Phao;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        correctCount = getIntent().getIntExtra("countCorrect", 0);
        score = getIntent().getIntExtra("score", 0);
        circularProgressBar.setProgressMax(listQuestions.size());
        txtScore.setText("Điểm của bạn: " + score);
        circularProgressBar.setProgress(correctCount);
        txtCorrectCount.setText(correctCount + "/" + listQuestions.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                anim_Phao.playAnimation();
            }
        },1500);

        igbExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.playClick(v.getContext());
                PlaySound.animClick(v);
                AlertDialog.Builder builder = new AlertDialog.Builder(WonActivity.this);
                builder.setTitle(R.string.exit);
                builder.setIcon(R.drawable.cancel);
                builder.setMessage(R.string.mess_exit);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        igbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.playClick(v.getContext());
                PlaySound.animClick(v);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(WonActivity.this, MainActivity.class));
                        finish();
                    }
                }, 500);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.playClick(v.getContext());
                PlaySound.animClick(v);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "Tôi đã phá đảo trò chơi với " +score+" điểm, bạn đã thử chưa: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                }, 500);
            }
        });
    }

    private void init() {
        btnShare = findViewById(R.id.btnShare);
        igbBack = findViewById(R.id.igbBack);
        igbExit = findViewById(R.id.igbExitGame);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        txtCorrectCount = findViewById(R.id.txCountCorrect);
        txtScore = findViewById(R.id.txScore);
        anim_Phao = findViewById(R.id.anim_Phao);
    }

}