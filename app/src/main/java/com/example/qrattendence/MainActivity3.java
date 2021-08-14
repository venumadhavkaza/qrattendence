package com.example.qrattendence;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
public class MainActivity3 extends AppCompatActivity {
    private ImageView qrCodeIV;
    private Button generateQrBtn;
    Bitmap bitmap;
    int dimen;
    QRGEncoder qrgEncoder;
    Handler handler = new Handler();
    Runnable runnable;
    LottieAnimationView qr ;
    int delay = 5000;
    int backpress=0;
    String keygen="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        generateQrBtn = (Button)findViewById(R.id.done);
        generateQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        qr = (LottieAnimationView)findViewById(R.id.qranim);

    }
    @Override
    protected void onResume() {
        super.onResume();
        //checkFirebaseForAttenedence();


        createQr();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void onBackPressed(){


        if (backpress>1) {
            handler.removeCallbacks(runnable);

            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }else{
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    public void createQr(){
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                qr.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity3.this, "This method is run every 5 seconds",
                        Toast.LENGTH_SHORT).show();

                qrCodeIV = (ImageView)findViewById(R.id.qrCode);

                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                dimen = width < height ? width : height;
                dimen = dimen * 3 / 4;
                Intent i2 = getIntent();
                keygen = i2.getStringExtra("genkey");
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                String formattedDate = sdf.format(d);
                //Toast.makeText(MainActivity3.this, keygen+d, Toast.LENGTH_SHORT).show();
                qrgEncoder = new QRGEncoder(keygen+d, null, QRGContents.Type.TEXT, dimen);
                try {
                    // getting our qrcode in the form of bitmap.
                    bitmap = qrgEncoder.encodeAsBitmap();

                    // the bitmap is set inside our image
                    // view using .setimagebitmap method.
                    qrCodeIV.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    // this method is called for
                    // exception handling.
                    Log.e("Tag", e.toString());
                }

                Handler handler = new Handler();




            }
        }, delay);

    }

    public void checkFirebaseForAttendence(){

       // FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference mDatabase = database.getReference("updated");


    }



}