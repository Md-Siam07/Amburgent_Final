package com.example.amburgent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity2 extends AppCompatActivity {

    NavigationView nav;
    DrawerLayout drawer;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        drawer = findViewById(R.id.drawer_layout);
        nav = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar
                , R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.prof_item:
                        Toast.makeText(getApplicationContext(), " Profile selected", Toast.LENGTH_LONG).show();
                   case R.id.trans_item:
                       Toast.makeText(getApplicationContext(), " Transaction History selected", Toast.LENGTH_LONG).show();
                    case R.id.rides_item:
                        Toast.makeText(getApplicationContext(), " Rides selected", Toast.LENGTH_LONG).show();

            }
                return true;
            }

        });
        Fragment mapsFragment = new GoogleMapFragment();
        initialize_all();
        //FirebaseApp.initializeApp(getApplicationContext());
       

    }

    public void initialize_all()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GoogleMapFragment obj = new GoogleMapFragment();
        fragmentTransaction.replace(R.id.frame_layout_replace,obj);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void log_out(View view) {

        FirebaseAuth.getInstance().signOut();

//        LoginManager login_manager= LoginManager.getInstance();
//        login_manager.logOut();

        Intent intent = new Intent(getApplicationContext(),SignUppage.class);
        startActivity(intent);
    }
}