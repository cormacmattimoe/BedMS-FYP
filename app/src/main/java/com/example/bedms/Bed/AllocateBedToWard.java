package com.example.bedms.Bed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.CreateNewPatient;
import com.example.bedms.R;
import com.example.bedms.UpdateBedHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllocateBedToWard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "testing" ;
    Button allocateBedBtn;
    EditText bedNameEt;
    String nameOfBed;
    String newWard;
    String bedId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner wardSpin;
    BottomNavigationView bottomnav;
    ArrayAdapter<String> wAdapter;
    ArrayList<String> wardsList = new ArrayList<>();
    Spinner bedSpin;
    ArrayAdapter<String> bAdapter;
    ArrayList<String> bedList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocatebedtoward);
        allocateBedBtn = findViewById(R.id.allocateBedToWardBtn);
        bedSpin = findViewById(R.id.bedSpinner);
        wardSpin = findViewById(R.id.spinBvWards);
        wardSpin.setOnItemSelectedListener(this);
        //Setting the ArrayAdapter data on the Spinner
        bAdapter  = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bedList);
        bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        bedSpin.setAdapter(bAdapter);

        getBedNames();

        wAdapter  = new ArrayAdapter(this,android.R.layout.simple_spinner_item,wardsList);
        wAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        wardSpin.setAdapter(wAdapter);

        getBedWard();

        bottomnav = findViewById(R.id.viewNav);
        //bottomnav.setOnNavigationItemSelectedListener(navListener);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.manageBeds:
                        Intent a = new Intent(AllocateBedToWard.this, CreateNewPatient.class);
                        startActivity(a);
                        break;
                    case R.id.viewBeds:
                        Intent b = new Intent(AllocateBedToWard.this, Inventoryofbeds.class);
                        startActivity(b);
                        break;
                }
                return false;
            }
        });
        allocateBedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameOfBed = bedSpin.getSelectedItem().toString();
                newWard = wardSpin.getSelectedItem().toString();
                updateBedWard(nameOfBed, newWard);

            }
        });

    }
    public void updateBedWard(final String bed, final String ward ) {
        //get bedId and update it with new ward
        db.collection("bed")
                .whereEqualTo("BedName", bed)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                bedId = document.getId();
                            }
                            db.collection("bed").document(bedId)
                                    .update("Ward", ward);
                            UpdateBedHistory ubh = new UpdateBedHistory();
                            ubh.updateBedHistory(bedId, "Bed allocated to ward");
                            Toast.makeText(AllocateBedToWard.this, "Bed allocated successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), AllocateBedToWard.class));
                        }
                    }
                });
    }

    public List<String> getBedWard() {
        wardSpin.setAdapter(wAdapter);
        db.collection("wards")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "onComplete with string: " + document.getId());

                        String wardsy = document.getId().toString();
                        Log.d(TAG, "onComplete with wardy: " + wardsList);
                        wardsList.add(wardsy);
                    }
                    wAdapter.notifyDataSetChanged();
                }
            }
        });
        return wardsList;
    }

    public List<String> getBedNames() {
        bedSpin.setAdapter(bAdapter);
        db.collection("bed")
                .whereEqualTo("Ward", "")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "onComplete with string: " + document.getId());

                        String bedNames = document.getString("Bed Name");
                        Log.d(TAG, "onComplete with wardy: " + bedList);
                        bedList.add(bedNames);
                    }
                    bAdapter.notifyDataSetChanged();
                }
            }
        });
        return bedList;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //  String text =  (String) parent.getItemAtPosition(position);
        //  Toast.makeText(getApplicationContext(),text , Toast.LENGTH_LONG).show();
        Toast.makeText(this, wardsList.get(position), Toast.LENGTH_LONG).show();
        Log.d(TAG, "onItemSelected:  " + this);
        Log.d(TAG, "onItemSelected: with pos " + wardsList.get(position));
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
                Intent i = new Intent(AllocateBedToWard.this, CreateNewPatient.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}