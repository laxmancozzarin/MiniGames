package com.example.lamiaapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    private MediaPlayer music;
    static int pausePosition = 0;
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MenuActivity.class);

                try {
                    if(music == null){
                        music = MediaPlayer.create( SplashActivity.this,R.raw.music);
                        music.start();
                    }
                    if(!music.isPlaying()){
                        music.seekTo(pausePosition);
                        music.start();
                    }
                }catch (Exception e){
                    Log.d("Music: ","Impossibile riprodurre la musica");
                }


                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    //ritorna la musica
    public MediaPlayer GetMusic() {
        return music;
    }

    //mette in pausa
    public void PauseMusic() {
        music.pause();
        pausePosition = music.getCurrentPosition();
    }

    //stoppa la musica
    public void Stop() {
        music.release();
        music = null;
    }
}