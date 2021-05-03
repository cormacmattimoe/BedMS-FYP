package com.example.bedms.Bed;

import android.content.Intent;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.BedCache;
import com.example.bedms.Model.Bed;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageBeds extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "actMain";
    TextView ccuTx, icuTx, medTx, surgTx;
    EditText numberOfBeds;
    Button addBedsbtn;
    String bedId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BottomNavigationView bottomnav;
    Spinner spinBedType;
    Spinner spinWardType;
    int numberOfBedsAlreadyExisting;
    ArrayAdapter<String> bAdapter;
    ArrayAdapter<String> wAdapter;
    ArrayList<String> bedTypes = new ArrayList<>();
    ArrayList<String> wards = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managebeds);
        numberOfBeds = findViewById(R.id.NumberBedsEd);
        addBedsbtn = findViewById(R.id.addBeds);
        spinBedType = findViewById(R.id.spinBeds);
        spinBedType.setOnItemSelectedListener(this);
        spinWardType = findViewById(R.id.spinWards);
        spinWardType.setOnItemSelectedListener(this);
        wards.add("St Johns");
        wards.add("St Marys");
        wards.add("St Pauls");
        wards.add("St Magz");
        wards.add("St Joes");

        //Creating the ArrayAdapter instance having the country list
        bAdapter  = new ArrayAdapter(this,android.R.layout.simple_spinner_item,bedTypes);
        wAdapter  = new ArrayAdapter(this,android.R.layout.simple_spinner_item,wards);
        bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        spinBedType.setAdapter(bAdapter);
        spinWardType.setAdapter(wAdapter);

        getBedType();


        bottomnav = findViewById(R.id.viewNav);
        bottomnav.getMenu().setGroupCheckable(0, false, true);
        //bottomnav.setOnNavigationItemSelectedListener(navListener);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.allocateBed:
                        Intent b = new Intent(ManageBeds.this, AllocateBedToWard.class);
                        startActivity(b);
                        break;
                    case R.id.viewBedsByWard:
                        Intent c = new Intent(ManageBeds.this, InventoryOfBeds.class);
                        startActivity(c);
                        break;
                    case R.id.viewBedsInventory:
                        Intent d = new Intent(ManageBeds.this, InventoryOfBedsAllocate.class);
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
                Toast.makeText(ManageBeds.this, "Beds added successfully ", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), ManageBeds.class));
            }
        });
    }


    public void createBeds() {
        final String bedType = spinBedType.getSelectedItem().toString();
        final String ward = spinWardType.getSelectedItem().toString();
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
                        String bedName = bedType + i;
                        Bed newBed = new Bed();
                        newBed.setBedType(bedType);
                        newBed.setBedStatus("Open");
                        newBed.setBedName(bedName);

                        bed.put("BedType", bedType);
                        bed.put("Status", "Open");
                        bed.put("Ward", ward);
                        bed.put("PatientID", "");
                        bed.put("BedName", bedName);
                        System.out.println("This is the bed" + bed);
                        db.collection("bed")
                            .add(bed)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    bedId = documentReference.getId();
                                    Log.d(TAG, "DocumentSnapshot written with ID: " + bedId);
                                    Log.d(TAG, "Bed added with ID: " + bedId);
                                    newBed.setBedId(bedId);
                                    newBed.setBedStatus("Open");
                                    newBed.setBedWard(ward);
                                    newBed.setBedName(bedName);
                                    Calendar calender;
                                    SimpleDateFormat simpledateformat;
                                    calender = Calendar.getInstance();
                                    simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String eventDate = simpledateformat.format(calender.getTime());
                                    BedCache.bedCache.put(bedId,newBed);

                                    UpdateBedHistory ubh = new UpdateBedHistory();
                                    ubh.updateBedHistory(bedId, "Added bed",eventDate);
                                  //  ubh.updateBedHistory(bedId, "Bed allocated to ward", eventDate);
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
//        Log.d(TAG, "onItemSelected:  " + this);
//        Log.d(TAG, "onItemSelected: with pos " + bedTypes.get(position));
        // An item was selected. You can retrieve the selected item using
        //parent.getItemAtPosition(pos)
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                Intent i = new Intent(ManageBeds.this, AdminHub.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

