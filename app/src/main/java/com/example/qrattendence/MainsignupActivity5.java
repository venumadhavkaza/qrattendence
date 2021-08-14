package com.example.qrattendence;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainsignupActivity5 extends AppCompatActivity {
    EditText em,pa ;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    Button b1;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainsignup5);
        em = (EditText)findViewById(R.id.editText);
        pa = (EditText)findViewById(R.id.editText2);
        b1 = (Button)findViewById(R.id.button4);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = em.getText().toString();
                password = pa.getText().toString();
                if(TextUtils.isEmpty(email)||!isEmailValid(email)){
                    em.setError("Enter a valid email");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    pa.setError("Enter a valid password");
                    return;
                }
                if(password.length()<5){
                    pa.setError("Enter password more than 5 characters");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(MainsignupActivity5.this,"confirm the mail",Toast.LENGTH_SHORT).show();
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                    Toast.makeText(MainsignupActivity5.this,"Regsitered Successfully", Toast.LENGTH_SHORT).show();
                                    em.setText("");
                                    pa.setText("");
                               }

                            }
                        });
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainsignupActivity5.this,MainActivity.class));

                    }
                    else{
                        Toast.makeText(MainsignupActivity5.this,"VALIDATION UNSUCCESSFUL",Toast.LENGTH_SHORT).show();

                    }
                    }
                });

            }
        });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onPause(){
        super.onPause();

    }

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}