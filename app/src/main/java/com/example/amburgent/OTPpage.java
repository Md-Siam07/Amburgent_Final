package com.example.amburgent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.goodiebag.pinview.Pinview.PinViewEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTPpage extends AppCompatActivity {

    Pinview pin;
    String phoneNumber,otpID,Otp,name;
    FirebaseAuth mAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        pin = findViewById(R.id.pinview);
        pin.setPinViewEventListener(new PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                Otp = pin.getValue().toString();
                // Toast.makeText(getApplicationContext(),otpID,Toast.LENGTH_LONG).show();
            }
        });
        Intent intent = getIntent();
        phoneNumber =   intent.getStringExtra("phoneNumber");
        name = intent.getStringExtra("name");
        mAuth   =   FirebaseAuth.getInstance();
        getOTP();



    }

    public void verify(View view) {
        if(Otp.isEmpty()){
            Toast.makeText(getApplicationContext(),"OTP FIELD IS BLANK",Toast.LENGTH_LONG).show();
        }
        else if(Otp.length()!=6){
            Toast.makeText(getApplicationContext(),"INVALID OTP",Toast.LENGTH_LONG).show();
        }
        else{
            PhoneAuthCredential credential;
            credential =  PhoneAuthProvider.getCredential(otpID, Otp);
            signInWithPhoneAuthCredential(credential);
        }
    }



    private void getOTP() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        otpID   =   s;
                        Toast.makeText(getApplicationContext(),"OTP"+otpID,Toast.LENGTH_LONG).show();
                        System.out.println(otpID);
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUI_phone(mAuth.getCurrentUser());
                            Intent intent   =   new Intent(getApplicationContext(),MainActivity2.class);
                            startActivity(intent);
                            finish();

                        } else {

                            Toast.makeText(getApplicationContext(),"OTP ENTERED IS NOT CORRECT",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void updateUI_phone(FirebaseUser user){

        Map<String,Object> mp = new HashMap<>();
        String photoUrl ="";
        String email= "";
        mp.put("Username",name);
        mp.put("Email",email);
        mp.put("Phone",phoneNumber);
        mp.put("PhotoUrl",photoUrl);
        mp.put("code","phone");

        db.collection("users").document(user.getUid().toString()).set(mp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_LONG).show();
                    }
                });
    }







}