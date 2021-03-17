package com.example.bedms;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.Duration;


public class CalculateWaitTime {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Integer> getTotalsLocal = new ArrayList<Integer>();
    int totalNumberOfEvents = 0;
    int totalWaitMins = 0;
    String longestWaitID = "";
    String shortestWaitID = "";
    float averageWait = 0;
    int shortestTimeInMins = 5000;
    int longestTimeInMins = 0;
    int differenceInMins = 0;
    boolean timer = false;
    Date startTime;
    Date endTime;
    String eventDateString = "";

    public CalculateWaitTime() {

    }

//this will work for bed delays - cleaning for now.
// remove the getTotalsOfBeds below.....
// return - Total # of this type of event
    // - average wait
    // - longest wait, longest wait ID
    // shortest wait, shortest wait ID
    // display on a screen made up of 4 squares
    //when click on shortest or longest - then you see the Bed ID.

    public void calculateTime(final String event) {


        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("bed")
                                        .document(document.getId())
                                        .collection("bedHistory4")
                                        .orderBy("dateAndTime")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    System.out.println("Bed Id " + document.getId());


                                                    for (QueryDocumentSnapshot history : task.getResult()) {
                                                        //several fields are not visible in here - why?  Timer, shortestWaitID, longestWaitID.  This is the issue we have with GetTotals
                                                        //its because of the second loop being fronted by Public VOID onComplete!!!

                                                        System.out.println("This is the bed history id " + ": " + history.getId());
                                                        if (event == history.getString("eventType")) {
                                                            System.out.println("This is the event" + ": " + history.getString("eventType"));
                                                            timer = true;
                                                            totalNumberOfEvents = totalNumberOfEvents + 1;
                                                            eventDateString = history.getString("dateAndTime");
                                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                            try {
                                                                startTime = format.parse(eventDateString);
                                                                System.out.println("This is start time " + ": " + startTime);
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else if (timer) {
                                                            eventDateString = history.getString("dateAndTime");
                                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                            try {
                                                                endTime = format.parse(eventDateString);
                                                                System.out.println("This is end time " + ": " + endTime);
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                            long difference = endTime.getTime() - startTime.getTime();
                                                            differenceInMins = (int) difference;
                                                            totalWaitMins = totalWaitMins + differenceInMins;
                                                            timer = false;
                                                            if (differenceInMins < shortestTimeInMins) {
                                                                shortestTimeInMins = differenceInMins;
                                                                shortestWaitID = document.getId();
                                                            } else if (differenceInMins > longestTimeInMins) {
                                                                longestTimeInMins = differenceInMins;
                                                                longestWaitID = document.getId();
                                                            }
                                                        }
                                                    }
                                                    //check after for loop -  if timer still = true -then we have no next event so the patient/bed is still waiting.  We then current date to calculate differnece
                                                    if (timer) {
                                                        Calendar cal = Calendar.getInstance();
                                                        endTime = cal.getTime();
                                                        System.out.println("This is end time " + ": " + endTime);
                                                        long difference = endTime.getTime() - startTime.getTime();
                                                        differenceInMins = (int) difference;
                                                        ;
                                                        System.out.println("This is difference " + ": " + differenceInMins);
                                                        totalWaitMins = totalWaitMins + differenceInMins;
                                                        timer = false;
                                                        if (differenceInMins < shortestTimeInMins) {
                                                            shortestTimeInMins = differenceInMins;
                                                            shortestWaitID = document.getId();
                                                        } else if (differenceInMins > longestTimeInMins) {
                                                            longestTimeInMins = differenceInMins;
                                                            longestWaitID = document.getId();
                                                        }
                                                    }


                                                    System.out.println("Total Number of events = " + totalNumberOfEvents);
                                                    System.out.println("Total of shortest time  =" + shortestTimeInMins);
                                                    System.out.println("Total of shortest time ID = " + shortestWaitID);
                                                    System.out.println("Total of longest time = " + longestTimeInMins);
                                                    System.out.println("Total of longest time ID = " + longestWaitID);
                                                    System.out.println("Total of number of minutes = " + totalWaitMins);
                                                    System.out.println("Average wait =  " + 90);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }
}




