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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.ConditionCache;
import com.example.bedms.Model.Bed;
import com.example.bedms.Model.Patient;
import com.example.bedms.R;
import com.example.bedms.UpdateBedHistory;
import com.example.bedms.UpdatePatientHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdmitPatient extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "testing";
    TextView pName;
    TextView pDOB;
    TextView pIllness;
    Spinner spinnyBeds, spinWards;
    String bedNameSelected, wardNameSelected;
    String paaId;
    String bedId, illness;
    BottomNavigationView bottomnav;
    Button btnAdmit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    ArrayAdapter<String> bAdapter;
    ArrayList<String> bedList = new ArrayList<>();
    ArrayList<String> bedsByWard = new ArrayList<>();
    ArrayAdapter<String> wAdapter;
    ArrayList<String> wardsList = new ArrayList<>();
    Patient tempPatient = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admitpatient);
        pName = findViewById(R.id.pNameEd);
        pDOB = findViewById(R.id.edDatePicker);
        pIllness = findViewById(R.id.illnessEd);
        btnAdmit = findViewById(R.id.btnDischarge);
        spinnyBeds = findViewById(R.id.spinBeds);
        spinWards = findViewById(R.id.spinWards);
        spinnyBeds.setOnItemSelectedListener(this);
        spinWards.setOnItemSelectedListener(this);
        wAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, wardsList);
        wAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinWards.setAdapter(wAdapter);

        System.out.println("Ward List" + wardsList);
        bAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bedList);
        bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnyBeds.setAdapter(bAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ConditionCache.conditionCache);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.illnessEd);
        textView.setAdapter(adapter);


        getWards();
        // getBedNamesByWard(wardNameSelected);


        // getBedNamesByWard(wardNameSelected);




        mAuth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        final String str, str2, str3;
        str = intent.getStringExtra("Name");
        str2 = intent.getStringExtra("Dob");
        paaId = intent.getStringExtra("PatientId");
        pName.setText(str);
        pDOB.setText(str2);




        btnAdmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                final String str, str2, str3;
                str = intent.getStringExtra("Name");
                str2 = intent.getStringExtra("Dob");
                paaId = intent.getStringExtra("PatientId");
                bedNameSelected = spinnyBeds.getSelectedItem().toString();
                patientCheckin(paaId, bedNameSelected);
                startActivity(new Intent(getApplicationContext(), DoctorHub.class));

            }
        });


    }

    public List<String> getWards() {
        db.collection("wards")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "onComplete with string: " + document.getId());

                        String wardsy = document.getId().toString();
                        Log.d(TAG, "onComplete with wardy: " + wardsList);
                        wardsList.add(wardsy);
                    }
                    wAdapter.notifyDataSetChanged();
                }
            }
        });
        return wardsList;
    }

    public List<String> getBedNamesByWard(String ward) {
        db.collection("bed")
                .whereEqualTo("Ward", ward)
                .whereEqualTo("Status", "Open")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            bedList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String bedName = document.getString("BedName");
                                if (bedList.contains(bedName)) {

                                } else {
                                    bedList.add(bedName);
                                }
                            }
                            bAdapter.notifyDataSetChanged();
                        }
                    }
                });
        System.out.println("This is the bed list" + bedList);
        return bedList;
    }


    public void patientCheckin(final String pId, final String bed) {
        String illness = pIllness.getText().toString();
        //On admission of patient
        // 1. update p.history to show they are admitted
        // 2. Update p.status to show they are admitted waiting on porter.
        // 3. Update waiting on porter list
        //4. Remove them from waiting to see doc list
        //5. Assign bed to patient - patient on way.
        final String currentUser = mAuth.getCurrentUser().getEmail();
        db.collection("patient").document(paaId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        // patientId = documentSnapshot.getId();
                        // Now Update bed number in Patient and status and Doctor
                        db.collection("patient").document(paaId).update(
                                "Status", "waiting for porter", "Doctor", currentUser, "BedName", bed);

                        UpdatePatientHistory ph = new UpdatePatientHistory();
                        ph.updatePatientHistory(paaId, "Admitted - waiting for porter");



                        UpdatePatientHistory uph = new UpdatePatientHistory();
                        uph.updatePatientHistory(paaId, "Bed reserved for patient");

                        db.collection("bed")
                                .whereEqualTo("BedName", bedNameSelected)
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
                                            }

                                        }
                                    }
                                });

                    }
                });
        db.collection("bed")
                .whereEqualTo("BedName", bedNameSelected)
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
                                db.collection("patient").document(paaId).update("Illness",illness);
                                db.collection("patient").document(paaId).update("Status", "waiting for porter");
                                db.collection("patient").document(paaId).update("WardName", wardNameSelected);
                                db.collection("bed").document(bedId).update("PatientID", paaId);
                                db.collection("bed").document(bedId).update("Status", "Bed allocated - patient on way");
                                ubh.updateBedHistory(bedId, "Bed allocated - patient on way");
                                startActivity(new Intent(getApplicationContext(), AdmitPatient.class));
                            }
                        }
                    }
                });


    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        wardNameSelected = spinWards.getSelectedItem().toString();
        getBedNamesByWard(wardNameSelected);
        bAdapter.notifyDataSetChanged();
        System.out.println("These are all the beds" + getBedNamesByWard(wardNameSelected).toString());
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
                Intent i = new Intent(AdmitPatient.this, DoctorHub.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
