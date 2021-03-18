/*

package com.example.bedms;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Auth.login;
import com.example.bedms.HospitalManager.hospitalmanagerhub;
import com.example.bedms.Model.Bed;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BedStatusChartsForDate extends AppCompatActivity {

    private static String TAG = "MainActivity";
    PieChart pieChart;
    BarChart chart;
    TextView totalbeds, occupanyRate;
    String dateFromDateSelected;
    String titleDate;
    int totalNumberBedsInWard;
    float occRate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Integer> getTotals = new ArrayList<Integer>();
    ArrayList<String> openByWard = new ArrayList<String>();
    ArrayList<String> allocatedByWard = new ArrayList<String>();
    ArrayList<String> occupiedByWard = new ArrayList<String>();
    ArrayList<String> cleaningByWard = new ArrayList<String>();
    int bedCount = 0;
    int open, ope = 0;
    int occupied, occ = 0;
    int allocated, all = 0;
    int waitingCleaning, wai = 0;
    int bedNotYetCreated, byc = 0;
    String testEventDate;
    String event;
    Date eventDateFromDb;
    Date dateSelectedFromScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedstatuschartdate);
        Log.d(TAG, "onCreate: starting to create chart");
        totalbeds = findViewById(R.id.tvTotalBeds);
        occupanyRate = findViewById(R.id.tvOccPercent);
        pieChart = (PieChart) findViewById(R.id.idPieChart);
        chart = findViewById(R.id.barchart);


        chart.setHighlightFullBarEnabled(true);
        chart.getDescription().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setCenterText("Beds by status");
        pieChart.setCenterTextSize(16);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12);

        getTotalsOfBeds();
        Intent intent = getIntent();

        dateFromDateSelected = intent.getStringExtra("Date");
        titleDate = intent.getStringExtra("titleDate");
        System.out.println("These are date after intent " + dateFromDateSelected);
        setTitle("Bed Status as of:  " + titleDate);

        System.out.println("This get totals after method call " + getTotals);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
            }

            @Override
            public void onNothingSelected() {
            }
        });

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int categorySelected = (int) h.getX();
                buildBarChart(categorySelected);
            }

            @Override
            public void onNothingSelected() {
            }
        });

    }
    public void getTotalsOfBeds() {
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("Bed Id " + document.getId());
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
                                                    event = "";
                                                    for (QueryDocumentSnapshot history : task.getResult()) {
                                                        testEventDate = history.getString("dateAndTime");
                                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                        try {
                                                            eventDateFromDb = format.parse(testEventDate);
                                                            dateSelectedFromScreen = dateFormatter.parse(dateFromDateSelected);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        System.out.println("This is date time returned " + ": " + eventDateFromDb);
                                                        System.out.println("This is date time string  " + ": " + testEventDate);
                                                        //Storing the eventType for transactions before or equal to the selectedDate
                                                        if (eventDateFromDb.compareTo(dateSelectedFromScreen) <= 0) {
                                                            event = history.getString("eventType");
                                                        }
                                                    }
                                                    bedCount++;

                                                    switch (event) {
                                                        case "Added bed":
                                                        case "Bed allocated to ward":
                                                        case "Bed is now open":
                                                        case "Bed is cleaned – ready for next patient":
                                                            open = open + 1;
                                                            /*
                                                            String ward = document.getString("Ward");
                                                            switch (ward) {
                                                                case "St Johns":
                                                                    openByWard.add(0, document.getId());
                                                                    break;
                                                                case "St Marys":
                                                                    openByWard.add(1, document.getId());
                                                                    break;
                                                                case "St Pauls":
                                                                    openByWard.add(2, document.getId());
                                                                    break;
                                                                case "St Magz":
                                                                    openByWard.add(3, document.getId());
                                                                    break;
                                                                case "St Joes":
                                                                    openByWard.add(4, document.getId());
                                                                    break;
                                                                default:
                                                                    throw new IllegalStateException("Unexpected value: " + ward);
                                                            }


                                                            break;

                                                        case "Bed allocated - patient on way":
                                                            allocated = allocated + 1;
                                                            /*
                                                            ward = document.getString("Ward");
                                                            switch (ward) {
                                                                case "St Johns":
                                                                    allocatedByWard.add(0, document.getId());
                                                                    break;
                                                                case "St Marys":
                                                                    allocatedByWard.add(1, document.getId());
                                                                    break;
                                                                case "St Pauls":
                                                                    allocatedByWard.add(2, document.getId());
                                                                    break;
                                                                case "St Magz":
                                                                    allocatedByWard.add(3, document.getId());
                                                                    break;
                                                                case "St Joes":
                                                                    System.out.println("Inside this is the array of wards for allocated  in ward beds  " + allocatedByWard);
                                                                    allocatedByWard.add(4, document.getId());
                                                                    break;
                                                                default:
                                                                    throw new IllegalStateException("Unexpected value: " + ward);
                                                            }

                                                            break;
                                                        case "Patient in bed in ward":
                                                            occupied = occupied + 1;
                                                            /*
                                                            ward = document.getString("Ward");
                                                            switch (ward) {
                                                                case "St Johns":
                                                                    occupiedByWard.add(0, document.getId());
                                                                    break;
                                                                case "St Marys":
                                                                    occupiedByWard.add(1, document.getId());
                                                                    break;
                                                                case "St Pauls":
                                                                    occupiedByWard.add(2, document.getId());
                                                                    break;
                                                                case "St Magz":
                                                                    occupiedByWard.add(3, document.getId());
                                                                    break;
                                                                case "St Joes":
                                                                    occupiedByWard.add(4, document.getId());
                                                                    break;
                                                                default:
                                                                    throw new IllegalStateException("Unexpected value: " + ward);
                                                            }


                                                            break;
                                                        case "Bed ready for cleaning":
                                                            waitingCleaning = waitingCleaning + 1;

                                                            ward = document.getString("Ward");
                                                            switch (ward) {
                                                                case "St Johns":
                                                                    cleaningByWard.add(0, document.getId());
                                                                    break;
                                                                case "St Marys":
                                                                    cleaningByWard.add(1, document.getId());
                                                                    break;
                                                                case "St Pauls":
                                                                    cleaningByWard.add(2, document.getId());
                                                                    break;
                                                                case "St Magz":
                                                                    cleaningByWard.add(3, document.getId());
                                                                    break;
                                                                case "St Joes":
                                                                    cleaningByWard.add(4, document.getId());
                                                                    break;
                                                                default:
                                                                    throw new IllegalStateException("Unexpected value: " + ward);
                                                            }

                                                            break;
                                                        default:
                                                            bedNotYetCreated = bedNotYetCreated + 1;
                                                            break;

                                                    }
                                                }
                                                System.out.println("Inside this is the array of wards for open beds  " + openByWard);
                                                System.out.println("Inside Total Number of beds " + bedCount);
                                                System.out.println("Inside Total of open " + open);
                                                System.out.println("Inside Total of allocated " + allocated);
                                                System.out.println("Inside Total of occupied " + occupied);
                                                System.out.println("Inside Total of cleaning " + waitingCleaning);
                                                System.out.println("Inside Total of beds not been created yet on this date  " + bedNotYetCreated);
                                                System.out.println("Inside Total of all status' of beds =  " + (open + occupied + allocated + waitingCleaning));

                                            }

                                        });
                            }

                            bedCount = bedCount - bedNotYetCreated;
                            totalNumberBedsInWard = 23;
                            totalbeds.setText(Integer.toString(totalNumberBedsInWard));
                            float occ = (float) occupied;
                            float bed = (float) bedCount;
                            float aloc = (float) allocated;

                            // Replace hard coding with aloc
                            System.out.println("Outside this is the array of wards for open beds  " + openByWard);
                            System.out.println("Outside Total Number of beds " + bedCount);
                            System.out.println("Outside Total of open " + open);
                            System.out.println("Outside Total of allocated " + allocated);
                            System.out.println("Outside Total of occupied " + occupied);
                            System.out.println("Outside Total of cleaning " + waitingCleaning);
                            System.out.println("Outside Total of beds not been created yet on this date  " + bedNotYetCreated);
                            System.out.println("Outside Total of all status' of beds =  " + (open + occupied + allocated + waitingCleaning));
                            occRate = (((4f+2f)/ 23f) * (100f));
                            occupanyRate.setText(String.valueOf(occRate));
                            getTotals.add(0, open);
                            getTotals.add(1, allocated);
                            getTotals.add(2, occupied);
                            getTotals.add(3, waitingCleaning);
                            buildPieChart();
                        }
                    }
                });
        //return getTotals;

    }


    public class MyXAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }
    }

    //Pie chart

    public void buildPieChart() {
        System.out.println("These are totals at build pie chart" + getTotals);
        int open = getTotals.get(0);
        int allocated = getTotals.get(1);
        int occcupied = getTotals.get(2);
        int waitingCleaning = getTotals.get(3);
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        yEntrys.add(new PieEntry(open, "Open"));
        yEntrys.add(new PieEntry(occcupied, "Occupied"));
        yEntrys.add(new PieEntry(allocated, "Allocated"));
        yEntrys.add(new PieEntry(waitingCleaning, "Cleaning"));


        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");

        pieDataSet.setSliceSpace(1);

        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        };
        pieDataSet.setValueFormatter(vf);


        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.cat1));
        colors.add(getResources().getColor(R.color.cat2));
        colors.add(getResources().getColor(R.color.cat3));
        colors.add(getResources().getColor(R.color.cat4));


        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.BLACK);


        //create pie data object
        PieData pieData = new PieData(pieDataSet);

        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);


        // legend.setForm(Legend.LegendForm.CIRCLE);
        //   legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        pieChart.setUsePercentValues(false);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.refreshDrawableState();


    }

    //Bar Chart

    public ArrayList<BarEntry> buildBarChart(final int categorySelected) {


        String status;
        switch (categorySelected) {
            case 0:
                status = "Open";
                break;
            case 1:
                status = "Bed Occupied";
                break;
            // buildBarChartWithPie(status, categorySelected);
            case 2:
                status = "Bed allocated - patient on way";
                break;
            // buildBarChartWithPie(status, categorySelected);
            case 3:
                status = "Bed ready for cleaning";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + categorySelected);
        }

        final ArrayList<BarEntry> getBedsByStatusByWard = new ArrayList<BarEntry>();
        db.collection("bed")
                .whereEqualTo("Status", status)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Bed tempBed = null;
                            int bedCount = 0;
                            int stJohns = 0;
                            int stMarys = 0;
                            int stPauls = 0;
                            int stMagz = 0;
                            int stJoes = 0;
                            int noWard = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String ward = document.getString("Ward");
                                switch (ward) {
                                    case "St Johns":
                                        stJohns = stJohns + 1;
                                        break;
                                    case "St Marys":
                                        stMarys = stMarys + 1;
                                        break;
                                    case "St Pauls":
                                        stPauls = stPauls + 1;
                                        break;
                                    case "St Magz":
                                        stMagz = stMagz + 1;
                                        break;
                                    case "St Joes":
                                        stJoes = stJoes + 1;
                                        break;
                                    case "":
                                        noWard = noWard + 1;
                                        break;

                                }
                                bedCount++;
                            }
                            getBedsByStatusByWard.add(new BarEntry(0, stJohns));
                            getBedsByStatusByWard.add(new BarEntry(1, stMarys));
                            getBedsByStatusByWard.add(new BarEntry(2, stPauls));
                            getBedsByStatusByWard.add(new BarEntry(3, stMagz));
                            getBedsByStatusByWard.add(new BarEntry(4, stJoes));
                            getBedsByStatusByWard.add(new BarEntry(5, noWard));

                            System.out.println("These are totals bar chart" + getBedsByStatusByWard);
                            String label = "";
                            BarDataSet bds;
                            bds = new BarDataSet(getBedsByStatusByWard, label);
                            switch (categorySelected) {
                                case 0:
                                    bds.setLabel("Open");
                                    bds.setColors(new int[]{getResources().getColor(R.color.cat1)});
                                    break;
                                case 1:
                                    bds.setLabel("Occupied");
                                    bds.setColors(new int[]{getResources().getColor(R.color.cat2)});
                                    break;
                                case 2:
                                    bds.setLabel("Allocated");
                                    bds.setColors(new int[]{getResources().getColor(R.color.cat3)});
                                    break;
                                case 3:
                                    bds.setLabel("Cleaning");
                                    bds.setColors(new int[]{getResources().getColor(R.color.cat4)});
                                    break;
                                default:
                                    throw new IllegalStateException("Unexpected value: " + categorySelected);
                            }

                            bds.setValueTextSize(10f);
                            chart.setBorderWidth(1f);
                            chart.setBorderColor(Color.RED);
                            chart.setDrawValueAboveBar(true);
                            chart.getXAxis().setTextSize(5f);
                            BarData Data = new BarData(bds);

                            final ArrayList<String> xAxisLabel = new ArrayList<>();
                            xAxisLabel.add("St Johns");
                            xAxisLabel.add("St Marys");
                            xAxisLabel.add("St Pauls");
                            xAxisLabel.add("St Magz");
                            xAxisLabel.add("St Joes");
                            xAxisLabel.add("NAY");


                            XAxis xAxis = chart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                            ValueFormatter fm = new ValueFormatter() {


                                @Override
                                public String getFormattedValue(float value) {
                                    return "" + (int) value;
                                }
                            };
                            xAxis.setValueFormatter(fm);
                            xAxis.setTextSize(10f);


                            ValueFormatter formatter = new ValueFormatter() {


                                @Override
                                public String getFormattedValue(float value) {
                                    return "" + xAxisLabel.get((int) value);
                                }
                            };

                            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                            xAxis.setValueFormatter(formatter);
                            chart.setData(Data);
                            chart.invalidate();
                            // chart.refreshDrawableState();


                        } else {
                            Log.d(TAG, "Error getting bed details: ", task.getException());
                        }

                    }

                });
        return getBedsByStatusByWard;


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
                Intent i = new Intent(BedStatusChartsForDate.this, hospitalmanagerhub.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Intent z = new Intent(BedStatusChartsForDate.this, BedStatusForDate.class);
                startActivity(z);
                return true;
            case R.id.item3:
                Intent S = new Intent(BedStatusChartsForDate.this, OccupancyPerMonth.class);
                startActivity(S);
                return true;
            case R.id.item4:
                Intent g = new Intent(BedStatusChartsForDate.this, CalculateWaitTime.class);
                startActivity(g);
                return true;

            case R.id.item5:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(BedStatusChartsForDate.this, login.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

*/