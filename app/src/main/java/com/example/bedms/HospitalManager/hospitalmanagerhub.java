package com.example.bedms.HospitalManager;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Model.Bed;
import com.example.bedms.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class hospitalmanagerhub extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private float[] yData = {40f, 10f, 20f, 30f};
    private String[] xData = {"Open", "Bed Occupied" , "Patient on way" , "waiting cleaning"};
    private String[] wardsName = {"St Johns", "St Magz" , "St Pauls" , "St Joes", "St Marys"};
    PieChart pieChart;
    BarChart chart;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    HashMap<String, Float> bedMap = new HashMap<String, Float>();
    Float open = 0f;
    String wardName;
    Float occcupied = 0f;;
    Float waitingPatient = 0f;;
    Float waitingCleaning = 0f;;
    ArrayList < Float > getTotals = new ArrayList < Float > ();
    ArrayList<BarEntry> getWardsInBed = new ArrayList<BarEntry>();
    ArrayList NoOfBeds = new ArrayList();
    ArrayList wardss = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalmanagerhub);
        Log.d(TAG, "onCreate: starting to create chart");



        pieChart = (PieChart) findViewById(R.id.idPieChart);
        chart = findViewById(R.id.barchart);

        chart.setHighlightFullBarEnabled(true);
       // pieChart.setDescription("Sales by employee (In Thousands $)");
        pieChart.setRotationEnabled(true);
        // pieChart.setUsePercentValues(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(50f);
        pieChart.setCenterText("Beds by status");
        pieChart.setCenterTextSize(8);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(8);
        //More options just check out the documentation!

       getBedStatus();
       getBedsInWard();

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                int pos1 = h.toString().indexOf("(sum): ");

                String ward = wardsName[pos1 + 1];
                Toast.makeText(hospitalmanagerhub.this, "Bed Ward " + ward + "\n", Toast.LENGTH_LONG).show();
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

                int pos1 = h.toString().indexOf("(sum): ");

                String status = xData[pos1 + 1];
                Toast.makeText(hospitalmanagerhub.this, "Bed Status " + status + "\n", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }
    public class MyXAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter {
        private String [] mValues;
        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];
        }
    }

    //   final ArrayList <Float> getTotals = new ArrayList<>();
    public ArrayList<BarEntry> getBedsInWard(){
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Ward success", Toast.LENGTH_LONG).show();
                Bed tempBed = null;
                int bedCount = 0;
                Float stJohns = 0f;
                Float stMarys = 0f;
                Float stPauls = 0f;
                Float stMagz = 0f;
                Float stJoes = 0f;
                Float noWard = 0f;

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
                getWardsInBed.add(new BarEntry(0, stJohns));
                getWardsInBed.add(new BarEntry(1, stMarys));
                getWardsInBed.add(new BarEntry(2, stPauls));
                getWardsInBed.add(new BarEntry(3, stMagz));
                getWardsInBed.add(new BarEntry(4, stJoes));
                getWardsInBed.add(new BarEntry(5, noWard));

                System.out.println("These are totals" + getWardsInBed);


                wardss.add("St Johns");
                wardss.add("St Marys");
                wardss.add("St Magz");
                wardss.add("St Pauls");
                wardss.add("St Joes");
                wardss.add("");

                ArrayList<IBarDataSet> dataSets = null;

                BarDataSet bds;
                bds = new BarDataSet(getWardsInBed,"Wards");
                /*
                bds1 = new BarDataSet(getWardsInBed, "St Marys");
                bds2 = new BarDataSet(getWardsInBed, "St Johns");
                bds3 = new BarDataSet(getWardsInBed, "St Pauls");
                bds4 = new BarDataSet(getWardsInBed, "St Magz");
                bds5 = new BarDataSet(getWardsInBed, "St Joes");
                dataSets = new ArrayList<>();
                dataSets.add(bds1);
                dataSets.add(bds2);
                dataSets.add(bds3);
                dataSets.add(bds4);
                dataSets.add(bds5);

                 */


                BarData Data = new BarData(bds);
                //   Data.setColors(ColorTemplate.COLORFUL_COLORS);
                bds.setColors(new int[] {Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.CYAN});

                XAxis xAxis = chart.getXAxis();
                xAxis.setValueFormatter(new MyXAxisValueFormatter(wardsName));
                xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
              //  xAxis.setGranularity(1);
                xAxis.setCenterAxisLabels(true);
                chart.setData(Data);
                chart.invalidate();






            }
            else {
                Log.d(TAG, "Error getting bed details: ", task.getException());
            }

        }

    });
        return getWardsInBed;


}

    public ArrayList<Float> getBedStatus(){
     //   final ArrayList <Float> getTotals = new ArrayList<>();
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Bed Status success", Toast.LENGTH_LONG).show();
                            Bed tempBed = null;
                            int bedCount = 0;
                            Float open = 0f;
                            Float occcupied = 0f;
                            Float waitingPatient = 0f;
                            Float waitingCleaning = 0f;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String status = document.getString("Status");
                                switch (status) {
                                    case "Open":
                                        open = open + 1;
                                        break;
                                    case "Bed Occupied":
                                        occcupied = occcupied + 1;
                                        break;
                                    case "Bed allocated - patient on way":
                                        waitingPatient = waitingPatient + 1;
                                        break;
                                    case "Bed ready for cleaning":
                                        waitingCleaning = waitingCleaning + 1;
                                        break;
                                    default:
                                     //   "blank":

                                }
                                bedCount++;
                            }
                            getTotals.add(0, open);
                            getTotals.add(1, occcupied);
                            getTotals.add(2, waitingPatient);
                            getTotals.add(3, waitingCleaning);

                            System.out.println("These are totals" + getTotals);

                            Log.d(TAG, "addDataSet started");
                            ArrayList<PieEntry> yEntrys = new ArrayList<>();
                           // ArrayList<String> xEntrys = new ArrayList<>();
                            int z = 0;
                            yData = new float[getTotals.size()];
                            ArrayList <Float> results = getTotals;
                          //  for(float f : getTotals) {
                             //   yData[z++] = f;
                           //   }
                            yEntrys.add(new PieEntry(open, "Open"));
                            yEntrys.add(new PieEntry(occcupied, "Occupied "));
                            yEntrys.add(new PieEntry(waitingPatient, "Allocated"));
                            yEntrys.add(new PieEntry(waitingCleaning, "Cleaning"));



                            //create the data set
                            PieDataSet pieDataSet = new PieDataSet(yEntrys, "Bed Status");
                            pieDataSet.setSliceSpace(1);
                            pieDataSet.setValueTextSize(5);

                            //add colors to dataset
                            ArrayList<Integer> colors = new ArrayList<>();
                            Color c1, c2,c3,c4,c5;
                            colors.add(Color.parseColor("#4AC948"));
                            colors.add(Color.parseColor("#1D7CF2"));
                            colors.add(Color.parseColor("#FF7F50"));
                            colors.add(Color.CYAN);
                            colors.add(Color.YELLOW);
                            colors.add(Color.MAGENTA);

                            colors.add(Color.YELLOW);
                            colors.add(Color.MAGENTA);

                            pieDataSet.setColors(colors);

                            //add legend to chart
                         //   Legend legend = pieChart.getLegend();
                          //  legend.setForm(Legend.LegendForm.CIRCLE);
                            //legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

                            //create pie data object
                            PieData pieData = new PieData(pieDataSet);
                            pieChart.setUsePercentValues(true);
                            pieChart.setData(pieData);
                            pieChart.invalidate();

                        }

                        else {
                            Log.d(TAG, "Error getting bed details: ", task.getException());
                        }

                    }

                });
        return getTotals;
    }
}
