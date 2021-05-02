package com.example.bedms.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.CleaningStaff.Cleaningstaffhub;
import com.example.bedms.Doctor.DoctorHub;
import com.example.bedms.HospitalManager.HospitalManagerHub;
import com.example.bedms.Porter.PorterHub;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    String employeeRole;
    FirebaseAuth fAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth.AuthStateListener mAuthStateListener;
    TextView title;
    ProgressBar progressBar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.bLogin);
        mCreateBtn = findViewById(R.id.createAc);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        //user.getEmail();



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Autheticate user to Firebase
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be more than 6 Characters");
                    return;
                }
                if (password.contains(" ")) {
                    mPassword.setError("Password contains spaces and needs to be changed");
                    return;
                }

                // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //  if (user != null) {
               /* user = fAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(Login.this, "You are logged in", Toast.LENGTH_SHORT).show();
                }

                */
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.collection("employees")
                                    .whereEqualTo("Email", email)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    employeeRole = document.getString("Role");
                                                }

                                                switch (employeeRole) {
                                                    case "Doctor":
                                                        Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent myIntent = new Intent(Login.this, DoctorHub.class);
                                                        Login.this.startActivity(myIntent);
                                                        break;
                                                    case "Porter":
                                                        Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(Login.this, PorterHub.class);
                                                        Login.this.startActivity(i);
                                                        break;
                                                    case "Admin":
                                                        Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent z = new Intent(Login.this, AdminHub.class);
                                                        Login.this.startActivity(z);
                                                        break;
                                                    case "Cleaning Staff":
                                                        Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent j = new Intent(Login.this, Cleaningstaffhub.class);
                                                        Login.this.startActivity(j);
                                                        break;
                                                    case "Hospital Manager":
                                                        Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent n = new Intent(Login.this, HospitalManagerHub.class);
                                                        Login.this.startActivity(n);
                                                        break;

                                                }
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));

                        }
                    }
                });
            }
        });








        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


    }
}