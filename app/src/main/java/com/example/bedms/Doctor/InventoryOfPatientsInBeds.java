package com.example.bedms.Doctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedms.Auth.Login;
import com.example.bedms.Model.Patient;
import com.example.bedms.Model.PatientInBedsAdapter;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InventoryOfPatientsInBeds extends AppCompatActivity {
    TextView title;
    BottomNavigationView bottomnav;
    RecyclerView rcvPatients;
    ArrayList<Patient> patientList = new ArrayList<Patient>();
    PatientInBedsAdapter painAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private static final String TAG = "Patient List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientinbeds);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setTitle("Doctors Home");

        rcvPatients = findViewById(R.id.rcvCleaning);



        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcvPatients.setLayoutManager(mLayoutManager);


        painAdapter = new PatientInBedsAdapter(patientList);
        //Add the divider line
        painAdapter.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcvPatients.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvPatients.setHasFixedSize(true);


        rcvPatients.setAdapter(painAdapter);
        patientList.clear(); // clear list
        painAdapter.notifyDataSetChanged();
        // if (patientList.isEmpty())
        // {
        //    Toast toast = Toast.makeText(getApplicationContext(),"There is no patients waiting", Toast.LENGTH_LONG);
        //   toast.setGravity(Gravity.CENTER, 0, 0);
        //   toast.show();
        // }else{
        retrievePatientsInBeds();
        //}



    }

    public ArrayList<Patient> retrievePatientsInBeds(){
        db.collection("patient")
                .whereEqualTo("Status", "Patient in bed in ward")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Patient tempPatient = null;
                            int counter = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //    if (document.getString("Status") == "waiting to see doctor") {
                                Log.d(TAG, "getName: " + document.getString("tag"));
                                tempPatient = new Patient();
                                String name = document.getString("Name");
                                String Dob = document.getString("DOB");
                                tempPatient.setpName(name);
                                tempPatient.setpDOB(Dob);
                                patientList.add(tempPatient);
                                counter = counter + 1;
                                painAdapter.notifyItemInserted(patientList.size() - 1);
                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        return patientList;
    }



    public void logout(View view){
        FirebaseAuth.getInstance().signOut(); //logout user
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adminhospital_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(InventoryOfPatientsInBeds.this, DoctorHub.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
