package com.example.bedms.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.CreateNewPatient;
import com.example.bedms.CleaningStaff.cleaningstaffhub;
import com.example.bedms.Employees.hospitalmanager;
import com.example.bedms.Porter.porterhub;
import com.example.bedms.R;

public class welcome extends AppCompatActivity {
    Button mAdmin, mHospitalManager,mTestdoctor,mPorter,mCleaning, mRegister, mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Welcome to BMS");
        setContentView(R.layout.welcomescreen);
        mAdmin = findViewById(R.id.admin);
        mPorter = findViewById(R.id.porter);
        mCleaning = findViewById(R.id.cleaningBtn);
        mHospitalManager = findViewById(R.id.hospital);
        mRegister = findViewById(R.id.reg);
        mLogin = findViewById(R.id.loginBtn);
        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateNewPatient.class));
            }
        });
        mHospitalManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), hospitalmanager.class));
            }
        });
        mPorter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), porterhub.class));
            }
        });
        mCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), cleaningstaffhub.class));
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });
    }
}