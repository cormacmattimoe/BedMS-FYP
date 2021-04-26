package com.example.bedms;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.Model.Bed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class bedDetails extends AppCompatActivity {
    private static final String TAG = "updateobs";
    TextView tvType, tvPatientId, tvStatus, tvWard;
    BottomNavigationView bottomnav;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String bedId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_details);
        setTitle("Bed Details");
        tvType = findViewById(R.id.tvName);
        tvPatientId = findViewById(R.id.tvPatientId);
        tvStatus = findViewById(R.id.tvStatus);
        tvWard = findViewById(R.id.tvWard);



        Intent intent = getIntent();
        String bedName;
        bedName = intent.getStringExtra("Name");
        tvType.setText(bedName);
        // showObstructionDetails();
        Intent i = getIntent();
        bedName = i.getStringExtra("BedName");
        tvType.setText(bedName);


        db.collection("bed")
                .whereEqualTo("BedName", bedName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Bed tempBed = null;
                            int counter = 0;
                            task.getResult();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                                bedId = document.getId();
                                counter = counter + 1;
                                tvStatus.setText(document.getString("Status"));
                                tvWard.setText(document.getString("Ward"));
                                tvPatientId.setText(document.getString("PatientID"));

                            }
                        }
                    }
                });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
                Intent i = new Intent(bedDetails.this, AdminHub.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(bedDetails.this, AdminHub.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}