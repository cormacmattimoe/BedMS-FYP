package com.example.bedms.Bed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedms.Admin.CreateNewPatient;
import com.example.bedms.Model.Bed;
import com.example.bedms.Model.BedAdapter;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Inventoryofbedsallocate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "actMain";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BedAdapter mAdapter;
    TextView totalbeds;
    ArrayList<Bed> allViewBeds = new ArrayList<>();
    int totalNumberBedsInWard;
    Spinner spinWards;
    BottomNavigationView bottomnav;
    Button wardNameBtn;
    String wardNameSelected;
    RecyclerView rcvViewBeds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventoryofbedsallocate);

        totalbeds = findViewById(R.id.tvTotal);
        rcvViewBeds = findViewById(R.id.rcvViewbedsOfWard);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcvViewBeds.setLayoutManager(mLayoutManager);


        mAdapter = new BedAdapter(allViewBeds);
        //Add the divider line
        mAdapter.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rcvViewBeds.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvViewBeds.setHasFixedSize(true);

        retrieveAllBedsFromDb();

        rcvViewBeds.setAdapter(mAdapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        bottomnav = findViewById(R.id.viewNav);
        //bottomnav.setOnNavigationItemSelectedListener(navListener);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.manageBeds:
                        Intent a = new Intent(Inventoryofbedsallocate.this, managebeds.class);
                        startActivity(a);
                        break;
                    case R.id.allocateBeds:
                        Intent b = new Intent(Inventoryofbedsallocate.this, AllocateBedToWard.class);
                        startActivity(b);
                        break;
                }
                return false;
            }
        });


    }

    public ArrayList<Bed> retrieveAllBedsFromDb() {
        db.collection("bed")
                .whereEqualTo("Ward", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Bed tempBed = null;
                            int counter = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String tempBedName = document.getString("BedName");
                                tempBed = new Bed();
                                tempBed.setBedName(tempBedName);
                                allViewBeds.add(counter, tempBed);
                                counter = counter + 1;
                                mAdapter.notifyItemInserted(allViewBeds.size() - 1);
                            }
                            totalNumberBedsInWard = counter;
                            totalbeds.setText(Integer.toString(totalNumberBedsInWard));

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }

                });
        return allViewBeds;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //  String text =  (String) parent.getItemAtPosition(position);
        //  Toast.makeText(getApplicationContext(),text , Toast.LENGTH_LONG).show();
        // An item was selected. You can retrieve the selected item using
        //parent.getItemAtPosition(pos)
        allViewBeds.clear(); // clear list
        mAdapter.notifyDataSetChanged();
        wardNameSelected = spinWards.getSelectedItem().toString();
        retrieveAllBedsFromDb();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Inventoryofbedsallocate.this, CreateNewPatient.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
