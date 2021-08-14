package com.example.qrattendence;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MaininfoActivity4 extends AppCompatActivity {
    Boolean student;
    TextView unid,scl,ongclass;
    Button b2;
    ImageView im1;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    int backpress=0;
    ImageButton logout ;
    String emailnoo="";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_READPERMISSION_CODE = 102;
    private static final int STORAGE_PERMISSION_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininfo4);
        checkPermisson(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        checkPermisson(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_READPERMISSION_CODE);
        checkPermisson(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);



        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        unid = (TextView)findViewById(R.id.textView6);
        scl = (TextView) findViewById(R.id.textView8);
        ongclass = (TextView)findViewById(R.id.textView10);
        b2 = (Button)findViewById(R.id.button3);
        im1 = (ImageView)findViewById(R.id.imageView3);
        logout = (ImageButton)findViewById(R.id.imageButton2) ;
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MaininfoActivity4.this, emailnoo+" is logged out successfully", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        String regno;
        emailnoo = firebaseUser.getEmail();

        if(emailnoo.matches(".*[0-9].*")){
            student = true;
            regno = emailnoo.substring(emailnoo.indexOf(".")+1,emailnoo.indexOf("@"));
            b2.setText("Show QR");
        }
        else{
            student = false;
            b2.setText("Scan QR");
            regno = emailnoo.substring(0,emailnoo.indexOf("."));
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        String dd = dayOfTheWeek.substring(0,3).toUpperCase();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());

        int hrs = calendar.get(Calendar.HOUR_OF_DAY);
        String hrss;
        if(hrs<=9){
            hrss = "0"+hrs*100;
        }else{
            hrss = String.valueOf(hrs*100);
        }


        unid.setText(regno.toUpperCase());
        if(regno.matches("prof")){
            unid.setText("Mr. Hari Kishan");
        }
        scl.setText("School of Computer Science and Engineering");
        ongclass.setText("No Class at the Moment");
        String checkval =regno+dd+hrss;


            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference mDatabase = database.getReference("attendence");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String courseslot = snapshot.child(checkval).getValue(String.class);
                    if(courseslot==null){

                    }else {
                        ongclass.setText(courseslot);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MaininfoActivity4.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();

            }
        });

        DatabaseReference getImage = database.getReference("images");

        // Adding listener for a single change
        // in the data at this location.
        // this listener will triggered once
        // with the value of the data at the location
        getImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // getting a DataSnapshot for the location at the specified
                // relative path and getting in the link variable
                String link = dataSnapshot.child(regno).getValue(String.class);

                // loading that data into rImage
                // variable which is ImageView
                Picasso.get().load(link).into(im1);
                Toast.makeText(MaininfoActivity4.this, "User details updated", Toast.LENGTH_SHORT).show();

            }

            // this will called when any problem
            // occurs in getting data
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // we are showing that error message in toast
                Toast.makeText(MaininfoActivity4.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
            }
        });

        if(ongclass.getText().toString().matches("No Class at the Moment")){
            b2.setVisibility(View.VISIBLE);
        }
        else{
            b2.setVisibility(View.VISIBLE);
        }

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(student){
                    String finalgenkey = regno+":"+emailnoo+"#"+ongclass.getText().toString()+"$";
                    Intent i2 = new Intent(MaininfoActivity4.this, MainActivity3.class);
                    i2.putExtra("genkey", finalgenkey);
                    startActivity(i2);

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }else{
                    Intent i2 = new Intent (MaininfoActivity4.this,MainActivity2.class);
                    i2.putExtra("slot",ongclass.getText().toString()+" "+dd+String.valueOf(d.getDate())+month_name);
                    Toast.makeText(MaininfoActivity4.this,ongclass.getText().toString()+" "+dd+String.valueOf(d.getDate())+month_name   , Toast.LENGTH_SHORT).show();
                    startActivity(i2);

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });

    }
    public void onBackPressed(){

        if (backpress>1) {
            finish();
        }else{
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }

    }

    public void checkPermisson(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(MaininfoActivity4.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MaininfoActivity4.this, new String[] { permission }, requestCode);
        }
        else {
            //Toast.makeText(MaininfoActivity4.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

}