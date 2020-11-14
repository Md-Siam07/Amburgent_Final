package com.example.amburgent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    EditText nameProf,phoneProf,emailProf;
    ImageView imgProf;
    String user_id,code;
    String changed_phone="",changed_email="",username,email,ph;

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private String imgUri;
    boolean nm=false,phn=true,em=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameProf = findViewById(R.id.username_prof);
        phoneProf = findViewById(R.id.phone_num_prof);
        emailProf = findViewById(R.id.email_prof);

        imgProf = findViewById(R.id.img_prof);




        if(mAuth.getCurrentUser()!=null){
            user_id=mAuth.getCurrentUser().getUid();
            DocumentReference docRef = db.collection("users").document(user_id);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    code = documentSnapshot.get("code").toString();
                    Toast.makeText(getApplicationContext(),code,Toast.LENGTH_LONG).show();
                    imgUri = documentSnapshot.get("PhotoUrl").toString();
                    if(imgUri.length()>0) Picasso.get().load(imgUri).into(imgProf);

                    nameProf.setText(documentSnapshot.get("Username").toString());
                    nameProf.setEnabled(false);
                    username = documentSnapshot.get("Username").toString();

                     ph = documentSnapshot.get("Phone").toString();


                    if(ph.length()>0){
                        phoneProf.setText(ph);

                    }


                     email = documentSnapshot.get("Email").toString();
                    if(email.length()>0){
                        emailProf.setText(email);

                    }

                    if(code.equals("google")){
                        emailProf.setEnabled(false);
                        emailProf.setClickable(true);

                        emailProf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(),"pressed",Toast.LENGTH_LONG).show();
                                openDialogcantedit();
                            }


                        });
                    }

                    else if(code.equals("phone")){
                        phoneProf.setEnabled(false);


                    }




                }
            });
        }

    }

    public void openDialogcantedit(){
        DialogcantEdit dia = new DialogcantEdit();
        dia.show(getSupportFragmentManager(),"dialog");


    }

    public void openDialogSuccess(){
        DialogSuccess dia = new DialogSuccess();
        dia.show(getSupportFragmentManager(),"dialog");


    }


    public void done(android.view.View view) {

        if(!code.equals("google")){

            changed_email = emailProf.getText().toString();
            emailProf.setText(changed_email);
        }
        else{
            changed_email = email;
        }


        if(!code.equals("phone")){
            changed_phone = phoneProf.getText().toString();
            phoneProf.setText(changed_phone);
        }
        else{
            changed_phone = ph;
        }
        Map<String,Object> mp = new HashMap<>();
        mp.put("Username",username);
        mp.put("Email",changed_email);
        mp.put("Phone",changed_phone);
        mp.put("PhotoUrl", imgUri);
        mp.put("code",code);

        db.collection("users").document(user_id).set(mp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_LONG).show();
                    }
                });


                Toast.makeText(getApplicationContext(),"pressed",Toast.LENGTH_LONG).show();
                openDialogSuccess();





    }
}