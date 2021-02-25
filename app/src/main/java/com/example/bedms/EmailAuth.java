/*
package com.example.bedms;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bedms.Auth.Register;
import com.example.bedms.Model.BedHistoryEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmailAuth {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth;
    Boolean matched = true;

    public boolean EmailAuth(final String email, final String password){
        db.collection("employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(email == document.getId()) {
                                        return matched;
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
                                    else {
                                        return false;
                                    }


    }
}

 */
