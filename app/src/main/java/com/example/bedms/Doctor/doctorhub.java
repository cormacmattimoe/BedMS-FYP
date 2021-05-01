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

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.Auth.login;
import com.example.bedms.Model.Patient;
import com.example.bedms.Model.PatientAdapter;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class doctorhub extends AppCompatActivity {
    TextView title;
    BottomNavigationView bottomnav;
    RecyclerView rcvPatients;
    ArrayList<Patient> patientList = new ArrayList<Patient>();
    PatientAdapter paAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    FirebaseUser user;

            private static final String TAG = "Patient List";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_doctor);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    setTitle("Doctors Home");
    title = findViewById(R.id.title22);
    mAuth = FirebaseAuth.getInstance();

    //get current user
    user = FirebaseAuth.getInstance().getCurrentUser();
    user.getEmail().toString();

    Intent intent = getIntent();
    String str;
    str = intent.getStringExtra("Welcome");
    title.setText(str);
    rcvPatients = findViewById(R.id.rcvBedsCleaning);


    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    rcvPatients.setLayoutManager(mLayoutManager);


    paAdapter = new PatientAdapter(patientList);
    //Add the divider line
    paAdapter.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    rcvPatients.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    rcvPatients.setHasFixedSize(true);


    rcvPatients.setAdapter(paAdapter);
    patientList.clear(); // clear list
    paAdapter.notifyDataSetChanged();
   // if (patientList.isEmpty())
   // {
    //    Toast toast = Toast.makeText(getApplicationContext(),"There is no patients waiting", Toast.LENGTH_LONG);
     //   toast.setGravity(Gravity.CENTER, 0, 0);
     //   toast.show();
   // }else{
        retrievePatientsWaiting();
    //}

    bottomnav = findViewById(R.id.viewNav);
    bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.viewPatientsInBed:
                    Intent a = new Intent(doctorhub.this, inventoryofpatientsinbeds.class);
                    startActivity(a);
                    break;
            }
            return false;
        }
    });
}

    public ArrayList<Patient> retrievePatientsWaiting(){
        db.collection("patient")
                .whereEqualTo("Status", "waiting to see doctor")
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
                                    paAdapter.notifyItemInserted(patientList.size() - 1);
                                }
                        }
                         else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        return patientList;
    }




@Override
public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospital_menu, menu);
        return true;
        }
@Override
public boolean onOptionsItemSelected (MenuItem item) {
    int id = item.getItemId();
    switch (id) {
        case R.id.item1:
            Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
            Intent i = new Intent(doctorhub.this, doctorhub.class);
            startActivity(i);
            return true;

        case R.id.item2:

            if (user != null){
                mAuth.signOut();
                Toast.makeText(this, user.getEmail()+ " Logged out!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "You aren't login Yet!", Toast.LENGTH_SHORT).show();
            }
            finish();
            Intent r = new Intent(doctorhub.this, login.class);
            startActivity(r);

        default:
            return super.onOptionsItemSelected(item);
    }
}
}
