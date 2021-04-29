package com.example.bedms.HospitalManager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.example.bedms.Auth.login;
import com.example.bedms.BedCache;
import com.example.bedms.BedHistoryEventHelper;
import com.example.bedms.BedStatusChartsForDate;
import com.example.bedms.CalculateWaitTime;
import com.example.bedms.Model.Bed;
import com.example.bedms.Model.BedHistoryEvent;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.example.bedms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

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


    String bedIdString;
    String wardIdString;

    Boolean firstTimeThrough = true;
    int[] occupancyRate ;    //declaring array
    int count =0;
    int numberOfDays = 0;

    private Hashtable<String, Bed> bedCache;

    int[] occupiedBeds;
    int[] bedTotals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupancypermonth);
        bedCache = BedCache.getBedCache();
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

        spinMonths.setOnItemSelectedListener(this);

        // Build array of allbeds that we will use to drive the gathering of data for the desired month




        spinMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //for month selected
                // call createOccupancytotals
                // then after totals are calculated - call createLineChart
                setTitle("Month Selected  = " );
                monthSelected = spinMonths.getSelectedItem().toString();
                    setTitle("Month Selected  = " + monthSelected);

//                    Enumeration<String> keys = bedCache.keys();
//                    int counter = 1;
//                    while(keys.hasMoreElements()){
//                        String key = keys.nextElement();
//                        System.out.println("in OnSelected : counter:  " + counter + ", BedId: " + key);
//                        counter++;
//                    }
                    createOccupancyTotalsForMonth(monthSelected);
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
            case ("April"):
                numberOfDays = 30;
                month = "04";
                break;
            default:
                return;
        }

        occupancyRate = new int[numberOfDays+1];
        occupiedBeds = new int[numberOfDays+1];
        bedTotals = new int[numberOfDays+1];



        for (int day = 1; day <= numberOfDays; day++) {
            //  int i = 2;
            if (day < 10) {
                dateSelected = ("0" + String.valueOf(day) + "-" + month + "-2021 23:59:59");
            } else {
                dateSelected = (String.valueOf(day) + "-" + month +"-2021 23:59:59");
            }
            if (day == 31){
                System.out.println("ewrpkjw");
            }

            //System.out.println("in COTFM : starting for date = " + dateSelected + "  day = " + day );

            //Get Latest Bed history per bed for a given day
            Enumeration<String> keys = bedCache.keys();
            while(keys.hasMoreElements()){
                String bedId = keys.nextElement();
                BedHistoryEvent latestEvent = bedCache.get(bedId).getLatestBedHistoryForDay(dateSelected);

                //Have the latest Event for that Day so now we work out the occupancy rate.
                if (latestEvent!= null){
                    int bedStatusCode = BedHistoryEventHelper.getBedStatusFromEvent(latestEvent);

                    //Change here to calculate occupancy rate.
                    AddToTotalOccupiedBeds(bedStatusCode, day);
                    AddToTotalBedCount(bedStatusCode, day);
                }
                System.out.println("Occupied: " + occupiedBeds[day] + ", Total for day: " + bedTotals[day]);
            }

            //System.out.println("in COTFM : occupied beds rate for day = " + day + " = "+ occupiedBeds[1] + " and number of days = " + numberOfDays );
        }
        CalculateOccupancyRate();
        createNewLineChart();
    }

    private void CalculateOccupancyRate() {
        for (int index = 1; index<occupiedBeds.length; index++){
            if(occupiedBeds[index] == 0){
                occupancyRate[index] = 0;
            } else {
                float occ = occupiedBeds[index];
                float totals = bedTotals[index];
                float occupiedRatePercentage = (( occ/totals) * (100f));
                occupancyRate[index] = (int) occupiedRatePercentage;
            }
        }
    }

    private void AddToTotalOccupiedBeds(int bedStatus, int day) {
        if ( bedStatus== 1 || bedStatus== 2){
            occupiedBeds[day]++;
        }
    }

    private void AddToTotalBedCount(int bedStatus, int day) {
        if ( bedStatus!=4){
            bedTotals[day]++;
        }
    }

    public void createNewLineChart() {
        //occupancyRate Array contains rate per day for the month........

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.clear();
        for (int i = 1; i <= numberOfDays ; i++) {
            yValues.add(new Entry(i, occupancyRate[i]));
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

        chart.getAxisLeft().setAxisMinimum(0);
        chart.getAxisLeft().setAxisMaximum(100);
        chart.getAxisRight().setAxisMinimum(0);
        chart.getAxisRight().setAxisMaximum(100);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospitalmanagerhubmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Intent i = new Intent(OccupancyPerMonth.this, HospitalManagerHub.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Intent z = new Intent(OccupancyPerMonth.this, StatsAsOfToday.class);
                startActivity(z);
                return true;
            case R.id.item3:
                Intent k = new Intent(OccupancyPerMonth.this,BedStatusForDate.class);
                startActivity(k);
                return true;
            case R.id.item4:
                Intent S = new Intent(OccupancyPerMonth.this, OccupancyPerMonth.class);
                startActivity(S);
                return true;
            case R.id.item5:
                Intent g = new Intent(OccupancyPerMonth.this, CalculateWaitTime.class);
                startActivity(g);
                return true;

            case R.id.item6:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(OccupancyPerMonth.this, login.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);

    }
    }
}
