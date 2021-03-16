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
import com.example.bedms.Model.BedHistoryEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class testingCalendar extends AppCompatActivity {

    TextView edDate;
    DatePickerDialog.OnDateSetListener mDateSetListener1, mDateSetListner2;
    Button statusBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView totalbeds;
    int totalNumberBedsInWard;
    ArrayList<Integer> getTotals = new ArrayList<Integer>();
    int bedCount = 0;
    int open = 0;
    int occcupied = 0;
    int allocated = 0;
    int waitingCleaning = 0;
    int other = 0;
    Date testingDate = Calendar.getInstance().getTime();
    Date eventDate;
    String testEventDate;
    Calendar dateSelectCal, dateNow;
    LocalDateTime dateSelectedLdt;
    String dateSelectedString;
    String event;
    Date date1;
    Date date2;
    LocalDateTime answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testingcalendar);

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
                        testingCalendar.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        mDateSetListener1,
                        year, month, day);
                dialog.show();


            }
        });
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) { ;

            String date = "21-01-2021 17:00:01";
            StringToLDTFormat dateConverted = new StringToLDTFormat();
                try {
                   answer  =   dateConverted.LDTDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("Final Converted " + answer);


            /*
                dateSelectCal = Calendar.getInstance();
                dateNow = Calendar.getInstance();

                dateSelectCal.set(year, month, dayOfMonth);
                System.out.println("This is date selected cal" + ": "+ dateSelectCal);
                dateNow.set(year, month, dayOfMonth);
                System.out.println("This is date selected date now" + ": "+ dateNow);

                Instant instant = dateSelectCal.toInstant();
                ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
                LocalDateTime selectedLocalDate = zone.toLocalDateTime();
                LocalDateTime timeNow = LocalDateTime.now();
                System.out.println("This is date selected local date " + ": "+ selectedLocalDate);

                System.out.println("Time it is now" + ": "+timeNow);

                Duration duration = Duration.between(selectedLocalDate, timeNow);// essentially "duration (mod 1 day)"
                Period period = Period.between(selectedLocalDate.toLocalDate(), timeNow.toLocalDate());
                System.out.println("The period in between with years" + period.getYears());
                System.out.println("The period in between with months " + period.getMonths());
                System.out.println("The period in between with days " + period.getDays());
                System.out.println("The duration in between with seconds " + duration.getSeconds());
                System.out.println("The duration in between with minutes " + (duration.getSeconds() / 60) );
                System.out.println("The duration in between with hours " + (duration.getSeconds() / 60 / 60) );

                /*
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;// "1986-04-08 12:30"
                String formattedDateTime = dateSelectedLdt.format(formatter); // "1986-04-08 12:30"
                System.out.println("Date selected in ldt   " + dateSelectedLdt);
                System.out.println("Date selected in cal " + dateSelectCal.getTime());
                // dateSelectedDate = dateFormat.parse(dateSelectedString);

                 */
                edDate.setText(dateSelectedString);
                ;

                System.out.println("Input date ");
            }

        };


        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for loop for incrementating the date each time
                System.out.println("testing");
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

}
