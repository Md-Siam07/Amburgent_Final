package com.example.amburgent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity implements  PopupMenu.OnMenuItemClickListener {

    NavigationView nav;
    DrawerLayout drawer;
    Toolbar toolbar;
    ImageView imageView;
    String  imgUri="";
    String user_id="";
    String code;
    String name,email,phone;


    static final int gal = 1;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        drawer = findViewById(R.id.drawer_layout);
        nav = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);

        View headerView = nav.getHeaderView(0);
        imageView = headerView.findViewById(R.id.Iv);


        



        if(mAuth.getCurrentUser()!=null){
            user_id=mAuth.getCurrentUser().getUid();
            DocumentReference docRef = db.collection("users").document(user_id);
            
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    imgUri = documentSnapshot.get("PhotoUrl").toString();
                    name = documentSnapshot.get("Username").toString();
                    email = documentSnapshot.get("Email").toString();
                    phone = documentSnapshot.get("Phone").toString();
                    code =  documentSnapshot.get("code").toString();



                    if(imgUri.length()>0) Picasso.get().load(imgUri).into(imageView);

                }
            });
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity2.this, v);
                popup.setOnMenuItemClickListener(MainActivity2.this);
                popup.inflate(R.menu.popup);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.pfpupload_item){
                            CropImage.activity()
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .setAspectRatio(1,1) // соотношение сторон 1 : 1
                                    .start(MainActivity2.this); // will use CROP_IMAGE_ACTIVITY_REQUEST_CODE

                        }
                        return  true;
                    }
                });

                popup.show();

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


               if(item.getItemId() == R.id.prof_item) {
                   Toast.makeText(getApplicationContext(), " Profile selected", Toast.LENGTH_LONG).show();
                   Toast.makeText(getApplicationContext(), code, Toast.LENGTH_LONG).show();
                   Intent intent = new Intent(getApplicationContext(), Profile.class);
                   startActivity(intent);

//                    case R.id.trans_item:
//                        Toast.makeText(getApplicationContext(), " Transaction History selected", Toast.LENGTH_LONG).show();
//                    case R.id.rides_item:
//                        Toast.zmakeText(getApplicationContext(), " Rides selected", Toast.LENGTH_LONG).show();

                }
                return true;
            }
        });

        //FirebaseApp.initializeApp(getApplicationContext());


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.logout){
            progressDialog = new ProgressDialog(MainActivity2.this);
            progressDialog.setMessage("Logging out....");
            progressDialog.show();
            handler=new Handler();
            runnable=new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), SignUppage.class);
                    startActivity(intent);
                    finish();
                }
            };
            handler.postDelayed(runnable,700);
            
            
            
            
            


            
//            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                public boolean onMenuItemClick(MenuItem item) {
//                    if(item.getItemId() == R.id.logout_menu){
//                        FirebaseAuth.getInstance().signOut();
//                        Intent intent = new Intent(getApplicationContext(), SignUppage.class);
//                        startActivity(intent);
//                         finish();
//                    }
//                    return  true;
//                }
//            });


        }
        return  true;

    }

    private void UpdateFirestore(String img) {
        if (mAuth.getCurrentUser() != null) {
            Map<String, Object> mp = new HashMap<>();
            user_id = mAuth.getCurrentUser().getUid();
            DocumentReference docRef = db.collection("users").document(user_id);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    name = documentSnapshot.get("Username").toString();
                    email = documentSnapshot.get("Email").toString();
                    phone = documentSnapshot.get("Phone").toString();
                    code = documentSnapshot.get("code").toString();

                    mp.put("Username", name);
                    mp.put("Email", email);
                    mp.put("Phone", phone);
                    mp.put("code", code);
                    mp.put("PhotoUrl",img);

                    db.collection("users").document(user_id).set(mp);
                    //Toast.makeText(getApplicationContext(), "main" + img +"\n"+ code, Toast.LENGTH_LONG).show();
                    if(imgUri.length()>0) Picasso.get().load(img).into(imageView);

                }
            });
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){

            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imgUri = resultUri.toString();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        //Toast.makeText(getApplicationContext(), "main1" + imgUri +"\n"+ code, Toast.LENGTH_LONG).show();


//
//            db.collection("users").document(user_id).set(mp)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_LONG).show();
//                        }
//                    });

            UpdateFirestore( imgUri);
        }


//    public void log_out(View view) {
//
//
////        LoginManager login_manager= LoginManager.getInstance();
////        login_manager.logOut();
//        FirebaseAuth.getInstance().signOut();
//        Intent intent = new Intent(getApplicationContext(), SignUppage.class);
//        startActivity(intent);
//        finish();
//
//    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}