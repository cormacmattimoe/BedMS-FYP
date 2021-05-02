package com.example.bedms.Employees;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.Model.Employee;
import com.example.bedms.Model.EmployeeAdapter;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InventoryOfEmployees extends AppCompatActivity {

    private static final String TAG = "actMain";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EmployeeAdapter eAdapter;
    TextView totalEmployees;
    ArrayList<Employee> allViewEmployees = new ArrayList<Employee>();
    int totalNumberEmployees;
    BottomNavigationView bottomnav;
    RecyclerView rcvViewEmployees;
    Button empButton;
    //BedAdapter adapter;
    //ArrayList<String> allBeds = new ArrayList<>();
    ArrayAdapter<String> lAdapter;
    //   ArrayList <String> spinData;
    ArrayList<String> wards = new ArrayList<>();
    // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, wards);
    //List<String> wardw = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventoryofemployees);

      //  empButton = findViewById(R.id.viewEmployeesBtn);
        totalEmployees = findViewById(R.id.tvTotal);
        rcvViewEmployees = findViewById(R.id.rcvViewPatients);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcvViewEmployees.setLayoutManager(mLayoutManager);


        eAdapter = new EmployeeAdapter(allViewEmployees);
        //Add the divider line
        eAdapter.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcvViewEmployees.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvViewEmployees.setHasFixedSize(true);


        rcvViewEmployees.setAdapter(eAdapter);
        allViewEmployees.clear(); // clear list
        eAdapter.notifyDataSetChanged();
        retrieveAllEmployees();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        bottomnav = findViewById(R.id.viewNav);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.manageEmployees:
                        Intent c = new Intent(InventoryOfEmployees.this, ManageEmployees.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });
     /*   empButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allViewEmployees.clear(); // clear list
                eAdapter.notifyDataSetChanged();
                retrieveAllEmployees();
            }
        });

      */

    }
        public ArrayList<Employee> retrieveAllEmployees() {
            String returnString = "";
            db.collection("employees")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Employee tempEmployee = null;
                                int counter = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "onComplete: " + document.getId());
                                    tempEmployee = new Employee();
                                    String empName = document.getString("Name");
                                    String empRole = document.getString("Role");
                                    tempEmployee.setEmployeeName(empName);
                                    tempEmployee.setEmployeeRole(empRole);

                                    //tempBed.setBedStatus("Empty");
                                    //  tempBed.setBedStatus(document.getData(" Staus"));
                                    // tempBed.setBedWard("Empty ");
                                    //tempBed.setPatientID("Empty ");
                                    allViewEmployees.add(tempEmployee);
                                    counter = counter + 1;
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    eAdapter.notifyItemInserted(allViewEmployees.size() - 1);
                                }
                       //         totalNumberEmployees = counter;
                 //               totalEmployees.setText(Integer.toString(totalNumberEmployees));
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }

                        }

                    });
            return allViewEmployees;

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
                Intent i = new Intent(InventoryOfEmployees.this, AdminHub.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
