package com.example.bedms.Doctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedms.Auth.login;
import com.example.bedms.Model.BedAdapter;
import com.example.bedms.Model.Patient;
import com.example.bedms.Model.PatientAdapter;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchPatient extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "testing";
    EditText pName;
    EditText pDOB;
    TextView pIllness;
    Spinner spinnyBeds;
    EditText searchTag;
    String patientId;
    Button btnSearch;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    ArrayAdapter<String> bAdapter;
    ArrayList<String> bedList = new ArrayList<>();
    ArrayList<Patient> patientList = new ArrayList<Patient>();
    RecyclerView rcvPatients;
    PatientAdapter paAdapter;
    ArrayAdapter <BedAdapter> pAdapter;
    BottomNavigationView bottomnav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchpatient);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        pName = findViewById(R.id.nameEd);
        pDOB = findViewById(R.id.dobEd);
        //searchTag = findViewById()
        btnSearch = findViewById(R.id.searchPatient);
        // getNextPatient();
        rcvPatients = findViewById(R.id.rcvBedsCleaning);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcvPatients.setLayoutManager(mLayoutManager);


        paAdapter = new PatientAdapter(patientList);
        //Add the divider line
        paAdapter.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcvPatients.setHasFixedSize(true);

        pName.getText().toString();
        pDOB.getText().toString();
        rcvPatients.setAdapter(paAdapter);

        mAuth = FirebaseAuth.getInstance();
        //String name = pName.getText().toString();
        // spinnyBeds.getSelectedItemId();



      //  bottomnav = findViewById(R.id.viewNav);
     /*   bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.findPatient:
                        Intent a = new Intent(SearchPatient.this, SearchPatient.class);
                        startActivity(a);
                        break;
                    case R.id.examinePatient:
                        Intent b = new Intent(SearchPatient.this, ExaminePatient.class);
                        startActivity(b);
                        break;
                    case R.id.dischargePatient:
                        Intent c = new Intent(SearchPatient.this, doctorhub.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });

      */

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientList.clear(); // clear list
                paAdapter.notifyDataSetChanged();
                retrievePatientsWaiting();
            }
        });
    }
        //This is the start of searching using patients first last do
    public ArrayList<Patient> retrievePatientsWaiting() {
        String nameSearch = pName.getText().toString();
        String dobSearch = pDOB.getText().toString();
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
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
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
        return patientList;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      //  patientList.clear(); // clear list
       // paAdapter.notifyDataSetChanged();
      //  retrievePatientsWaiting(wards.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospital_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(SearchPatient.this, doctorhub.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(SearchPatient.this, login.class);
                startActivity(r);
                /*
            case R.id.item3:
                Toast.makeText(getApplicationContext(), "Allocate bed to ward Selected", Toast.LENGTH_LONG).show();
                i = new Intent(this, AllocateBedToWard.class);
                startActivity(i);
                return true;
            case R.id.item4:
                Toast.makeText(getApplicationContext(), "View beds by ward", Toast.LENGTH_LONG).show();
                i = new Intent(Inventoryofbeds.this, Inventoryofbeds.class);
                startActivity(i);
                return true;

                 */
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}