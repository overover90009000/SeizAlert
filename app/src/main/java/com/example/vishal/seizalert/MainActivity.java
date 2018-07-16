package com.example.vishal.seizalert;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private SensorManager sm;
    private double aVal, aLast, shake;
    private SoundPool soundPool;
    private int sound1;
    private boolean played = false;
    boolean active = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        aVal = SensorManager.GRAVITY_EARTH;
        aLast = SensorManager.GRAVITY_EARTH;
        shake = 0.0d;
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            double x = sensorEvent.values[0];
            double y = sensorEvent.values[1];
            double z = sensorEvent.values[2];

            // aVal = aLast;
            aVal = (double) Math.sqrt((double)(x*x + y*y + z*z));
            double delta = aVal - aLast;
            shake = shake * 0.9d + delta;

            if (shake > 80 && !played && active) {
                System.out.println("shake: " + shake);
                played = true;
                active = false;
                startActivity(new Intent(MainActivity.this, WarningActivity.class));

                finish();

            } // else if (shake < 1) {
//                played = false;
//            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public String mReadJsonData(String params) {
        String mResponse = " ";
        try {
            File f = new File("/data/data/" + getPackageName() + "/" + params);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            mResponse = new String(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mResponse;
    }
}
