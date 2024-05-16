package com.example.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.R;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Button btn_prev, btn_play, btn_next;
    private ImageView coverArt;
    private int currentSongIndex = 0;

    private SeekBar seekBar;
    private int[] songs = {R.raw.limp_bizkit_behind_blue_eyes, R.raw.back_in_black, R.raw.blue_bird};

    private int[] covers = {R.drawable.limp_bizkit_new_old_songs, R.drawable.back_in_black, R.drawable.blue_bird};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_prev = findViewById(R.id.prev);
        btn_play = findViewById(R.id.play);
        btn_next = findViewById(R.id.next);
        coverArt = findViewById(R.id.cover_art);
        seekBar = findViewById(R.id.seekBar);

        mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex]);
        coverArt.setImageResource(covers[currentSongIndex]);

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btn_play.setText("Играть");
                } else {
                    mediaPlayer.start();
                    btn_play.setText("Пауза");
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                currentSongIndex = (currentSongIndex + 1) % songs.length;
                mediaPlayer = MediaPlayer.create(MainActivity.this, songs[currentSongIndex]);
                coverArt.setImageResource(covers[currentSongIndex]);
                mediaPlayer.start();
                btn_play.setText("Пауза");
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                currentSongIndex = (currentSongIndex - 1 + songs.length) % songs.length;
                mediaPlayer = MediaPlayer.create(MainActivity.this, songs[currentSongIndex]);
                coverArt.setImageResource(covers[currentSongIndex]);
                mediaPlayer.start();
                btn_play.setText("Пауза");
            }
        });
    }
}
