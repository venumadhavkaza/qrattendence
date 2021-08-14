package com.example.qrattendence;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {
    Handler handler  = new Handler();
    Runnable runnable;
    int delay = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();


        handler.postDelayed(runnable =new Runnable()
        {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                Intent i = new Intent(splashscreen.this,MainActivity.class);
                startActivity(i);
                finish();
                handler.removeCallbacks(runnable);
            }
        },delay);

    }
}