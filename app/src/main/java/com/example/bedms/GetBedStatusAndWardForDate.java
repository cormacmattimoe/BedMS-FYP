package com.example.bedms;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class GetBedStatusAndWardForDate extends AppCompatActivity {


    public GetBedStatusAndWardForDate() {
    }

    public int[] GetBedStatusAndWard(final String dateSelected, final String bedID) {

        final int[] bedStatus = new int[]{0,0};

        /*
         //first element od bedStatus = Status  and is :  0 = open, [1]  = allocated, [2]   = occupied, [3]  = cleaning, [4] = no history for that date - bed not yet created.
         //Second element is the ward the bed is in
                         open =                   [0][w]
                         allocated =              [1][w]
                         occupied =               [2][w]
                         cleaning =               [3][w]
                         notcreatedyet =          [4][w]       This will be set if event = "".
                         Ward name =                 [w]       Element [2] where w =
                                                                     0 = "St Johns":
                                                                     1 = "St Marys":
                                                                     2 = "St Pauls":
                                                                     3 = "St Magz":
                                                                     4 = "St Joes":
                                                                     5 = other Ward (default)
         this module will take in the BedID and the dateSelected.  it will use the BedID to retrieve bedHistory.
         then for each history record - Check date is before dateSelected. If yes, store event-type. The last event stored will be the bed status as at date/
         return bedStatus array [] which has following meaning :

        */

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bed")
                .document(bedID)
                .collection("bedHistory4")
                .orderBy("dateAndTime")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String event = "";
                            String eventDateString = "";
                            Date eventDateFromDb = new Date();
                            Date dateSelectedFromScreen = new Date();
                            for (QueryDocumentSnapshot history : task.getResult()) {
                                eventDateString = history.getString("dateAndTime");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                try {
                                    eventDateFromDb = format.parse(eventDateString);
                                    dateSelectedFromScreen = dateFormatter.parse(dateSelected);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("This is date time returned " + ": " + eventDateFromDb);
                                System.out.println("This is date time string  " + ": " + dateSelectedFromScreen);

                                //Storing the eventType for transactions before or equal to the selectedDate
                                if (eventDateFromDb.compareTo(dateSelectedFromScreen) <= 0) {
                                    event = history.getString("eventType");
                                }
                            } // end For loop


                            // now use the event to set the status in the bedStatus array.
                            int statusCode = 0;
                            int wardCode = 0;
                            switch (event) {
                                case "Added bed":
                                case "Bed allocated to ward":
                                case "Bed is now open":
                                case "Bed is cleaned – ready for next patient":
                                    statusCode = 0;
                                    break;
                                case "Bed allocated - patient on way":
                                    statusCode = 1;
                                    break;
                                case "Patient in bed in ward":
                                    statusCode = 2;
                                    break;
                                case "Bed ready for cleaning":
                                    statusCode = 3;
                                    break;
                                default:
                                    statusCode = 4;
                                    System.out.println("This bed is the bed not created yet" + bedID);
                                    break;
                            }   // end Switch for bedStatus

                            // now call routine to get the ward for bed using BedID
                            // and set variable in bedStatus for this bed

                            GetWardForBed gwb = new GetWardForBed();
                            String ward = gwb.GetWard(bedID);
                            // depending on ward, set bedStatus[5] as follows:
                            //                  0 = "St Johns":
                            //                  1 = "St Marys":
                            //                  2 = "St Pauls":
                            //                  3 = "St Magz":
                            //                  4 = "St Joes":
                            //                  5 = other Ward (default)
                            switch (ward) {
                                case "St Johns":
                                    wardCode = 0;
                                    break;
                                case "St Marys":
                                    wardCode = 1;
                                    break;
                                case "St Pauls":
                                    wardCode = 2;
                                    break;
                                case "St Magz":
                                    wardCode = 3;
                                    break;
                                case "St Joes":
                                    wardCode = 4;
                                    break;
                                default:
                                    wardCode = 5;
                            }  // end Switch for Ward
                            bedStatus[0] = statusCode;
                            bedStatus[1] = wardCode;
                            }
                    }
                });
        return bedStatus;
    }
}