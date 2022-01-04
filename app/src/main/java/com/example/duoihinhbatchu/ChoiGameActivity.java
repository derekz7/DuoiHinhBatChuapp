package com.example.duoihinhbatchu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duoihinhbatchu.Adapter.DapAnAdapter;
import com.example.duoihinhbatchu.Database.UserDB;
import com.example.duoihinhbatchu.Models.CauDo;
import com.example.duoihinhbatchu.Models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChoiGameActivity extends AppCompatActivity {
    private int timer = 30;
    private List<CauDo> listQuestions;
    private TextView tvScore;
    private ProgressBar progressBar;
    private ImageButton igbSetting;
    private ImageView imgQuestion, imgStar;
    private ImageView[] imgHeart;
    private GridView gridViewDapAn, gridViewTraLoi;
    private List<String> arrDapan;
    private List<String> arrTraloi;
    private DapAnAdapter dapAnAdapter, traLoiAdapter;
    private String dapan = "";
    private int index = 0, currentQuestions = 0, heart = 3, countCorrect = 0;
    private User user;
    private UserDB db;
    private int score, newScore = 0;
    private CountDownTimer countDownTimer;
    private SharedPreferences sharedPreferences;
    private Animation anim_heart;
    private LinearLayout view_heart;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choi_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        getData();
        user = (User) getIntent().getSerializableExtra("user");
        score = user.getScore();
        tvScore.setText(String.valueOf(score));
        loadView();
        // Hien thi view cau hoi
        ViewQuestions();
        eventClick();

    }

    private void loadView() {
        dapAnAdapter = new DapAnAdapter(this, R.layout.item_dapan, arrDapan);
        traLoiAdapter = new DapAnAdapter(this, R.layout.item_traloi, arrTraloi);
        gridViewDapAn.setAdapter(dapAnAdapter);
        gridViewTraLoi.setAdapter(traLoiAdapter);
    }

    private void eventClick() {
        igbSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                dialog_setting();
            }
        });

        gridViewDapAn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaySound.playType(view.getContext());
                String s = arrDapan.get(position);
                if (s != null) {
                    arrDapan.set(position, "");
                    for (int i = 0; i < arrTraloi.size(); i++) {
                        if (arrTraloi.get(i).length() == 0) {
                            arrTraloi.set(i, s);
                            break;
                        }
                    }
                }
                dapAnAdapter.notifyDataSetChanged();
                traLoiAdapter.notifyDataSetChanged();
//                System.out.println("now index " + index);

            }
        });

        gridViewTraLoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaySound.playType(view.getContext());
                String s = arrTraloi.get(position);
                for (int i = 0; i < arrDapan.size(); i++) {
                    if (arrDapan.get(i).length() == 0) {
                        index = i;
                        break;
                    }
                }
                if (s.length() != 0 && index < arrDapan.size()) {
                    if (arrDapan.get(index).length() != 0) {
                        return;
                    }
                    arrTraloi.set(position, "");
                    arrDapan.set(index, s);
                }
                dapAnAdapter.notifyDataSetChanged();
                traLoiAdapter.notifyDataSetChanged();
                checkWin();
            }
        });
    }


    //show đáp án
    private void showDapAn() {
        if (dapan.length() >= 8) {
            //set cột cho gridview hiển thị đáp án
            gridViewDapAn.setNumColumns(dapan.length() - 2);
        } else
            gridViewDapAn.setNumColumns(dapan.length());
        gridViewDapAn.setGravity(Gravity.CENTER);
        for (int i = 0; i < dapan.length(); i++) {
            arrDapan.add("");
        }
        dapAnAdapter.notifyDataSetChanged();
    }

    //show tra loi
    private void showTraLoi() {
        if (dapan.length() >= 8) {
            gridViewTraLoi.setNumColumns(dapan.length() - 2);
        } else {
            gridViewTraLoi.setNumColumns(dapan.length());
        }

        gridViewTraLoi.setGravity(Gravity.CENTER);
        char[] arr;
        arr = dapan.toCharArray();
        Random random = new Random();
        for (int i = 0; i < dapan.length(); i++) {
            arrTraloi.add(String.valueOf(arr[i]));
            char a = (char) (random.nextInt(26) + 65);
            arrTraloi.add(String.valueOf(a));
        }
        Collections.shuffle(arrTraloi);
        traLoiAdapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    private void ViewQuestions() {
        setCountDownTimer();
        if (currentQuestions >= listQuestions.size()) {
            // Hoan thanh
            Intent intent = new Intent(ChoiGameActivity.this,WonActivity.class);
            intent.putExtra("countCorrect",countCorrect);
            intent.putExtra("score",score);
            startActivity(intent);

        } else {
            progressBar.setVisibility(View.VISIBLE);
            arrDapan.clear();
            arrTraloi.clear();
            CauDo cauDo = listQuestions.get(currentQuestions);
            System.out.println(currentQuestions);
            Picasso.get().load(cauDo.getImgUrl()).into(imgQuestion);
            dapan = StringUtils.xoaDauString(cauDo.getDapan());
            showDapAn();
            showTraLoi();
            countDownTimer.start();
        }
    }

    private void setCountDownTimer() {
        progressBar.setProgress(timer);
        progressBar.setMax(timer);
        countDownTimer = new CountDownTimer(timer * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int count = progressBar.getProgress() - 1;
                if (count < 0) {
                    countDownTimer.cancel();
                    countDownTimer.onFinish();
                } else {
                    progressBar.setProgress(count);
                    System.out.println(count);
                }
            }

            @Override
            public void onFinish() {
                heart--;
                setHeart(heart);
                if (heart > 0) {
                    dialogTimeOut();
                }

            }
        };
    }

    private void checkWin() {
        String a = "";
        for (String s : arrDapan) {
            a += s;
        }
        if (a.length() == dapan.length()) {
            if (a.equalsIgnoreCase(dapan)) {
//                Toast.makeText(ChoiGameActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                int time = progressBar.getProgress();
                if (time <= timer / 3) {
                    newScore = 50;
                } else if (time <= timer / 1.5) {
                    newScore = 100;
                } else newScore = 150;
                countCorrect++;
                score += newScore;
                user.setScore(score);
                db.updateScore(user.getName(), score);
                tvScore.setText(String.valueOf(user.getScore()));
                countDownTimer.cancel();
                dialog_Score(time);
            } else {
                heart--;
                setHeart(heart);
                PlaySound.playSound(this, R.raw.losing);
                gridViewDapAn.startAnimation(animShake());
//                Toast.makeText(ChoiGameActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void setHeart(int heart) {
        switch (heart) {
            case 0:
                view_heart.startAnimation(animShake());
                imgHeart[0].setVisibility(View.GONE);
                imgHeart[1].setVisibility(View.GONE);
                imgHeart[2].startAnimation(anim_heart);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgHeart[2].setVisibility(View.GONE);
                        countDownTimer.cancel();
                        dialog_Lose();
                    }
                }, 500);

                break;
            case 1:
                view_heart.startAnimation(animShake());
                imgHeart[0].setVisibility(View.GONE);
                imgHeart[1].startAnimation(anim_heart);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgHeart[1].setVisibility(View.GONE);
                    }
                }, 500);
                break;
            case 2:
                view_heart.startAnimation(animShake());
                imgHeart[0].startAnimation(anim_heart);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgHeart[0].setVisibility(View.GONE);
                    }
                }, 500);
                break;
            case 3:
                imgHeart[0].setVisibility(View.VISIBLE);
                imgHeart[1].setVisibility(View.VISIBLE);
                imgHeart[2].setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
    private void init() {
        progressBar = findViewById(R.id.progressBar);
        imgQuestion = findViewById(R.id.imgQuestion);
        tvScore = findViewById(R.id.tvScore);
        igbSetting = findViewById(R.id.imbSetting);
        gridViewDapAn = findViewById(R.id.gridViewDapAn);
        gridViewTraLoi = findViewById(R.id.gridViewTraLoi);
        imgHeart = new ImageView[heart];
        imgHeart[0] = findViewById(R.id.imgHeart1);
        imgHeart[1] = findViewById(R.id.imgHeart2);
        imgHeart[2] = findViewById(R.id.imgHeart3);
        view_heart = findViewById(R.id.view_heart);
    }
    private void getData() {
        db = new UserDB();
        listQuestions = MainActivity.listQuestions;
        arrDapan = new ArrayList<>();
        arrTraloi = new ArrayList<>();
        sharedPreferences = getSharedPreferences("currentQuestion", MODE_PRIVATE);
        currentQuestions = sharedPreferences.getInt("currentQuestion", 0);
        countCorrect = sharedPreferences.getInt("countCorrect", 0);
        anim_heart = AnimationUtils.loadAnimation(this, R.anim.anim_zoomout);
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    @SuppressLint("SetTextI18n")
    private void dialog_Lose() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loser);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        Button btnSkip = dialog.findViewById(R.id.btnSkip);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);
        PlaySound.playSound(this, R.raw.game_over);
        score -= 150;
        user.setScore(score);
        db.updateScore(user.getName(), score);
        tvScore.setText(String.valueOf(user.getScore()));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentQuestion", currentQuestions);
        editor.apply();

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        heart = 3;
                        setHeart(heart);
                        currentQuestions++;
                        ViewQuestions();
                        dialog.dismiss();
                    }
                }, 500);

            }
        });
        dialog.show();
    }

    private void dialogTimeOut() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_timeout);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        Button btnReplay, btnExit;
        btnReplay = dialog.findViewById(R.id.btnReplay);
        btnExit = dialog.findViewById(R.id.btnExit);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);
        PlaySound.playSound(this, R.raw.gameover);
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ViewQuestions();
                        dialog.dismiss();
                    }
                }, 500);

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ChoiGameActivity.this, MainActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                }, 500);

            }
        });
        dialog.show();
    }

    private void dialogPause() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pause);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        Button btnReplay, btnExit, btnResume;
        btnReplay = dialog.findViewById(R.id.btnReplay_);
        btnExit = dialog.findViewById(R.id.btnExit_);
        btnResume = dialog.findViewById(R.id.btnResume);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                countDownTimer.start();
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                dialog.dismiss();
            }
        });
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        countCorrect = 0;
                        currentQuestions = 0;
                        ViewQuestions();
                        dialog.dismiss();
                    }
                }, 500);

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChoiGameActivity.this);
                        builder.setIcon(R.drawable.cancel);
                        builder.setTitle(R.string.exit);
                        builder.setMessage(R.string.mess_exit);
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        });
                        builder.create().show();
                    }
                }, 500);

            }
        });

        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void dialog_Score(int time) {
        PlaySound.playSound(this, R.raw.achivement);
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_score);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        TextView tvScore, tvDapAn;
        tvScore = dialog.findViewById(R.id.tvNewScore);
        tvDapAn = dialog.findViewById(R.id.tvDapAn);
        Button btnNext = dialog.findViewById(R.id.btnNext);
        imgStar = dialog.findViewById(R.id.imgStar);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);
        if (time <= timer / 3) {
            imgStar.setImageResource(R.drawable.onestar);
        } else if (time < timer / 1.5) {
            imgStar.setImageResource(R.drawable.twostar);
        } else {
            imgStar.setImageResource(R.drawable.threestar);
        }
        tvScore.setText("+ " + newScore);
        tvDapAn.setText("Đáp án: "+listQuestions.get(currentQuestions).getDapan());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentQuestion", (currentQuestions+1));
        editor.putInt("countCorrect", countCorrect);
        editor.apply();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentQuestions++;
                        ViewQuestions();
                        dialog.dismiss();
                    }
                }, 500);

            }
        });

        dialog.show();
    }


    private void dialog_setting() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_settings);
        final ImageButton igbExit, igbHome, igbMusic, igbFB;
        final SeekBar skMusic;
        igbHome = dialog.findViewById(R.id.igbHome);
        igbMusic = dialog.findViewById(R.id.igbMusic);
        igbFB = dialog.findViewById(R.id.igbFB);
        skMusic = dialog.findViewById(R.id.skbarVolume);
        igbExit = dialog.findViewById(R.id.igbExit);
        if (MainActivity.mpBackground.isPlaying()) {
            igbMusic.setImageResource(R.drawable.music);
        } else {
            igbMusic.setImageResource(R.drawable.mute);
        }
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
        igbFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.facebook.com/eelcud"));
                startActivity(intent);
            }
        });

        igbExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(ChoiGameActivity.this);
                builder.setIcon(R.drawable.cancel);
                builder.setTitle(R.string.exit);
                builder.setMessage(R.string.mess_exit);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                builder.create().show();
            }
        });
        igbHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                startActivity(new Intent(ChoiGameActivity.this, MainActivity.class));
                dialog.dismiss();
                finish();
            }
        });
        if (MainActivity.mpBackground.isPlaying()) {
            igbMusic.setImageResource(R.drawable.music);
            skMusic.setEnabled(true);
            skMusic.setAlpha(1);
        } else {
            igbMusic.setImageResource(R.drawable.mute);
            skMusic.setEnabled(false);
            skMusic.setAlpha(0.5F);
        }
        igbMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                if (MainActivity.mpBackground.isPlaying()) {
                    igbMusic.setImageResource(R.drawable.mute);
                    MainActivity.mpBackground.pause();
                    skMusic.setEnabled(false);
                    skMusic.setAlpha((float) 0.5);
                } else {
                    MainActivity.mpBackground.start();
                    igbMusic.setImageResource(R.drawable.music);
                    skMusic.setEnabled(true);
                    skMusic.setAlpha((float) 1);
                }

            }
        });
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        skMusic.setMax(max);
        skMusic.setProgress(currentVol);
        skMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                countDownTimer.cancel();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                countDownTimer.start();
            }
        });
        dialog.show();
    }

    private Animation animShake() {
        return AnimationUtils.loadAnimation(this, R.anim.anim_shake);
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        progressBar.setVisibility(View.GONE);
        dialogPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.mpBackground.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.mpBackground.pause();
    }
}