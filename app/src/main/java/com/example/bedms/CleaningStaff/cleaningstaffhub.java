package com.example.bedms.CleaningStaff;

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

import com.example.bedms.Auth.login;
import com.example.bedms.Model.Bed;
import com.example.bedms.Model.BedAdapter;
import com.example.bedms.R;
import com.example.bedms.UpdateBedHistory;
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
            BedAdapter bAdapter;
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
    rcvBedsForCleaning = findViewById(R.id.rcvBedsTobeCleaned);


    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    rcvBedsForCleaning.setLayoutManager(mLayoutManager);


    bAdapter = new BedAdapter(bedList);
    //Add the divider line
    bAdapter.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    rcvBedsForCleaning.setHasFixedSize(true);


    rcvBedsForCleaning.setAdapter(bAdapter);
    bedList.clear(); // clear list
    bAdapter.notifyDataSetChanged();
    retrieveBedsForCleaning();


}

    public ArrayList<Bed> retrieveBedsForCleaning(){
        db.collection("waitingForCleaning")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Bed tempBed = null;
                            int counter = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "getName: " + document.getString("tag"));
                                    tempBed = new Bed();
                                //    String bedName = document.getString("Bed Name");
                                    String bedName = document.getId();
                                    tempBed.setBedName(bedName);
                                    bedList.add(tempBed);
                                    counter = counter + 1;
                                    bAdapter.notifyItemInserted(bedList.size() - 1);

                                UpdateBedHistory ubh = new UpdateBedHistory();
                                ubh.updateBedHistory(bedName, "Beds to be cleaned");
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
