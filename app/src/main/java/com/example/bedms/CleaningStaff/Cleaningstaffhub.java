package com.example.bedms.CleaningStaff;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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


import com.example.bedms.Auth.Login;
import com.example.bedms.Model.Bed;
import com.example.bedms.Model.BedAdapterCleaning;
import com.example.bedms.R;
import com.example.bedms.qrMainScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Cleaningstaffhub extends AppCompatActivity {
    TextView title;
    BottomNavigationView bottomnav;
    RecyclerView rcvBedsForCleaning;
    ArrayList<Bed> bedList = new ArrayList<Bed>();
    BedAdapterCleaning bAdaptClean;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    FirebaseUser user;

            private static final String TAG = "Beds for cleaning";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cleaningstaff);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    setTitle("Cleaning staff Home");
    title = findViewById(R.id.title22);
    mAuth = FirebaseAuth.getInstance();

    //get current user
    user = FirebaseAuth.getInstance().getCurrentUser();
    user.getEmail().toString();


    // Intent intent = getIntent();
   // String str;
    //str = intent.getStringExtra("Welcome");
    //title.setText(str);




    bottomnav = findViewById(R.id.viewNav);
    bottomnav.getMenu().setGroupCheckable(0, false, true);
    bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.scanBed:
                    Intent a = new Intent(Cleaningstaffhub.this, qrMainScreen.class);
                    startActivity(a);
                    break;
            }
            return false;
        }
    });

    rcvBedsForCleaning = findViewById(R.id.rcvCleaning);


    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    rcvBedsForCleaning.setLayoutManager(mLayoutManager);


    bAdaptClean = new BedAdapterCleaning(bedList);
    //Add the divider line
    rcvBedsForCleaning.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    bAdaptClean.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    rcvBedsForCleaning.setHasFixedSize(true);


    rcvBedsForCleaning.setAdapter(bAdaptClean);
    bedList.clear(); // clear list
    bAdaptClean.notifyDataSetChanged();
    retrieveBedsForCleaning();


}

    public ArrayList<Bed> retrieveBedsForCleaning(){
        db.collection("bed")
                .whereEqualTo("Status", "Bed ready for cleaning")
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
                            if (bedList.size() == 0) {
                                Toast toast = Toast.makeText(getApplicationContext(), "There is no beds to be cleaned", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
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
        startActivity(new Intent(getApplicationContext(), Login.class));
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
            Intent i = new Intent(Cleaningstaffhub.this, Cleaningstaffhub.class);
            startActivity(i);
            return true;

        case R.id.item2:
            if (user != null){
                mAuth.signOut();
                Toast.makeText(this, user.getEmail()+ " Logged out!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "You aren't Login Yet!", Toast.LENGTH_SHORT).show();
            }
            finish();
            Intent r = new Intent(Cleaningstaffhub.this, Login.class);
            startActivity(r);
        default:
            return super.onOptionsItemSelected(item);
    }
}
}
