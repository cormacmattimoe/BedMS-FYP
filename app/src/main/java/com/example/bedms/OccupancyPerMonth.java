package com.example.bedms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bedms.Auth.login;
import com.example.bedms.HospitalManager.HospitalManagerHub;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OccupancyPerMonth extends AppCompatActivity implements
        OnChartGestureListener, OnChartValueSelectedListener , AdapterView.OnItemSelectedListener  {


    LineChart chart;
    SeekBar seekBarX, seekBarY;
    Spinner spinMonths;
    String monthSelected;
    ArrayAdapter<CharSequence> adapter;
    String dateSelected;
    BedStatusChartsForDate bscd;

    //  int monthsList = new ArrayList<>();

    float occRate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    ArrayList<BedInfo> allBeds = new ArrayList<BedInfo>();
    ArrayList<BedInfo> allBedsWithStatus = new ArrayList<BedInfo>();

    String bedIdString;
    String wardIdString;

    Boolean firstTimeThrough = true;
    int[] occupancyRate ;    //declaring array
    int count =0;
    int numberOfDays = 0;


    int[] occupiedBeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupancypermonth);
        chart = findViewById(R.id.chart1);
        spinMonths = findViewById(R.id.spinMonths);
        adapter = new ArrayAdapter<CharSequence>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.Months));
        //Setting the ArrayAdapter data on the Spinner
        adapter = ArrayAdapter.createFromResource(this,
                R.array.Months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinMonths.setAdapter(adapter);
        monthSelected = "";
        getAllBedIDs();
        createNewLineChart(); //Need to figure out why it's being called twice
        firstTimeThrough = false;
        spinMonths.setOnItemSelectedListener(this);
        //     spinMonths.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        // Build array of allbeds that we will use to drive the gathering of data for the desired month




        spinMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //for month selected
                // call createOccupancytotals
                // then after totals are calculated - call createLineChart
                setTitle("Month Selected  = " );
                monthSelected = spinMonths.getSelectedItem().toString();
                if(!monthSelected.equals("Please select here...")) {
                    setTitle("Month Selected  = " + monthSelected);
                    for (int b = 0; b < allBeds.size(); b++) {
                        System.out.println("in OnSelected : all beds " + b + " = " + allBeds.get(b).getBedId());
                    }

                    createOccupancyTotalsForMonth(monthSelected);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void getAllBedIDs() {
        //creates array called allBeds - containing bedID and wardID

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
    }

    public void createOccupancyTotalsForMonth(String monthSelected) {

        String month = "";
        //  1. For i is 1 - the days of the specific month
        //  2. using allBeds array - calculate the occupancy per day and store it in occupancy [day] array.
        //  3. Call createLineChart with the array for the month where x is the day of the month,
        //    and y is the occupancy rate.
        //     Date format = ("dd-MM-yyyy HH:mm:ss");
        switch (monthSelected) {
            case ("January"):
                numberOfDays = 31;
                month = "01";
                break;
            case ("February"):
                numberOfDays = 28;
                month = "02";
                break;
            case ("March"):
                numberOfDays = 31;
                month = "03";
                break;
            default:
                return;
        }

        occupancyRate = new int[numberOfDays+1];
        occupiedBeds = new int[numberOfDays+1];

        for (int i = 1; i < numberOfDays+1; i++) {
            //  int i = 2;
            if (i < 10) {
                dateSelected = ("0" + String.valueOf(i) + "-" + month + "-2021 00:00:00");
            } else {
                dateSelected = (String.valueOf(i) + "-" + month +"-2021 00:00:00");
            }
            System.out.println("in COTFM : starting for date = " + dateSelected + "  day = " + i );

            getBedHistoryforEachBedforDay(i,numberOfDays, dateSelected);

            System.out.println("in COTFM : Occupancy rate for day = " + i + " = "+ occupancyRate[1] + " and number of days = " + numberOfDays );
        }
        //finished calculating all totals, now plot chart;
        // while(stillCalculating){
        // Do nothing.
        // }
        createNewLineChart();





    }


    public void getBedHistoryforEachBedforDay(int day, int numberOfDays, String dateSelected) {
        //run thru each bed and get its status and occ rate, and add it to the array allBedsWithStatus

        allBedsWithStatus.clear();
        int numberOfBeds  = allBeds.size();
        for (BedInfo bed:allBeds) { // THIS IS EMPTY
            bedIdString = bed.getBedId();
            wardIdString = bed.getWard();
            //System.out.println("Before calculate This the bed id and ward id for index = " + u + "  bed ID = " + bedIdString + "  ward ID = " + wardIdString);
            getHistoryDetailsNew(bedIdString, numberOfBeds, wardIdString, day, numberOfDays, dateSelected);
//            System.out.println("after calc: This is the occ rate for bed index = " + u + " and day =  " + day + " occupancy = " + occupancyRate[day]);
        }
    }

    private void getHistoryDetailsNew(String bedId, int numberOfBeds, String ward, int day, int numberOfDays, String dateSelected) {
        // Do database query for each bed ID to get the bed history back
        //System.out.println("in getHistoryDetails");

        CollectionReference eventsCollection = db.collection("bed")
                .document(bedId)
                .collection("bedHistory4");

        Task<QuerySnapshot> events = eventsCollection.get();
        while (!events.isComplete()){

        }

        int bedStatus = getBedStatus(events, dateSelected);
        AddToTotal(bedStatus, day);

    }

    private void AddToTotal(int bedStatus, int day) {
        if (bedStatus!= 4){
            occupiedBeds[day]++;
        }
    }

    public void getHistoryDetails(String bedId, int numberOfBeds, String ward, int day, int numberOfDays, String dateSelected) {
        // Do database query for each bed ID to get the bed history back
        System.out.println("in getHistoryDetails");

        db.collection("bed")
                .document(bedId)
                .collection("bedHistory4")
                .orderBy("dateAndTime")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> returnedHistory) {
                        if (returnedHistory.isSuccessful()) {
                            // System.out.println("count: " + count);
                            //  System.out.println("BedId: " + bedId);
                            BedInfo currentBed = new BedInfo();
                            int bedStatus = getBedStatus(returnedHistory, dateSelected);
                            currentBed.setCurrentStatus(bedStatus);
                            currentBed.setBedId(bedId);
                            currentBed.setWard(ward);
                            allBedsWithStatus.add(currentBed);
                            count++;

                            if (count == numberOfBeds) {
                                System.out.println("Going to build totals for day = " + day + " and count = " + count + "# of beds = " + numberOfBeds + "  date = "+ dateSelected);
                                count = 0;
                                for (int c = 0; c < numberOfBeds; c++) {
                                    System.out.println("day = " + day + " date = "+ dateSelected + "allbeds = " + allBeds.get(c).getBedId() + " abws = " +allBedsWithStatus.get(c).getBedId());
                                }
                                buildTotals(allBedsWithStatus, day, numberOfDays);
                            }
                        }
                    }
                });
    }


    public int getBedStatus(Task<QuerySnapshot> returnedHistory, String dateSelected) {
        // runs thru the returnedHistory until = dateSelected and returns state at that date

        if (returnedHistory.getResult().isEmpty()){
            return 4;
        }

        int statusCode;

        String event = "";
        String eventDateString = "";
        Date eventDateFromDb = new Date();
        Date dateSelectedFromScreen = new Date();

        for (QueryDocumentSnapshot history : returnedHistory.getResult()) {
            eventDateString = history.getString("dateAndTime");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            try {
                eventDateFromDb = format.parse(eventDateString);
                dateSelectedFromScreen = dateFormatter.parse(dateSelected);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Storing the eventType for transactions before or equal to the selectedDate
            if (eventDateFromDb.compareTo(dateSelectedFromScreen) <= 0) {
                event = history.getString("eventType");
            }
        }

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
                break;
        }
        return statusCode;
    }


    public void buildTotals(ArrayList <BedInfo> allBedDetails, int day,int numberOfDays) {

        int bedCountatDate;
        int open;
        int occupied;
        int allocated;
        int cleaning;
        int bedNotYetCreated;
        /*
        we need 2 stats for each bed: statusCode and wardCode.
              StatusCode represents :  0 = open, 1  = allocated, 2   = occupied, 3 = cleaning, 4 = no history for that date - bed not yet created.
              wardCode represents:      0 = "St Johns":1 = "St Marys":2 = "St Pauls":3 = "St Magz":4 = "St Joes":5 = other Ward (default)

          Then we use allBedsStatusbyWard to show stats for all beds. This is a 5 X 6 array showing total by Status and Ward combination :
              Status  and is :  0 = open, [1]  = allocated, [2]   = occupied, [3]  = cleaning, [4] = no history for that date - bed not yet created.
              Ward the bed is in
                  Example:  allBedsStatusbyWard[2][3] = total of beds which have status 2 (occupied) and ward 3 (St Mags).
         */

        System.out.println("In Build Totals");
        int[][] allBedStatusbyWard = new int[5][6];
        String wardString;
        int statusCode, wardCode;
        for (int k = 0; k < allBedDetails.size(); k++) {
            BedInfo bed = allBedDetails.get(k);
            wardString = bed.getWard();
            statusCode = bed.getCurrentStatus();
            switch (wardString) {
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

            allBedStatusbyWard[statusCode][wardCode]++;
            System.out.println("in build totals Day = " + day + " the bedId = " + bed.getBedId() + " Ward id = " + wardString + " Status:" + statusCode +" Ward Code:" + wardCode);
            System.out.println("Status/Ward count: " + allBedStatusbyWard[statusCode][wardCode]);

        }

        open = totalCategory(0, allBedStatusbyWard);
        allocated = totalCategory(1, allBedStatusbyWard);
        occupied = totalCategory(2, allBedStatusbyWard);
        cleaning = totalCategory(3, allBedStatusbyWard);
        bedNotYetCreated = totalCategory(4, allBedStatusbyWard);
        bedCountatDate = allBedDetails.size() - bedNotYetCreated;

        System.out.println("Totals for day = " + day + " Open:" + open + " Allocated:" + allocated + " Occupied:" + occupied + " Cleaning:" + cleaning + " Bed Not yet Created:" + bedNotYetCreated + " and overall total on the date = " + bedCountatDate);

        //Calculate Total Beds and Occupancy rate @ date and display on screen.
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float occ = (float) occupied;
        float aloc = (float) allocated;
        float bedC = (float) bedCountatDate;
        occRate = (((occ + aloc) / bedC) * (100f));
        String str = String.format("%.02f", occRate);


        // now store occupancy rate for the day and return.
        // If the day being processed = number of days then print the chart.
        occupancyRate[day] = (int) occRate;

        System.out.println("at end of Build Totals, day = " + day + " numberofdays = " + numberOfDays + " occRate% = " + occupancyRate[day]);
        allBedDetails.clear();

        if (day==numberOfDays)
        {
            for (int j = 1; j < numberOfDays+1; j++) {
                System.out.println("Occupancy Rate day: " + j + " = " + occupancyRate[j] +"%");
            }
            createLineChart();
        }
        else {
            System.out.println("no match - day = " + day + " numberofdays = " + numberOfDays + " continuing");
        }

    }

    public int totalCategory(int category, int[][] allBeds2dtc) {
        int totalCategory = 0;
        for (int count = 0; count < 6; count++) {
            totalCategory = totalCategory + allBeds2dtc[category][count];
        }
        return totalCategory;
    }

    public void createLineChart() {
        //occupancyRate Array contains rate per day for the month........

        ArrayList<Entry> yValues = new ArrayList<>();
        // clear array before starting ?
        if(firstTimeThrough )
        {
            for (int i = 1; i < numberOfDays; i++) {
                yValues.add(new Entry(i, 0));
            }
        }else{
            yValues.clear();
            for (int i = 1; i < numberOfDays ; i++) {
                yValues.add(new Entry(i, occupiedBeds[i]));
            }
        }
        //chart.setOnChartValueSelectedListener(this);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        LineDataSet set1;

        set1 = new LineDataSet(yValues, "Occupany Rate ");
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setColor(Color.RED);

        ArrayList <ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData dataLine = new LineData(set1);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setTextSize(10f);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setData(dataLine);
        chart.invalidate();
    }



    public void createNewLineChart() {
        //occupancyRate Array contains rate per day for the month........

        ArrayList<Entry> yValues = new ArrayList<>();
        // clear array before starting ?
        if(firstTimeThrough )
        {
            for (int i = 1; i < numberOfDays; i++) {
                yValues.add(new Entry(i, 0));
            }
        }else{
            yValues.clear();
            for (int i = 1; i < numberOfDays ; i++) {
                yValues.add(new Entry(i, occupiedBeds[i]));
            }
        }
        //chart.setOnChartValueSelectedListener(this);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        LineDataSet set1;

        set1 = new LineDataSet(yValues, "Occupany Rate ");
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setColor(Color.RED);

        ArrayList <ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData dataLine = new LineData(set1);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setTextSize(10f);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setData(dataLine);
        chart.invalidate();
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

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
                Intent i = new Intent(OccupancyPerMonth.this, HospitalManagerHub.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Intent z = new Intent(OccupancyPerMonth.this, BedStatusForDate.class);
                startActivity(z);
                return true;
            case R.id.item3:
                Intent S = new Intent(OccupancyPerMonth.this, OccupancyPerMonth.class);
                startActivity(S);
                return true;
            case R.id.item4:
                Intent g = new Intent(OccupancyPerMonth.this, CalculateWaitTime.class);
                startActivity(g);
                return true;

            case R.id.item5:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(OccupancyPerMonth.this, login.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
