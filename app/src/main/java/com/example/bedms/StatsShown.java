package com.example.bedms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatsShown extends AppCompatActivity {

    TextView tvEvents, tvMins, tvShortest, tvLongest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_wait_time);
        tvEvents = findViewById(R.id.tvEvents);
        tvShortest = findViewById(R.id.tvShort);
        tvLongest = findViewById(R.id.tvLong);






        Intent intent = getIntent();
        tvEvents.getText().toString();
        String events;

        events = intent.getStringExtra("Events");
        System.out.println("These are date after intent " + events);

        tvEvents.setText(events);;


    }
}