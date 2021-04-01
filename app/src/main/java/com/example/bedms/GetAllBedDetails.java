/*
package com.example.bedms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GetAllBedDetails {
    String ward;
    FirebaseFirestore db =  FirebaseFirestore.getInstance();
    String bedIdString;
    String wardIdString;
    ArrayList <BedInfo> listOfBeds = new ArrayList<>();;
    BedInfo bif;


    public void getAllBedDetails() {
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
                                listOfBeds.add(bifIn);
                                System.out.println("THIS IS BIF from inside bed details" + bifIn.getBedId() + " " + bifIn.getWard());
                            }
                            BedStatusChartsForDate.Callback.class


                        }
                    }
                });
    }
}

 */