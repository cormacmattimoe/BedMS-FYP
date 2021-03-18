package com.example.bedms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.bedms.Auth.login;
import com.example.bedms.CleaningStaff.bedDetailsclean;
import com.example.bedms.HospitalManager.hospitalmanagerhub;
import com.example.bedms.Model.Bed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BedStatusForDate extends AppCompatActivity {

    TextView edDate;
    DatePickerDialog.OnDateSetListener mDateSetListener1;
    Button statusBtn;
    String dateSelectedString;
    String titleDate;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputdate);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        edDate = (TextView) findViewById(R.id.edDatePicker);
        statusBtn = findViewById(R.id.btnStatus);

        setTitle("Bed Status for.. ");

        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR);
                int min = cal.get(Calendar.MINUTE);
                int seconds = cal.get(Calendar.SECOND);
                DatePickerDialog dialog = new DatePickerDialog(
                        BedStatusForDate.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        mDateSetListener1,
                        year, month, day);
                dialog.show();


            }
        });
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateSelectedString = (dayOfMonth + "-" + "0" + (month + 1) + "-" + year + " " + "00" + ":" + "00" + ":" + "00");
                titleDate = (dayOfMonth + "-" + "0" + (month + 1) + "-" + year);
                System.out.println("Date selected string " + dateSelectedString);
                edDate.setText(dateSelectedString);
            }
        };


        statusBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BedStatusChartsForDate.class);
                    intent.putExtra("Date", dateSelectedString);
                    intent.putExtra("titleDate", titleDate);
                    v.getContext().startActivity(intent);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospitalmanagerhubmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Intent i = new Intent(BedStatusForDate.this, hospitalmanagerhub.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Intent z = new Intent(BedStatusForDate.this, BedStatusForDate.class);
                startActivity(z);
                return true;
            case R.id.item3:
                Intent S = new Intent(BedStatusForDate.this, OccupancyPerMonth.class);
                startActivity(S);
                return true;
            case R.id.item4:
                Intent g = new Intent(BedStatusForDate.this, CalculateWaitTime.class);
                startActivity(g);
                return true;

            case R.id.item5:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(BedStatusForDate.this, login.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}






            /*
            bedCount++;

            totalNumberBedsInWard = bedCount;
            totalbeds.setText(Integer.toString(totalNumberBedsInWard));

          */


