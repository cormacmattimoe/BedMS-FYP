package com.example.bedms.Employees;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.Bed.Inventoryofbedsallocate;
import com.example.bedms.Bed.managebeds;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class manageemployees extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth;
    EditText mFullName, mEmail,mDob;
    Spinner mRole;
    ArrayAdapter<String> eAdapter;
    ArrayList<String> employeeData = new ArrayList<>();
    Button mAdd;
    String name = "fred";
    BottomNavigationView bottomnav;
    private static final String TAG = "Mydatabase";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageemployees);


        mEmail = findViewById(R.id.eEmail);
        mFullName = findViewById(R.id.eName);
        mDob = findViewById(R.id.eDob);
        mRole = findViewById(R.id.spinner);
        mAdd = findViewById(R.id.addEmp);

        //Creating the ArrayAdapter instance having the country list
        eAdapter  = new ArrayAdapter(this,android.R.layout.simple_spinner_item,employeeData);
        eAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mRole.setAdapter(eAdapter);

        getRole();
        bottomnav = findViewById(R.id.viewNav);
        //bottomnav.setOnNavigationItemSelectedListener(navListener);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.viewEmployees:
                        Intent a = new Intent(manageemployees.this, Inventoryofemployees.class);
                        startActivity(a);
                        break;
                }
                return false;
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String name = mFullName.getText().toString();
                String DOB = mDob.getText().toString();
                String role = mRole.getSelectedItem().toString();

                Map<String, Object> employee = new HashMap<>();


                employee.put("Email", email);
                employee.put("Name", name);
                employee.put("DOB", DOB);
                employee.put("Role", role);
                employee.put("Registered", false);
                db.collection("employees").document(email)
                .set(employee);
              //  String str = mFullName.getText().toString();
                String str2 = mEmail.getText().toString();
                Log.i("Send email", "");

                Intent sendEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+ str2)); // enter an email id here
                sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Approval Message : Register"); //subject of the email
                sendEmail.putExtra(Intent.EXTRA_TEXT, "Hi, you are now able to Register in app."); //body of the email
                startActivity(Intent.createChooser(sendEmail, "Choose an email client from..."));
                return;
            }

        });
    }
    public List<String> getRole() {

        mRole.setAdapter(eAdapter);
        db.collection("roles")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "onComplete with string: " + document.getId());

                        String roles = document.getId().toString();
                        Log.d(TAG, "onComplete with bedType: " + employeeData);
                        employeeData.add(roles);
                    }
                    eAdapter.notifyDataSetChanged();
                }
            }
        });
        return employeeData;
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
                Toast.makeText(getApplicationContext(), "Home ", Toast.LENGTH_LONG).show();
                Intent i = new Intent(manageemployees.this, AdminHub.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
