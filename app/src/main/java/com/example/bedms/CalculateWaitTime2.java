package com.example.bedms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CalculateWaitTime2 extends AppCompatActivity {

    Button btnOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_caltime);
        btnOk = findViewById(R.id.btnOk);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateWaitTime cwt = new CalculateWaitTime();
                cwt.calculateTime("Bed ready for cleaning");
            }
        });

    }
}