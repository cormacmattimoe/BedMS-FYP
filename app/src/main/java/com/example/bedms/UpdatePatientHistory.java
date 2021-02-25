package com.example.bedms;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bedms.Model.PatientHistoryEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdatePatientHistory {
    private static final String TAG = "patientHistory" ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void updatePatientHistory(String patientId, String eventType){
        Calendar calender;
        SimpleDateFormat simpledateformat;
        String Date;
        PatientHistoryEvent newEvent = new PatientHistoryEvent();
        newEvent.setEventType(eventType);

        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpledateformat.format(calender.getTime());
        newEvent.setDateAndTime(Date);
        db.collection("patient")
                .document(patientId)
                .collection("patientHistory")
                .add(newEvent)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Patient history updated for " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
}

