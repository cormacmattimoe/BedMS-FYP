package com.example.bedms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bedms.Auth.login;
import com.example.bedms.HospitalManager.hospitalmanagerhub;
import com.github.mikephil.charting.charts.LineChart;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class OccupancyPerMonth extends AppCompatActivity implements
        OnChartGestureListener, OnChartValueSelectedListener {


    private LineChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvOccrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupancypermonth);
        chart = findViewById(R.id.chart1);
        tvOccrate = findViewById(R.id.tvOccRate);

        setTitle("Bed Stats For Month");

        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);




        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);


        ArrayList <Entry> yValues = new ArrayList<>();



        yValues.add(new Entry(0,0));
        yValues.add(new Entry(1,1));
        yValues.add(new Entry(2,10));
        yValues.add(new Entry(3,10));
        yValues.add(new Entry(4,10));
        yValues.add(new Entry(5,10));
        yValues.add(new Entry(6,10));
        yValues.add(new Entry(7,10));
        yValues.add(new Entry(8,10));
        yValues.add(new Entry(9,10));
        yValues.add(new Entry(10,25));
        yValues.add(new Entry(11,25));
        yValues.add(new Entry(12,25));
        yValues.add(new Entry(13,25));
        yValues.add(new Entry(14,25));
        yValues.add(new Entry(15,25));
        yValues.add(new Entry(16,25));
        yValues.add(new Entry(17,25));
        yValues.add(new Entry(18,25));
        yValues.add(new Entry(19,25));
        yValues.add(new Entry(20,34));
        yValues.add(new Entry(21,34));
        yValues.add(new Entry(22,34));
        yValues.add(new Entry(23,34));
        yValues.add(new Entry(24,34));
        yValues.add(new Entry(25,34));
        yValues.add(new Entry(26,34));
        yValues.add(new Entry(27,34));
        yValues.add(new Entry(28,34));
        yValues.add(new Entry(29,34));
        yValues.add(new Entry(30,34));

        LineDataSet set1, set2,set3,set4;


        set1 = new LineDataSet(yValues, "Beds ");


        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setColor(Color.DKGRAY);

        ArrayList <ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);



        ArrayList <Entry> y2Values = new ArrayList<>();


        y2Values.add(new Entry(0,0));
        y2Values.add(new Entry(1,1));
        y2Values.add(new Entry(2,0));
        y2Values.add(new Entry(3,1));
        y2Values.add(new Entry(4,2));
        y2Values.add(new Entry(5,3));
        y2Values.add(new Entry(6,4));
        y2Values.add(new Entry(7,4));
        y2Values.add(new Entry(8,4));
        y2Values.add(new Entry(9,6));
        y2Values.add(new Entry(10,6));
        y2Values.add(new Entry(11,6));
        y2Values.add(new Entry(12,10));
        y2Values.add(new Entry(13,15));
        y2Values.add(new Entry(14,16));
        y2Values.add(new Entry(15,16));
        y2Values.add(new Entry(16,16));
        y2Values.add(new Entry(17,16));
        y2Values.add(new Entry(18,16));
        y2Values.add(new Entry(19,15));
        y2Values.add(new Entry(20,14));
        y2Values.add(new Entry(21,13));
        y2Values.add(new Entry(22,13));
        y2Values.add(new Entry(23,13));
        y2Values.add(new Entry(24,13));
        y2Values.add(new Entry(25,13));
        y2Values.add(new Entry(26,15));
        y2Values.add(new Entry(27,16));
        y2Values.add(new Entry(28,13));
        y2Values.add(new Entry(29,13));
        y2Values.add(new Entry(30,19));




        set2 = new LineDataSet(y2Values, "Occupancy ");


        set2.setDrawCircles(false);
        set2.setDrawValues(false);
        set2.setDrawCircleHole(false);

        set2.setColor(Color.BLUE);
        ArrayList <ILineDataSet> dataset2 = new ArrayList<>();
        dataset2.add(set2);

        ArrayList <Entry> y3Values = new ArrayList<>();
        y3Values.add(new Entry(0,0));
        y3Values.add(new Entry(1,9));
        y3Values.add(new Entry(2,8));
        y3Values.add(new Entry(3,7));
        y3Values.add(new Entry(4,5));
        y3Values.add(new Entry(5,5));
        y3Values.add(new Entry(6,6));
        y3Values.add(new Entry(7,6));
        y3Values.add(new Entry(8,3));
        y3Values.add(new Entry(9,3));
        y3Values.add(new Entry(10,18));
        y3Values.add(new Entry(11,17));
        y3Values.add(new Entry(12,13));
        y3Values.add(new Entry(13,7));
        y3Values.add(new Entry(14,6));
        y3Values.add(new Entry(15,7));
        y3Values.add(new Entry(16,5));
        y3Values.add(new Entry(17,6));
        y3Values.add(new Entry(18,4));
        y3Values.add(new Entry(19,7));
        y3Values.add(new Entry(20,19));
        y3Values.add(new Entry(21,21));
        y3Values.add(new Entry(22,19));
        y3Values.add(new Entry(23,18));
        y3Values.add(new Entry(24,18));
        y3Values.add(new Entry(25,20));
        y3Values.add(new Entry(26,16));
        y3Values.add(new Entry(27,14));
        y3Values.add(new Entry(28,17));
        y3Values.add(new Entry(29,20));
        y3Values.add(new Entry(30,15));


        set3 = new LineDataSet(y3Values, "Open ");

        set3.setDrawCircles(false);
        set3.setDrawValues(false);
        set3.setDrawCircleHole(false);

        set3.setColor(Color.GREEN);
        ArrayList <ILineDataSet> dataset3 = new ArrayList<>();
        dataset3.add(set3);



        LineData dataLine = new LineData(set1,set2,set3);
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
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(OccupancyPerMonth.this, login.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
