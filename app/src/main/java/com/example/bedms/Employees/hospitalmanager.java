package com.example.bedms.Employees;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class hospitalmanager extends AppCompatActivity {
    BottomNavigationView bottomnav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalmanager);
        bottomnav = findViewById(R.id.viewNav);
        //bottomnav.setOnNavigationItemSelectedListener(navListener);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.addEmp:
                        Intent a = new Intent(hospitalmanager.this, manageemployees.class);
                        startActivity(a);
                        break;
                    case R.id.createPatient:
                        Intent b = new Intent(hospitalmanager.this, AdminHub.class);
                        startActivity(b);
                        break;
                    case R.id.viewEmployees:
                        Intent c = new Intent(hospitalmanager.this, Inventoryofemployees.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });
    }
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.hospital_menu, menu);
            return true;
        }
        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            int id = item.getItemId();
            switch (id) {
                case R.id.item1:
                    Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(hospitalmanager.this, hospitalmanager.class);
                    startActivity(i);
                    return true;

                case R.id.item2:
                    Toast.makeText(getApplicationContext(), "Create Patient" , Toast.LENGTH_LONG).show();
                    i = new Intent(hospitalmanager.this, AdminHub.class);
                    startActivity(i);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }