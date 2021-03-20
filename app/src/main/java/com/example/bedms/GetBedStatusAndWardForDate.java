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

    public int[] GetBedStatusAndWardForDate(final String dateSelected, final String bedID) {

        final int[] bedStatus = new int[6];
        bedStatus[0] = 0;  //open
        bedStatus[1] = 0;  //allocated
        bedStatus[2] = 0;  //occupied
        bedStatus[3] = 0;  //cleaning
        bedStatus[4] = 0;  //no history for that date - bed not yet created.
        bedStatus[5] = 0;  // Ward of the bed - see meaning below.

        // this module will take in the BedID and the dateSelected.  it will use the BedID to retrieve bedHistory.
        // then for each history record - Check date is before dateSelected. If yes, store event-type. The last event stored will be the bed status as at date/
        // return bedStatus array [] which has following meaning :
        // open =                   [1,0,0,0,0,w]
        // allocated =              [0,1,0,0,0,w]
        // occupied =               [0,0,1,0,0,w]
        // cleaning =               [0,1,0,0,0,w]
        // notcreatedyet =          [0,0,0,0,1,w]       This will be set if event = "".
        // Ward name =              [x,x,x,x,x,w]       Position[5] where w =
        //                                                             1 = "St Johns":
        //                                                             2 = "St Marys":
        //                                                             3 = "St Pauls":
        //                                                             4 = "St Magz":
        //                                                             5 = "St Joes":
        //                                                             6 = other Ward (default)

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
                            switch (event) {
                                case "Added bed":
                                case "Bed allocated to ward":
                                case "Bed is now open":
                                case "Bed is cleaned – ready for next patient":
                                    bedStatus[0] = 1;
                                    break;
                                case "Bed allocated - patient on way":
                                    bedStatus[1] = 1;
                                    break;
                                case "Patient in bed in ward":
                                    bedStatus[2] = 1;
                                    break;
                                case "Bed ready for cleaning":
                                    bedStatus[3] = 1;
                                    break;
                                default:
                                    bedStatus[4] = 1;
                                    System.out.println("This bed is the bed not created yet" + bedID);
                                    break;
                            }   // end Switch for bedStatus

                            // now call routine to get the ward for bed using BedID
                            // and set variable in bedStatus for this bad

                            GetWardForBed gwb = new GetWardForBed();
                            String ward = gwb.GetWardForBed(bedID);
                            // depending on ward, set bedStatus[5] as follows:
                            //                  1 = "St Johns":
                            //                  2 = "St Marys":
                            //                  3 = "St Pauls":
                            //                  4 = "St Magz":
                            //                  5 = "St Joes":
                            //
                            //                  6 = other Ward (default)
                            switch (ward) {
                                case "St Johns":
                                    bedStatus [5] = 1;
                                    break;
                                case "St Marys":
                                    bedStatus [5] = 2;
                                    break;
                                case "St Pauls":
                                    bedStatus [5] = 3;
                                    break;
                                case "St Magz":
                                    bedStatus [5] = 4;
                                    break;
                                case "St Joes":
                                    bedStatus [5] = 5;
                                    break;
                                default:
                                    bedStatus [5] = 6;
                            }  // end Switch for Ward
                        }


                        }
                });

        return bedStatus;
    }
}