package com.example.bedms.Doctor;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Auth.login;
import com.example.bedms.R;
import com.example.bedms.UpdateBedHistory;
import com.example.bedms.UpdatePatientHistory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class dischargepatient extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "testing";
    TextView pName;
    TextView pDOB;
    TextView pIllness;
    String bedName;
    String bedId;
    String patientId;
    BottomNavigationView bottomnav;
    Button btnDischarge;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    ArrayAdapter<String> bAdapter;
    ArrayList<String> bedList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dischargepatient);
        pName = findViewById(R.id.pNameEd);
        pDOB = findViewById(R.id.pDOBEd);
        pIllness = findViewById(R.id.illnesEd);
        btnDischarge = findViewById(R.id.btnAdmit);
        mAuth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        final String str, str2, str3;
        str = intent.getStringExtra("Name");
        str2 = intent.getStringExtra("Dob");
        bedId = intent.getStringExtra("BedId");
        pName.setText(str);
        pDOB.setText(str2);


        btnDischarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Patient discharged" , Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), doctorhub.class));

                db.collection("patient").document().update("Status", "patient discharged");

                UpdatePatientHistory uph = new UpdatePatientHistory();
                uph.updatePatientHistory("PatientId", "Patient has been discharged");

                UpdateBedHistory ubh = new UpdateBedHistory();
                ubh.updateBedHistory("BedName", "Bed ready for cleaning");

                db.collection("waitingForCleaning")
                        .add("BedName");

            }
        });


    }






    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //  String text =  (String) parent.getItemAtPosition(position);
        //  Toast.makeText(getApplicationContext(),text , Toast.LENGTH_LONG).show();
        Toast.makeText(this, bedList.get(position), Toast.LENGTH_LONG).show();
        Log.d(TAG, "onItemSelected:  " + this);
        Log.d(TAG, "onItemSelected: with pos " + bedList.get(position));
        // An item was selected. You can retrieve the selected item using
        //parent.getItemAtPosition(pos)

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
                Intent i = new Intent(dischargepatient.this, doctorhub.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(dischargepatient.this, login.class);
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
