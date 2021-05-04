package com.example.bedms;

import androidx.annotation.NonNull;

import com.example.bedms.Model.Bed;
import com.example.bedms.Model.BedHistoryEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Hashtable;

public class BedCache {
    public static Hashtable<String,Bed> bedCache = new Hashtable<>();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void SetupCache() {
        loadBeds();
    }

    public static Hashtable<String,Bed> getBedCache(){
        return bedCache;
    }

    public static void ReloadCache(){
        bedCache = new Hashtable<>();
        loadBeds();
    }


    private static void loadBeds(){
        //Load in all beds
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> loadAllBeds) {
                        if (loadAllBeds.isSuccessful()) {
                            for (QueryDocumentSnapshot bedResult : loadAllBeds.getResult()) {
                                Bed bed = new Bed();
                                bed.setBedId(bedResult.getId());
                                bed.setBedName(bedResult.getString("BedName"));
                                bed.setBedType(bedResult.getString("BedType"));
                                bed.setPatientID(bedResult.getString("PatientID"));
                                bed.setBedStatus(bedResult.getString("Status"));
                                bed.setBedWard(bedResult.getString("Ward"));


                                Hashtable<String,BedHistoryEvent> bedHistoryEventHashTable = new Hashtable<>();
                                //populate bed history
                              db.collection("bed")
                                .document(bed.getBedId())
                                .collection("bedHistory4")
                                .orderBy("dateAndTime")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> loadBedHistoryForBed) {
                                        if (loadBedHistoryForBed.isSuccessful()) {
                                            for (QueryDocumentSnapshot bedHistoryEventResult : loadBedHistoryForBed.getResult()) {

                                                BedHistoryEvent bedHistoryEvent = new BedHistoryEvent();
                                                //Build bed history events
                                                bedHistoryEvent.setDateAndTime(bedHistoryEventResult.getString("dateAndTime"));
                                                bedHistoryEvent.setEventType(bedHistoryEventResult.getString("eventType"));
                                                bedHistoryEventHashTable.put(bedHistoryEventResult.getId(),bedHistoryEvent);
                                            }
                                        }
                                    }
                                });

                                bed.setBedHistoryEventHashTable(bedHistoryEventHashTable);
                                bedCache.put(bed.getBedId(),bed);
                            }
                        }
                    }
                });
    }
}
