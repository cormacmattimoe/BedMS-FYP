package com.example.bedms.Nurse;

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
import com.example.bedms.Model.Patient;
import com.example.bedms.Model.PatientAdapter;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class nursehub extends AppCompatActivity {
            TextView title;
            BottomNavigationView bottomnav;
            RecyclerView rcvPatientsWaiting;
            ArrayList<Patient> patientList = new ArrayList<Patient>();
            PatientAdapter paAdapter;
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            private static final String TAG = "Patient List";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nurse);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    setTitle("Nurses Home");
    title = findViewById(R.id.title22);

    Intent intent = getIntent();
    String str;
    str = intent.getStringExtra("Welcome");
    title.setText(str);
    rcvPatientsWaiting = findViewById(R.id.rcvBedsTobeCleaned);


    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    rcvPatientsWaiting.setLayoutManager(mLayoutManager);


    paAdapter = new PatientAdapter(patientList);
    //Add the divider line
    paAdapter.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    rcvPatientsWaiting.setHasFixedSize(true);


    rcvPatientsWaiting.setAdapter(paAdapter);
    patientList.clear(); // clear list
    paAdapter.notifyDataSetChanged();
    retrievePatientsWaiting();


}

    public ArrayList<Patient> retrievePatientsWaiting(){
        db.collection("patient")
                .whereEqualTo("Status", "In bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Patient tempPatient = null;
                            int counter = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //    if (document.getString("Status") == "waiting to see doctor") {
                                    Log.d(TAG, "getName: " + document.getString("tag"));
                                    tempPatient = new Patient();
                                    String name = document.getString("Name");
                                    String Dob = document.getString("DOB");
                                    tempPatient.setpName(name);
                                    tempPatient.setpDOB(Dob);
                                    patientList.add(tempPatient);
                                    counter = counter + 1;
                                    paAdapter.notifyItemInserted(patientList.size() - 1);
                                }
                            }
                         else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        return patientList;
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
            Intent i = new Intent(nursehub.this, nursehub.class);
            startActivity(i);
            return true;

        case R.id.item2:
            Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent r = new Intent(nursehub.this, login.class);
            startActivity(r);
        default:
            return super.onOptionsItemSelected(item);
    }
}
}
