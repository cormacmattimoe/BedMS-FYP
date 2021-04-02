package com.example.bedms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bedms.Auth.login;
import com.example.bedms.CleaningStaff.bedDetailsclean;
import com.example.bedms.HospitalManager.hospitalmanagerhub;
import com.example.bedms.Model.Bed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
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
    ArrayList<BedInfo> allBedsWithoutStatus = new ArrayList<BedInfo>();
    String bedIdString;
    String wardIdString;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference bedsRef = db.collection("bed");
    CalendarView calV;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputdate);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        calV = findViewById(R.id.calendarView);

        setTitle("Bed Status for.. ");

        getAllBedDetails(new Callbacka() {
            @Override
            public void calla() {
                System.out.println("This is the size after call back  " + allBedsWithoutStatus.size());
                for (int i = 0; i < allBedsWithoutStatus.size(); i++) {
                    bedIdString = "";
                    wardIdString = "";
                }
            }
        });
        calV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateSelectedString = (dayOfMonth + "-" + "0" + (month + 1) + "-" + year + " " + "00" + ":" + "00" + ":" + "00");
                titleDate = (dayOfMonth + "-" + "0" + (month + 1) + "-" + year);

                if (dateSelectedString == null) {
                    Toast.makeText(getApplicationContext(), "Please choose a date to continue", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(view.getContext(), BedStatusChartsForDate.class);
                    intent.putExtra("All Beds",allBedsWithoutStatus);
                    intent.putExtra("Date", dateSelectedString);
                    intent.putExtra("titleDate", titleDate);
                    view.getContext().startActivity(intent);
                }

            }
        });
    }

    public void getAllBedDetails(Callbacka callback){
        System.out.println("Get all bed details ");
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task2.getResult()) {
                                final BedInfo bifIn = new BedInfo();
                                bedIdString = document.getId();
                                wardIdString = document.getString("Ward");
                                bifIn.setBedId(bedIdString);
                                bifIn.setWard(wardIdString);
                                allBedsWithoutStatus.add(bifIn);
                            }
                               callback.calla();
                        }
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
    // Callback function
    public interface Callbacka {
        void calla();
    }
}







            /*
            bedCount++;

            totalNumberBedsInWard = bedCount;
            totalbeds.setText(Integer.toString(totalNumberBedsInWard));

          */
