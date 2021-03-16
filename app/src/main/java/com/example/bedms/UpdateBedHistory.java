package com.example.bedms;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.bedms.Model.BedHistoryEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UpdateBedHistory {
    private static final String TAG = "bedHistory" ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateBedHistory(String bedId, String eventType){
        Calendar calender;
        SimpleDateFormat simpledateformat;
        String eventDate;
        BedHistoryEvent newEvent = new BedHistoryEvent();
        newEvent.setEventType(eventType);


        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        eventDate = simpledateformat.format(calender.getTime());
        newEvent.setDateAndTime(eventDate);

        db.collection("bed")
                .document(bedId)
                .collection("bedHistory")
                .add(newEvent)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Bed history updated for " + documentReference.getId());
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

