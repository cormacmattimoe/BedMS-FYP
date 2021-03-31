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
import com.example.bedms.HospitalManager.hospitalmanagerhub;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
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
    TextView totalbeds, occupanyRate;
    String dateFromDateSelected;
    int days = 0;

    float occRate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,String> myMap = new HashMap<String,String>();

    int categorySelected;
    BedInfo bip;
    String bi;
    int bedCountatDate = 0;
    int open;
    int occupied;
    int allocated;
    int cleaning;
    int bedNotYetCreated;
    int totalNumberOfBeds;
    int[][] allBedStatusbyWard = new int[5][6];
    ArrayList<BedInfo> allBeds = new ArrayList<BedInfo>();
    int[] bedStatus = new int[]{0, 0};
    String ward;
    BedInfo bedId;
    int statusCode;
    int wardCode;
    String bedIdString;
    String wardIdString;
    int status;
    Map<String, String> bedsAll = new HashMap<>();
    BedInfo bifOut = new BedInfo();
    Boolean firstTimeThrough = true;
    int occupancyRate[];    //declaring array





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

        // monthsList = R.array.Months;
    //    adapter  = new ArrayAdapter(this,android.R.layout.simple_spinner_item,monthsList);
    //   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //    spinMonths.setAdapter(adapter);


        spinMonths.setOnItemSelectedListener(this);
   //     spinMonths.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        createLineChart();
        firstTimeThrough = false;

        spinMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                monthSelected = spinMonths.getSelectedItem().toString();
                createOccupancyTotalsForMonth(monthSelected);
                createLineChart();
                setTitle("Occupancy Rate for " + monthSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setTitle("Occupancy Rate for " + monthSelected);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void createOccupancyTotalsForMonth(String monthSelected){

        //1. For i is 1 - the days of the specific month
        //  2. Call the getAllBedsIds()
        //  3. It calls build totals
        //  4. Store that in an array for the day
        //5. Call createLineChart with the array for the month where x is the day of the month,
        //    and y is the occupancy rate.
       // Date format = ("dd-MM-yyyy HH:mm:ss");
        occupancyRate = new int[31];
       // for (int i = 1; i < 29 ; i++) {
        int i = 1;
            String day = String.valueOf(i);
            if(i < 10) {
                dateSelected = ("0" + day + "-02-2021 00:00:00");
            }
            else {
                dateSelected = (day + "-02-2021 00:00:00");
            }
            getAllBedIDs();
            occupancyRate[i] = (int) occRate;

    }

    public void getAllBedIDs() {
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final BedInfo bifIn = new BedInfo();
                                bedIdString = document.getId();
                                wardIdString = document.getString("Ward");
                                bifIn.setBedId(bedIdString);
                                bifIn.setWard(wardIdString);
                                System.out.println("this is bif " + bifIn.getBedId() + " " + bifIn.getWard());

                                //Now get the history of the bed to get the status
                                db.collection("bed")
                                        .document(bedIdString)
                                        .collection("bedHistory4")
                                        .orderBy("dateAndTime")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
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
                                                    System.out.println("This bed is the bed not created yet" + bedId);
                                                    break;
                                            }   // end Switch for bedStatus

                                            switch (wardIdString) {
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
                                            bifIn.setStatus(statusCode);
                                            allBeds.add(bifIn);
                                        }
                                    }
                                });

                            }

                            System.out.println("This is the number of beds in allBeds  " + allBeds.size());
                            for (int j = 0; j < allBeds.size(); j++) {
                                System.out.println("This is allBeds after getting from bif " + allBeds.get(j).getBedId() + " " + allBeds.get(j).getWard());
                            }
                        }
                        buildTotals();

                    }
                });


    }
    public int totalCategory(int category, int[][] allBedStatusByWard) {
        int totalCategory = 0;
        for (int count = 0; count < 6; count++) {
            totalCategory = totalCategory + allBedStatusByWard[category][count];
        }
        return totalCategory;
    }
    public void buildTotals(){
        for (int i = 0; i < allBeds.size(); i++) {
            wardIdString = allBeds.get(i).getWard();
            bedStatus[0] = allBeds.get(i).getStatus();
            statusCode = allBeds.get(i).getStatus();
            switch (wardIdString) {
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
            bedStatus[1] = wardCode;
            System.out.println("This is the the bed id" + allBeds.get(i).getBedId() + " " + "Ward id = " + wardIdString + "Ward Code =  " + wardCode + "Status = " + statusCode);
            allBedStatusbyWard[statusCode][wardCode] = allBedStatusbyWard[statusCode][wardCode] + 1;
        }
        open = totalCategory(0, allBedStatusbyWard);
        allocated = totalCategory(1, allBedStatusbyWard);
        occupied = totalCategory(2, allBedStatusbyWard);
        cleaning = totalCategory(3, allBedStatusbyWard);
        bedNotYetCreated = totalCategory(4, allBedStatusbyWard);
        bedCountatDate = allBeds.size() - bedNotYetCreated;

        float occ = (float) occupied;
        float aloc = (float) allocated;
        float bedC = (float) bedCountatDate;
        occRate = (((occ + aloc) / bedC) * (100f));



    }


    public void createLineChart() {
        ArrayList<Entry> yValues = new ArrayList<>();
        days = 0;
        switch(monthSelected){
            case("January"):
                days = 31;
            case("February"):
                days = 28;
            case("March"):
                days = 31;
            default:
                days = 30;
        }
        if(firstTimeThrough)
            {
                for (int i = 1; i < days; i++) {
                    yValues.add(new Entry(i, 0));
                }
            }else{
                for (int i = 1; i < days ; i++) {
                    yValues.add(new Entry(i, occupancyRate[i]));
                }
                 }
        chart.setOnChartValueSelectedListener(this);

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
                Intent i = new Intent(OccupancyPerMonth.this, hospitalmanagerhub.class);
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
