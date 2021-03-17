
package com.example.bedms;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.bedms.Model.BedHistory2Event;
import com.example.bedms.Model.BedHistoryEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateBedHistory3 {
    private static final String TAG = "bedHistory" ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateBedHistory3(String bedId, String eventType){
        BedHistory3Event newEvent = new BedHistory3Event();
        newEvent.setEventType(eventType);
    //    newEvent.setDateAndTime(eventType);

        db.collection("bed")
                .document(bedId)
                .collection("bedHistory3")
                .add(newEvent)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Bed history 3 added " + documentReference.getId());
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



