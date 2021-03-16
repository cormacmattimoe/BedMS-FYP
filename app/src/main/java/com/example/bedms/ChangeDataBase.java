/*
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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChangeDataBase extends AppCompatActivity {

    TextView edDate;
    DatePickerDialog.OnDateSetListener mDateSetListener1, mDateSetListner2;
    Button goBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView totalbeds;
    int totalNumberBedsInWard;
    ArrayList<Integer> getTotals = new ArrayList<Integer>();
    int bedCount;
    int open = 0;
    int occcupied = 0;
    int allocated = 0;
    int waitingCleaning = 0;
    int other = 0;
    Date testingDate = Calendar.getInstance().getTime();
    Date eventDate;
    String testEventDate;
    Date dateSelectedDate;
    String dateSelectedString;
    String event;
    Date date1;
    Date date2;
    Timestamp tsDate;
    Date tempDate;
    String OutputDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_date_base);


        goBtn = findViewById(R.id.btngo);


        goBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //for loop for incrementating the date each time

                convertHistory();
            }
        });
    }


    public int convertHistory() {

        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("bed")
                                        .document(document.getId())
                                        .collection("bedHistory")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    System.out.println("Bed Id " + document.getId());
                                                    for (QueryDocumentSnapshot history : task.getResult()) {
                                                        event = history.getString("eventType");
                                                        testEventDate = history.getString("dateAndTime");
                                                        // testEventDate = format (dd-mm-yyyy HH:mm :SS)
                                                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                        try {
                                                            tempDate = format.parse(testEventDate);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                        OutputDate = fm.format(tempDate);
                                                        System.out.println("This is date time from bed history  (old) " + ": " + testEventDate);
                                                        System.out.println("This is date time for bed history 4 (new) " + ": " + OutputDate);


                                                        UpdateBedHistory4 ubh = new UpdateBedHistory4();
                                                        ubh.updateBedHistory4(document.getId(), event, OutputDate);

                                                      //  System.out.println("This is the bed id " + document.getId());
                                                        System.out.println("This is bed history " + history.getId());
                                                        System.out.println("This is the event " + event);
                                                        System.out.println("This is the date" + OutputDate);


                                                    }
                                                    bedCount++;
                                                }
                                            }
                                        });
                            }
                            System.out.println("Total Number beds " + bedCount);
                        }
                    }
                });

        return bedCount;
    }
}

 */