package com.example.bedms.Doctor;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Model.Bed;
import com.example.bedms.Model.Patient;
import com.example.bedms.R;
import com.example.bedms.UpdateBedHistory;
import com.example.bedms.UpdatePatientHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DischargePatient extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "testing";
    TextView pName;
    TextView pDOB;
    TextView pIllness;
    String bedName;
    String bedId;
    String patientId;
    BottomNavigationView bottomnav;
    Button btnDischarge;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    ArrayAdapter<String> bAdapter;
    ArrayList<String> bedList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dischargepatient);
        pName = findViewById(R.id.pNameEd);
        pDOB = findViewById(R.id.edDatePicker);
        pIllness = findViewById(R.id.illnessEd);
        btnDischarge = findViewById(R.id.btnAdmit);
        mAuth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        final String str, str2, str3;
        str = intent.getStringExtra("Name");
        str2 = intent.getStringExtra("Dob");
        patientId = intent.getStringExtra("PatientId");
       // bedId = intent.getStringExtra("BedId");
        pName.setText(str);
        pDOB.setText(str2);


        btnDischarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("bed")
                        .whereEqualTo("PatientId", patientId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Bed tempBed = null;
                                    int counter = 0;
                                    task.getResult();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        bedId = document.getId();
                                        UpdateBedHistory ubh = new UpdateBedHistory();
                                        db.collection("bed").document(bedId).update("Status", "waiting for cleaning");
                                        ubh.updateBedHistory(bedId, "Bed is ready for cleaning ");
                                        startActivity(new Intent(getApplicationContext(), DischargePatient.class));
                                    }
                                }
                            }
                        });

                db.collection("patient")
                        .whereEqualTo("Name", str)
                        .whereEqualTo("DOB", str2)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Patient temPatient = null;
                                    int counter = 0;
                                    task.getResult();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        patientId = document.getId();
                                        UpdatePatientHistory uph = new UpdatePatientHistory();
                                        db.collection("patient").document(patientId).update("Status", "Discharged");
                                        uph.updatePatientHistory(patientId, "Patient discharged");
                                        startActivity(new Intent(getApplicationContext(), AdmitPatient.class));
                                    }
                                }
                            }
                        });

                Toast.makeText(getApplicationContext(), "Patient discharged" , Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), DoctorHub.class));

            }
        });


    }






    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //  String text =  (String) parent.getItemAtPosition(position);
        //  Toast.makeText(getApplicationContext(),text , Toast.LENGTH_LONG).show();
        Toast.makeText(this, bedList.get(position), Toast.LENGTH_LONG).show();
        Log.d(TAG, "onItemSelected:  " + this);
        Log.d(TAG, "onItemSelected: with pos " + bedList.get(position));
        // An item was selected. You can retrieve the selected item using
        //parent.getItemAtPosition(pos)

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adminhospital_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(DischargePatient.this, DoctorHub.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
