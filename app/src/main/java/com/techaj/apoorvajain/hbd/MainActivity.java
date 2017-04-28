package com.techaj.apoorvajain.hbd;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    AnimationDrawable frameAnimation;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.med);    // raw folder contains file named med.mp3 which is birthday song
        mediaPlayer.setLooping(true); // Start the animation (looped playback).


        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setBackgroundResource(R.drawable.photo);        //drawable contains photos for slide show from i0 to i9.jpg

        frameAnimation = (AnimationDrawable) imageView.getBackground();


    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        frameAnimation.start();               //starting slide show

        mediaPlayer.start();                 // starting song

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();


    }


}

