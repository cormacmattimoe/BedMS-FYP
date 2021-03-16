package com.example.bedms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bedms.Admin.CreateNewPatient;
import com.example.bedms.Model.Bed;
import com.example.bedms.Model.BedHistory2Event;
import com.example.bedms.Model.BedHistoryEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class InputDateForBedStatus extends AppCompatActivity {

    TextView edDate;
    DatePickerDialog.OnDateSetListener mDateSetListener1, mDateSetListner2;
    Button statusBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView totalbeds;
    int totalNumberBedsInWard;
    ArrayList<Integer> getTotals = new ArrayList<Integer>();
    int bedCount = 0;
    int open ;
    int occupied;
    int allocated;
    int waitingCleaning;
    int other;
    Date dateSelectedtesting;
    Date testingDate = Calendar.getInstance().getTime();
    Date eventDate;
    String testEventDate;
    Date dateSelectedDate;
    String dateSelectedString;
    String event;
    Date eventDateFromDb;
    Date dateSelectedFromScreen;
    LocalDateTime dateLTD;
    LocalDateTime LtdDate;
    LocalDateTime historyDateLtd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputdate);

        edDate = (TextView) findViewById(R.id.edDatePicker);
        statusBtn = findViewById(R.id.btnStatus);

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
                        InputDateForBedStatus.this,
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
                System.out.println("Date selected string " + dateSelectedString);
//                dateLTD = LocalDateTime.parse(dateSelectedString, dateFormatter);
                System.out.println("Input date " + dateLTD);

                edDate.setText(dateSelectedString);
            }
        };


        statusBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //for loop for incrementating the date each time
                try {
                    getTotalsOfBeds(dateSelectedString,testEventDate );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
            Table below shows connection between the status in the bed collection with the eventType in the bedHistory collection.

            Bed Status	            Bed History – EventType


            Open 	                Added Bed
            Open 	                Bed allocated to a ward

            Allocated 	            Bed allocated – patient on way
            Occupied	            Patient in bed in ward
            Waiting for cleaning    Bed ready for cleaning


            Open 	                Bed is cleaned – ready for next patient
            Open	                Bed is now open




     */


    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Integer> getTotalsOfBeds(final String stringInDate, String stringBeenSelected) throws ParseException {
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            bedCount = 0;
                            open = 0;
                            allocated = 0;
                            occupied = 0;
                            waitingCleaning = 0;
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("bed")
                                        .document(document.getId())
                                        .collection("bedHistory4")
                                        .orderBy("dateAndTime")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    System.out.println("Bed Id " + document.getId());
                                                    event = "";
                                                    for (QueryDocumentSnapshot history : task.getResult()) {

                                                        testEventDate = history.getString("dateAndTime");

                                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                        SimpleDateFormat dateFormatter = new  SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                        try {
                                                            eventDateFromDb = format.parse(testEventDate);
                                                            dateSelectedFromScreen = dateFormatter.parse(dateSelectedString);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        System.out.println("This is date time returned " + ": " + eventDateFromDb);
                                                        System.out.println("This is date time string  " + ": " + testEventDate);

                                                        //Storing the eventType for transactions before or equal to the selectedDate
                                                        if (eventDateFromDb.compareTo(dateSelectedFromScreen) <= 0) {
                                                            event = history.getString("eventType");
                                                        }


                                                    }

                                                    bedCount++;

                                                    switch (event) {

                                                        case "Added bed":
                                                        case "Bed allocated to ward":
                                                        case "Bed is now open":
                                                        case "Bed is cleaned – ready for next patient":
                                                            open = open + 1;
                                                            break;


                                                        case "Bed allocated - patient on way":
                                                            allocated = allocated + 1;
                                                            break;
                                                        case "Patient in bed in ward":
                                                            occupied = occupied + 1;
                                                            break;
                                                        case "Bed ready for cleaning":
                                                            waitingCleaning = waitingCleaning + 1;
                                                            break;
                                                        default:
                                                            other = other + 1;
                                                            break;
                                                    }
                                                }
                                                System.out.println("Total Number of beds " + bedCount);
                                                System.out.println("Total of open " + open);
                                                System.out.println("Total of allocated " + allocated);
                                                System.out.println("Total of occupied " + occupied);
                                                System.out.println("Total of cleaning " + waitingCleaning);
                                                System.out.println("Total of other beds  " + other);
                                                System.out.println("Total of all status' of beds =  " + (open + occupied + allocated + waitingCleaning + other));
                                            }


                                        });

                            }
                            getTotals.add(0, open);
                            getTotals.add(1, allocated);
                            getTotals.add(2, occupied);
                            getTotals.add(3, waitingCleaning);
                            getTotals.add(4, other);
                            System.out.println("These are the totals " + getTotals);
                        }
                    }
                });
        return getTotals;

    }
}






            /*
            bedCount++;

            totalNumberBedsInWard = bedCount;
            totalbeds.setText(Integer.toString(totalNumberBedsInWard));

          */


