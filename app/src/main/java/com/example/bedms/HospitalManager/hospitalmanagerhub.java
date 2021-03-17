package com.example.bedms.HospitalManager;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.CreateNewPatient;
import com.example.bedms.Auth.login;
import com.example.bedms.Auth.welcome;
import com.example.bedms.Bed.Inventoryofbeds;
import com.example.bedms.BedStatusForDate;
import com.example.bedms.Model.Bed;
import com.example.bedms.OccupancyPerMonth;
import com.example.bedms.R;
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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class hospitalmanagerhub extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private String[] xData = {"Open", "Occupied", "On way", "Cleaning"};
    private String[] wardsName = {"St Johns", "St Magz", "St Pauls", "St Joes", "St Marys"};
    PieChart pieChart;
    BarChart chart;
    TextView totalbeds, occupanyRate;
    int totalNumberBedsInWard;
    float occRate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Integer> getTotals = new ArrayList<Integer>();


    ArrayList NoOfBeds = new ArrayList();
    ArrayList wardss = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalmanagerhub);
        Log.d(TAG, "onCreate: starting to create chart");
        String titleVariable = "";
        setTitle("Bed Status" + titleVariable);
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
        //More options just check out the documentation!
        getTotalsOfBeds();


        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());


                int pos1 = h.toString().indexOf("(sum): ");

                String ward = wardsName[pos1 + 1];

            }

            @Override
            public void onNothingSelected() {

            }
        });


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());
                // Depending of what has been clicked I will show the beds that are with that status across the wards
                // Eg. Cleaning - show the beds that are waiting to be cleaned.
                int categorySelected = (int) h.getX();
                buildBarChart(categorySelected);


            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    public ArrayList<Integer> getTotalsOfBeds() {
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Bed tempBed = null;
                            int bedCount = 0;
                            int open = 0;
                            int occupied = 0;
                            int waitingPatient = 0;
                            int waitingCleaning = 0;
                            int other = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String status = document.getString("Status");
                                switch (status) {
                                    case "Open":
                                        open = open + 1;
                                        break;
                                    case "Bed Occupied":
                                        occupied = occupied + 1;
                                        break;
                                    case "Bed allocated - patient on way":
                                        waitingPatient = waitingPatient + 1;
                                        break;
                                    case "Bed ready for cleaning":
                                        waitingCleaning = waitingCleaning + 1;
                                        break;
                                    default:
                                        other = other + 1;
                                        break;

                                }
                                bedCount++;
                                totalNumberBedsInWard = bedCount;
                                totalbeds.setText(Integer.toString(totalNumberBedsInWard));
                               float occ = (float) occupied;
                               float bed = (float) bedCount;
                               float aloc = (float) waitingPatient;
                               occRate = (((occ+aloc) /bed)) * (100f);
                               occupanyRate.setText(String.valueOf(occRate));
                            }

                            getTotals.add(0, open);
                            getTotals.add(1, occupied);
                            getTotals.add(2, waitingPatient);
                            getTotals.add(3, waitingCleaning);
                            buildPieChart(getTotals);

                        }

                    }
                });
        return getTotals;
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

    public void buildPieChart(ArrayList<Integer> getTotals) {

        System.out.println("These are totals hos manager" + getTotals);
        int open = getTotals.get(0);
        int occcupied = getTotals.get(1);
        int waitingPatient = getTotals.get(2);
        int waitingCleaning = getTotals.get(3);
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        yEntrys.add(new PieEntry(open, "Open"));
        yEntrys.add(new PieEntry(occcupied, "Occupied"));
        yEntrys.add(new PieEntry(waitingPatient, "Allocated"));
        yEntrys.add(new PieEntry(waitingCleaning, "Cleaning"));


        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys,"");

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

                            System.out.println("These are totals" + getBedsByStatusByWard);
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
                                    return "" + xAxisLabel.get((int)value);
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
                Intent i = new Intent(hospitalmanagerhub.this, hospitalmanagerhub.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Intent z = new Intent(hospitalmanagerhub.this, BedStatusForDate.class);
                startActivity(z);
                return true;
            case R.id.item3:
                Intent S = new Intent(hospitalmanagerhub.this, OccupancyPerMonth.class);
                startActivity(S);
                return true;

            case R.id.item4:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(hospitalmanagerhub.this, login.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

