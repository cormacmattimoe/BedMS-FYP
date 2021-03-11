package com.example.bedms.CleaningStaff;

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

import com.example.bedms.Admin.CreateNewPatient;
import com.example.bedms.Model.Bed;
import com.example.bedms.Porter.porterhub;
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

import java.util.ArrayList;

public class bedDetailsclean extends AppCompatActivity {
    private static final String TAG = "updateobs";
    TextView tvName, tvPatientId, tvStatus, tvWard;
    BottomNavigationView bottomnav;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Button cleanBtn;
    String bedId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String str, str2, str3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beddetailsclean);
        setTitle("Bed Details");
        tvName = findViewById(R.id.tvName);
        tvStatus = findViewById(R.id.tvStatus);
        tvWard = findViewById(R.id.tvWard);
        cleanBtn = findViewById(R.id.cleanBtn);

        getBedsForCleaning();


        // showObstructionDetails();
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameSearch = tvName.getText().toString();
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
                db.collection("bed").document(bedId).update("Status", "Bed Cleaned ready for next patient");
                db.collection("bed").document(bedId).update("Status", "Open");

                UpdateBedHistory ubh = new UpdateBedHistory();
                ubh.updateBedHistory(bedId, "Bed is cleaned and ready for next patient");
                ubh.updateBedHistory(bedId, "Bed is now open");


          /*      String bedSearch = tvName.getText().toString();
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

           */
                Toast.makeText(getApplicationContext(), "Bed Cleaned", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), cleaningstaffhub.class));

            }
        });
    }


    public void getBedsForCleaning() {
        db.collection("bed")
                .whereEqualTo("Status", "waiting for cleaning")
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
                                counter = counter + 1;
                                tvName.setText(document.getString("BedName"));
                                tvStatus.setText(document.getString("Status"));
                                tvWard.setText(document.getString("Ward"));
                            }
                        }
                    }
                });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
                Intent i = new Intent(bedDetailsclean.this, CreateNewPatient.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(bedDetailsclean.this, CreateNewPatient.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}