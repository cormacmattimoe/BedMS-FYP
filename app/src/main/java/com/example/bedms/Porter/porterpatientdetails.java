package com.example.bedms.Porter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.Doctor.AdmitPatient;
import com.example.bedms.Doctor.dischargepatient;
import com.example.bedms.Doctor.doctorhub;
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

public class porterpatientdetails extends AppCompatActivity {
    TextView tvName, tvDob, tvStatus, tvWard;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button dischargeBtn;
    String patientId;
    BottomNavigationView bottomnav;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientdetails);
        setTitle("Patient Details");
        tvName = findViewById(R.id.tvName);
        tvDob = findViewById(R.id.tvPatientId);
        tvStatus = findViewById(R.id.tvStatus);
        tvWard = findViewById(R.id.tvWard);


        Intent intent = getIntent();
        final String str, str2, str3;
        str = intent.getStringExtra("Name");
        str2 = intent.getStringExtra("Dob");
        tvName.setText(str);
        tvDob.setText(str2);
        // showObstructionDetails();




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
                                tvStatus.setText(document.getString("Status"));
                            }
                        }
                    }
                });

     /*   bottomnav = findViewById(R.id.viewNav);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.admitPatient:
                        Intent intent = new Intent(porterpatientdetails.this, AdmitPatient.class);
                        intent.putExtra("Name", str);
                        intent.putExtra("Dob", str2);
                        intent.putExtra("PatientId", patientId);
                        startActivity(intent);
                        break;
                    case R.id.dischargePatient:
                        Intent i = new Intent(porterpatientdetails.this, dischargepatient.class);
                        i.putExtra("Name", str);
                        i.putExtra("Dob", str2);
                        i.putExtra("PatientId", patientId);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });

      */

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
                Intent i = new Intent(porterpatientdetails.this, doctorhub.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}