package com.example.bedms.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.CreateNewPatient;
import com.example.bedms.Doctor.doctorhub;
import com.example.bedms.Model.Bed;
import com.example.bedms.Model.Patient;
import com.example.bedms.Porter.porterhub;
import com.example.bedms.R;
import com.example.bedms.UpdateBedHistory;
import com.example.bedms.UpdatePatientHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class patientlistporterdetails extends AppCompatActivity {
    TextView tvName, tvDob, tvStatus, tvWard, tvBedName;
    Chronometer ch;
    String patientId,bedId;
    String dateTime;
    Button btnDelivered;
    Button dischargeBtn;
    BottomNavigationView bottomnav;
    FirebaseAnalytics fba;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientporterdetails);
        setTitle("Patient Details");
        tvName = findViewById(R.id.tvName);
        tvDob = findViewById(R.id.tvPatientId);
        tvStatus = findViewById(R.id.tvStatus);
        tvWard = findViewById(R.id.tvWardPlace);
        tvBedName = findViewById(R.id.tvBedNameEd);
        btnDelivered = findViewById(R.id.deliveredBtn);
        ch = findViewById(R.id.chrono);

        ch.start();


        Intent intent = getIntent();
        final String str, str2, str3;
        str = intent.getStringExtra("Name");
        str2 = intent.getStringExtra("Dob");
        tvName.setText(str);
        tvDob.setText(str2);


        String nameSearch = tvName.getText().toString();
        String dobSearch = tvDob.getText().toString();
        //Getting the information from the patient record using the dob and name.
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
                                counter = counter + 1;
                                tvStatus.setText(document.getString("Status"));
                                tvWard.setText(document.getString("WardName"));
                                tvBedName.setText(document.getString("BedName"));
                            }
                        }
                    }
                });
        /*


        //To retrieve the time the patient was allocated to the porter from the patients history
        db.collection("patient")
                .document(patientId)
                .collection("patientHistory")
                .whereEqualTo("eventType", "Admitted - waiting for porter")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               //
                           }
                       }
                   }
               });

         */

        btnDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameSearch = tvName.getText().toString();
                String dobSearch = tvDob.getText().toString();
                /*
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
                                        counter = counter + 1;
                                        */
                db.collection("patient").document(patientId).update("Status", "Patient in bed in ward");
                UpdatePatientHistory uph = new UpdatePatientHistory();
                uph.updatePatientHistory(patientId, "Patient delivered to bed");

                String bedSearch = tvBedName.getText().toString();
                db.collection("bed")
                        .whereEqualTo("BedName", bedSearch)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Bed tempBed = null;
                                    int counter = 0;
                                    task.getResult();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        bedId = document.getId();
                                        UpdateBedHistory ubh = new UpdateBedHistory();
                                        ubh.updateBedHistory(bedId, "Patient in bed in ward");

                                        db.collection("bed").document(bedId).update("Status", "Bed Occupied");

                                    }

                                }
                            }
                        });
                Toast.makeText(getApplicationContext(), "Patient Delivered", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), porterhub.class));

            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospital_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
                Intent i = new Intent(patientlistporterdetails.this, doctorhub.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(patientlistporterdetails.this, CreateNewPatient.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}