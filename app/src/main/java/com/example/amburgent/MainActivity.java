package com.example.amburgent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    private Handler handler;
    private Runnable runnable;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(getApplicationContext());

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        firebaseUser = firebaseAuth.getCurrentUser();
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if(firebaseUser!=null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                    finish();
                }
                else{

                    startActivity(new Intent(getApplicationContext(),SignUppage.class));
                    finish();
                }
            }
        };
        handler.postDelayed(runnable,700);
    }

}