package com.example.vishal.seizalert;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class PhoneInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_input);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},1);

        StringBuilder sb = new StringBuilder();
        sb.append("Current number: ");
        sb.append(mReadJsonData("newPhoneNumber"));

        TextView currentNum = findViewById(R.id.currentPhoneNum);
        currentNum.setText(sb);

        Button actButton = (Button) findViewById(R.id.chngActBtn);
        actButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText phNum = (EditText) findViewById(R.id.phoneNumText);
                String phoneNumber = phNum.getText().toString();
                String currentPhoneNumber = mReadJsonData("newPhoneNumber");
                if (phoneNumber.equals("") && !currentPhoneNumber.equals("")) {
                    startActivity(new Intent(PhoneInputActivity.this, MainActivity.class));
                    return;
                } else if (phoneNumber.equals("") && currentPhoneNumber.equals("")){
                    Toast.makeText(PhoneInputActivity.this, "Please enter a phone number!",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    mCreateAndSaveFile("newPhoneNumber", phoneNumber);
                    startActivity(new Intent(PhoneInputActivity.this, MainActivity.class));
                    return;
                }
            }
        });
    }

    public boolean fileExists(String params) {
        File f = new File(params);
        if(f.exists() && !f.isDirectory()) {
            return true;
        }

        return false;
    }

    public void mCreateAndSaveFile(String params, String mJsonResponse) {
        try {
            FileWriter file = new FileWriter("/data/data/" + getApplicationContext().getPackageName() + "/" + params);
            file.write(mJsonResponse);
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String mReadJsonData(String params) {
        String mResponse = "";
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
