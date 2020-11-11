package com.example.amburgent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity implements  PopupMenu.OnMenuItemClickListener {

    NavigationView nav;
    DrawerLayout drawer;
    Toolbar toolbar;
    ImageView imageView;
    String  imgUri;
    String user_id="";
    String code;

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        drawer = findViewById(R.id.drawer_layout);
        nav = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);
        View headerView = nav.getHeaderView(0);
        imageView = headerView.findViewById(R.id.Iv);

        Intent intent = getIntent();
        code = intent.getStringExtra("code");

        if(mAuth.getCurrentUser()!=null){
            user_id=mAuth.getCurrentUser().getUid();
            DocumentReference docRef = db.collection("users").document(user_id);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    imgUri = documentSnapshot.get("PhotoUrl").toString();
                    if(imgUri.length()>0) Picasso.get().load(imgUri).into(imageView);

                }
            });
        }



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
//                        Toast.makeText(getApplicationContext(), " Rides selected", Toast.LENGTH_LONG).show();

                }
                return true;
            }
        });

        //FirebaseApp.initializeApp(getApplicationContext());


    }

    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup);
        popup.show();

    }


    public void log_out(View view) {


//        LoginManager login_manager= LoginManager.getInstance();
//        login_manager.logOut();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), SignUppage.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pfpshow_item:
                Toast.makeText(getApplicationContext(), " Profile selected", Toast.LENGTH_LONG).show();
            case R.id.pfpupload_item:
                Toast.makeText(getApplicationContext(), " Transaction History selected", Toast.LENGTH_LONG).show();


        }
        return true;
    }
}