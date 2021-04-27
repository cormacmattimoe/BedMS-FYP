package com.example.bedms.HospitalManager;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bedms.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HospitalManagerHub extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ImageButton imgStatsOfToday, imgButtonCharts, imgCalculate, imgOccupancy;
    FirebaseAuth auth;
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

}

