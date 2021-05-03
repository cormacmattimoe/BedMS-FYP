package com.example.bedms.Porter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedms.Auth.Login;
import com.example.bedms.Model.Patient;
import com.example.bedms.Model.PorterPatientAdapter;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PorterHub extends AppCompatActivity {
    TextView title;
    BottomNavigationView bottomnav;
    RecyclerView rcvPatientsWaiting;
    ArrayList<Patient> patientList = new ArrayList<Patient>();
    PorterPatientAdapter papAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    FirebaseUser user;

            private static final String TAG = "Patient List";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_porter);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    setTitle("Porters Home");
    mAuth = FirebaseAuth.getInstance();

    //get current user
    user = FirebaseAuth.getInstance().getCurrentUser();
    user.getEmail().toString();

    rcvPatientsWaiting = findViewById(R.id.rcvPatientsDoctorScreen);


    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    rcvPatientsWaiting.setLayoutManager(mLayoutManager);


    papAdapter = new PorterPatientAdapter(patientList);
    //Add the divider line
    papAdapter.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    rcvPatientsWaiting.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    rcvPatientsWaiting.setHasFixedSize(true);


    rcvPatientsWaiting.setAdapter(papAdapter);
    patientList.clear(); // clear list
    papAdapter.notifyDataSetChanged();
    retrievePatientsWaiting();


}

    public ArrayList<Patient> retrievePatientsWaiting(){
        db.collection("patient")
                .whereEqualTo("Status", "waiting for porter")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Patient tempPatient = null;
                            int counter = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "getName: " + document.getString("tag"));
                                    tempPatient = new Patient();
                                    String name = document.getString("Name");
                                    String Dob = document.getString("DOB");
                                    tempPatient.setpName(name);
                                    tempPatient.setpDOB(Dob);
                                    patientList.add(tempPatient);
                                    counter = counter + 1;
                                    papAdapter.notifyItemInserted(patientList.size() - 1);
                                }

                            if (patientList.size() == 0) {
                                Toast toast = Toast.makeText(getApplicationContext(), "There is no patients waiting", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            }

                         else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        return patientList;
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
            Intent i = new Intent(PorterHub.this, PorterHub.class);
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
            Intent r = new Intent(PorterHub.this, Login.class);
            startActivity(r);
        default:
            return super.onOptionsItemSelected(item);
    }
}
}
