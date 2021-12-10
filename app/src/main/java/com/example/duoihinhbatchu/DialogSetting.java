package com.example.duoihinhbatchu;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;

public class DialogSetting {
    private Context context;
    private Dialog dialog;
    AudioManager audioManager;

    public DialogSetting(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void show(int gravity) {
        dialog = new Dialog(context);
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
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(Gravity.CENTER == gravity);
        igbFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.facebook.com/eelcud"));
                context.startActivity(intent);
            }
        });

        igbExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.animClick(v);
                PlaySound.playClick(v.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                context.startActivity(new Intent(context, MainActivity.class));
                dialog.cancel();
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
        dialog.show();
    }

    private void cancel() {
        dialog.cancel();
    }


}

