package com.example.duoihinhbatchu;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PlaySound {
    public static MediaPlayer mpClick,mp,mpAchiv;

    public static void playClick(Context context){
        mpClick = MediaPlayer.create(context,R.raw.click);
        mpClick.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mpClick.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    public static void playSound(Context context,int rawId){
        mpAchiv = MediaPlayer.create(context,rawId);
        mpAchiv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mpAchiv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    public static void playType(Context context){
        mp = MediaPlayer.create(context,R.raw.keytype);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    public static void animClick(View v){
        Animation animation = AnimationUtils.loadAnimation(v.getContext(),R.anim.anim_click);
        v.startAnimation(animation);
    }
}
