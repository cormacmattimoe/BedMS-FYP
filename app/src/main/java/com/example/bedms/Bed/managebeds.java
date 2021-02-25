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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.CreateNewPatient;
import com.example.bedms.R;
import com.example.bedms.UpdateBedHistory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class managebeds extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "actMain";
    TextView ccuTx, icuTx, medTx, surgTx;
    EditText numberOfBeds;
    Button addBedsbtn;
    String bedId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BottomNavigationView bottomnav;
    Spinner spinBedType;
    int numberOfBedsAlreadyExisting;
    ArrayAdapter<String> bAdapter;
    ArrayList<String> bedTypes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managebeds);
        numberOfBeds = findViewById(R.id.NumberBedsEd);
        addBedsbtn = findViewById(R.id.addBeds);
        spinBedType = findViewById(R.id.spinBeds);
        spinBedType.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        bAdapter  = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bedTypes);
        bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinBedType.setAdapter(bAdapter);

        getBedType();


        bottomnav = findViewById(R.id.viewNav);
        //bottomnav.setOnNavigationItemSelectedListener(navListener);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.allocateBed:
                        Intent b = new Intent(managebeds.this, AllocateBedToWard.class);
                        startActivity(b);
                        break;
                    case R.id.viewBedsByWard:
                        Intent c = new Intent(managebeds.this, Inventoryofbeds.class);
                        startActivity(c);
                        break;
                    case R.id.viewBedsInventory:
                        Intent d = new Intent(managebeds.this, Inventoryofbedsallocate.class);
                        startActivity(d);
                        break;

                }
                return false;
            }
        });


        addBedsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createBeds();
                Toast.makeText(managebeds.this, "Beds added successfully ", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),managebeds.class));
            }
        });
    }


    public void createBeds() {
        final String bedType = spinBedType.getSelectedItem().toString();
        final String bedQuan = numberOfBeds.getText().toString();
        // final int[] totalNumberOfBedsType = {0};
        final int numberOfNewBeds = Integer.parseInt(bedQuan);
        //Find out how many beds exist for requested bed type
        db.collection("bed").whereEqualTo("BedType", bedType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    int count = querySnapshot.size();
                    System.out.println(count);
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {

                    }
                    System.out.println("Show details of bed" + " this is bed type=" + bedType + "this is bed quan" + bedQuan + " this is bed number" + numberOfNewBeds);
                    final Map<String, Object> bed = new HashMap<>();
                    int startingPosition = count;
                    for (int i = startingPosition; i < numberOfNewBeds + startingPosition; i++) {
                        bed.put("BedType", bedType);
                        bed.put("Status", "Open");
                        bed.put("Ward", "");
                        bed.put("PatientID", "");
                        bed.put("BedName", bedType + i);
                        bed.put("QrId", "");
                        System.out.println("This is the bed" + bed);
                        db.collection("bed")
                            .add(bed)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                    bedId = documentReference.getId();
                                    UpdateBedHistory ubh = new UpdateBedHistory();
                                    ubh.updateBedHistory(bedId, "Added bed");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                                });

                    }
                }
            }
        });
    }





    public List<String> getBedType() {
        spinBedType.setAdapter(bAdapter);
        db.collection("bedType")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "onComplete with string: " + document.getId());

                        String typeBed = document.getId().toString();
                        Log.d(TAG, "onComplete with bedType: " + bedTypes);
                        bedTypes.add(typeBed);
                    }
                    bAdapter.notifyDataSetChanged();
                }
            }
        });
        return bedTypes;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //  String text =  (String) parent.getItemAtPosition(position);
        //  Toast.makeText(getApplicationContext(),text , Toast.LENGTH_LONG).show();
        // Toast.makeText(this, bedTypes.get(position), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemSelected:  " + this);
        Log.d(TAG, "onItemSelected: with pos " + bedTypes.get(position));
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
                Intent i = new Intent(managebeds.this, CreateNewPatient.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

