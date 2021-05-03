

package com.example.bedms.HospitalManager;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.BedCache;
import com.example.bedms.BedHistoryEventHelper;
import com.example.bedms.BedInfo;
import com.example.bedms.Model.Bed;
import com.example.bedms.Model.BedHistoryEvent;
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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class BedStatusChartsForDate extends AppCompatActivity  {

    private static String TAG = "MainActivity";
    private Hashtable<String, Bed> bedCache;
    PieChart pieChart;
    BarChart chart;
    TextView totalbeds, occupanyRate;
    String dateFromDateSelected;
    String titleDate;
    float occRate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int categorySelected = 0;
    int open;
    int occupied;
    int allocated;
    int cleaning;
    int bedNotYetCreated;
    int[][] allBedStatusbyWard = new int[5][6];
    ArrayList<BedInfo> allBedsWithoutStatus = new ArrayList<BedInfo>();
    ArrayList<BedInfo> allBedsWithStatus = new ArrayList<BedInfo>();
    String bedIdString;
    String wardIdString;
    String wardId;
    int statusCode;
    BedInfo bedDetails =  new BedInfo();
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedstatuschartdate);
        Log.d(TAG, "onCreate: starting to create chart");
        totalbeds = findViewById(R.id.tvTotalBeds);
        occupanyRate = findViewById(R.id.tvOccPercent);
        pieChart = findViewById(R.id.idPieChart);
        chart = findViewById(R.id.barchart);
        bedCache = BedCache.bedCache;

        // logic for main onCreate Method:
        // 1. get date from calling screen using Intent.
        // 2. Creates array of all beds called allBedsWithoutStatus using an intent (all beds gathered in previous call)
        // 3. call getBedHistory
        // 4. getBedHistory loops thru all beds calling getHistoryDetails
        // 5. getHistoryDetails call Firebase d/b for all history records.  It then calls getStatus to calculate status as off dateSelected.
        // 6. once we have status, then bed, ward and status are all added to new array allBedsWithStatus.
        // 7. once we have all beds processed we then call buildtotals which calculates totals for open, allocated, occupied, cleaning, and notyetcreated.
        // 8. buildTotals then calls BuildPieChart with Open, allocated, occupied and cleaning
        // 9. buildPieChart then calls BuildBarChart.  The first time thru categorySelected is set to 99. This is to show all bed status in the chart.


        // 10. onClick PieChart - the categorySelected is used to seed buildBarChart which shows the beds/ward for the category selected.


        Intent intent = getIntent();
        dateFromDateSelected = intent.getStringExtra("Date");
        titleDate = intent.getStringExtra("titleDate");

        setTitle("Bed Status as of:  " + titleDate);

        ClearTotals();
        GetLatestBedStatusForDate(dateFromDateSelected);
        buildPieChart(open, occupied, allocated, cleaning);


//        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//            }
//
//            @Override
//            public void onNothingSelected() {
//            }
//        });

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

    private void ClearTotals() {
        open = allocated = occupied = cleaning = bedNotYetCreated = 0;
        allBedStatusbyWard = new int[5][6];
    }

    private void GetLatestBedStatusForDate(String dateSelected) {
        //Get Latest Bed history per bed for a given day

            Enumeration<String> keys = bedCache.keys();
            while(keys.hasMoreElements()){
                String bedId = keys.nextElement();
                Bed bed = bedCache.get(bedId);
                BedHistoryEvent latestEvent = bed.getLatestBedHistoryForDay(dateSelected);

                //Have the latest Event for that Day so now we work out the occupancy rate.
                if (latestEvent!= null){
                    if (bedId.equals("JOzWQGBlnsB4Ca2RZca6")){
                        System.out.println("Here");
                    }
                    int bedStatusCode = BedHistoryEventHelper.getBedStatusFromEvent(latestEvent);
                if (bedStatusCode == 1){
                        System.out.println("Here");
                    }
                    //Change here to calculate occupancy rate.
                    SumTotals(bedStatusCode,GetWardIndex(bed.getBedWard()));
                }
            }
        int bedCountAtDate = open+allocated+occupied+cleaning;
        float occ = (float) occupied;
        float aloc = (float) allocated;
        float bedC = (float) bedCountAtDate;
        occRate = (((occ + aloc) / bedC) * (100f));
        String str = String.format("%.0f", occRate);
        totalbeds.setText(Integer.toString(bedCountAtDate));
        occupanyRate.setText(str);
    }

    private int GetWardIndex(String bedWard) {
        int wardCode;
        switch (bedWard) {
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
        return wardCode;
    }

    private void SumTotals(int bedStatusCode,int Ward) {
        switch (bedStatusCode){
            case 0:
                open++;
                allBedStatusbyWard[0][Ward]++;
                break;
            case 1:
                occupied++;
                allBedStatusbyWard[1][Ward]++;
                break;
            case 2:
                allocated++;
                allBedStatusbyWard[2][Ward]++;
                break;
            case 3:
                cleaning++;
                allBedStatusbyWard[3][Ward]++;
                break;
            case 4:
                bedNotYetCreated++;
                allBedStatusbyWard[4][Ward]++;
                break;
            default:
                break;
        }
    }

    public void buildPieChart(int open,int occupied, int allocated,  int cleaning ) {
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

        pieChart.setUsePercentValues(false);
        pieChart.setData(pieData);
        pieChart.setNoDataText("No Chart Data"); // this is the top line
        pieChart.invalidate();
        //    pieChart.refreshDrawableState();

        buildBarChart(categorySelected);

    }    // end buildPieChart



    public void buildBarChart( int bedStatus) {
        ArrayList<BarEntry> getBedsByStatusByWard = new ArrayList<BarEntry>();
        chart.setHighlightFullBarEnabled(true);
        chart.getDescription().setEnabled(false);

            getBedsByStatusByWard.add(new BarEntry(0, allBedStatusbyWard[bedStatus][0]));
            getBedsByStatusByWard.add(new BarEntry(1, allBedStatusbyWard[bedStatus][1]));
            getBedsByStatusByWard.add(new BarEntry(2, allBedStatusbyWard[bedStatus][2]));
            getBedsByStatusByWard.add(new BarEntry(3, allBedStatusbyWard[bedStatus][3]));
            getBedsByStatusByWard.add(new BarEntry(4, allBedStatusbyWard[bedStatus][4]));
            getBedsByStatusByWard.add(new BarEntry(5, allBedStatusbyWard[bedStatus][5]));


            String label = "";
            BarDataSet bds;
            bds = new BarDataSet(getBedsByStatusByWard, label);
            switch (bedStatus) {
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
                    throw new IllegalStateException("Unexpected value: " + bedStatus);
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



    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospitalmanagerhubmainmenu, menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Intent i = new Intent(BedStatusChartsForDate.this, HospitalManagerHub.class);
                startActivity(i);
                return true;
            case R.id.item2: // For todays date we go to the same class as any other day but we specify today's date
                Intent intent = new Intent(BedStatusChartsForDate.this, BedStatusChartsForDate.class);
                intent.putExtra("All Beds", allBedsWithoutStatus);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String today = dtf.format(LocalDateTime.now());
                intent.putExtra("Date", today+ " " + "23" + ":" + "59" + ":" + "59");
                intent.putExtra("titleDate", "Today");
                startActivity(intent);
                return true;
//                Intent z = new Intent(BedStatusChartsForDateg.this, StatsAsOfToday.class);
//                startActivity(z);
//                return true;
            case R.id.item3:
                Intent k = new Intent(BedStatusChartsForDate.this, BedStatusForDate.class);
                startActivity(k);
                return true;
            case R.id.item4:
                Intent S = new Intent(BedStatusChartsForDate.this, OccupancyPerMonth.class);
                startActivity(S);
                return true;
            case R.id.item5:
                Intent g = new Intent(BedStatusChartsForDate.this, CalculateWaitTime.class);
                startActivity(g);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}