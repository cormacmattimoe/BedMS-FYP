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

import com.example.bedms.OccupancyPerMonth;
import com.example.bedms.R;
import com.example.bedms.StatsShown;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HospitalManagerHub extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ImageButton imageButtonAnimals, imageButtonVets, imageButtonWeather, imageButtonMap, imageButtonToDo, imageButtonEmissions, imageButtonInvoiceExpense, buttonSideMenu;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_splash);
        setTitle("Manager Hub");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void clickHospitalManager(View view) {
        imageButtonAnimals = findViewById(R.id.hosManHub);
        startActivity(new Intent(HospitalManagerHub.this, StatsAsOfToday.class));

    }

    public void clickDate(View view) {
        imageButtonVets = findViewById(R.id.imageButtonVets);
        startActivity(new Intent(HospitalManagerHub.this, BedStatusForDate.class));

    }

    public void clickStats(View view) {
        imageButtonWeather = findViewById(R.id.imageButtonWeather);
        startActivity(new Intent(HospitalManagerHub.this, StatsShown.class));
    }

    public void clickRate(View view) {
        imageButtonToDo = findViewById(R.id.imageButtonToDoList);
        startActivity(new Intent(HospitalManagerHub.this, OccupancyPerMonth.class));

    }

}

  //      auth = FirebaseAuth.getInstance();
  //      user = auth.getCurrentUser();
   //     buttonSideMenu = findViewById(R.id.side_menu);
  /*     buttonSideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer(toolbar);


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (auth.getCurrentUser() == null) {
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

   */
/*
    private void drawer(Toolbar toolbar) {
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
       // getUserName(navigationView);
    }

/*
    private void getUserName(NavigationView navigationView) {
        View headerLayout = navigationView.getHeaderView(0);
       // TextView username = headerLayout.findViewById(R.id.farmersEmail);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            username.setText(currentUser.getEmail());
        }
    }




    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}

 */
