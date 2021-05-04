package com.example.bedms.Patient;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Doctor.AdmitPatient;
import com.example.bedms.Doctor.DischargePatient;
import com.example.bedms.Doctor.DoctorHub;
import com.example.bedms.Model.Patient;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class PatientDetailsDoctorScreen extends AppCompatActivity {
    TextView tvName, tvDob, tvStatus, tvWard;
    //Chronometer ch;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button dischargeBtn;
    String patientId;
    BottomNavigationView bottomnav;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientdetailsdoctorscreen);
        setTitle("Patient Details");
        tvName = findViewById(R.id.tvBedIdCal);
        tvDob = findViewById(R.id.tvPatientId);
        tvStatus = findViewById(R.id.tvStatus);
        tvWard = findViewById(R.id.tvWard);

        TextView timeWaitingTextView = findViewById(R.id.timeWaitingTextView);

        //Get Current Date
         Date now = new Date();


        Intent intent = getIntent();
        final String str, str2, str3;
        str = intent.getStringExtra("Name");
        str2 = intent.getStringExtra("Dob");
        tvName.setText(str);
        tvDob.setText(str2);


        String nameSearch = tvName.getText().toString();
        String dobSearch = tvDob.getText().toString();
        db.collection("patient")
                .whereEqualTo("Name", nameSearch)
                .whereEqualTo("DOB", dobSearch)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Patient tempPatient = null;
                            int counter = 0;
                            task.getResult();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                patientId = document.getId();
                                tvStatus.setText(document.getString("Status"));
                            }
                            db.collection("patient")
                            .document(patientId)
                            .collection("patientHistory")
                            .orderBy("dateAndTime")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                   if (task.isSuccessful()) {
                                       for (QueryDocumentSnapshot document : task.getResult()) {
                                           String eventTime = document.getString("dateAndTime");
                                           try {
                                               SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                        Date eventTimeAsDate = dtf.parse(eventTime);
                                                        long time = now.getTime() - eventTimeAsDate.getTime();
                                                        long second = (time / 1000) % 60;
                                                        long minute = (time / (1000 * 60)) % 60;
                                                        long hour = (time / (1000 * 60 * 60)) % 24;
                                                        long day = (time / ((1000 * 60 * 60)) % 24) % 365;

                                                        if (day > 0){
                                                            timeWaitingTextView.setText(String.format("%02d day(s) %02d:%02d:%02d",day, hour, minute, second));
                                                        } else if (day == 0){
                                                            timeWaitingTextView.setText(String.format("%02d:%02d:%02d", hour, minute, second));
                                                        }

                                           } catch (ParseException e) {
                                               e.printStackTrace();
                                           }
                                       }
                                   }
                               }
                           });


                        }
                    }
                });
        bottomnav = findViewById(R.id.viewNav);
        bottomnav.getMenu().setGroupCheckable(0, false, true);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.admitPatient:
                        Intent intent = new Intent(PatientDetailsDoctorScreen.this, AdmitPatient.class);
                        intent.putExtra("Name", str);
                        intent.putExtra("Dob", str2);
                        intent.putExtra("PatientId", patientId);
                        startActivity(intent);
                        break;
                    case R.id.dischargePatient:
                        Intent i = new Intent(PatientDetailsDoctorScreen.this, DischargePatient.class);
                        i.putExtra("Name", str);
                        i.putExtra("Dob", str2);
                        i.putExtra("PatientId", patientId);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adminhospital_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
                Intent i = new Intent(PatientDetailsDoctorScreen.this, DoctorHub.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}