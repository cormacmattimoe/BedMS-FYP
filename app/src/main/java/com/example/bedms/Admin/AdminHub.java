package com.example.bedms.Admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Auth.Login;
import com.example.bedms.Bed.Managebeds;
import com.example.bedms.ConditionCache;
import com.example.bedms.Employees.ManageEmployees;
import com.example.bedms.Patient.InventoryOfPatientsAdmin;
import com.example.bedms.R;
import com.example.bedms.UpdatePatientHistory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class AdminHub extends AppCompatActivity {
    private static final String TAG = "testing";
    EditText pName, pDob, pAddress, pPhone, pNextofkin, pNokPhone, pNokRel, pIllness;
    String bedSelected;
    Button btnbAddPatient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BottomNavigationView bottomnav;
    String patientId;
    DatePickerDialog.OnDateSetListener mDateSetListner1, mDateSetListner2;
    TextView et_date1;
    static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    FirebaseAuth mAuth;

    FirebaseUser user;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewpatient);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        pName = findViewById(R.id.pNameEd);
        pDob = findViewById(R.id.edDatePicker);
        pAddress = findViewById(R.id.pAddressEd);
        pPhone = findViewById(R.id.pPhoneNumberEd);
        pNextofkin = findViewById(R.id.pNextOfKinEd);
        pNokPhone = findViewById(R.id.nokPhone);
        pNokRel = findViewById(R.id.nokRel);
        pIllness = findViewById(R.id.pIllnessEd);
        btnbAddPatient = findViewById(R.id.createPatient);
        mAuth = FirebaseAuth.getInstance();

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        user.getEmail().toString();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ConditionCache.conditionCache);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.pIllnessEd);
        textView.setAdapter(adapter);
        /*
        et_date1 = (TextView) findViewById(R.id.et_date1);

        et_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Date date;
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        CreateNewPatient.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        mDateSetListner1,
                        year, month, day);
                dialog.show();
            }
        });
        mDateSetListner1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String dob = dayOfMonth + "/" + month + "/" + year;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/yyyy");
                Date tmpDate = null;
                try {
                    tmpDate = simpleDateFormat.parse(dob);
                    SimpleDateFormat simpleDateFormatNew = new SimpleDateFormat("dd/MM/yyyy");
                    String finalDate = simpleDateFormatNew.format(tmpDate);
                    et_date1.setText(finalDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //et_date1.setText(dob);

            }
        };

         */

        bottomnav = findViewById(R.id.viewNav);
        //bottomnav.setOnNavigationItemSelectedListener(navListener);

        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.manageBed:
                        Intent a = new Intent(AdminHub.this, Managebeds.class);
                        startActivity(a);
                        break;
                    case R.id.manageEmployees:
                        Intent b = new Intent(AdminHub.this, ManageEmployees.class);
                        startActivity(b);
                        break;
                    case R.id.viewPatientList:
                        Intent c = new Intent(AdminHub.this, InventoryOfPatientsAdmin.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });

        btnbAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = pName.getText().toString();
                String dob = pDob.getText().toString();
                String address = pAddress.getText().toString();
                String phone = pPhone.getText().toString();
                String nextofkin = pNextofkin.getText().toString();
                String nokPhone = pNokPhone.getText().toString();
                String nokRel = pNokRel.getText().toString();
                String illness = pIllness.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    pName.setError("Name is Required.");
                }
                if (TextUtils.isEmpty(dob)) {
                    pDob.setError("Date of birth is Required.");

                }
                if (TextUtils.isEmpty(address)) {
                    pAddress.setError("Address is Required.");

                }
                if (TextUtils.isEmpty(phone)) {
                    pPhone.setError("Phone Number is Required.");


                }
                if (TextUtils.isEmpty(nextofkin)) {
                    pNextofkin.setError("Next of kin is Required.");


                }
                if (TextUtils.isEmpty(nokPhone)) {
                    pNokPhone.setError("Next of kin phonr is Required.");


                }
                if (TextUtils.isEmpty(nokRel)) {
                    pNokRel.setError("Next of kin relationship is Required.");


                }
                if (TextUtils.isEmpty(phone)) {
                    pPhone.setError("Phone Number is Required.");


                }
                if (TextUtils.isEmpty(illness)) {
                    pIllness.setError("Illness is Required.");
                }
                btnbAddPatient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        admitPatient();
                        Toast.makeText(AdminHub.this, "Admitted patient", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), AdminHub.class));

                    }
                });
            }
        });

    //    MyTask mt = new MyTask(AdminHub.this, pIllness);

    //    mt.execute("https://api.infermedica.com/v2/conditions/");
    }


    public void admitPatient() {

        String name = pName.getText().toString();
        String dob = pDob.getText().toString();
        String address = pAddress.getText().toString();
        String phone = pPhone.getText().toString();
        String nextofkin = pNextofkin.getText().toString();
        String nokPhone = pNokPhone.getText().toString();
        String nokRel = pNokRel.getText().toString();
        String illness = pIllness.getText().toString();

        final Map<String, Object> patient = new HashMap<>();
        patient.put("Name", name);
        patient.put("DOB", dob);
        patient.put("Address", address);
        patient.put("PhoneNumber", phone);
        patient.put("NextofKin", nextofkin);
        patient.put("NokPhone", nokPhone);
        patient.put("NokRelationship", nokRel);
        patient.put("Illness", illness);
        patient.put("Status", "waiting to see doctor");
        patient.put("BedName", "");
        patient.put("WardName", "");
        patient.put("Doctor", "");


        //   patient.put("Illness", illness);
      /*  patient.put("bedNumber", " ");

        patient.put("Doctor", " ");
        patient.put("Date", "");
        patient.put("Time", " ");

       */

        String patId = db.collection("patient").document().getId();
        db.collection("patient").document(patId)
                .set(patient);
        db.collection("patient").document(patId);


        db.collection("patient").document(patId).getId();
        UpdatePatientHistory uph = new UpdatePatientHistory();
        uph.updatePatientHistory(patId, "Patient checked into hospital");


    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut(); //logout user
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospital_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AdminHub.this, AdminHub.class);
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
                Intent r = new Intent(AdminHub.this, Login.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}