package com.example.qrattendence;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button b1,b2;
    EditText ed1,ed2;
    FirebaseAuth mAuth;
    LottieAnimationView progressBarl;
    static String email,password;
    Button b3;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //ActionBar bar = getSupportActionBar();

        //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4267B2")));
        b1 = (Button)findViewById(R.id.button);
       // Utils.blackIconStatusBar(MainActivity.this,Color.parseColor("#4267B2"));
        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);
        //b3 = (Button)findViewById(R.id.button3);



        progressBarl = (LottieAnimationView)findViewById(R.id.progressbar1);
        progressBarl.pauseAnimation();
        progressBarl.setVisibility(View.INVISIBLE);
        b2 = (Button)findViewById(R.id.button2);
        b2.setText("Activate");
        b1.setText("Login");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(MainActivity.this, MaininfoActivity4.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Toast.makeText(this, "Welcome Back", Toast.LENGTH_SHORT).show();
            startActivity(i);

        } else {
            // User is signed out
            //Toast.makeText(this, "Login to Continue", Toast.LENGTH_SHORT).show();
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               email = ed1.getText().toString();
               password = ed2.getText().toString();
                if(TextUtils.isEmpty(email)||!isEmailValid(email)){
                    ed1.setError("Enter a valid email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    ed2.setError("Enter a valid password");
                    return;
                }
                if(password.length()<5){
                    ed2.setError("Enter password more than 5 characters");
                    return;
                }

                progressBarl.setVisibility(View.VISIBLE);
                progressBarl.playAnimation();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                progressBarl.pauseAnimation();
                                progressBarl.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(MainActivity.this, MaininfoActivity4.class));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);                                finish();
                            } else {
                                progressBarl.pauseAnimation();
                                progressBarl.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this, "INVALID DETAILS", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,MainsignupActivity5.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}