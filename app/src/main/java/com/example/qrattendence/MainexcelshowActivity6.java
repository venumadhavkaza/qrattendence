package com.example.qrattendence;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class MainexcelshowActivity6 extends AppCompatActivity {
    Button b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainexcelshow6);
        b1 = (Button)findViewById(R.id.exc);
        b2 = (Button)findViewById(R.id.button6);
        Intent i5 = getIntent();
        String filepath = i5.getStringExtra("excelpath");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filepath);
                Toast.makeText(MainexcelshowActivity6.this, filepath, Toast.LENGTH_SHORT).show();
                //String davUrl = filepath;
            //String  path = Environment.getExternalStorageDirectory().getPath() ;
            Uri uri = FileProvider.getUriForFile(MainexcelshowActivity6.this,getPackageName()+".provider",file);
            Intent intent = new Intent();
            //intent.setType("/");
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //startActivity(intent);
                intent.setDataAndType(uri, "application/vnd.ms-excel");
                startActivity(intent);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}