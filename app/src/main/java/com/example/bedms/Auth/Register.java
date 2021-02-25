package com.example.bedms.Auth;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Model.Bed;
import com.example.bedms.Model.Patient;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Register extends AppCompatActivity {
    private static final String TAG = "Welcome";
    EditText mFullName, mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    DocumentSnapshot doc = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    Button mAdmin, mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_register);


        mFullName = findViewById(R.id.Name);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.signupButton);
        mLoginBtn = findViewById(R.id.alreadytv);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
/*
        Intent intent = getIntent();
        String str;
        String str2;
        str = intent.getStringExtra("Welcome1");
        str2 = intent.getStringExtra("Welcome2");
        mFullName.setText(str);
        mEmail.setText(str2);

 */


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();

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

                db.collection("employees")
                .whereEqualTo("Email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult() == null) {
                                Toast.makeText(Register.this, "Error ! " + "Sorry you are not registered please contact admin", Toast.LENGTH_SHORT).show();
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                            fAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser fuser = fAuth.getCurrentUser();
                                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(Register.this, "Please check your emails to verify your account", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                                    }
                                                });

                                                db.collection("employees").document(email)
                                                        .update("Registered", true);
                                            } else {
                                                Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                    });
                                }
                            }
                        }
                    }
                });
    }
});


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });
    }
}


