package com.example.bedms.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Doctor.doctorhub;
import com.example.bedms.Model.Bed;
import com.example.bedms.Model.Patient;
import com.example.bedms.R;
import com.example.bedms.UpdateBedHistory;
import com.example.bedms.UpdatePatientHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class patientlistinbeddetails extends AppCompatActivity {
    TextView tvName, tvDob, tvStatus, tvWard, tvBedName;
    String patientId,bedId;
    Button dischargeBtn;
    BottomNavigationView bottomnav;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientinbeddetails);
        setTitle("Patient Details");
        tvName = findViewById(R.id.tvName);
        tvDob = findViewById(R.id.tvPatientId);
        tvStatus = findViewById(R.id.tvStatus);
        tvWard = findViewById(R.id.tvWardName);
        tvBedName =findViewById(R.id.tvBedName);
        dischargeBtn = findViewById(R.id.discharge);

        Intent intent = getIntent();
        final String str, str2, str3;
        str = intent.getStringExtra("Name");
        str2 = intent.getStringExtra("Dob");
        tvName.setText(str);
        tvDob.setText(str2);

        getPatientdetails();



        dischargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                    }
                                }
                            }
                        });

                db.collection("bed")
                        .whereEqualTo("PatientID", patientId)
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
                                        db.collection("bed").document(bedId).update("Status", "waiting for cleaning");
                                        ubh.updateBedHistory(bedId, "Bed is ready for cleaning");
                                    }
                                }
                            }
                        });



                Toast.makeText(getApplicationContext(), "Patient discharged", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), doctorhub.class));

            }
        });



    }

        public void getPatientdetails() {

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
                                    counter = counter + 1;
                                  //  tvName.setText(document.getString("Name"));
                                 //   tvDob.setText(document.getString("DOB"));
                                    tvStatus.setText(document.getString("Status"));
                                    tvWard.setText(document.getString("WardName"));
                                    tvBedName.setText(document.getString("BedName"));

                                }
                            }
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
                Intent i = new Intent(patientlistinbeddetails.this, doctorhub.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(patientlistinbeddetails.this, doctorhub.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}