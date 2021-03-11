package com.example.bedms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bedms.Admin.CreateNewPatient;
import com.example.bedms.Model.Bed;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InputDateForBedStatus extends AppCompatActivity {

    TextView edDate;
    DatePickerDialog.OnDateSetListener mDateSetListener1, mDateSetListner2;
    Button statusBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView totalbeds;
    int totalNumberBedsInWard;
    ArrayList<Integer> getTotals = new ArrayList<Integer>();
    Date tmpDate = null;


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
                DatePickerDialog dialog = new DatePickerDialog(
                        InputDateForBedStatus.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        mDateSetListener1,
                        year, month, day);
                dialog.show();
            }
        });

        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String dob = dayOfMonth + "/" + month + "/" + year;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yyyy");
                try {
                    tmpDate = simpleDateFormat.parse(dob);
                    SimpleDateFormat simpleDateFormatNew = new SimpleDateFormat("dd/MM/yyyy");
                    String finalDate = simpleDateFormatNew.format(tmpDate);
                    edDate.setText(finalDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //et_date1.setText(dob);

            }
        };

        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTotalsOfBeds(tmpDate);
            }
        });
    }

    public ArrayList<Integer> getTotalsOfBeds(final Date selectDate) {
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
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    System.out.println("Bed Id " + document .getId());
                                                    for (QueryDocumentSnapshot history : task.getResult()) {
                                                        System.out.println("Bed History " + history.getId());
                                                        System.out.println("Bed Status " + history.getString("eventType"));
                                                        System.out.println("Date of event " + history.getString("dateAndTime"));

                                                    }
                                                    //     if(history.getString("dateAndTime") < selectDate)
                                                    //      {
                                                    //          status = history.getString("eventType");
                                                    //   }

                                                }
                                            }
                                        });


                            }
                        }
                    }
                });


        return getTotals;
    }
}