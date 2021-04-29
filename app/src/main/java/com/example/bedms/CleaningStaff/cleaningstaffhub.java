package com.example.bedms.CleaningStaff;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bedms.Auth.login;
import com.example.bedms.Doctor.doctorhub;
import com.example.bedms.Doctor.inventoryofpatientsinbeds;
import com.example.bedms.Model.Bed;
import com.example.bedms.Model.BedAdapterCleaning;
import com.example.bedms.R;
import com.example.bedms.UpdateBedHistory;
import com.example.bedms.qrMainScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class cleaningstaffhub extends AppCompatActivity {
            TextView title;
            BottomNavigationView bottomnav;
            RecyclerView rcvBedsForCleaning;
            ArrayList<Bed> bedList = new ArrayList<Bed>();
            BedAdapterCleaning bAdaptClean;
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            private static final String TAG = "Beds for cleaning";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cleaningstaff);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    setTitle("Cleaning staff Home");
    title = findViewById(R.id.title22);

    // Intent intent = getIntent();
   // String str;
    //str = intent.getStringExtra("Welcome");
    //title.setText(str);




    bottomnav = findViewById(R.id.viewNav);
    bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.scanBed:
                    Intent a = new Intent(cleaningstaffhub.this, qrMainScreen.class);
                    startActivity(a);
                    break;
            }
            return false;
        }
    });

    rcvBedsForCleaning = findViewById(R.id.rcvBedsCleaning);


    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    rcvBedsForCleaning.setLayoutManager(mLayoutManager);


    bAdaptClean = new BedAdapterCleaning(bedList);
    //Add the divider line
    bAdaptClean.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    rcvBedsForCleaning.setHasFixedSize(true);


    rcvBedsForCleaning.setAdapter(bAdaptClean);
    bedList.clear(); // clear list
    bAdaptClean.notifyDataSetChanged();
    retrieveBedsForCleaning();


}

    public ArrayList<Bed> retrieveBedsForCleaning(){
        db.collection("bed")
                .whereEqualTo("Status", "waiting for cleaning")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Bed tempBed = null;
                            int counter = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "getName: " + document.getString("tag"));
                                    tempBed = new Bed();
                                //    String bedName = document.getString("Bed Name");
                                    String bedName = document.getString("BedName");
                                    String ward = document.getString("Ward");
                                    String status = document.getString("Status");
                                    tempBed.setBedName(bedName);
                                    tempBed.setBedWard(ward);
                                    tempBed.setBedStatus(status);

                                    bedList.add(tempBed);
                                    counter = counter + 1;
                                    bAdaptClean.notifyItemInserted(bedList.size() - 1);
                                }
                            }
                         else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        return bedList;
    }



public void logout(View view){
        FirebaseAuth.getInstance().signOut(); //logout user
        startActivity(new Intent(getApplicationContext(), login.class));
        finish();
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
            Intent i = new Intent(cleaningstaffhub.this, cleaningstaffhub.class);
            startActivity(i);
            return true;

        case R.id.item2:
            Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent r = new Intent(cleaningstaffhub.this, login.class);
            startActivity(r);
        default:
            return super.onOptionsItemSelected(item);
    }
}
}
