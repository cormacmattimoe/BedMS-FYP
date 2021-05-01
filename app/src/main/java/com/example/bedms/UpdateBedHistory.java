package com.example.bedms;


import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.bedms.BedCache;
import com.example.bedms.Model.Bed;
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
    private static final String TAG = "bedHistory4" ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateBedHistory(String bedId, String eventType){
        Calendar calender;
        SimpleDateFormat simpledateformat;
        calender = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String eventDate = simpledateformat.format(calender.getTime());
        updateBedHistory(bedId,eventType,eventDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateBedHistory(String bedId, String eventType,String eventDate){

        BedHistoryEvent newEvent = new BedHistoryEvent();
        newEvent.setEventType(eventType);

        newEvent.setDateAndTime(eventDate);

        db.collection("bed")
                .document(bedId)
                .collection(TAG)
                .add(newEvent)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String newEventId = documentReference.getId();
                        //Get bed, Add event to Cache
                        Bed bed = BedCache.bedCache.get(bedId);
                        if (bed.getBedHistoryEventHashTable()!= null){
                            BedCache.bedCache.get(bedId).getBedHistoryEventHashTable().put(newEventId,newEvent);
                        }

                        Log.d(TAG, "Bed history updated for " + bedId + " EventID: " + newEventId);
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

