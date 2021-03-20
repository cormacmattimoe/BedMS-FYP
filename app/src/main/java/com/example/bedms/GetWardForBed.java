
package com.example.bedms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetWardForBed extends AppCompatActivity {

    private String ward;

    public GetWardForBed(){

    }
    //see ExampleTest

    public String GetWardForBed(String bedID) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("bed")
                .document(bedID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                GetWardForBed.this.ward = document.getString("Ward");
                            }
                        }
                    }
                });
        return ward;
    }
}