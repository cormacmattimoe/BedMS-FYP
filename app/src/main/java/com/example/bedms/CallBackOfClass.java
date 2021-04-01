/*

package com.example.bedms;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

public class BedStatusChartsForDate extends AppCompatActivity {

    private static String TAG = "MainActivity";
    PieChart pieChart;
    BarChart chart;
    TextView totalbeds, occupanyRate;
    String dateFromDateSelected;
    String titleDate;

    float occRate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, String> myMap = new HashMap<String, String>();
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
    ArrayList<BedInfo> allBedsWithoutStatus = new ArrayList<BedInfo>();
    int[] bedStatus = new int[]{0, 0};
    String ward;
    BedInfo bedId;
    String dateSelected;
    int statusCode;
    int wardCode;
    String bedIdStringa;
    String bedIdString;
    String wardIdString = new String();
    int status;
    Map<String, String> bedsAll = new HashMap<>();
    BedInfo bifOut = new BedInfo();
    ArrayList<BedInfo> allBedsWithStatus = new ArrayList<BedInfo>();

    BedInfo bedDetails =  new BedInfo();
    BedInfo bifIn = new BedInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedstatuschartdate);
        Log.d(TAG, "onCreate: starting to create chart");
        totalbeds = findViewById(R.id.tvTotalBeds);
        occupanyRate = findViewById(R.id.tvOccPercent);
        pieChart = findViewById(R.id.idPieChart);
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
        categorySelected = 99;

        // now get all beds...
        // and for each bed get its status and Ward which is returned in bedStatusAndWard array.
        //then add that Array to the overall beds array which shows the numbers in each status, and then the numbers in each Ward for each Status.

        // ArrayList<String> allBeds;


        System.out.println("This is the size before  " + allBedsWithoutStatus.size());


        getAllBedDetails(new Callbacka() {
            @Override
            public void calla() {
                System.out.println("This is the size after call back  " + allBedsWithoutStatus.size());
                for (int i = 0; i < allBedsWithoutStatus.size(); i++) {
                    bedIdString = "";
                    wardIdString = "";
                    System.out.println("This the list of beds after call " + i + "  " + allBedsWithoutStatus.get(i).getBedId());
                }
                for (int u = 0; u < allBedsWithoutStatus.size(); u++) {
                    bedIdString = allBedsWithoutStatus.get(u).getBedId();
                    wardIdString = allBedsWithoutStatus.get(u).getWard();
                    System.out.println("Before calculate This the bed id and ward id " + u + "  " + bedIdString + " " + wardIdString);
                    calculateStatusAndWard(new Callbackb() {
                        @Override
                        public void callb() {
                            for (int j = 0; j < allBedsWithStatus.size(); j++) {
                                System.out.println("List of beds with status added  " + "  " + j + " " + allBedsWithStatus.get(j).getBedId() + allBedsWithStatus.get(j).getWard() + " " + allBedsWithStatus.get(j).getStatus());
                            }
                        }
                    });
                    allBedsWithoutStatus = new ArrayList<>();
                }
            }
        });





        //  totalNumberOfBeds = allBeds.size();




        /*
          first element of singleBedStatusAndWard  = Status  and is :  0 = open, [1]  = allocated, [2]   = occupied, [3]  = cleaning, [4] = no history for that date - bed not yet created.
          Second element is the Ward the bed is in
                         open =                   [0][w]
                         allocated =              [1][w]
                         occupied =               [2][w]
                         cleaning =               [3][w]
                         notcreatedyet =          [4][w]       This will be set if event = "".
                         Ward name =                 [w]   0 = "St Johns":1 = "St Marys":2 = "St Pauls":3 = "St Magz":4 = "St Joes":5 = other Ward (default)





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
                categorySelected = (int) h.getX();
                System.out.println("this is the value of category selected = " + categorySelected);
                buildBarChart(categorySelected);
            }

            @Override
            public void onNothingSelected() {
            }
        });

    }

    public void getAllBedDetails(Callbacka callback) {
        System.out.println("Get all bed details ");
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task2.getResult()) {
                                final BedInfo bifIn = new BedInfo();
                                bedIdStringa = document.getId();
                                wardIdString = document.getString("Ward");
                                bifIn.setBedId(bedIdStringa);
                                bifIn.setWard(wardIdString);
                                allBedsWithoutStatus.add(bifIn);
                            }
                            callback.calla();
                        }
                    }
                });
    }




    // Method here to get all the beds from the beds collection to then to be passed in to get the bed history in the next method
    public void calculateStatusAndWard(Callbackb callbackb) {


        //   System.out.println("this should be details returned "+   .getAllBedDetails());
        //Now get the history of the bed to get the status
        db.collection("bed")
                .document(bedIdString)
                .collection("bedHistory4")
                .orderBy("dateAndTime")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                        if (task1.isSuccessful()) {
                            System.out.println("This is bed id using for history " + bedIdString);
                            String event = "";
                            String eventDateString = "";
                            Date eventDateFromDb = new Date();
                            Date dateSelectedFromScreen = new Date();
                            for (QueryDocumentSnapshot history : task1.getResult()) {
                                eventDateString = history.getString("dateAndTime");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                System.out.println("This is bed id using for inside method at history " + bedIdString);
                                try {
                                    eventDateFromDb = format.parse(eventDateString);
                                    dateSelectedFromScreen = dateFormatter.parse(dateFromDateSelected);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //   System.out.println("This is date time returned " + ": " + eventDateFromDb);
                                //   System.out.println("This is date time string  " + ": " + dateSelectedFromScreen);

                                //Storing the eventType for transactions before or equal to the selectedDate
                                if (eventDateFromDb.compareTo(dateSelectedFromScreen) <= 0) {
                                    event = history.getString("eventType");
                                }
                            }
                            // end For loop
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
                            bedDetails.setBedId(bedIdString);
                            bedDetails.setWard(wardIdString);
                            bedDetails.setStatus(statusCode);
                            System.out.println("This is bip before " + bedDetails.getBedId() + " " + bedDetails.getWard() + " " +  bedDetails.getStatus());
                            allBedsWithStatus.add(bedDetails);
                            System.out.println("Bed details added");
                            callbackb.callb();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task1.getException());
                        }

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
        for (int i = 0; i < allBedsWithStatus.size(); i++) {
            wardIdString = allBedsWithStatus.get(i).getWard();
            bedStatus[0] = allBedsWithStatus.get(i).getStatus();
            statusCode = allBedsWithStatus.get(i).getStatus();
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
            System.out.println("This is the the bed id" + allBedsWithStatus.get(i).getBedId() + " " + "Ward id = " + wardIdString + "Ward Code =  " + wardCode + "Status = " + statusCode);
            allBedStatusbyWard[statusCode][wardCode] = allBedStatusbyWard[statusCode][wardCode] + 1;
        }
        open = totalCategory(0, allBedStatusbyWard);
        allocated = totalCategory(1, allBedStatusbyWard);
        occupied = totalCategory(2, allBedStatusbyWard);
        cleaning = totalCategory(3, allBedStatusbyWard);
        bedNotYetCreated = totalCategory(4, allBedStatusbyWard);
        bedCountatDate = allBedsWithStatus.size() - bedNotYetCreated;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        //set up Total Beds and Occupancy rate @ date.
        totalbeds.setText(Integer.toString(bedCountatDate));
        float occ = (float) occupied;
        float aloc = (float) allocated;
        float bedC = (float) bedCountatDate;
        occRate = (((occ + aloc) / bedC) * (100f));
        String str = String.format("%.02f", occRate);
        occupanyRate.setText(str);
        buildPieChart(open, allocated, occupied, cleaning);


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
        yEntrys.add(new PieEntry(allocated, "Allocated"));
        yEntrys.add(new PieEntry(occupied, "Occupied"));
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
        pieChart.setNoDataText("No Chart Data"); // this is the top line
        pieChart.invalidate();
        //    pieChart.refreshDrawableState();


        buildBarChart(categorySelected);

    }    // end buildPieChart



    public ArrayList<BarEntry> buildBarChart( int categorySelected) {
        ArrayList<BarEntry> getBedsByStatusByWard = new ArrayList<BarEntry>();
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
            getBedsByStatusByWard.add(new BarEntry(0, allBedStatusbyWard[categorySelected][0]));
            getBedsByStatusByWard.add(new BarEntry(1, allBedStatusbyWard[categorySelected][1]));
            getBedsByStatusByWard.add(new BarEntry(2, allBedStatusbyWard[categorySelected][2]));
            getBedsByStatusByWard.add(new BarEntry(3, allBedStatusbyWard[categorySelected][3]));
            getBedsByStatusByWard.add(new BarEntry(4, allBedStatusbyWard[categorySelected][4]));
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
                    bds.setLabel("Allocated");
                    bds.setColors(new int[]{getResources().getColor(R.color.cat2)});
                    break;
                case 2:
                    bds.setLabel("Occupied");
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
            class MyValueFormatter extends ValueFormatter {

                private DecimalFormat mFormat;

                public MyValueFormatter() {
                    mFormat = new DecimalFormat("#");
                }

                @Override
                public String getFormattedValue(float value) {
                    return mFormat.format(value);
                }
            }
            class MyDecimalValueFormatter extends ValueFormatter {

                private DecimalFormat mf;

                public MyDecimalValueFormatter() {
                    mf = new DecimalFormat("#");
                }

                @Override
                public String getFormattedValue(float value) {
                    return mf.format(value);
                }
            }

            MyDecimalValueFormatter fs = new MyDecimalValueFormatter();

            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);
            Data.setValueFormatter(fs);
            Data.setValueFormatter(new MyValueFormatter());
            xAxis.setValueFormatter(formatter);
            chart.setData(Data);
            chart.invalidate();
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

    // Callback function
    public interface Callbacka {
        void calla();
    }
    // Callback function
    public interface Callbackb {
        void callb();
    }

}
*/