package com.example.bedms.HospitalManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Auth.login;
import com.example.bedms.BedInfo;
import com.example.bedms.OccupancyPerMonth;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class CalculateWaitTime extends AppCompatActivity {

     Button btnOk;
     FirebaseFirestore db = FirebaseFirestore.getInstance();
     ArrayList<Integer> getTotalsLocal = new ArrayList<Integer>();
     int totalNumberOfEvents = 0;
     TextView tvEvents, tvMins, tvShortest, tvLongest, tvlongId, tvShortId, tvAverage, tvShortTxt;
     int totalWaitMins = 0;
     String longestWaitID;
     String shortestWaitID;
     float averageWait = 0;
     float totalNumberOfEventsf;
     float totalWaitMinsf;
     int shortestTimeInMins = 50000;
     int longestTimeInMins = 0;
     int differenceInMins = 0;
     int avWaitHours = 0;
     boolean timer = false;
     //String event = "Bed ready for cleaning";
     String event = "Bed allocated - patient on way";
     Date startTime;
     int count = 0;
     int numberOfBeds = 0;
     Date endTime;
     String bedIdString, wardIdString;
     ArrayList<BedInfo> allBeds = new ArrayList<BedInfo>();
     String eventDateString = "";
     int[] arrayOfStats = new int[6];
     ArrayAdapter<CharSequence> adapter;
     Spinner statsWanted;
     String statsSelected;
     String hours = "(hrs)";
     String mins = "(min)";





        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_calculate_wait_time);
            tvEvents = findViewById(R.id.tvEvents);

            tvShortTxt = findViewById(R.id.tvShortTxt);
            tvLongest = findViewById(R.id.tvLong);
            tvShortest = findViewById(R.id.tvShort);
            tvlongId = findViewById(R.id.tvLongIdd);
            tvShortId = findViewById(R.id.tvShorttid);
            tvAverage = findViewById(R.id.tvAverage);

            statsWanted = findViewById(R.id.statsWanted);
            adapter = new ArrayAdapter<CharSequence>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    getResources().getStringArray(R.array.statsWanted));
            //Setting the ArrayAdapter data on the Spinner
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.statsWanted, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            statsWanted.setAdapter(adapter);
            statsSelected = "";
            getAllBeds();

                statsWanted.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        statsSelected = statsWanted.getSelectedItem().toString();
                        setTitle("Key Stats for...");
                        // set event == the event requested by user
                        switch (statsSelected){
                            case "Bed- Cleaning" :
                                event = "Bed ready for cleaning";
                                break;
                            case  "Patient - from time admitted to bed":
                                event = "Bed allocated - patient on way";
                                break;
                            default:
                                System.out.println("Stats dropdown not found" + statsSelected);
                        }
                        // calls getBedHistory which scrolls thru allBeds to gather history for each bed.
                        // getHistoryDetails then called for each bed.
                        // getBedHistory then calls calculateWaitTime
                        // calculateWaitTime returns results to screen
                        System.out.println("After button is pressed in OnClick ");
                        for (int b = 0; b < allBeds.size(); b++) {
                            System.out.println("OnClick: Bed ID = " + allBeds.get(b).getBedId());
                        }
                        totalNumberOfEvents = 0;
                        shortestTimeInMins = 50000;
                        longestTimeInMins = 0;
                        shortestWaitID = "";
                        longestWaitID = "";
                        avWaitHours = 0;
                        count = 0;
                        System.out.println("beginning - event = " + event);
                        getBedHistory();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }


    public void getAllBeds(){
    //called during onCreate to gahter all bed details

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
                                allBeds.add(bifIn);
                            }
                        }
                    }
                });
        System.out.println("After Get all bed routine ");
    }



    public void getHistoryDetails(String bedId) {
        // Do database query for each bed ID to get the bed history back

        db.collection("bed")
                .document(bedId)
                .collection("bedHistory4")
                .orderBy("dateAndTime")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> returnedHistory) {
                        if (returnedHistory.isSuccessful()) {
                            System.out.println("count: " + count);
                            System.out.println("BedId: " + bedId);
                            BedInfo currentBed = new BedInfo();
                            calculateTime(returnedHistory, bedId);
                            count++;

                            if (count == numberOfBeds) {
                                returnTotalsToScreen();
                            }
                        }
                    }
                });
    }

public void returnTotalsToScreen(){
    System.out.println("");
    totalNumberOfEventsf = (int) totalNumberOfEvents;
    totalWaitMinsf = (int) totalWaitMins;
    averageWait = (totalWaitMinsf / totalNumberOfEventsf);
    avWaitHours = (int) averageWait/60;

    tvEvents.setText(String.valueOf(totalNumberOfEvents));
    if(shortestTimeInMins < 60){
        tvShortest.setText(String.valueOf(shortestTimeInMins));
        tvShortTxt.setText(mins);
    }
    else{
        tvShortest.setText(String.valueOf(shortestTimeInMins/60));
        tvShortTxt.setText(hours);
    }
    tvLongest.setText(String.valueOf(longestTimeInMins/60));
    tvShortId.setText(String.valueOf(shortestWaitID));
    tvlongId.setText(String.valueOf(longestWaitID));
    tvAverage.setText(String.valueOf(avWaitHours));

    arrayOfStats[0] = totalNumberOfEvents;
    arrayOfStats[1] = totalWaitMins;
    arrayOfStats[2] = shortestTimeInMins;
    arrayOfStats[3] = longestTimeInMins;
    System.out.println("These are the array of stats " + Arrays.toString(arrayOfStats));


}

    public void getBedHistory(){
        // uses allBeds array and gets history for each bed.
        // getHistoryDetails then calls calculateWaitTime
        // calculateWaitTime returns results to screen

        numberOfBeds  = allBeds.size();
        for (int u = 0; u < allBeds.size(); u++) {
            bedIdString = allBeds.get(u).getBedId();
            wardIdString = allBeds.get(u).getWard();
            System.out.println("Before calculate This the bed id and ward id " + u + "  " + bedIdString + " " + wardIdString);
            getHistoryDetails(bedIdString);
        }
    }


    public void calculateTime(Task<QuerySnapshot> returnedHistory, String bedID ) {

        // this is called for each bed, with the history in the returnedHistory array
        // then we check for event, and record time between it and the next event (or now if there are no more events)
        // this routine updates the following global variables:
        //  - numberOfEvents, waitTime, and Longest and Shortest Time and Longest and Shortest ID.

        int statusCode;
        String eventDateString = "";


        for (QueryDocumentSnapshot history : returnedHistory.getResult()) {

                System.out.println("This is the bed history id " + ": " + history.getId());
                System.out.println("This is the event from screen " + ": " + event);
                System.out.println("This is the event from d/b " + ": " + history.getString("eventType"));

                if (event.equals(history.getString("eventType"))) {
                    timer = true;
                    totalNumberOfEvents = totalNumberOfEvents + 1;
                    System.out.println("found an event - " + history.getString("eventType") + "  and totanNumberofEvents = " + totalNumberOfEvents);

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
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
                    differenceInMins = (int) minutes;
                    System.out.println("This is the wait time in mins " + ": " + differenceInMins);
                    totalWaitMins = totalWaitMins + differenceInMins;
                    timer = false;
                    if (differenceInMins < shortestTimeInMins) {
                        System.out.println("For loop: changing shortest for Bed ID = " + bedID);
                        shortestTimeInMins = differenceInMins;
                        shortestWaitID = bedID;
                    } else if (differenceInMins > longestTimeInMins) {
                        System.out.println("For loop: changing longest for Bed ID = " + bedID);
                        longestTimeInMins = differenceInMins;
                        longestWaitID = bedID;
                    }
                }
            }

            //check after for-loop -  if timer = true -then we have no next event so the patient/bed is still waiting.  We then current date to calculate differnece

            if (timer) {
                Calendar cal = Calendar.getInstance();
                endTime = cal.getTime();
                System.out.println("This is end time " + ": " + endTime);
                long difference = endTime.getTime() - startTime.getTime();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
                differenceInMins = (int) minutes;
                System.out.println("This is difference " + ": " + differenceInMins);
                totalWaitMins = totalWaitMins + differenceInMins;
                System.out.println("This is the wait time in mins " + ": " + differenceInMins);
                timer = false;
                if (differenceInMins < shortestTimeInMins) {
                    System.out.println("If timer: changing shortest for Bed ID = " + bedID);
                    shortestTimeInMins = differenceInMins;
                    shortestWaitID = bedID;
                } else if (differenceInMins > longestTimeInMins) {
                    System.out.println("If Timer: changing longest for Bed ID = " + bedID);
                    longestTimeInMins = differenceInMins;
                    longestWaitID = bedID;
                }
            }

        System.out.println("At end of Calc Wait Time for Bed ID = " + bedID);
        System.out.println("Total Number of events so far = " + totalNumberOfEvents);
        System.out.println("Current shortest time  =" + shortestTimeInMins);
        System.out.println("Current shortest time (hrs)  =" + shortestTimeInMins/60);
        System.out.println("Current longest time = " + longestTimeInMins);
        System.out.println("Current longest time (hrs) = " + longestTimeInMins/60);
        System.out.println("Current longest time ID = " + longestWaitID);
        System.out.println("Current Total of number of minutes = " + totalWaitMins);

    }


     @Override
     public boolean onCreateOptionsMenu (Menu menu){
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.hospitalmanagerhubmenu, menu);
         return true;
     }
     @Override
     public boolean onOptionsItemSelected (MenuItem item){
         int id = item.getItemId();
         switch (id) {
             case R.id.item1:
                 Intent i = new Intent(CalculateWaitTime.this, StatsAsOfToday.class);
                 startActivity(i);
                 return true;
             case R.id.item2:
                 Intent z = new Intent(CalculateWaitTime.this, BedStatusForDate.class);
                 startActivity(z);
                 return true;
             case R.id.item3:
                 Intent S = new Intent(CalculateWaitTime.this, OccupancyPerMonth.class);
                 startActivity(S);
                 return true;
             case R.id.item4:
                 Intent g = new Intent(CalculateWaitTime.this, CalculateWaitTime.class);
                 startActivity(g);
                 return true;

             case R.id.item5:
                 FirebaseAuth.getInstance().signOut();
                 finish();
                 Intent r = new Intent(CalculateWaitTime.this, login.class);
                 startActivity(r);
             default:
                 return super.onOptionsItemSelected(item);
         }
     }
}


