package com.techaj.apoorvajain.hbd;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    AnimationDrawable frameAnimation;
    MediaPlayer mMediaPlayer;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;
    private String LOG = "LOG of HBD";
    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    mMediaPlayer.stop();
                    releaseMediaPlayer();
                    //stop();
                    Log.d(LOG, "AudioFocus: received AUDIOFOCUS_LOSS");

                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//              if (mp.isPlaying())
                    mMediaPlayer.pause();

                    Log.d(LOG, "AudioFocus: received AUDIOFOCUS_LOSS_TRANSIENT");

                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mMediaPlayer.setVolume(0.3f, 0.3f);
                    Log.d(LOG,  "AudioFocus: received AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;

                case AudioManager.AUDIOFOCUS_GAIN:
                    if (mMediaPlayer == null)
                        createAudioPlayerAndStart();
                        mMediaPlayer.start();
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                        Log.d(LOG, "AudioFocus: received AUDIOFOCUS_GAIN");


                    break;

                default:
                    Log.e(LOG, "Unknown audio focus change code");
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.v(LOG,"Inside create");


        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setBackgroundResource(R.drawable.photo);        //drawable contains photos for slide show from i0 to i9.jpg

        frameAnimation = (AnimationDrawable) imageView.getBackground();

        frameAnimation.start();//starting slide show
        createAudioPlayerAndStart();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(LOG,"Inside start");
        if (mMediaPlayer == null)
            createAudioPlayerAndStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(LOG,"Inside restart");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.v(LOG,"Inside stop");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG,"Inside destroy");

        releaseMediaPlayer();

    }

    private void createAudioPlayerAndStart(){
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // We have audio focus now.
            Log.d(LOG, "AUDIOFOCUS_REQUEST_GRANTED");
            // Create and setup the {@link MediaPlayer} for the audio resource associated
            // with the current word


            mMediaPlayer = MediaPlayer.create(this, R.raw.med);    // raw folder contains file named med.mp3 which is birthday song
            mMediaPlayer.setLooping(true); // Start the animation (looped playback).

            // Start the audio file
            mMediaPlayer.start();

           /* // Setup a listener on the media player, so that we can stop and release the
            // media player once the sound has finished playing.
            mMediaPlayer.setOnCompletionListener(mCompletionListener);      not applicable here as we have used setLooping true*/
        }
    }
    private void releaseMediaPlayer() {
        Log.v(LOG,"Inside releaseMediaPlayer()");
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.stop();
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}

