package com.example.bedms.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Doctor.AdmitPatient;
import com.example.bedms.Doctor.SearchPatient;
import com.example.bedms.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView title;
    Button checkIn, examinePatientBtn;

    private static final String TAG = "Welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        checkIn = findViewById(R.id.findPatients);
        examinePatientBtn = findViewById(R.id.examinePatient);

        title = findViewById(R.id.title22);

        Intent intent = getIntent();
        String str;
        str = intent.getStringExtra("Welcome");
        title.setText(str);


        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchPatient.class));
            }
        });
        examinePatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdmitPatient.class));
            }
        });

    }


    public void logout(View view){
        FirebaseAuth.getInstance().signOut(); //logout user
        startActivity(new Intent(getApplicationContext(), login.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                return true;

            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Search Patient", Toast.LENGTH_LONG).show();
                i = new Intent(MainActivity.this,SearchPatient.class);
                startActivity(i);
                return true;
                /*
            case R.id.item3:
                Toast.makeText(getApplicationContext(), "Allocate bed to ward Selected", Toast.LENGTH_LONG).show();
                i = new Intent(this, AllocateBedToWard.class);
                startActivity(i);
                return true;
            case R.id.item4:
                Toast.makeText(getApplicationContext(), "View beds by ward", Toast.LENGTH_LONG).show();
                i = new Intent(Inventoryofbeds.this, Inventoryofbeds.class);
                startActivity(i);
                return true;

                 */
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}