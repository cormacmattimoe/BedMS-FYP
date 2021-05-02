package com.example.bedms.HospitalManager;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bedms.Auth.Login;
import com.example.bedms.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HospitalManagerHub extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ImageButton imgStatsOfToday, imgButtonCharts, imgCalculate, imgOccupancy;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoshub);
        setTitle("Manager Hub");

        mAuth = FirebaseAuth.getInstance();

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        user.getEmail().toString();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void clickHospitalManager(View view) {
        imgStatsOfToday = findViewById(R.id.statsToday);
        startActivity(new Intent(HospitalManagerHub.this, StatsAsOfToday.class));

    }

    public void clickDate(View view) {
        imgButtonCharts = findViewById(R.id.imgBtnCalendar);
        startActivity(new Intent(HospitalManagerHub.this, BedStatusForDate.class));

    }

    public void clickStats(View view) {
        imgCalculate = findViewById(R.id.imgBtnStats);
        startActivity(new Intent(HospitalManagerHub.this, CalculateWaitTime.class));
    }

    public void clickRate(View view) {
        imgOccupancy = findViewById(R.id.imgBtnOccup);
        startActivity(new Intent(HospitalManagerHub.this, OccupancyPerMonth.class));

    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospital_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(HospitalManagerHub.this, HospitalManagerHub.class);
                startActivity(i);
                return true;

            case R.id.item2:

                if (user != null){
                    mAuth.signOut();
                    Toast.makeText(this, user.getEmail()+ " Logged out!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "You aren't Login Yet!", Toast.LENGTH_SHORT).show();
                }
                finish();
                Intent r = new Intent(HospitalManagerHub.this, Login.class);
                startActivity(r);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

