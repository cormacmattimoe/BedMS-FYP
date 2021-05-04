package com.example.bedms.CleaningStaff;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.Model.Bed;
import com.example.bedms.R;
import com.example.bedms.UpdateBedHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BedDetailsclean extends AppCompatActivity {
    private static final String TAG = "updateobs";
    TextView tvName, tvPatientId, tvStatus, tvWard, tvTime;
    BottomNavigationView bottomnav;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Button cleanBtn;
    String bedId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String str, str2, str3;
    FirebaseUser user;
    //Get Current Date
    Date now = new Date();
    String bedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beddetailsclean);
        setTitle("Bed Details");

        Intent intent = getIntent();
        bedName = intent.getStringExtra("Name");


        tvName = findViewById(R.id.tvBedIdCal);
        tvStatus = findViewById(R.id.tvStatus);
        tvWard = findViewById(R.id.tvWard);
        cleanBtn = findViewById(R.id.cleanBtn);
        tvTime = findViewById(R.id.tvTime);
        user = FirebaseAuth.getInstance().getCurrentUser();

        getBedsForCleaning();


        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                db.collection("bed").document(bedId).update("PatientID", "");
                db.collection("bed").document(bedId).update("Status", "Bed Cleaned ready for next patient");
                db.collection("bed").document(bedId).update("Status", "Open");
//                db.collection("bed").document(bedId).update("CleanedBy: ",user);

                UpdateBedHistory ubh = new UpdateBedHistory();
                ubh.updateBedHistory(bedId, "Bed is cleaned and ready for next patient");
                ubh.updateBedHistory(bedId, "Bed is now open");


                Toast.makeText(getApplicationContext(), "Bed Cleaned", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), Cleaningstaffhub.class));

            }
        });
    }


    public void getBedsForCleaning() {
        db.collection("bed")
                .whereEqualTo("BedName" , bedName)
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
                            db.collection("bed")
                                    .document(bedId)
                                    .collection("bedHistory4")
                                    .orderBy("dateAndTime", Query.Direction.DESCENDING)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
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
                                                            tvTime.setText(String.format("%02d day(s) %02d:%02d:%02d",day, hour, minute, second));
                                                        } else if (day == 0){
                                                            tvTime.setText(String.format("%02d:%02d:%02d", hour, minute, second));
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                            }
                                        }
                                    });
                        }
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
                Intent i = new Intent(BedDetailsclean.this, Cleaningstaffhub.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}