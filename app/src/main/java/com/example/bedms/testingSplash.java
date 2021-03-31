package com.example.bedms;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.bedms.HospitalManager.hospitalmanagerhub;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class testingSplash extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


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
        startActivity(new Intent(testingSplash.this, hospitalmanagerhub.class));

    }

    public void clickDate(View view) {
        imageButtonVets = findViewById(R.id.imageButtonVets);
        startActivity(new Intent(testingSplash.this, BedStatusForDate.class));

    }

    public void clickStats(View view) {
        imageButtonWeather = findViewById(R.id.imageButtonWeather);
        startActivity(new Intent(testingSplash.this, StatsShown.class));
    }

    public void clickRate(View view) {
        imageButtonToDo = findViewById(R.id.imageButtonToDoList);
        startActivity(new Intent(testingSplash.this, OccupancyPerMonth.class));

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

 */

/*
    public void clickAnimals(View view) {
        imageButtonAnimals = findViewById(R.id.imageButtonAnimals);
        Toast.makeText(MainActivity.this, "Animals Registered", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, AnimalActivity.class));

    }

    public void clickVets(View view) {
        imageButtonVets = findViewById(R.id.imageButtonVets);
        startActivity(new Intent(MainActivity.this, VetActivity.class));

    }

    public void clickWeather(View view) {
        imageButtonWeather = findViewById(R.id.imageButtonWeather);
        startActivity(new Intent(MainActivity.this, WeatherActivity.class));
    }

    public void clickToDoList(View view) {
        imageButtonToDo = findViewById(R.id.imageButtonToDoList);
        startActivity(new Intent(MainActivity.this, ToDoListActivity.class));

    }

    public void clickGoogleMap(View view) {
        imageButtonMap = findViewById(R.id.imageButtonGoogleMap);
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
    }

    public void clickEmissions(View view) {
        imageButtonEmissions = findViewById(R.id.imageButtonEmissions);
        startActivity(new Intent(MainActivity.this, EmissionsActivity.class));
    }

    public void clickInvoiceExpense(View view) {
        imageButtonInvoiceExpense = findViewById(R.id.imageButtonInvoiceExpense);
        startActivity(new Intent(MainActivity.this, InvoiceReceiptActivity.class));
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}

 */
