package com.example.vishal.seizalert;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WarningActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private int sound1;
    private MediaPlayer mp;
    private int timer = 15;
    private boolean pressed = false;
    private TextView countdown;
    private boolean kill = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);

        countdown = (TextView) findViewById(R.id.look);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
        sound1 = soundPool.load(this, R.raw.alarm, 1);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTimer();
            }
        }, 1000);

        Button soundBtn = (Button) findViewById(R.id.button);
        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kill = true;
                startActivity(new Intent(WarningActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void mReadJsonData(String params) {
        try {
            File f = new File("/data/data/" + getPackageName() + "/" + params);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void startTimer() {
        if (kill) return;
        if (timer < 1) {
            //finish code
            pressed = false;
            countdown.setText((timer + ""));
            startActivity(new Intent(WarningActivity.this, SMSActivity.class));
            return;
        }
        pressed = true;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //startTimer();
                        //Toast.makeText(MainActivity.this, "YOOOOOOOO", Toast.LENGTH_SHORT).show()System.out.println(timer);
                        countdown.setText((timer + ""));
                        //pressed = false;
                        timer--;
                        soundPool.play(sound1, 1, 1, 1, 0, 1);
                        startTimer();

                    }
                },
                1000
        );
    }
}