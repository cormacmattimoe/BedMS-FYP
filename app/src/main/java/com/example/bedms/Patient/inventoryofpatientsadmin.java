package com.example.bedms.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedms.Admin.CreateNewPatient;
import com.example.bedms.Model.AdminPatientAdapter;
import com.example.bedms.Model.Patient;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class inventoryofpatientsadmin extends AppCompatActivity {

    private static final String TAG = "actMain";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AdminPatientAdapter pAdapter;
    TextView totalPatients;
    ArrayList<Patient> allViewPatients = new ArrayList<Patient>();
    int totalNumberPatients;
    BottomNavigationView bottomnav;
    TextView listTv;
    RecyclerView rcvPatients;
    Button empButton;
    //BedAdapter adapter;
    //ArrayList<String> allBeds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventoryofpatientsadmin);

      //  empButton = findViewById(R.id.viewEmployeesBtn);
        listTv = findViewById(R.id.emptyList);
        rcvPatients = findViewById(R.id.rcvViewPatients);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcvPatients.setLayoutManager(mLayoutManager);


        pAdapter = new AdminPatientAdapter(allViewPatients);
        //Add the divider line
        pAdapter.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcvPatients.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvPatients.setHasFixedSize(true);


        rcvPatients.setAdapter(pAdapter);
        //retrieveAllPatients();
      //  allViewPatients.clear(); // clear list
        pAdapter.notifyDataSetChanged();



        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

     /*   empButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allViewEmployees.clear(); // clear list
                eAdapter.notifyDataSetChanged();
                retrieveAllEmployees();
            }
        });

      */

    //    if (allViewPatients.isEmpty()) {
      //      listTv.setText("There is no patients added ");
            //   Toast.makeText(getApplicationContext(), "There is no patients added", Toast.LENGTH_LONG).show();
     //   }
        retrieveAllPatients();

    }

        public ArrayList<Patient> retrieveAllPatients() {
                db.collection("patient")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Patient tempPatient = null;
                                    int counter = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // if (document.getString("Status") == "waiting to see doctor") {
                                        Log.d(TAG, "getName: " + document.getString("tag"));
                                        tempPatient = new Patient();
                                        String name = document.getString("Name");
                                        String Dob = document.getString("DOB");
                                        tempPatient.setpName(name);
                                        tempPatient.setpDOB(Dob);
                                        allViewPatients.add(tempPatient);
                                        counter = counter + 1;
                                        pAdapter.notifyItemInserted(allViewPatients.size() - 1);
                                    }
                                }
                                else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }

                            }
                        });
                return allViewPatients;
            }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patientinventory, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(inventoryofpatientsadmin.this, CreateNewPatient.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
