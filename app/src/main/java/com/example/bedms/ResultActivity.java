package com.example.bedms;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    TextView result;
    //ArrayList <String> conditions = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        result = (TextView)findViewById(R.id.result);
        String d = getIntent().getStringExtra("link");


        MyTask mt = new MyTask(ResultActivity.this, result);

        mt.execute("https://api.infermedica.com/v2/conditions/c_926");

    }


}