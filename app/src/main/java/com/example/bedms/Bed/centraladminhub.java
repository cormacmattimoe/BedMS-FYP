package com.example.bedms.Bed;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.Employees.manageemployees;
import com.example.bedms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class centraladminhub extends AppCompatActivity {
    Button manageBtn, inventoryBtn, addEmpBtn, allocateBedBtn, admitPatientBtn;
    BottomNavigationView bottomnav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centraladminhub);
        addEmpBtn = findViewById(R.id.addEmp);
        admitPatientBtn = findViewById(R.id.admitPatient);
        bottomnav = findViewById(R.id.viewNav);
        //bottomnav.setOnNavigationItemSelectedListener(navListener);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.manageBed:
                        Intent a = new Intent(centraladminhub.this, managebeds.class);
                        startActivity(a);
                        break;
                    case R.id.allocateBed:
                        Intent b = new Intent(centraladminhub.this, AllocateBedToWard.class);
                        startActivity(b);
                        break;
                    case R.id.viewBeds:
                        Intent c = new Intent(centraladminhub.this, managebeds.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });
        addEmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), manageemployees.class));
            }
        });

        admitPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminHub.class));
            }
        });
    }

}




