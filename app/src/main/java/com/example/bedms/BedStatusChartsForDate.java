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
    ArrayList<BarEntry> getBedsByStatusByWard = new ArrayList<BarEntry>();
    float occRate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int bedCountatDate = 0;
    int open = 0;
    int occupied = 0;
    int allocated = 0;
    int cleaning = 0;
    int bedNotYetCreated = 0;
    int totalNumberOfBeds;
    int[][] allBedStatusbyWard = new int[5][6];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedstatuschartdate);
        Log.d(TAG, "onCreate: starting to create chart");
        totalbeds = findViewById(R.id.tvTotalBeds);
        occupanyRate = findViewById(R.id.tvOccPercent);
        pieChart = (PieChart) findViewById(R.id.idPieChart);
        chart = findViewById(R.id.barchart);

        // logic for main onCreate Method:
        // 1. get date from calling screen using Intent.
        // 2. Gets array of all beds in system - getAllBeds()
        // 3. call GetStatusAndWardForDate for every bed for selected Date.  returns 2 element Array(Status,Ward).
        // 4. The count of numbers of beds is stored in a 2-d array [status][ward] - allBedStatusByWard
        // 5. Total Open, allocated, occupied and cleaning are passed to BuildPieChart
        // 6. for first time thru BuildBarChart called with category - 99 and allBedStatusByWard - to show all bed status
        // 7. onClick PieChart - the categorySelected is used to seed buildBarChart which shows the beds/ward for the category selected.


        Intent intent = getIntent();
        dateFromDateSelected = intent.getStringExtra("Date");
        titleDate = intent.getStringExtra("titleDate");
        System.out.println("These are date after intent " + dateFromDateSelected);
        setTitle("Bed Status as of:  " + titleDate);

        // now get all beds...
        // and for each bed get its status and Ward which is returned in bedStatusAndWard array.
        //then add that Array to the overall beds array which shows the numbers in each status, and then the numbers in each Ward for each Status.

        ArrayList<String> allBeds;
        int[] singleBedStatusAndWard;
        GetBedStatusAndWardForDate gbswd = new GetBedStatusAndWardForDate();

        allBeds = getAllBedIDs();
        totalNumberOfBeds = allBeds.size();


        for (int i = 0; i < totalNumberOfBeds; i++) {
            singleBedStatusAndWard = gbswd.GetBedStatusAndWard(dateFromDateSelected, allBeds.get(i));
            int statusCode = singleBedStatusAndWard[0];
            int wardCode = singleBedStatusAndWard[1];
            allBedStatusbyWard[statusCode][wardCode] = allBedStatusbyWard[statusCode][wardCode] + 1;
        }

        /*
          first element of singleBedStatusAndWard  = Status  and is :  0 = open, [1]  = allocated, [2]   = occupied, [3]  = cleaning, [4] = no history for that date - bed not yet created.
          Second element is the Ward the bed is in
                         open =                   [0][w]
                         allocated =              [1][w]
                         occupied =               [2][w]
                         cleaning =               [3][w]
                         notcreatedyet =          [4][w]       This will be set if event = "".
                         Ward name =                 [w]   0 = "St Johns":1 = "St Marys":2 = "St Pauls":3 = "St Magz":4 = "St Joes":5 = other Ward (default)
         */

        System.out.println("This get totals after call all beds and their status:  " + allBedStatusbyWard);
        open = totalCategory(0,allBedStatusbyWard);
        allocated = totalCategory(1,allBedStatusbyWard);
        occupied = totalCategory(2,allBedStatusbyWard);
        cleaning = totalCategory(3,allBedStatusbyWard);
        bedNotYetCreated = totalCategory(4,allBedStatusbyWard);
        bedCountatDate = totalNumberOfBeds - bedNotYetCreated;


        buildPieChart(open, allocated, occupied, cleaning);

        //set up Total Beds and Occupancy rate @ date.
        totalbeds.setText(Integer.toString(bedCountatDate));
        float occ = (float) occupied;
        float aloc = (float) allocated;
        float bedC = (float) bedCountatDate;
        occRate = (((occ + aloc) / bedC) * (100f));
        occupanyRate.setText(String.valueOf(occRate));

        buildBarChart(99,allBedStatusbyWard);


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
                System.out.println("this is the value of category selected = " + categorySelected);
                buildBarChart(categorySelected,allBedStatusbyWard);
            }

            @Override
            public void onNothingSelected() {
            }
        });

    }

    // Method here to get all the beds from the beds collection to then to be passed in to get the bed history in the next method
    public ArrayList<String> getAllBedIDs() {
        final ArrayList<String> allBedslocal = new ArrayList<String>();
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                allBedslocal.add(document.getId());
                            }
                            System.out.println( "this is allBeds" + allBedslocal);
                        }
                    }
                });
        return allBedslocal;
    }

    public int totalCategory(int category, int[][] allBedStatusByWard) {
        int totalCategory = 0;
        for (int count = 0; count < 5; count++) {
            totalCategory = totalCategory + allBedStatusByWard[category][count];
        }
        return totalCategory;
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


    public void buildPieChart(int open, int allocated, int occupied, int cleaning ) {
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

        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        yEntrys.add(new PieEntry(open, "Open"));
        yEntrys.add(new PieEntry(occupied, "Occupied"));
        yEntrys.add(new PieEntry(allocated, "Allocated"));
        yEntrys.add(new PieEntry(cleaning, "Cleaning"));
        //Not Yet Created - not showing as it is deducted from total beds for that date.

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(1);

        ValueFormatter vf = new ValueFormatter() { //value format here, here is the overridden method
            @Override
            public String getFormattedValue(float value) {
                return "" + (int) value;
            }
        };
        pieDataSet.setValueFormatter(vf);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.cat1));
        colors.add(getResources().getColor(R.color.cat2));
        colors.add(getResources().getColor(R.color.cat3));
        colors.add(getResources().getColor(R.color.cat4));

        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(12);
        pieDataSet.setValueTextColor(Color.BLACK);
        PieData pieData = new PieData(pieDataSet);

        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        // legend.setForm(Legend.LegendForm.CIRCLE);
        //   legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        pieChart.setUsePercentValues(false);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.refreshDrawableState();


    }    // end buildPieChart



    public ArrayList<BarEntry> buildBarChart( int categorySelected, int [][] BedStatusbyWard) {

        chart.setHighlightFullBarEnabled(true);
        chart.getDescription().setEnabled(false);

        if (categorySelected == 99){
            // first time thru category = 99 - meaning show all beds.
            // To do this add all categories for all wards to show total for all categories for all wards
            // OR show stacked barchart for Ward - showing open, allocated, oppupoed, cleaning by ward in a stacked barchart.
            //
            //  more here for all wards.....
        }
            else {
            getBedsByStatusByWard.add(new BarEntry(0, BedStatusbyWard[categorySelected][0]));
            getBedsByStatusByWard.add(new BarEntry(1, BedStatusbyWard[categorySelected][1]));
            getBedsByStatusByWard.add(new BarEntry(2, BedStatusbyWard[categorySelected][2]));
            getBedsByStatusByWard.add(new BarEntry(3, BedStatusbyWard[categorySelected][3]));
            getBedsByStatusByWard.add(new BarEntry(4, BedStatusbyWard[categorySelected][4]));
            //getBedsByStatusByWard.add(new BarEntry(5, BedStatusbyWard[categorySelected][5]));


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
            }
        return getBedsByStatusByWard;
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

