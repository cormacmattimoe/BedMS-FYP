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
    int totalNumberBedsInWard;
    float occRate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Integer> getTotals = new ArrayList<Integer>();
    ArrayList<String> openByWard = new ArrayList<String>();
    ArrayList<String> allocatedByWard = new ArrayList<String>();
    ArrayList<String> occupiedByWard = new ArrayList<String>();
    ArrayList<String> cleaningByWard = new ArrayList<String>();
    TextView edDate;
    DatePickerDialog.OnDateSetListener mDateSetListener1, mDateSetListner2;
    Button statusBtn;
    Date dateSelectedtesting;
    Date testingDate = Calendar.getInstance().getTime();
    Date eventDate;

    Date dateSelectedDate;
    String dateSelectedString;
    String event;
    Date eventDateFromDb;
    Date dateSelectedFromScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedstatuschartdate);
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

        Intent intent = getIntent();
        dateFromDateSelected = intent.getStringExtra("Date");
        System.out.println("These are date after intent " + dateFromDateSelected);

        // need to change getTotals to work off the results from getTotalsOfBed.  This has changed.  More to be done here
        getTotals = getTotalsOfBeds();
        // then call PieChart with totals.
        // and call buildbarchart too


        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
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
        ArrayList<Integer> localTotals = new ArrayList<Integer>();
        int bedCount = 0;
        int open, ope = 0;
        int occupied, occ = 0;
        int allocated, all = 0;
        int waitingCleaning, wai = 0;
        int bedNotYetCreated, byc = 0;

        // need to create another 2-d array linking Status (open etc) with Ward.  how do we pass back everything.....need more time here.

        String testEventDate;
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // run thru each bed
                                // call GetBedStatusAndWardForDate with dateFromDateSelected
                                //
                                // this module will take in the BedID and the dateFromDateSelected.  it will use the BedID to retrieve bedHistory.
                                //
                                // it will return array [] which is 6 integers long and has following meaning :
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
                                int[] results = new int[5]; //  six elements in this array.
                                for (int index = 0; index <= 5; index++) {
                                    results[index] = 0;
                                }
                                // call GetBedAndStatusAndWardForDate with the datefromDateSelected. that will return and array showing the status and the Ward for the bed.

                                // results = GetBedStatusAndWardForDate(dateFromDateSelected);  //this is not working?????
                                //
                                // then run thru resutls as follows:
                                //  open = open + results[0]
                                //  allocated = allocated + results[1]
                                // and so on.
                                // still do do  :
                                // update the 2-d array for Status and Ward.....
                                // pass back the totals
                                // call piechart from parent class.

                                System.out.println("Inside Total Number of beds ");
                                System.out.println("Inside Total of open ");
                                System.out.println("Inside Total of allocated ");
                                System.out.println("Inside Total of occupied ");
                                System.out.println("Inside Total of cleaning ");
                                System.out.println("Inside Total of beds not been created yet on this date  ");
                                System.out.println("Inside Total of all status' of beds =  ");

                            }
                        }

                    }
                });

        // Remeber :   bedCount = bedCount - bedNotYetCreated;
                    /*totalNumberBedsInWard = 23;
                    totalbeds.setText(Integer.toString(totalNumberBedsInWard));
                    float occ = (float) occupied;
                    float bed = (float) bedCount;
                    float aloc = (float) allocated;

                    // Replace hard coding with aloc
                    occRate = ((4f+2f/23f) * 100f);
                    occupanyRate.setText(String.valueOf(occRate));
                    BedStatusChartsForDate.this.getTotals.add(0, BedStatusChartsForDate.this.open);
                    BedStatusChartsForDate.this.getTotals.add(1, BedStatusChartsForDate.this.allocated);
                    BedStatusChartsForDate.this.getTotals.add(2, BedStatusChartsForDate.this.occupied);
                    BedStatusChartsForDate.this.getTotals.add(3, BedStatusChartsForDate.this.waitingCleaning);
                    System.out.println("getTotals - just before call to PieChart " + BedStatusChartsForDate.this.getTotals);
                    buildPieChart(BedStatusChartsForDate.this.getTotals);



        return localTotals;

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

    public void buildPieChart(final ArrayList<Integer> getTotals) {
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
                Intent i = new Intent(BedStatusChartsForDate.this, BedStatusChartsForDate.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Intent z = new Intent(BedStatusChartsForDate.this, BedStatusForDate.class);
                startActivity(z);
                return true;
            case R.id.item3:
                Intent S = new Intent(BedStatusChartsForDate.this, BedStatusChartsForDate.class);
                startActivity(S);
                return true;

            case R.id.item4:
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