package com.example.bedms;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Auth.login;
import com.example.bedms.HospitalManager.hospitalmanagerhub;
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
     TextView tvEvents, tvMins, tvShortest, tvLongest, tvlongId, tvShortId, tvAverage;
     int totalWaitMins = 0;
     String longestWaitID;
     String shortestWaitID;
     float averageWait = 0;
     float totalNumberOfEventsf;
     float totalWaitMinsf;
     int shortestTimeInMins = 5000;
     int longestTimeInMins = 0;
     int differenceInMins = 0;
     boolean timer = false;
     Date startTime;
     Date endTime;
     String eventDateString = "";
     int[] arrayOfStats = new int[6];
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_calculate_wait_time2);
            btnOk = findViewById(R.id.btnok);
            tvEvents = findViewById(R.id.tvEvents);
            tvMins = findViewById(R.id.tvMins);
            tvShortest = findViewById(R.id.tvShort);
            tvLongest = findViewById(R.id.tvLong);
            tvShortId = findViewById(R.id.tvLongIdd);
            tvlongId = findViewById(R.id.tvShorttid);
            tvAverage = findViewById(R.id.tvAverage);

            setTitle("Key Stats for...");


            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    calculateTime("Bed ready for cleaning");




                }
            });
        }


//this will work for bed delays - cleaning for now.
// remove the getTotalsOfBeds below.....
// return - Total # of this type of event
    // - average wait
    // - longest wait, longest wait ID
    // shortest wait, shortest wait ID
    // display on a screen made up of 4 squares
    //when click on shortest or longest - then you see the Bed ID.

    public  int[] calculateTime(final String event) {
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
                                                        System.out.println("This is the event" + ": " + history.getString("eventType"));

                                                        if (event.equals(history.getString("eventType"))) {
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
                                                            long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
                                                            differenceInMins = (int) minutes;
                                                            System.out.println("This is the wait time in mins " + ": " + differenceInMins);
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
                                                        long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
                                                        differenceInMins = (int) minutes;
                                                        System.out.println("This is difference " + ": " + differenceInMins);
                                                        totalWaitMins = totalWaitMins + differenceInMins;
                                                        System.out.println("This is the wait time in mins " + ": " + differenceInMins);
                                                        timer = false;
                                                        if (differenceInMins < shortestTimeInMins) {
                                                            shortestTimeInMins = differenceInMins;
                                                            shortestWaitID = document.getId();
                                                        } else if (differenceInMins > longestTimeInMins) {
                                                            longestTimeInMins = differenceInMins;
                                                            longestWaitID = document.getId();
                                                        }
                                                    }
                                                    totalNumberOfEventsf = (int) totalNumberOfEvents;
                                                    totalWaitMinsf = (int) totalWaitMins;
                                                    averageWait = (totalWaitMinsf / totalNumberOfEventsf);


                                                    System.out.println("Total Number of events = " + totalNumberOfEvents);
                                                    System.out.println("Total of shortest time  =" + shortestTimeInMins);
                                                    System.out.println("Total of shortest time ID = " + shortestWaitID);
                                                    System.out.println("Total of longest time = " + longestTimeInMins);
                                                    System.out.println("Total of longest time ID = " + longestWaitID);
                                                    System.out.println("Total of number of minutes = " + totalWaitMins);
                                                    System.out.println("Average wait =  " + averageWait);


                                                }

                                            }
                                        });
                            }
                            tvEvents.setText(String.valueOf(totalNumberOfEvents));
                            tvMins.setText(String.valueOf(totalWaitMins));
                            tvShortest.setText(String.valueOf(shortestTimeInMins));
                            tvLongest.setText(String.valueOf(longestTimeInMins));
                            tvShortId.setText(String.valueOf(shortestWaitID));
                            tvlongId.setText(String.valueOf(longestWaitID));
                            tvAverage.setText(String.valueOf(averageWait));

                            arrayOfStats[0] = totalNumberOfEvents;
                            arrayOfStats[1] = totalWaitMins;
                            arrayOfStats[2] = shortestTimeInMins;
                            arrayOfStats[3] = longestTimeInMins;
                            //  arrayOfStats[4] = Integer.parseInt(shortestWaitID);
                            //arrayOfStats[5] = Integer.parseInt(longestWaitID);
                            System.out.println("These are the array of stats " + Arrays.toString(arrayOfStats));


                        }
                    }

                });
        System.out.println("These are the array of shortest " + shortestTimeInMins);
        return arrayOfStats;
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
                 Intent i = new Intent(CalculateWaitTime.this, hospitalmanagerhub.class);
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


